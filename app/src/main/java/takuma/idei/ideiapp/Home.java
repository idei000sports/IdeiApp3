package takuma.idei.ideiapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Fragment {
    private TextView song1;
    private TextView song2;
    private TextView song3;
    private TextView artist1;
    private TextView artist2;
    private TextView artist3;
    private TextView album1;
    private TextView album2;
    private TextView album3;
    private TextView count1;
    private TextView count2;
    private TextView count3;
    private ImageView recentlyImg1;
    private TextView recentlyText1;


    private ImageView[] recentlyImg = new ImageView[6];
    private TextView[] recentlyTxt = new TextView[6];

    private TextView SQLText;
    private SQLiteDatabase mDb;
    static final String TAG = "MainActivity";
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        PlayDataBase hlpr = new PlayDataBase(getActivity());
        mDb = hlpr.getWritableDatabase();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setViewetc(rootView);
        rankingPlayed();
        recentlyPlayed();






        return rootView;
    }


    public void recentlyPlayed () {
        String[][] recent = new String[6][2];
        try {
            Cursor cursor = mDb.rawQuery("SELECT song, album_art_path, id from count_table ORDER BY id DESC;", null);
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

        }

        for (int i = 0; i < 6; i++) {
            recentlyTxt[i].setText(recent[i][0]);
            Bitmap bmImg = BitmapFactory.decodeFile(recent[i][1]);
            recentlyImg[i].setImageBitmap(bmImg);
        }


    }
    public void rankingPlayed() {
        String[][] ranking = new String[3][4];
        try {
            // rawQueryというSELECT専用メソッドを使用してデータを取得する
            Cursor cursor = mDb.rawQuery("select song, artist, album, count from count_table ORDER BY count DESC;", null);
            // Cursorの先頭行があるかどうか確認
            boolean next = cursor.moveToFirst();
            int i = 0;
            // 取得した全ての行を取得
            while (next) {
                // 取得したカラムの順番(0から始まる)と型を指定してデータを取得する
                if (i < 3) {
                    ranking[i][0] = cursor.getString(cursor.getColumnIndex("song"));
                    ranking[i][1] = cursor.getString(cursor.getColumnIndex("artist"));
                    ranking[i][2] = cursor.getString(cursor.getColumnIndex("album"));
                    ranking[i][3] = cursor.getString(cursor.getColumnIndex("count"));
                }
                i++;
                // 次の行が存在するか確認
                next = cursor.moveToNext();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "エクセプション", Toast.LENGTH_SHORT).show();
            System.out.println(e);
        } finally {
            // finallyは、tryの中で例外が発生した時でも必ず実行される
            // dbを開いたら確実にclose
        }
        song1.setText(ranking[0][0]);
        song2.setText(ranking[1][0]);
        song3.setText(ranking[2][0]);
        artist1.setText(ranking[0][1]);
        artist2.setText(ranking[1][1]);
        artist3.setText(ranking[2][1]);
        album1.setText(ranking[0][2]);
        album2.setText(ranking[1][2]);
        album3.setText(ranking[2][2]);
        count1.setText(ranking[0][3]);
        count2.setText(ranking[1][3]);
        count3.setText(ranking[2][3]);
    }


    public void setViewetc (View rootView) {
        SQLText = rootView.findViewById(R.id.SQLText);
        song1 = rootView.findViewById(R.id.song1);
        song2 = rootView.findViewById(R.id.song2);
        song3 = rootView.findViewById(R.id.song3);
        artist1 = rootView.findViewById(R.id.artist1);
        artist2 = rootView.findViewById(R.id.artist2);
        artist3 = rootView.findViewById(R.id.artist3);
        album1 = rootView.findViewById(R.id.album1);
        album2 = rootView.findViewById(R.id.album2);
        album3 = rootView.findViewById(R.id.album3);
        count1 = rootView.findViewById(R.id.count1);
        count2 = rootView.findViewById(R.id.count2);
        count3 = rootView.findViewById(R.id.count3);
        recentlyImg1 = rootView.findViewById(R.id.recently1art);
        recentlyText1 = rootView.findViewById(R.id.recently1text);


        int[] recentlyImgIDs = {R.id.recently1art, R.id.recently2art, R.id.recently3art, R.id.recently4art, R.id.recently5art, R.id.recently6art};
        int[] recentlyTxtIDs = {R.id.recently1text, R.id.recently2text, R.id.recently3text, R.id.recently4text, R.id.recently5text, R.id.recently6text};
        for (int i = 0; i < 6; i++) {
            recentlyImg[i] = rootView.findViewById(recentlyImgIDs[i]);
            recentlyTxt[i] = rootView.findViewById(recentlyTxtIDs[i]);
        }

    }

}
