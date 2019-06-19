package takuma.idei.ideiapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ArtistPopularRecycleViewAdapter extends RecyclerView.Adapter<ArtistPopularViewHolder> {
    private List<ArtistPopularListItem> list;

    public ArtistPopularRecycleViewAdapter(List<ArtistPopularListItem> list) {
        this.list = list;
    }

    @Override
    public ArtistPopularViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_song_row, parent,false);
        ArtistPopularViewHolder vh = new ArtistPopularViewHolder(inflate);


        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = vh.getAdapterPosition();
                ArtistPopularListItem artistPopularListItem = list.get(position);

                String title = artistPopularListItem.getSong_title();


                onItemClicked(title);
            }
        });

        return vh;



    }

    @Override
    public void onBindViewHolder(ArtistPopularViewHolder holder, int position) {

        holder.songTitleView.setText(list.get(position).getSong_title());
        holder.rankingView.setText(Integer.toString(list.get(position).getRanking()));
        holder.playcountView.setText(Integer.toString(list.get(position).getPlaycount()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    protected void onItemClicked(@NonNull String title) {
    }
}
