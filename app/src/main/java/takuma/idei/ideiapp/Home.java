package takuma.idei.ideiapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        String song = "";
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
                song = cursor.getString(cursor.getColumnIndex("song"));
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



        return rootView;
    }



    private void readDB() throws Exception {



        /*
        String str = "";
        String[][] ranking = new String[3][4];
        int i = 0;

        Cursor cursor = mDb.query(
                "count_table", new String[] {"song", "artist", "album", "count"},
                null, null, null, null, "count desc");
        // 参照先を一番始めに
        boolean isEof = cursor.moveToFirst();
        while(isEof) {

            str = cursor.getString(cursor.getColumnIndex("song"));
            if (i < 4) {
                ranking[i][0] = cursor.getString(cursor.getColumnIndex("song"));
                ranking[i][1] = cursor.getString(cursor.getColumnIndex("artist"));
                ranking[i][2] = cursor.getString(cursor.getColumnIndex("album"));
                ranking[i][3] = cursor.getString(cursor.getColumnIndex("count"));
            }
            i++;

            isEof = cursor.moveToNext();
        }
        cursor.close();
        */
    }

}
