package takuma.idei.ideiapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ArtistPopularViewHolder extends RecyclerView.ViewHolder {
    public TextView songTitleView;
    public TextView rankingView;
    public TextView playcountView;

    public ArtistPopularViewHolder(View itemView) {
        super(itemView);
        songTitleView = (TextView) itemView.findViewById(R.id.popular_song_title);
        rankingView = (TextView) itemView.findViewById(R.id.popular_ranking);
        playcountView = (TextView) itemView.findViewById(R.id.popular_count);

    }
}
