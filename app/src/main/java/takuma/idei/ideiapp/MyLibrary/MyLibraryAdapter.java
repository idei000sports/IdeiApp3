package takuma.idei.ideiapp.MyLibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import takuma.idei.ideiapp.R;

public class MyLibraryAdapter extends ArrayAdapter<MyLibraryListItem> {

    private int mResource;
    private List<MyLibraryListItem> mItems;
    private LayoutInflater mInflater;

    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param resource リソースID
     * @param items リストビューの要素
     */
    public MyLibraryAdapter(Context context, int resource, List<MyLibraryListItem> items) {
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
        MyLibraryListItem item = mItems.get(position);

        // サムネイル画像を設定
        ImageView thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        thumbnail.setImageBitmap(item.getThumbnail());

        // タイトルを設定
        TextView album_title = (TextView)view.findViewById(R.id.my_library_album_title);
        album_title.setText(item.getAlbumTitle());

        TextView artist_name = (TextView)view.findViewById(R.id.my_library_artist_name);
        artist_name.setText(item.getArtistName());

        return view;
    }
}