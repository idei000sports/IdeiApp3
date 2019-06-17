package takuma.idei.ideiapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLPlayDataBaseHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "counter.db";

    static final int DB_VERSION = 1;

    static final String CREATE_COUNT_TABLE = "create table count_table (id integer primary key AUTOINCREMENT, song text, artist text, album text, album_art_path text, count integer, path text);";
    static final String CREATE_COUNT_AND_DATE_TABLE = "CREATE TABLE count_and_date_table (id integer PRIMARY KEY AUTOINCREMENT, artist text, album text, album_art_path text, date text, path text);";
    static final String DROP_COUNT_TABLE = "drop table count_table;";
    static final String DROP_COUNT_AND_DATE_TABLE = "DROP TABLE count_and_date_table;";


    public SQLPlayDataBaseHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
    }

    /**
     * データベースファイル初回使用時に実行される処理
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成のクエリを発行
        db.execSQL(CREATE_COUNT_TABLE);
        for (int i = 0; i < 10; i++) {
            db.execSQL("INSERT INTO count_table (song, artist, album, album_art_path, count, path) VALUES ('song', 'artist', 'album', 'album_art_path', '0', 'path');");
        }
        db.execSQL(CREATE_COUNT_AND_DATE_TABLE);
        for (int i = 0; i < 10; i++) {
            db.execSQL("INSERT INTO count_and_date_table (artist, album, album_art_path, date, path) VALUES ('artist', 'album', 'album_art_path', datetime('now', 'localtime'), 'path');");
        }

    }

    /**
     * データベースのバージョンアップ時に実行される処理
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // テーブルの破棄と再作成
        db.execSQL(DROP_COUNT_TABLE);
        db.execSQL(DROP_COUNT_AND_DATE_TABLE);
        onCreate(db);
    }

}