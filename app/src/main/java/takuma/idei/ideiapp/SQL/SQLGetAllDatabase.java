package takuma.idei.ideiapp.SQL;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import takuma.idei.ideiapp.R;

public class SQLGetAllDatabase extends Activity {
    String sql;
    ListView listView;
    ArrayList<String> arr;
    ArrayAdapter<String> adapter;

    public void getSQLDatabase(String want) {
        sql = "SELECT * FROM " + want + ";";
        arr = new ArrayList<>();
        String one; String two; String three; String four; String five;

        SQLAlbumAndSongTableHelper sqlAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(this);
        SQLiteDatabase sqLiteDatabase = sqlAlbumAndSongTableHelper.getReadableDatabase();
        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {
            cursor.moveToFirst();
            if (cursor != null && cursor.getCount() > 0) {
                boolean next = cursor.moveToNext();
                while (next) {
                    switch (want) {
                        case "count_table":
                            one = cursor.getString(cursor.getColumnIndex("song_title"));
                            two = cursor.getString(cursor.getColumnIndex("artist_name"));
                            three = cursor.getString(cursor.getColumnIndex("album_title"));
                            four = cursor.getString(cursor.getColumnIndex("album_art_path"));
                            five = cursor.getString(cursor.getColumnIndex("song_path"));
                            arr.add("1" + one + "\n 2" + two + "\n 3" + three + "\n 4" + four + "\n 5" + five);
                            break;
                        case "count_and_date_table":
                            one = cursor.getString(cursor.getColumnIndex("artist_name"));
                            two = cursor.getString(cursor.getColumnIndex("album_title"));
                            three = cursor.getString(cursor.getColumnIndex("album_art_path"));
                            four = cursor.getString(cursor.getColumnIndex("date"));
                            five = cursor.getString(cursor.getColumnIndex("song_path"));
                            arr.add("1" + one + "\n 2" + two + "\n 3" + three + "\n 4" + four + "\n 5" + five);
                            break;

                        case "album":
                            one = cursor.getString(cursor.getColumnIndex("artist_name"));
                            two = cursor.getString(cursor.getColumnIndex("album_title"));
                            three = cursor.getString(cursor.getColumnIndex("album_path"));
                            four = cursor.getString(cursor.getColumnIndex("album_art_path"));

                            arr.add("1" + one + "\n 2" + two + "\n 3" + three + "\n 4" + four);
                            break;
                        case "song":
                            one = cursor.getString(cursor.getColumnIndex("song_title"));
                            two = cursor.getString(cursor.getColumnIndex("artist_name"));
                            three = cursor.getString(cursor.getColumnIndex("album_title"));
                            four = cursor.getString(cursor.getColumnIndex("song_path"));
                            five = cursor.getString(cursor.getColumnIndex("track_number"));
                            arr.add("1" + one + "\n 2" + two + "\n 3" + three + "\n 4" + four + "\n 5" + five);
                            break;


                    }



                    next = cursor.moveToNext();

                }
            }
        }catch (Exception e) {

        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr);
        listView.setAdapter(adapter);

    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_database);

        findViewById(R.id.count_table_button).setOnClickListener(this::onClick);
        findViewById(R.id.count_and_date_table_button).setOnClickListener(this::onClick);
        findViewById(R.id.song_button).setOnClickListener(this::onClick);
        findViewById(R.id.album_button).setOnClickListener(this::onClick);
        listView = findViewById(R.id.sql_list);


        

    }
    public void onClick(View v) {
        if(v != null) {
            try {
                switch (v.getId()) {
                    case R.id.count_table_button:
                        Toast.makeText(this, "countablebutton", Toast.LENGTH_SHORT).show();
                        getSQLDatabase("count_table");
                        //5
                        break;
                    case R.id.count_and_date_table_button:
                        getSQLDatabase("count_and_date_table");
                        //5
                       
                        break;
                    case R.id.song_button:
                        getSQLDatabase("song");
                        //5
                      
                        break;
                    case R.id.album_button:
                        getSQLDatabase("album");
                        //4
                       
                        break;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
