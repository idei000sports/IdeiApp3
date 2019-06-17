package takuma.idei.ideiapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import java.util.List;

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
    public static String songPath;
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
        songPath = playList.get(next_track_number);
        next_track_number += 1;

        playSong(songPath);

        //再生が終わったとき
        mediaPlayer.setOnCompletionListener(mp -> {
            MakeHistory makeHistory = new MakeHistory();
            makeHistory.makeCountTable(getApplicationContext(), title_name, artist_name, album_name, albumArtPath, songPath);
            makeHistory.makeCountDataTable(getApplicationContext(), artist_name, album_name, albumArtPath, songPath);

            stopSong();
            if(REPEAT) next_track_number -= 1;
            playPlayList();
        });
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
        makeSongData(songPath);
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(songPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            totalTime = mediaPlayer.getDuration();
            currentPosition = mediaPlayer.getCurrentPosition();
            playingNow = true;

            //Toast.makeText(this, songPath, Toast.LENGTH_SHORT).show();

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
        public void playSongFromTop(String songPath) {
            Toast.makeText(getApplicationContext(), songPath, Toast.LENGTH_SHORT).show();
            playSong(songPath);
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
