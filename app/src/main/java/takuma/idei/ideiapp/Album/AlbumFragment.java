package takuma.idei.ideiapp.Album;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import takuma.idei.ideiapp.AllSongsDataBase;
import takuma.idei.ideiapp.Bean.SongBean;
import takuma.idei.ideiapp.MusicPlayer.MusicPlayerService;
import takuma.idei.ideiapp.MusicPlayerAIDL;
import takuma.idei.ideiapp.R;

public class AlbumFragment extends Fragment {
    private String artistName;
    private String albumTitle;
    private TextView album_title_view;
    private ImageView album_artwork_view;
    private TextView artist_name_view;
    private RecyclerView recyclerView;
    private AllSongsDataBase asdb;

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

    public void playAlbum(String artistName, String albumTitle, int position) {
        ArrayList<String> song_paths = asdb.getAlbumPaths(getContext(), artistName, albumTitle);
        String album_art_path = asdb.getAlbumArtWork(getContext(), artistName, albumTitle);
        try {
            binder.setPlayList(song_paths, album_art_path, position);
        }catch (Exception e) {

        }




    }










    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Intent serviceIntent = new Intent(getActivity(), MusicPlayerService.class);
        Objects.requireNonNull(getActivity()).startService(serviceIntent);
        getActivity().bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);

        recyclerView = rootView.findViewById(R.id.album_song_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        artistName = getArguments().getString("ARTIST_NAME");
        albumTitle = getArguments().getString("ALBUM_TITLE");

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

        asdb = new AllSongsDataBase();
        ArrayList<SongBean> songBean = asdb.getAlbumSong(getContext(),artistName, albumTitle);
        if (songBean.size() != 0) {
            for (int i = 0; i < songBean.size(); i++) {
                AlbumListItem item = new AlbumListItem(songBean.get(i).getArtist_name(), songBean.get(i).getSong_title(), songBean.get(i).getSong_path());
                listItems.add(item);
            }
        }
        String album_art_work_path = asdb.getAlbumArtWork(getContext(), artistName, albumTitle);
        Bitmap bmp = BitmapFactory.decodeFile(album_art_work_path);
        album_artwork_view.setImageBitmap(bmp);

        AlbumRecycleViewAdapter adapter = new AlbumRecycleViewAdapter(listItems) {
            @Override
            protected void onItemClicked(@NonNull int position) {
                try {
                    playAlbum(artistName, albumTitle,position);
                }catch (Exception e) {

                }
            }
        };

        recyclerView.setAdapter(adapter);
    }
}
