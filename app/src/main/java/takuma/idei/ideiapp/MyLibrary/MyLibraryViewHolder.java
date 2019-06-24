package takuma.idei.ideiapp.MyLibrary;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import takuma.idei.ideiapp.R;

public class MyLibraryViewHolder extends RecyclerView.ViewHolder {
    public TextView albumView;
    public TextView artistView;
    public ImageView thumbnailView;
    public MyLibraryViewHolder(View itemView) {
        super(itemView);
        albumView = (TextView) itemView.findViewById(R.id.my_library_album_title);
        artistView = (TextView) itemView.findViewById(R.id.my_library_artist_name);
        thumbnailView = (ImageView) itemView.findViewById(R.id.thumbnail);

    }
}
