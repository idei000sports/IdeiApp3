package takuma.idei.ideiapp.Artist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import takuma.idei.ideiapp.R;

public class ArtistViewHolder extends RecyclerView.ViewHolder {
    public TextView songTitleView;
    public TextView rankingView;
    public TextView playcountView;

    public ArtistViewHolder(View itemView) {
        super(itemView);
        songTitleView = (TextView) itemView.findViewById(R.id.popular_song_title);
        rankingView = (TextView) itemView.findViewById(R.id.popular_ranking);
        playcountView = (TextView) itemView.findViewById(R.id.popular_count);

    }
}
