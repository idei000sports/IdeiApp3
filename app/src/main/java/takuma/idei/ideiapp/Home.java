package takuma.idei.ideiapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Home extends Fragment {

    private TextView[] recentlyViewTextList = new TextView[6];
    private ImageView[] recentlyViewImageList = new ImageView[6];
    private TextView[] thisMonthFavViewTextList = new TextView[6];
    private ImageView[] thisMonthFavViewImageList = new ImageView[6];
    private TextView[] playbackViewTextList = new TextView[6];
    private ImageView[] playbackViewImageList = new ImageView[6];
    private TextView[] myFavoriteViewTextList = new TextView[6];
    private ImageView[] myFavoriteViewImageList = new ImageView[6];


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private String today = dateFormat.format(new Date());
    private SQLiteDatabase sqLiteDatabase;
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        PlayDataBase playDataBase = new PlayDataBase(getActivity());
        sqLiteDatabase = playDataBase.getWritableDatabase();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home_tester, container, false);
        setAndGetViewID(rootView);
        recentlyPlayed(rootView);
        thisMonthPlayed(rootView);
        playbackPlayed(rootView);
        myFavoritePlayed(rootView);

        return rootView;
    }

    public String[][] getSQL(String sql, String columnName) {
        String[][] list = new String[6][2];
        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {
            boolean next = cursor.moveToNext();
            int i = 0;
            while (next || i < 6) {
                if (i < 6) {
                    String album = cursor.getString(cursor.getColumnIndex(columnName));
                    String albumArtPath = cursor.getString(cursor.getColumnIndex("album_art_path"));

                    list[i][0] = album;
                    list[i][1] = albumArtPath;
                }
                i++;
                next = cursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public void myFavoritePlayed(View rootView) {
        TextView my_favorite_list_title = (TextView)rootView.findViewById(R.id.my_favorite_title).findViewById(R.id.list_title);
        TextView my_favorite_list_info = (TextView)rootView.findViewById(R.id.my_favorite_title).findViewById(R.id.list_info);
        my_favorite_list_title.setText("あなたのお気に入りのアルバムと曲");
        my_favorite_list_info.setText("");

        String sql = "SELECT album, album_art_path, count(*) AS COUNT FROM count_and_date_table GROUP BY artist, album ORDER BY COUNT DESC;";
        String[][] myFavorite = getSQL(sql, "album");
        setViewList(myFavoriteViewImageList, myFavoriteViewTextList, myFavorite);
    }



    public void playbackPlayed(View rootView) {
        TextView playback_list_title = (TextView)rootView.findViewById(R.id.playback_title).findViewById(R.id.list_title);
        TextView playback_title_info = (TextView)rootView.findViewById(R.id.playback_title).findViewById(R.id.list_info);
        playback_list_title.setText("プレイバック");
        playback_title_info.setText("あなたが過去数ヶ月間で一番聴いた音楽");

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.YEAR, -1);
        String oneYearBefore = dateFormat.format(cal1.getTime());

        String sql = "SELECT album, album_art_path, count(*) AS COUNT FROM count_and_date_table  WHERE date BETWEEN '" + oneYearBefore + "' AND '" + today + "' GROUP BY artist, album ORDER BY COUNT DESC;";
        String[][] thisYear = getSQL(sql, "album");

        setViewList(playbackViewImageList, playbackViewTextList, thisYear);

    }


    public void thisMonthPlayed(View rootView) {
        TextView this_month_fav_list_title = (TextView)rootView.findViewById(R.id.this_month_fav_title).findViewById(R.id.list_title);
        TextView this_month_fav_list_title_info = (TextView)rootView.findViewById(R.id.this_month_fav_title).findViewById(R.id.list_info);
        this_month_fav_list_title.setText("最近のお気に入り");
        this_month_fav_list_title_info.setText("あなたが今月リピートで聴いた音楽");

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.MONTH, -1);
        String oneMonthBefore = dateFormat.format(cal1.getTime());

        String sql = "SELECT album, album_art_path, count(*) AS COUNT FROM count_and_date_table  WHERE date BETWEEN '" + oneMonthBefore + "' AND '" + today + "' GROUP BY artist, album ORDER BY COUNT DESC;";
        String[][] thisMonth = getSQL(sql, "album");
        setViewList(thisMonthFavViewImageList, thisMonthFavViewTextList, thisMonth);

    }


    public void recentlyPlayed (View rootView) {
        TextView recent_history_title = (TextView)rootView.findViewById(R.id.recent_history_title).findViewById(R.id.list_title);
        TextView recent_history_title_info = (TextView)rootView.findViewById(R.id.recent_history_title).findViewById(R.id.list_info);
        recent_history_title.setText("最近再生した曲");
        recent_history_title_info.setText("");

        String sql = "SELECT song, album_art_path, id from count_table ORDER BY id DESC;";
        String[][] recent = getSQL(sql, "song");
        setViewList(recentlyViewImageList, recentlyViewTextList, recent);

    }

    private void setViewList(ImageView[] ImageViewList, TextView[] ViewList, String[][] playHistoryList) {
        Drawable drawable;


        for (int i = 0; i < 6; i++) {
            Bitmap bmp = BitmapFactory.decodeFile(playHistoryList[i][1]);
            ImageViewList[i].setImageBitmap(bmp);


            ViewList[i].setText(playHistoryList[i][0]);


        }
    }




    private void setAndGetViewID (View rootView) {
        int[] list_txts = {R.id.list_txt1, R.id.list_txt2, R.id.list_txt3, R.id.list_txt4, R.id.list_txt5, R.id.list_txt6};
        int[] list_imgs = {R.id.list_img1, R.id.list_img2, R.id.list_img3, R.id.list_img4, R.id.list_img5, R.id.list_img6};
        for (int i = 0; i < 6; i++) {
            recentlyViewTextList[i] = (TextView) rootView.findViewById(R.id.recent_history).findViewById(list_txts[i]);
            recentlyViewImageList[i] = (ImageView) rootView.findViewById(R.id.recent_history).findViewById(list_imgs[i]);

            thisMonthFavViewTextList[i] = (TextView)rootView.findViewById(R.id.this_month_fav).findViewById(list_txts[i]);
            thisMonthFavViewImageList[i] = (ImageView) rootView.findViewById(R.id.this_month_fav).findViewById(list_imgs[i]);

            playbackViewTextList[i] = (TextView) rootView.findViewById(R.id.playback).findViewById(list_txts[i]);
            playbackViewImageList[i] = (ImageView) rootView.findViewById(R.id.playback).findViewById(list_imgs[i]);

            myFavoriteViewTextList[i] = (TextView) rootView.findViewById(R.id.my_favorite).findViewById(list_txts[i]);
            myFavoriteViewImageList[i] = (ImageView) rootView.findViewById(R.id.my_favorite).findViewById(list_imgs[i]);
        }
    }

}
