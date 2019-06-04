package takuma.idei.ideiapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlayDataBase extends SQLiteOpenHelper {
    static final String DB_NAME = "count.db";

    static final int DB_VERSION = 1;

    static final String CREATE_TABLE = "create table count_table (id integer primary key AUTOINCREMENT, song text, artist text, album text, album_art_path text, count integer);";
    static final String DROP_TABLE = "drop table count_table;";


    public PlayDataBase(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
    }

    /**
     * データベースファイル初回使用時に実行される処理
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成のクエリを発行
        db.execSQL(CREATE_TABLE);
        db.execSQL("INSERT INTO count_table (song, artist, album, album_art_path, count) VALUES ('song', 'artist', 'album', 'album_art_path', '0');");
        db.execSQL("INSERT INTO count_table (song, artist, album, album_art_path, count) VALUES ('song', 'artist', 'album', 'album_art_path', '0');");
        db.execSQL("INSERT INTO count_table (song, artist, album, album_art_path, count) VALUES ('song', 'artist', 'album', 'album_art_path', '0');");
        db.execSQL("INSERT INTO count_table (song, artist, album, album_art_path, count) VALUES ('song', 'artist', 'album', 'album_art_path', '0');");
        db.execSQL("INSERT INTO count_table (song, artist, album, album_art_path, count) VALUES ('song', 'artist', 'album', 'album_art_path', '0');");
        db.execSQL("INSERT INTO count_table (song, artist, album, album_art_path, count) VALUES ('song', 'artist', 'album', 'album_art_path', '0');");
        db.execSQL("INSERT INTO count_table (song, artist, album, album_art_path, count) VALUES ('song', 'artist', 'album', 'album_art_path', '0');");
        db.execSQL("INSERT INTO count_table (song, artist, album, album_art_path, count) VALUES ('song', 'artist', 'album', 'album_art_path', '0');");
        db.execSQL("INSERT INTO count_table (song, artist, album, album_art_path, count) VALUES ('song', 'artist', 'album', 'album_art_path', '0');");

    }

    /**
     * データベースのバージョンアップ時に実行される処理
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // テーブルの破棄と再作成
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

}