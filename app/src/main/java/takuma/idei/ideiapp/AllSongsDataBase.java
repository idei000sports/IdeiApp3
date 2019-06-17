package takuma.idei.ideiapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class AllSongsDataBase {


    public ArrayList<AlbumBean> getArtists(Context context) {
        ArrayList<AlbumBean> artists = new ArrayList<>();
        SQLAlbumAndSongTableHelper SQLAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLAlbumAndSongTableHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = null;
        int count = 0;
        try {
            String sql = "SELECT * FROM album;";
            cursor = sqLiteDatabase.rawQuery(sql, null);

            boolean isEof = cursor.moveToFirst();

            while (isEof) {
                String album_title = cursor.getString(cursor.getColumnIndex("album_title"));
                String artist_name = cursor.getString(cursor.getColumnIndex("artist_name"));
                String album_art_path = cursor.getString(cursor.getColumnIndex("album_art_path"));
                String album_path = cursor.getString(cursor.getColumnIndex("album_path"));
                AlbumBean albumBean = new AlbumBean(artist_name,album_title,album_art_path, album_path);
                artists.add(albumBean);
                isEof = cursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

        return artists;

    }


    public ArrayList<String> getSong(Context context) {
        ArrayList<String> songs = new ArrayList<>();
        SQLAlbumAndSongTableHelper SQLAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLAlbumAndSongTableHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = null;
        int count = 0;
        try {
            String sql = "SELECT * FROM song;";
            cursor = sqLiteDatabase.rawQuery(sql, null);

            boolean isEof = cursor.moveToFirst();

            while (isEof) {
                String song = cursor.getString(cursor.getColumnIndex("song_title"));
                String trackn = cursor.getString(cursor.getColumnIndex("track_number"));
                songs.add(trackn + song);
                isEof = cursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

        return songs;
    }





    public void putSong(Context context, String artist_name, String song_title, String album_title, String song_path, String track_number){
        checkSong(context, artist_name, song_title, album_title, song_path, track_number);
    }


    public void putAlbum(Context context, String artist_name, String album_title, String album_path, String album_art_path){
        checkAlbum(context, artist_name, album_title, album_path, album_art_path);
    }

    public void checkAlbum(Context context, String artist_name, String album_title, String album_path, String album_art_path) {
        SQLAlbumAndSongTableHelper SQLAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLAlbumAndSongTableHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = null;
        int count = 0;
        try {
            String sql = "SELECT count(*) FROM album WHERE album_title = '" + album_title + "' AND artist_name = '" + artist_name + "';";
            cursor = sqLiteDatabase.rawQuery(sql, null);

            //再生回数が記録されているかチェック、なければcountは0のまま
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count(*)"));
            }

            if (count > 0) {
                values = new ContentValues();
                values.put("album_path", album_path);
                values.put("album_art_path", album_art_path);
                sqLiteDatabase.update("album", values, "album_title = ? AND artist_name = ?", new String[]{album_title, artist_name});

            } else if (count == 0) {
                values = new ContentValues();
                values.put("album_title", album_title);
                values.put("artist_name", artist_name);
                values.put("album_art_path", album_art_path);
                values.put("album_path", album_path);
                sqLiteDatabase.insert("album", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
    }


    public void checkSong(Context context, String artist_name, String song_title, String album_title, String song_path, String track_number) {
        SQLAlbumAndSongTableHelper SQLAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLAlbumAndSongTableHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = null;
        int count = 0;
        try {
            String sql = "SELECT count(*) FROM song WHERE song_title = '" + song_title + "' AND artist_name = '" + artist_name + "';";
            cursor = sqLiteDatabase.rawQuery(sql, null);

            //再生回数が記録されているかチェック、なければcountは0のまま
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count(*)"));
            }

            if (count > 0) {
                values = new ContentValues();
                values.put("song_path", song_path);
                sqLiteDatabase.update("song", values, "song_title = ? AND artist_name = ? AND album_title = ?", new String[]{song_title, artist_name, album_title});

            } else if (count == 0) {
                values = new ContentValues();
                values.put("song_title", song_title);
                values.put("artist_name", artist_name);
                values.put("album_title", album_title);
                values.put("song_path", song_path);
                values.put("track_number", track_number);
                sqLiteDatabase.insert("song", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
    }
}



