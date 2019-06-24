package takuma.idei.ideiapp.Album;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import takuma.idei.ideiapp.R;


public class AlbumRecycleViewAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
    private List<AlbumListItem> list;



    public AlbumRecycleViewAdapter(List<AlbumListItem> list) {
        this.list = list;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_row, parent,false);
        AlbumViewHolder vh = new AlbumViewHolder(inflate);


        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = vh.getAdapterPosition();
                AlbumListItem albumListItem = list.get(position);


                String songPath = albumListItem.getSong_path();

                onItemClicked(songPath);
            }
        });

        return vh;



    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {

        holder.album_song_title.setText(list.get(position).getSong_title());
        holder.album_song_artist.setText(list.get(position).getArtist_name());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    protected void onItemClicked(@NonNull String songPath) {
    }
}
