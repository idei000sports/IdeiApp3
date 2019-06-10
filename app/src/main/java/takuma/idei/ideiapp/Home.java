package takuma.idei.ideiapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    /*
        やりたいこと
        日付で3日セレクト
        アルバム名の件数を取得



     */



    private ImageView[] recentlyImg = new ImageView[6];
    private TextView[] recentlyTxt = new TextView[6];
    private SQLiteDatabase sqLiteDatabase;
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        PlayDataBase playDataBase = new PlayDataBase(getActivity());
        sqLiteDatabase = playDataBase.getWritableDatabase();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setViewetc(rootView);
        recentlyPlayed();
        dateTest(rootView);

        return rootView;
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void dateTest(View rootView) {

        TextView textView = rootView.findViewById(R.id.SQLTest);
        //WHERE date BETWEEN '2019-06-10 16:33:00' AND '2019-06-10 16:39:00'
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -7);
        String oneWeekBefore = dateFormat.format(cal1.getTime());

        Date date1 = new Date();
        String today = dateFormat.format(date1);

        TextView oneweek = rootView.findViewById(R.id.oneweek);
        oneweek.setText(oneWeekBefore);

        TextView todayView = rootView.findViewById(R.id.today);
        todayView.setText(today);





        String sql = "SELECT artist, album, count(*) AS COUNT FROM count_and_date_table WHERE date BETWEEN '" + oneWeekBefore + "' AND '" + today + "' GROUP BY artist, album ORDER BY COUNT DESC;";


        String date = "";
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            boolean next = cursor.moveToNext();
            if(next) {
                date = cursor.getString(cursor.getColumnIndex("COUNT"));
                next = cursor.moveToNext();
            }
            //2019-06-10 16:15:12
            //2019-06-10 16:04:46

        }catch (Exception e) {

        }

        textView.setText(date);
    }


    public void recentlyPlayed () {
        String[][] recent = new String[6][2];
        try (Cursor cursor = sqLiteDatabase.rawQuery("SELECT song, album_art_path, id from count_table ORDER BY id DESC;", null)) {
            boolean next = cursor.moveToNext();
            int i = 0;
            while (next) {
                if (i < 6) {
                    String song = cursor.getString(cursor.getColumnIndex("song"));
                    String albumArtPath = cursor.getString(cursor.getColumnIndex("album_art_path"));

                    recent[i][0] = song;
                    recent[i][1] = albumArtPath;
                }
                i++;
                next = cursor.moveToNext();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 6; i++) {
            recentlyTxt[i].setText(recent[i][0]);
            Bitmap bmImg = BitmapFactory.decodeFile(recent[i][1]);
            recentlyImg[i].setImageBitmap(bmImg);
        }


    }



    public void setViewetc (View rootView) {

        int[] recentlyImgIDs = {R.id.recently1art, R.id.recently2art, R.id.recently3art, R.id.recently4art, R.id.recently5art, R.id.recently6art};
        int[] recentlyTxtIDs = {R.id.recently1text, R.id.recently2text, R.id.recently3text, R.id.recently4text, R.id.recently5text, R.id.recently6text};
        for (int i = 0; i < 6; i++) {
            recentlyImg[i] = rootView.findViewById(recentlyImgIDs[i]);
            recentlyTxt[i] = rootView.findViewById(recentlyTxtIDs[i]);
        }

    }

}
