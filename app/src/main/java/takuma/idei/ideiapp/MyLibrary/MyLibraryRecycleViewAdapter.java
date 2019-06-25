package takuma.idei.ideiapp.MyLibrary;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import takuma.idei.ideiapp.R;

public class MyLibraryRecycleViewAdapter extends RecyclerView.Adapter<MyLibraryViewHolder> {

    private List<MyLibraryListItem> list;

    public MyLibraryRecycleViewAdapter(List<MyLibraryListItem> list) {
        this.list = list;
    }

    @Override
    public MyLibraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent,false);
        MyLibraryViewHolder vh = new MyLibraryViewHolder(inflate);


        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = vh.getAdapterPosition();
                MyLibraryListItem myLibraryListItem = list.get(position);

                String albumTitle = myLibraryListItem.getAlbumTitle();
                String artistName = myLibraryListItem.getArtistName();
                Bitmap albumArt = myLibraryListItem.getThumbnail();

                onItemClicked(albumTitle, artistName);
            }
        });

        return vh;



    }

    @Override
    public void onBindViewHolder(MyLibraryViewHolder holder, int position) {
        holder.albumView.setText(list.get(position).getAlbumTitle());
        holder.artistView.setText(list.get(position).getArtistName());
        holder.thumbnailView.setImageBitmap(list.get(position).getThumbnail());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    protected void onItemClicked(@NonNull String albumTitle, String artistName) {
    }


}
