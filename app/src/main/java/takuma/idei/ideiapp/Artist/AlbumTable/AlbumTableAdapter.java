package takuma.idei.ideiapp.Artist.AlbumTable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import takuma.idei.ideiapp.R;

public class AlbumTableAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<AlbumTableItem> albumList;



    public AlbumTableAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setAlbumList(ArrayList<AlbumTableItem> albumList) {
        this.albumList = albumList;
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = layoutInflater.inflate(R.layout.fragment_artist_album_table_row,parent,false);
        ((ImageView)convertView.findViewById(R.id.fragment_artist_album_table_row_albumart)).setImageBitmap(albumList.get(position).getAlbumArt());
        ((TextView)convertView.findViewById(R.id.fragment_artist_album_table_row_album_title)).setText(albumList.get(position).getAlbumTitle());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumTableItem albumTableItem = albumList.get(position);

                String albumTitle = albumTableItem.getAlbumTitle();


                onItemClicked(albumTitle);
            }
        });
        return convertView;
    }

    protected void onItemClicked(@NonNull String item) {
    }
}
