package takuma.idei.ideiapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MakeHistory {

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    public void makeCountDataTable(Context context, String artist_name, String album_name, String albumArtPath, String songPath) {
        SQLPlayDataBaseHelper SQLPlayDataBaseHelper = new SQLPlayDataBaseHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLPlayDataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("artist", artist_name);
            values.put("album", album_name);
            values.put("album_art_path", albumArtPath);
            values.put("date", getDateTime());
            values.put("path", songPath);
            sqLiteDatabase.insert("count_and_date_table", null, values);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void makeCountTable(Context context, String title_name, String artist_name, String album_name, String albumArtPath, String songPath) {
        int count = 0;
        SQLPlayDataBaseHelper SQLPlayDataBaseHelper = new SQLPlayDataBaseHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLPlayDataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = null;
        try {
            String sql = "SELECT count FROM count_table WHERE song = '" + title_name + "' AND artist = '" + artist_name + "';";
            cursor = sqLiteDatabase.rawQuery(sql, null);

            //再生回数が記録されているかチェック、なければcountは0のまま
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }

            if (count > 0) {
                count += 1;
                int nextAutoIncrement = 0;
                sql = "SELECT MAX(id) FROM count_table;";
                cursor = sqLiteDatabase.rawQuery(sql, null);
                if (cursor.moveToNext()) {
                    nextAutoIncrement = cursor.getInt(cursor.getColumnIndex("MAX(id)")) + 1;
                }

                values.put("count", count);
                values.put("id", nextAutoIncrement);

                sqLiteDatabase.update("count_table", values, "song = ? AND artist = ? AND album = ?", new String[]{title_name, artist_name, album_name});

            } else if (count == 0) {
                values.put("song", title_name);
                values.put("artist", artist_name);
                values.put("album", album_name);
                values.put("album_art_path", albumArtPath);
                values.put("count", 1);
                values.put("path", songPath);
                sqLiteDatabase.insert("count_table", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
    }
}
