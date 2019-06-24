package takuma.idei.ideiapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import takuma.idei.ideiapp.SQL.SQLAlbumAndSongTableHelper;

public class MakeHistory {

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    public void makeCountDataTable(Context context, String artist_name, String album_name, String albumArtPath, String songPath) {
        SQLAlbumAndSongTableHelper albumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = albumAndSongTableHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("artist_name", artist_name);
            values.put("album_title", album_name);
            values.put("album_art_path", albumArtPath);
            values.put("date", getDateTime());
            values.put("song_path", songPath);

            //artist_name, album_title, album_art_path, date, song_path,;
            sqLiteDatabase.insert("count_and_date_table", null, values);
        }  catch (Exception e) {
            e.printStackTrace();
            System.out.println("makeCountDataTable :eraaaaaaa");
        }

        albumAndSongTableHelper.close();

    }



    public void makeCountTable(Context context, String title_name, String artist_name, String album_name, String albumArtPath, String songPath) {
        int count = 0;
        SQLAlbumAndSongTableHelper sqlAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = sqlAlbumAndSongTableHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String sql = "SELECT count FROM count_table WHERE song_title = '" + title_name + "' AND artist_name = '" + artist_name + "';";


        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {

            //再生回数が記録されているかチェック、なければcountは0のまま
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        sql = "SELECT MAX(id) FROM count_table;";

        if (count > 0) {
            count += 1;
            int nextAutoIncrement = 0;

            try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {
                if (cursor.moveToNext()) {
                    nextAutoIncrement = cursor.getInt(cursor.getColumnIndex("MAX(id)")) + 1;
                }


                values.put("count", count);
                values.put("id", nextAutoIncrement);

                sqLiteDatabase.update("count_table", values, "song_title = ? AND artist_name = ? AND album_title = ?", new String[]{title_name, artist_name, album_name});
            }

        } else if (count == 0) {
            values.put("song_title", title_name);
            values.put("artist_name", artist_name);
            values.put("album_title", album_name);
            values.put("album_art_path", albumArtPath);
            values.put("count", 1);
            values.put("song_path", songPath);
            sqLiteDatabase.insert("count_table", null, values);
        }

        sqlAlbumAndSongTableHelper.close();
    }
}
