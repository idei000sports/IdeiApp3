package takuma.idei.ideiapp.Album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import takuma.idei.ideiapp.R;

public class AlbumAdapter extends ArrayAdapter<AlbumListItem> {
    private int mResource;
    private List<AlbumListItem> mItems;
    private LayoutInflater mInflater;

    public AlbumAdapter(Context context, int resource, List<AlbumListItem> items) {
        super(context, resource, items);

        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
            view = convertView;
        }
        else {
            view = mInflater.inflate(mResource, null);
        }

        // リストビューに表示する要素を取得
        AlbumListItem item = mItems.get(position);

        TextView album_song_title = (TextView) view.findViewById(R.id.album_song_title);
        album_song_title.setText(item.getSong_title());

        TextView album_song_artist = (TextView) view.findViewById(R.id.album_song_artist);
        album_song_artist.setText(item.getArtist_name());


        return view;
    }


}
