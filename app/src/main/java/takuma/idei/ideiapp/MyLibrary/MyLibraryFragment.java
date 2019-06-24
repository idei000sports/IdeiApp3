package takuma.idei.ideiapp.MyLibrary;

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

import java.util.ArrayList;

import takuma.idei.ideiapp.Album.AlbumFragment;
import takuma.idei.ideiapp.AllSongsDataBase;
import takuma.idei.ideiapp.Artist.ArtistFragment;
import takuma.idei.ideiapp.Bean.AlbumBean;
import takuma.idei.ideiapp.R;

public class MyLibraryFragment extends Fragment implements View.OnClickListener{

    View rootView;
    AllSongsDataBase asdb;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        asdb = new AllSongsDataBase();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_selectfolder, container, false);
        rootView.findViewById(R.id.playlist_tab).setOnClickListener(this);
        rootView.findViewById(R.id.artist_tab).setOnClickListener(this);
        rootView.findViewById(R.id.album_tab).setOnClickListener(this);

        return rootView;
    }

    public void openArtistAndAlbumFragment(String select, String albumTitle, String artistName) {
        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("ALBUM_TITLE", albumTitle);
        bundle.putString("ARTIST_NAME", artistName);

        Fragment fragment = new ArtistFragment();

        switch (select){
            case "artist_tab":
                fragment = new ArtistFragment();
                break;
            case "album_tab":
                fragment = new AlbumFragment();
                break;
        }


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
        ArrayList<AlbumBean> list = asdb.getAlbumTable(getContext());
        Bitmap bmp;
        MyLibraryListItem item = new MyLibraryListItem();

        for (int i = 0; i < list.size(); i++) {
            bmp = BitmapFactory.decodeFile(list.get(i).getAlbumArt());
            if (select.equals("artist_tab")) {
                item = new MyLibraryListItem(bmp, list.get(i).getAlbumTitle(), list.get(i).getArtist());
            }   else if (select.equals("album_tab")) {
                item = new MyLibraryListItem(bmp, list.get(i).getAlbumTitle(), list.get(i).getArtist());
            }
            listItems.add(item);
            //MyLibraryListItemはサムネとアーティスト名orアルバム(タイトル)
        }


        RecyclerView recyclerView = rootView.findViewById(R.id.artist_and_album_list);
        MyLibraryRecycleViewAdapter adapter = new MyLibraryRecycleViewAdapter(listItems) {
            @Override
            protected void onItemClicked(@NonNull String albumTitle, String artistName) {
                openArtistAndAlbumFragment(select, albumTitle, artistName);
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