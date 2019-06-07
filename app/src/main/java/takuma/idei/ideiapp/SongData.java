package takuma.idei.ideiapp;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SongData extends BaseObservable {
    private String artist;
    private String album;
    private String title;
    private static boolean playingNow;
    private int playOrPause;

    private static ImageView albumArtView;
    private Bitmap albumArt;

    private static ImageButton button;

    private int totalTime;
    private int currentPosition;
    private String elapsedTimeLabel;
    private String remainingTimeLabel;
    private int progress;

    public SongData() {
        getServiceInfo();
    }


    public void getServiceInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        setAlbum(MusicPlayerService.album_name);
                        setTitle(MusicPlayerService.title_name);
                        setArtist(MusicPlayerService.artist_name);
                        setPlayingNow(MusicPlayerService.playingNow);
                        setAlbumArt(BitmapFactory.decodeFile(MusicPlayerService.albumArtPath));
                        setTotalTime(MusicPlayerService.totalTime);
                        currentPosition = MusicPlayerService.mediaPlayer.getCurrentPosition();


                        if (playingNow == true) {
                            setPlayOrPause(R.drawable.pause);
                        } else {
                            setPlayOrPause(R.drawable.playbutton);
                        }

                        Message msg = new Message();
                        msg.what = currentPosition;
                        handler.sendMessage(msg);

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {} catch (Exception e) { }
                }
            }
        }).start();
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int currentPosition = msg.what;
            // 再生位置を更新
            //MusicPlayerActivity.positionBar.setProgress(currentPosition);
            setProgress(currentPosition);

            String elapsedTime = createTimeLabel(currentPosition);
            String remainingTime = createTimeLabel(getTotalTime()-currentPosition);

            setElapsedTimeLabel(elapsedTime);
            setRemainingTimeLabel(remainingTime);

            return true;
        }
    });

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    @BindingAdapter("app:srcCompat")
    public static void albumArter(ImageView albumArtView, Bitmap albumArt) {
        albumArtView.setImageBitmap(albumArt);
    }

    @BindingAdapter("app:srcCompat")
    public static void srcCompat(ImageButton button, int playOrPause) {
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
        artist = art;
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

    public void setPlayingNow(Boolean playingNow) {
        this.playingNow = playingNow;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.playingNow);
    }

    @Bindable
    public Bitmap getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.albumArt);
    }


    @Bindable
    public int getPlayOrPause() {
        return playOrPause;
    }

    public void setPlayOrPause(int playOrPause) {
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

    public void setElapsedTimeLabel(String elapsedTimeLabel) {
        this.elapsedTimeLabel = elapsedTimeLabel;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.elapsedTimeLabel);
    }

    @Bindable
    public String getRemainingTimeLabel() {
        return remainingTimeLabel;
    }

    public void setRemainingTimeLabel(String remainingTimeLabel) {
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
}
