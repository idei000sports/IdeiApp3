package takuma.idei.ideiapp;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MusicPlayerService extends Service {
    //プレーヤー
    public static MediaPlayer mediaPlayer;
    //プレイリスト
    private List<String> playList;
    //次の番号
    private int next_track_number;
    //タグ
    public static String artist_name;
    public static String album_name;
    public static String title_name;
    public static String albumArtPath;
    //曲の長さ
    public static int totalTime;
    //現在の再生位置
    public static int currentPosition;
    //再生中か
    public static boolean playingNow = false;
    //リピートを設定しているか
    private boolean REPEAT = false;


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.seekTo(0);
    }
    //メイン、作ったプレイリストを再生
    private void playPlayList() {
        String songPath = playList.get(next_track_number);
        next_track_number += 1;
        makeSongData(songPath);

        playSong(songPath);

        //再生が終わったとき
        mediaPlayer.setOnCompletionListener(mp -> {
            makeHistory();
            makeHistoryDate();
            stopSong();
            if(REPEAT) next_track_number -= 1;
            playPlayList();
        });
    }
    //履歴の作成
    private void makeHistory() {
        int count = 0;
        PlayDataBase playDataBase = new PlayDataBase(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = playDataBase.getWritableDatabase();
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
                if(cursor.moveToNext()) {
                    nextAutoIncrement = cursor.getInt(cursor.getColumnIndex("MAX(id)")) + 1;
                }

                values.put("count", count);
                values.put("id", nextAutoIncrement);

                sqLiteDatabase.update("count_table", values, "song = ? AND artist = ? AND album = ?", new String[]{title_name, artist_name, album_name});

            } else if (count == 0){
                values.put("song", title_name);
                values.put("artist", artist_name);
                values.put("album", album_name);
                values.put("album_art_path", albumArtPath);
                values.put("count", 1);
                sqLiteDatabase.insert("count_table", null, values);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

    }

    private void makeHistoryDate() {
        PlayDataBase playDataBase = new PlayDataBase(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = playDataBase.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("artist", artist_name);
            values.put("album", album_name);
            values.put("album_art_path", albumArtPath);
            values.put("date", getDateTime());
            sqLiteDatabase.insert("count_and_date_table", null, values);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    //タグ情報をstaticフィールドに
    private void makeSongData(String songPath) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(songPath);
            title_name = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            artist_name = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            album_name = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //再生
    private void playSong(String songPath) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(songPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            totalTime = mediaPlayer.getDuration();
            currentPosition = mediaPlayer.getCurrentPosition();
            playingNow = true;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //停止
    private void stopSong() {
        if (mediaPlayer == null) return;
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        playingNow = false;
    }


    private final MusicPlayerAIDL.Stub MusicPlayerAIDLBinder = new MusicPlayerAIDL.Stub() {

        //throws RemoteException
        //削ったけど大丈夫か
        @Override
        public void playOrPauseSong() {

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playingNow = false;

            } else {
                mediaPlayer.start();
                playingNow = true;
            }

        }


        @Override
        public void skipNext() {
            stopSong();
            playPlayList();
        }

        @Override
        public void skipBack()  {
            stopSong();
            next_track_number = next_track_number - 2;
            playPlayList();
        }



        @Override
        public void setPlayList(List<String> playList, String albumArtPath){
            MusicPlayerService.albumArtPath = albumArtPath;
            MusicPlayerService.this.playList = playList;
            next_track_number = 0;

            stopSong();
            playPlayList();
        }

        @Override
        public void setRepeat() {
            REPEAT = !REPEAT;
        }

        @Override
        public void setSeek(int progress){
            mediaPlayer.seekTo(progress);
        }

    };



    @Override
    public IBinder onBind(Intent intent) {
        return MusicPlayerAIDLBinder;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
