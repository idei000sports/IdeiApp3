package takuma.idei.ideiapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerService extends Service {
    private MediaPlayer mediaPlayer;
    private int totalTime = 100;
    private int currentPosition;
    private String message = "message";
    private String folderPath;
    private SongData songData;
    private String artist_name;
    private String album_name;
    private String title_name;
    private List<String> album;
    private int next_track_number;
    private boolean playingNow = false;
    private boolean REPERT = false;

    private final RemoteCallbackList<IMusicServiceCallback> mCallbackList = new RemoteCallbackList<IMusicServiceCallback>();


    public int onStartCommand(Intent intent, int flags, int startId){
        sendMessage("こんにちは");
        stopSelf();
        return START_NOT_STICKY;
    }

    protected void sendMessage(String msg){
        makeSongData();
        Intent broadcast = new Intent();
        broadcast.putExtra("message", msg);
        broadcast.putExtra("artist", artist_name);
        broadcast.putExtra("album", album_name);
        broadcast.putExtra("title", title_name);
        broadcast.putExtra("totalTime", mediaPlayer.getDuration());
        broadcast.putExtra("currentPosition", mediaPlayer.getCurrentPosition());
        broadcast.putExtra("playingNow", playingNow);
        broadcast.setAction("DO_ACTION");
        getBaseContext().sendBroadcast(broadcast);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.seekTo(0);


    }

    public void makeSongData() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(folderPath);
        } catch (Exception e) {
        }

        title_name = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        artist_name = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        album_name = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
    }


    public String getAlbumSong(int track) {
        String songPath = album.get(track);
        return songPath;
    }

    public void playAlbum() {

        String songPath = getAlbumSong(next_track_number);
        next_track_number = next_track_number + 1;

        folderPath = songPath;
        makeSongData();


        try {
            if (mediaPlayer == null || !mediaPlayer.isPlaying()) {

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(songPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                playingNow = true;


                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        try {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            playingNow = false;
                            if(REPERT == true) {
                                next_track_number = next_track_number -1;
                            }
                            playAlbum();
                        }catch (Exception e) {

                        }
                    }
                });


            } else {

            }
        }catch (Exception e) {

        }
    }


    private final MusicPlayerAIDL.Stub MusicPlayerAIDLBinder = new MusicPlayerAIDL.Stub() {

        @Override
        public String playOrPauseSong() throws RemoteException {
            String PLAYORPAUSE = "";
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                PLAYORPAUSE = "PAUSE";

            } else {
                mediaPlayer.start();
                PLAYORPAUSE = "PLAY";
            }
            return PLAYORPAUSE;

        }

        @Override
        public String playNow() throws RemoteException {
            String playNow;
            if (mediaPlayer.isPlaying()) {
                playNow = "PLAY";
            } else {
                playNow = "PAUSE";
            }

            return playNow;

        }
        @Override
        public void stopSong() throws RemoteException {
            if (mediaPlayer == null) return;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();;
            mediaPlayer = null;
            playingNow = false;
        }

        @Override
        public void skipNext() throws RemoteException {
            stopSong();
            playAlbum();
        }

        @Override
        public void skipBack() throws RemoteException {
            stopSong();
            next_track_number = next_track_number - 2;
            playAlbum();
        }

        @Override
        public ArrayList<String> getNowSongData() throws RemoteException {
            ArrayList<String> song = new ArrayList<>();
            song.add(artist_name);
            song.add(album_name);
            song.add(title_name);

            return song;
        }



        @Override
        public void setSelectSong(String path) throws  RemoteException {
            try {
                if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    folderPath = path;
                } else {
                    stopSong();
                }
            } catch (Exception e) {

            }
        }

        @Override
        public void setAlbum(List<String> makedAlbum) throws RemoteException {
            album = makedAlbum;
            next_track_number = 0;
            try {
                stopSong();
                playAlbum();
            }catch (Exception e) {

            }
        }
        @Override
        public void setRepert() throws RemoteException {
            if (REPERT == true) {
                REPERT = false;
            }else {
                REPERT = true;
            }
        }

        @Override
        public void setSeek(int progress) throws RemoteException {
            mediaPlayer.seekTo(progress);
        }
        @Override
        public int getTotalTime() throws RemoteException {
            int totalTime = mediaPlayer.getDuration();
            return totalTime;
        }

        @Override
        public int getCurrentPosition()throws RemoteException {
            int currentPosition = mediaPlayer.getCurrentPosition();
            return currentPosition;
        }
        @Override
        public void registerCallback(IMusicServiceCallback callback)
                throws RemoteException {
            mCallbackList.register(callback);
        }

        @Override
        public void unregisterCallback(IMusicServiceCallback callback)
                throws RemoteException {
            mCallbackList.unregister(callback);
        }

    };


    @Override
    public IBinder onBind(Intent intent) {
        sendMessage("こんばんは");

        return MusicPlayerAIDLBinder;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
