package takuma.idei.ideiapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLAlbumAndSongTableHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "albumAndSong.db";

    static final int DB_VERSION = 1;

    static final String CREATE_ALBUM_TABLE = "create table album(artist_name text, album_title text, album_path text, album_art_path text);";
    static final String CREATE_SONG_TABLE = "create table song(song_title text, artist_name text, album_title text, song_path text, track_number text);";

    static final String DROP_ALBUM_TABLE = "drop table album;";
    static final String DROP_SONG_TABLE = "drop table song;";


    public SQLAlbumAndSongTableHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
    }

    /**
     * データベースファイル初回使用時に実行される処理
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成のクエリを発行
        db.execSQL(CREATE_ALBUM_TABLE);
        db.execSQL(CREATE_SONG_TABLE);
    }

    /**
     * データベースのバージョンアップ時に実行される処理
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // テーブルの破棄と再作成
        db.execSQL(DROP_ALBUM_TABLE);
        db.execSQL(DROP_SONG_TABLE);
        onCreate(db);
    }

}
