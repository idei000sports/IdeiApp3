package takuma.idei.ideiapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyLibraryFragment extends Fragment implements View.OnClickListener{

    TextView playlistTab;
    TextView artistTab;
    TextView albumTab;
    View rootView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_selectfolder, container, false);

        playlistTab = rootView.findViewById(R.id.playlist_tab);
        playlistTab.setOnClickListener(this);
        artistTab = rootView.findViewById(R.id.artist_tab);
        artistTab.setOnClickListener(this);
        albumTab = rootView.findViewById(R.id.album_tab);
        albumTab.setOnClickListener(this);


        makeFolderList();

        return rootView;
    }

    public void makeAlbumList(){
        ArrayList<MyLibraryListItem> listItems = new ArrayList<>();
        //リサイクルビューメモ
        //listItemsにいれていく
        AllSongsDataBase asdb = new AllSongsDataBase();
        ArrayList<AlbumBean> artists = asdb.getArtists(getContext());

        for (int i = 0; i < artists.size(); i++) {
            Bitmap bmp = BitmapFactory.decodeFile(artists.get(i).getAlbumArt());
            MyLibraryListItem item = new MyLibraryListItem(bmp, artists.get(i).getAlbumTitle());
            listItems.add(item);
        }


        RecyclerView recyclerView = rootView.findViewById(R.id.songList);
        MyLibraryRecycleViewAdapter adapter = new MyLibraryRecycleViewAdapter(listItems) {
            @Override
            protected void onItemClicked(@NonNull String item) {
            }
        };
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    public void makeFolderList(){
        ArrayList<MyLibraryListItem> listItems = new ArrayList<>();
        //リサイクルビューメモ
        //listItemsにいれていく
        AllSongsDataBase asdb = new AllSongsDataBase();
        ArrayList<AlbumBean> artists = asdb.getArtists(getContext());

        for (int i = 0; i < artists.size(); i++) {
            Bitmap bmp = BitmapFactory.decodeFile(artists.get(i).getAlbumArt());
            MyLibraryListItem item = new MyLibraryListItem(bmp, artists.get(i).getArtist());
            listItems.add(item);
        }


        RecyclerView recyclerView = rootView.findViewById(R.id.songList);
        MyLibraryRecycleViewAdapter adapter = new MyLibraryRecycleViewAdapter(listItems) {
            @Override
            protected void onItemClicked(@NonNull String item) {
            }
        };
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }


    public void onClick (View v){
        if(v != null) {
            try {
                switch (v.getId()) {
                    case R.id.playlist_tab:
                        break;
                    case R.id.artist_tab:
                        makeFolderList();
                        break;
                    case R.id.album_tab:
                        makeAlbumList();
                        break;

                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}