package takuma.idei.ideiapp.Artist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import takuma.idei.ideiapp.Album.AlbumFragment;
import takuma.idei.ideiapp.AllSongsDataBase;
import takuma.idei.ideiapp.Artist.AlbumTable.AlbumTableAdapter;
import takuma.idei.ideiapp.Artist.AlbumTable.AlbumTableItem;
import takuma.idei.ideiapp.Bean.AlbumBean;
import takuma.idei.ideiapp.Bean.SongBean;
import takuma.idei.ideiapp.MusicPlayer.MusicPlayerService;
import takuma.idei.ideiapp.MusicPlayerAIDL;
import takuma.idei.ideiapp.R;


public class ArtistFragment extends Fragment {
    private String artistName;
    private String albumTitle;
    private View rootView;
    private RecyclerView recyclerView;

    AllSongsDataBase asdb;

    private MusicPlayerAIDL binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = MusicPlayerAIDL.Stub.asInterface(service);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Intent serviceIntent = new Intent(getActivity(), MusicPlayerService.class);
        Objects.requireNonNull(getActivity()).startService(serviceIntent);
        getActivity().bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        artistName = (String)getArguments().getString("ARTIST_NAME");

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_artist, container, false);
        TextView artist_name_view = rootView.findViewById(R.id.artist_name);
        artist_name_view.setText(artistName);

        recyclerView = rootView.findViewById(R.id.popular_song_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        makePopularSong();
        makeAlbumListTableRow();

        return rootView;
    }

    public void makeAlbumListTableRow() {
        asdb = new AllSongsDataBase();
        TableRow tableRow1= rootView.findViewById(R.id.fragment_artist_album_row1);

        AlbumTableAdapter adapter = new AlbumTableAdapter(getContext()) {
            @Override
            protected void onItemClicked(@NonNull String albumTitle) {
                openAlbum(albumTitle, artistName);
            }
        };

        ArrayList<AlbumTableItem> albumTableItems = new ArrayList<>();
        ArrayList<AlbumBean> albumList = asdb.getAlbumFromArtist(getContext(), artistName);

        for (int i = 0; i < albumList.size(); i++) {
            AlbumTableItem ATI = new AlbumTableItem();
            ATI.setAlbumTitle(albumList.get(i).getAlbumTitle());
            Bitmap bmp = BitmapFactory.decodeFile(albumList.get(i).getAlbumArt());
            ATI.setAlbumArt(bmp);
            albumTableItems.add(ATI);
        }

        adapter.setAlbumList(albumTableItems);
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            tableRow1.addView(adapter.getView(i, null, tableRow1));
        }
    }


    public void openAlbum(String albumTitle, String artistName) {
        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("ALBUM_TITLE", albumTitle);
        bundle.putString("ARTIST_NAME", artistName);


        AlbumFragment fragment = new AlbumFragment();

        //値を書き込む
        fragment.setArguments(bundle);

        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);//前のfragmentへもどるのに必要
        transaction.commit();
    }


    public void makePopularSong() {
        ArrayList<ArtistListItem> listItems = new ArrayList<>();

        asdb = new AllSongsDataBase();
        ArrayList<SongBean> songBean = asdb.getPopularSong(getContext(),artistName);
        if (songBean.size() != 0) {
            for (int i = 0; i < songBean.size(); i++) {
                String song_title = songBean.get(i).getSong_title();
                int playcount = songBean.get(i).getPlaycount();
                ArtistListItem item = new ArtistListItem(song_title, i + 1, playcount);
                listItems.add(item);

                if(i > 4) {
                    break;
                }
            }
        }

        ArtistPopularRecycleViewAdapter adapter = new ArtistPopularRecycleViewAdapter(listItems) {
            @Override
            protected void onItemClicked(@NonNull String song_title) {
                SongBean songBean = asdb.getPopluarSongPath(getContext(), artistName, song_title);

                try {
                    binder.playSongFromTop(songBean.getSong_path(), songBean.getAlbum_art_path());
                }catch (Exception e) {
                }
            }
        };

        recyclerView.setAdapter(adapter);
    }

}
