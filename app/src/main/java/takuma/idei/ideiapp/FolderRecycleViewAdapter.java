package takuma.idei.ideiapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FolderRecycleViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<SampleListItem> list;

    public FolderRecycleViewAdapter(List<SampleListItem> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent,false);
        ViewHolder vh = new ViewHolder(inflate);


        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = vh.getAdapterPosition();
                SampleListItem sli = list.get(position);

                String item = sli.getTitle();
                onItemClicked(item);
            }
        });

        return vh;



    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleView.setText(list.get(position).getTitle());
        holder.thumbnailView.setImageBitmap(list.get(position).getThumbnail());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    protected void onItemClicked(@NonNull String item) {
    }


}
