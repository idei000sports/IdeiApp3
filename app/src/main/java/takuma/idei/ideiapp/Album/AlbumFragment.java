package takuma.idei.ideiapp.Album;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import takuma.idei.ideiapp.AllSongsDataBase;
import takuma.idei.ideiapp.Bean.SongBean;
import takuma.idei.ideiapp.MusicPlayer.MusicPlayerService;
import takuma.idei.ideiapp.MusicPlayerAIDL;
import takuma.idei.ideiapp.R;

public class AlbumFragment extends Fragment {
    View rootView;
    String album_art_work_path;
    TextView album_title_view;
    ImageView album_artwork_view;
    TextView artist_name_view;
    RecyclerView recyclerView;

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

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_album, container, false);

        recyclerView = rootView.findViewById(R.id.album_song_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        String artistName = getArguments().getString("ARTIST_NAME");
        String albumTitle = getArguments().getString("ALBUM_TITLE");

        artist_name_view = rootView.findViewById(R.id.fragment_album_artist_info);
        artist_name_view.setText(artistName + "のアルバム");
        album_title_view = rootView.findViewById(R.id.album_name);
        album_title_view.setText(albumTitle);
        album_artwork_view = rootView.findViewById(R.id.album_art_work);

        makeSongList(artistName, albumTitle);

        return rootView;
    }
    public void makeSongList(String artistName, String albumTitle) {
        ArrayList<AlbumListItem> listItems = new ArrayList<>();

        AllSongsDataBase asdb = new AllSongsDataBase();
        ArrayList<SongBean> songBean = asdb.getAlbumSong(getContext(),artistName, albumTitle);
        if (songBean.size() != 0) {
            for (int i = 0; i < songBean.size(); i++) {
                AlbumListItem item = new AlbumListItem(songBean.get(i).getArtist_name(), songBean.get(i).getSong_title(), songBean.get(i).getSong_path());
                listItems.add(item);
            }
        }
        Bitmap bmp = BitmapFactory.decodeFile(asdb.getAlbumArtWork(getContext(), artistName, albumTitle));
        album_artwork_view.setImageBitmap(bmp);

        AlbumRecycleViewAdapter adapter = new AlbumRecycleViewAdapter(listItems) {
            @Override
            protected void onItemClicked(@NonNull String songPath) {
                try {
                    binder.playSongFromTop(songPath, album_art_work_path);
                }catch (Exception e) {

                }
            }
        };

        recyclerView.setAdapter(adapter);
    }
}
