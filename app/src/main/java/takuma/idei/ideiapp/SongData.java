package takuma.idei.ideiapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import takuma.idei.ideiapp.MusicPlayer.MusicPlayerService;

import static android.content.ContentValues.TAG;

public class SongData extends BaseObservable {
    private String artist;
    private String album;
    private String title;
    private static boolean playingNow;
    private int playOrPause;
    private String bottomPlayerSong;

    //private static ImageView albumArtView;
    private Bitmap albumArt;

    //private static ImageButton button;

    private int totalTime;
    private int currentPosition;
    private String elapsedTimeLabel;
    private String remainingTimeLabel;
    private int progress;

    public SongData() {
        new Thread(() -> {
            while (true) try {
                setAlbum(MusicPlayerService.album_name);
                setTitle(MusicPlayerService.title_name);
                setArtist(MusicPlayerService.artist_name);
                setPlayingNow(MusicPlayerService.playingNow);

                if(MusicPlayerService.albumArtPath != null) {
                    setAlbumArt(BitmapFactory.decodeFile(MusicPlayerService.albumArtPath));
                }


                setTotalTime(MusicPlayerService.totalTime);

                try {
                    currentPosition = MusicPlayerService.mediaPlayer.getCurrentPosition();
                }catch (Exception e) {

                }

                if(getTitle() != null) {
                    setBottomPlayerSong(getTitle() + " - " + getArtist());
                }


                if (getPlayingNow()) {
                    setPlayOrPause(R.drawable.pause);
                } else {
                    setPlayOrPause(R.drawable.playbutton);
                }

                Message msg = new Message();
                msg.what = currentPosition;
                handler.sendMessage(msg);

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "SongData: interrr" );
            } catch (Exception e) {
                Log.e(TAG, "SongData: aaaa" );
            }
        }).start();
    }

    private Handler handler = new Handler(msg -> {
        int currentPosition = msg.what;
        // 再生位置を更新
        //MusicPlayerActivity.positionBar.setProgress(currentPosition);
        setProgress(currentPosition);

        String elapsedTime = createTimeLabel(currentPosition);
        String remainingTime = createTimeLabel(getTotalTime()-currentPosition);

        setElapsedTimeLabel(elapsedTime);
        setRemainingTimeLabel(remainingTime);

        return true;
    });

    private String createTimeLabel(int time) {
        String timeLabel;
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    @BindingAdapter("albumArt")
    public static void albumArt(ImageView albumArtView, Bitmap albumArt) {
        albumArtView.setImageBitmap(albumArt);
    }

    @BindingAdapter("playOrPause")
    public static void playOrpause(ImageButton button, int playOrPause) {
        button.setImageResource(playOrPause);
    }

    public SongData(String art, String alb, String tit){
        artist = art;
        album = alb;
        title = tit;
    }

    @Bindable
    public String getArtist() {
        return artist;
    }

    public void setArtist(String art) {
        this.artist = art;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.artist);
    }

    @Bindable
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String alb) {
        album = alb;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.album);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String tit) {
        title = tit;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.title);
    }

    @Bindable
    public Boolean getPlayingNow() {
        return playingNow;
    }

    private void setPlayingNow(Boolean playingNow) {
        this.playingNow = playingNow;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.playingNow);
    }

    @Bindable
    public Bitmap getAlbumArt() {
        return albumArt;
    }

    private void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.albumArt);
    }


    @Bindable
    public int getPlayOrPause() {
        return playOrPause;
    }

    private void setPlayOrPause(int playOrPause) {
        this.playOrPause = playOrPause;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.playOrPause);
    }

    @Bindable
    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.totalTime);
    }

    @Bindable
    public String getElapsedTimeLabel() {
        return elapsedTimeLabel;
    }

    private void setElapsedTimeLabel(String elapsedTimeLabel) {
        this.elapsedTimeLabel = elapsedTimeLabel;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.elapsedTimeLabel);
    }

    @Bindable
    public String getRemainingTimeLabel() {
        return remainingTimeLabel;
    }

    private void setRemainingTimeLabel(String remainingTimeLabel) {
        this.remainingTimeLabel = remainingTimeLabel;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.remainingTimeLabel);
    }
    @Bindable
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.progress);
    }
    @Bindable
    public String getBottomPlayerSong() {
        return bottomPlayerSong;
    }

    public void setBottomPlayerSong(String bottomPlayerSong) {
        this.bottomPlayerSong = bottomPlayerSong;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.bottomPlayerSong);
    }
}
