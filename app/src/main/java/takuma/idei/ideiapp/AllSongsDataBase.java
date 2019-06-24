package takuma.idei.ideiapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import takuma.idei.ideiapp.Bean.AlbumBean;
import takuma.idei.ideiapp.Bean.SongBean;
import takuma.idei.ideiapp.SQL.SQLAlbumAndSongTableHelper;

public class AllSongsDataBase {


    public SongBean getPopluarSongPath(Context context, String artist_name, String song_title) {
        String song_path = "";
        String album_art_path = "";

        SQLAlbumAndSongTableHelper SQLAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLAlbumAndSongTableHelper.getReadableDatabase();
        String sql = "SELECT song_path, album_art_path FROM count_table WHERE artist_name = '" + artist_name + "' AND song_title = '" + song_title + "';";
        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {
            boolean isEof = cursor.moveToNext();
            while (isEof) {
                album_art_path = cursor.getString(cursor.getColumnIndex("album_art_path"));
                song_path = cursor.getString(cursor.getColumnIndex("song_path"));
                isEof = cursor.moveToNext();
            }
            cursor.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        SQLAlbumAndSongTableHelper.close();

        SongBean songBean = new SongBean();
        songBean.setSong_path(song_path);
        songBean.setAlbum_art_path(album_art_path);

        return songBean;
    }


    public ArrayList<AlbumBean> getAlbumFromArtist(Context context, String artist_name) {
        ArrayList<AlbumBean> album_list = new ArrayList<>();
        SQLAlbumAndSongTableHelper SQLAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLAlbumAndSongTableHelper.getReadableDatabase();
        String sql = "SELECT album_art_path, album_title FROM album WHERE artist_name = '" + artist_name + "';";
        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {
            boolean isEof = cursor.moveToNext();
            while (isEof) {
                String album_title = cursor.getString(cursor.getColumnIndex("album_title"));
                String album_art_path = cursor.getString(cursor.getColumnIndex("album_art_path"));
                AlbumBean albumBean = new AlbumBean();
                albumBean.setAlbumTitle(album_title);
                albumBean.setAlbumArt(album_art_path);
                album_list.add(albumBean);
                isEof = cursor.moveToNext();
            }
            cursor.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        SQLAlbumAndSongTableHelper.close();

        return album_list;

    }

    public String getAlbumArtWork(Context context, String artist_name, String album_title) {
        String album_art_path = "";
        SQLAlbumAndSongTableHelper SQLAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLAlbumAndSongTableHelper.getReadableDatabase();
        String sql = "SELECT album_art_path FROM album WHERE artist_name = '" + artist_name + "' AND album_title = '" + album_title + "';";
        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {
            boolean isEof = cursor.moveToNext();
            while (isEof) {
                album_art_path = cursor.getString(cursor.getColumnIndex("album_art_path"));
                isEof = cursor.moveToNext();
            }
            cursor.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        SQLAlbumAndSongTableHelper.close();

        return album_art_path;
    }




    public ArrayList<SongBean> getAlbumSong(Context context, String artist_name, String album_title) {
        ArrayList<SongBean> songBeanList = new ArrayList<>();
        SQLAlbumAndSongTableHelper SQLAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLAlbumAndSongTableHelper.getReadableDatabase();
        String sql = "SELECT * FROM song WHERE artist_name = '" + artist_name + "' AND album_title = '" + album_title + "' ORDER BY track_number ASC;";
        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {
            boolean isEof = cursor.moveToNext();
            while (isEof) {
                String getedSong_title = cursor.getString(cursor.getColumnIndex("song_title"));
                String getedArtist_name = cursor.getString(cursor.getColumnIndex("artist_name"));
                String getedAlbum_title = cursor.getString(cursor.getColumnIndex("album_title"));
                String getedSong_path = cursor.getString(cursor.getColumnIndex("song_path"));
                String getedTrack_number = cursor.getString(cursor.getColumnIndex("track_number"));


                SongBean songBean = new SongBean();
                songBean.setSong_title(getedSong_title);
                songBean.setArtist_name(getedArtist_name);
                songBean.setAlbum_title(getedAlbum_title);
                songBean.setSong_path(getedSong_path);
                songBean.setTrack_number(getedTrack_number);
                songBeanList.add(songBean);
                isEof = cursor.moveToNext();
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }

        SQLAlbumAndSongTableHelper.close();


        return songBeanList;
    }

    public ArrayList<SongBean> getPopularSong(Context context, String artist) {
        ArrayList<SongBean> songBeanList = new ArrayList<SongBean>();
        SQLAlbumAndSongTableHelper SQLAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLAlbumAndSongTableHelper.getReadableDatabase();


        String sql = "select song_title, song_path, count from count_table WHERE artist_name = '" + artist + "' ORDER BY count DESC;";
        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {

            boolean isEof = cursor.moveToNext();

            while (isEof) {
                String song_title = cursor.getString(cursor.getColumnIndex("song_title"));
                String song_path = cursor.getString(cursor.getColumnIndex("song_path"));
                int play_count = cursor.getInt(cursor.getColumnIndex("count"));
                SongBean songBean = new SongBean();
                songBean.setSong_title(song_title);
                songBean.setSong_path(song_path);
                songBean.setPlaycount(play_count);
                songBeanList.add(songBean);

                isEof = cursor.moveToNext();
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }

        SQLAlbumAndSongTableHelper.close();


        return songBeanList;
    }



    public ArrayList<AlbumBean> getAlbumTable(Context context) {
        ArrayList<AlbumBean> artists = new ArrayList<>();
        SQLAlbumAndSongTableHelper SQLAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLAlbumAndSongTableHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        int count = 0;
        String sql = "SELECT * FROM album;";

        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {

            boolean isEof = cursor.moveToFirst();

            while (isEof) {
                String artist_name = cursor.getString(cursor.getColumnIndex("artist_name"));
                String album_title = cursor.getString(cursor.getColumnIndex("album_title"));
                String album_art_path = cursor.getString(cursor.getColumnIndex("album_art_path"));
                String album_path = cursor.getString(cursor.getColumnIndex("album_path"));

                AlbumBean albumBean = new AlbumBean(artist_name, album_title, album_art_path, album_path);
                artists.add(albumBean);
                isEof = cursor.moveToNext();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        SQLAlbumAndSongTableHelper.close();
        return artists;

    }






    public void putSong(Context context, String artist_name, String song_title, String album_title, String song_path, String track_number){
        checkSong(context, artist_name, song_title, album_title, song_path, track_number);
    }


    public void putAlbum(Context context, String artist_name, String album_title, String album_path, String album_art_path){
        checkAlbum(context, artist_name, album_title, album_path, album_art_path);
    }

    private static String escape(String value) {
        value = value.replaceAll("'", "''");
        return value;
    }


    public void checkAlbum(Context context, String artist_name, String album_title, String album_path, String album_art_path) {



        SQLAlbumAndSongTableHelper SQLAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLAlbumAndSongTableHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        int count = 0;
        String sql = "SELECT count(*) FROM album WHERE album_title = '" + escape(album_title) + "'  AND artist_name = '" + escape(artist_name) + "';";


        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {

            //再生回数が記録されているかチェック、なければcountは0のまま
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count(*)"));
            }
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
        SQLAlbumAndSongTableHelper.close();
    }



    public void checkSong(Context context, String artist_name, String song_title, String album_title, String song_path, String track_number) {
        /*
        artist_name = escape(artist_name);
        song_title = escape(song_title);
        album_title = escape(album_title);
        song_path = escape(song_path);
        */


        SQLAlbumAndSongTableHelper SQLAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(context);
        SQLiteDatabase sqLiteDatabase = SQLAlbumAndSongTableHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        int count = 0;
        String sql = "SELECT count(*) FROM song WHERE song_title = '" + escape(song_title) + "' AND artist_name = '" + escape(artist_name) + "';";


        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {

            //再生回数が記録されているかチェック、なければcountは0のまま
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count(*)"));
            }
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
        SQLAlbumAndSongTableHelper.close();
    }
}



