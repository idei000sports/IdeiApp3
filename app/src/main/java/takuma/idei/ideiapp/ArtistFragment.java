package takuma.idei.ideiapp;

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

public class ArtistFragment extends Fragment {
    private String artistName;
    private ArrayList<SongBean> songBeanArrayList;
    private View rootView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        AlbumBean albumBean = (AlbumBean) getArguments().getSerializable("ARTIST_KEY");
        artistName = albumBean.getArtist();

        AllSongsDataBase allSongsDataBase = new AllSongsDataBase();
        songBeanArrayList = allSongsDataBase.getPopularSong(getContext(), artistName);
        
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_artist, container, false);
        TextView artist_name = rootView.findViewById(R.id.artist_name);
        artist_name.setText(artistName);
        makePopularSong();



        return rootView;
    }


    public void makePopularSong() {
        ArrayList<ArtistPopularListItem> listItems = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ArtistPopularListItem item = new ArtistPopularListItem("曲名", 1, 333);
            listItems.add(item);
        }

        AllSongsDataBase asdb = new AllSongsDataBase();
        ArrayList<SongBean> songBean = asdb.getPopularSong(getContext(),artistName);
        if (songBean.size() != 0) {
            for (int i = 0; i < 5; i++) {
                String song_title = songBean.get(i).getTitle();
                int playcount = songBean.get(i).getCount();

                ArtistPopularListItem item = new ArtistPopularListItem(song_title, i + 1, playcount);
                listItems.add(item);
            }
        }




        RecyclerView recyclerView = rootView.findViewById(R.id.popular_song_list);
        ArtistPopularRecycleViewAdapter adapter = new ArtistPopularRecycleViewAdapter(listItems) {
            @Override
            protected void onItemClicked(@NonNull String item) {

            }
        };
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

}
