package takuma.idei.ideiapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyLibraryFragment extends Fragment implements View.OnClickListener{
    private FragmentManager fragmentManager;
    TextView playlistTab;
    TextView artistTab;
    TextView albumTab;
    View rootView;
    AllSongsDataBase asdb;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_selectfolder, container, false);
        asdb = new AllSongsDataBase();
        playlistTab = rootView.findViewById(R.id.playlist_tab);
        playlistTab.setOnClickListener(this);
        artistTab = rootView.findViewById(R.id.artist_tab);
        artistTab.setOnClickListener(this);
        albumTab = rootView.findViewById(R.id.album_tab);
        albumTab.setOnClickListener(this);


        //makeFolderList();



        return rootView;
    }

    public void openArtistFragment(AlbumBean albumBean) {

        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ArtistFragment fragment = new ArtistFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("ARTIST_KEY", albumBean);
        //値を書き込む
        fragment.setArguments(bundle);


        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);//前のfragmentへもどるのに必要
        transaction.commit();
    }



    public void makeList(String select) {
        ArrayList<MyLibraryListItem> listItems = new ArrayList<>();
        //リサイクルビューメモ
        //listItemsにいれていく
        ArrayList<AlbumBean> list = asdb.getArtists(getContext());
        //ここのメソッドを切り替え
        if (select.equals("artist_tab")) {
            list = asdb.getArtists(getContext());
            for (int i = 0; i < list.size(); i++) {
                Bitmap bmp = BitmapFactory.decodeFile(list.get(i).getAlbumArt());
                MyLibraryListItem item = new MyLibraryListItem(bmp, list.get(i).getArtist());
                listItems.add(item);
            }
        } else if (select.equals("album_tab")) {
            list = asdb.getAlbum(getContext());
            for (int i = 0; i < list.size(); i++) {
                Bitmap bmp = BitmapFactory.decodeFile(list.get(i).getAlbumArt());
                MyLibraryListItem item = new MyLibraryListItem(bmp, list.get(i).getAlbumTitle());
                listItems.add(item);
            }
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.artist_and_album_list);
        MyLibraryRecycleViewAdapter adapter = new MyLibraryRecycleViewAdapter(listItems) {
            @Override
            protected void onItemClicked(@NonNull AlbumBean albumBean) {


                openArtistFragment(albumBean);
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


        RecyclerView recyclerView = rootView.findViewById(R.id.popular_song_list);
        MyLibraryRecycleViewAdapter adapter = new MyLibraryRecycleViewAdapter(listItems) {
            @Override
            protected void onItemClicked(@NonNull AlbumBean item) {
                Toast.makeText(getContext(), "item", Toast.LENGTH_SHORT).show();
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
                        makeList("artist_tab");
                        break;
                    case R.id.album_tab:
                        makeList("album_tab");
                        break;

                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}