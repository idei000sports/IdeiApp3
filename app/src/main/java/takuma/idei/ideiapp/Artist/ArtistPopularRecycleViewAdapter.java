package takuma.idei.ideiapp.Artist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import takuma.idei.ideiapp.R;

public class ArtistPopularRecycleViewAdapter extends RecyclerView.Adapter<ArtistViewHolder> {
    private List<ArtistListItem> list;

    public ArtistPopularRecycleViewAdapter(List<ArtistListItem> list) {
        this.list = list;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_song_row, parent,false);
        ArtistViewHolder vh = new ArtistViewHolder(inflate);


        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = vh.getAdapterPosition();
                ArtistListItem artistListItem = list.get(position);

                String song_title = artistListItem.getSong_title();


                onItemClicked(song_title);
            }
        });

        return vh;



    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {

        holder.songTitleView.setText(list.get(position).getSong_title());
        holder.rankingView.setText(Integer.toString(list.get(position).getRanking()));
        holder.playcountView.setText(Integer.toString(list.get(position).getPlaycount()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    protected void onItemClicked(@NonNull String song_title) {
    }
}
