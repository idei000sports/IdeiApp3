package takuma.idei.ideiapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public ImageView thumbnailView;
    public ViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.title);
        thumbnailView = (ImageView) itemView.findViewById(R.id.thumbnail);

    }
}
