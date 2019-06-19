package takuma.idei.ideiapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

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

                String title = myLibraryListItem.getTitle();





                AlbumBean albumBean = new AlbumBean();
                albumBean.setArtist(title);

                onItemClicked(albumBean);
            }
        });

        return vh;



    }

    @Override
    public void onBindViewHolder(MyLibraryViewHolder holder, int position) {
        holder.titleView.setText(list.get(position).getTitle());
        holder.thumbnailView.setImageBitmap(list.get(position).getThumbnail());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    protected void onItemClicked(@NonNull AlbumBean albumBean) {
    }


}
