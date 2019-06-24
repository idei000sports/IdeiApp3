package takuma.idei.ideiapp.Album;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import takuma.idei.ideiapp.R;

public class AlbumViewHolder extends RecyclerView.ViewHolder {
    public TextView album_song_title;
    public TextView album_song_artist;

    public AlbumViewHolder(View itemView) {
        super(itemView);
        album_song_title = (TextView) itemView.findViewById(R.id.album_song_title);
        album_song_artist = (TextView) itemView.findViewById(R.id.album_song_artist);


    }
}
