package takuma.idei.ideiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ArtistPopularAdapter extends ArrayAdapter<ArtistPopularListItem> {
    private int mResource;
    private List<ArtistPopularListItem> mItems;
    private LayoutInflater mInflater;

    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param resource リソースID
     * @param items リストビューの要素
     */
    public ArtistPopularAdapter(Context context, int resource, List<ArtistPopularListItem> items) {
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
        ArtistPopularListItem item = mItems.get(position);

        TextView songTitle = (TextView) view.findViewById(R.id.popular_song_title);
        songTitle.setText(item.getSong_title());

        TextView ranking = (TextView) view.findViewById(R.id.popular_ranking);
        ranking.setText(item.getRanking());

        TextView playCount = (TextView) view.findViewById(R.id.popular_count);
        playCount.setText(item.getPlaycount());

        return view;
    }

}
