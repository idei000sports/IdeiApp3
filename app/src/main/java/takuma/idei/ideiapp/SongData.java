package takuma.idei.ideiapp;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageButton;

public class SongData extends BaseObservable {
    private String artist;
    private String album;
    private String title;
    private static boolean playingNow;
    private int playOrPause;


    private String albumArt;
    private static ImageButton button;

    /*
    albumArt = (ImageView)findViewById(R.id.AlbumArt);
    albumArt.setImageBitmap(BitmapFactory.decodeFile(binder.getAlbumArt()));

     */


    public SongData() {
        playOrPause = R.drawable.playbutton;
        getServiceInfo();
    }

    @BindingAdapter("app:srcCompat")
    public static void srcCompat(ImageButton button, int playOrPause) {
        button.setImageResource(playOrPause);
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

                        if (playingNow == true) {
                            setPlayOrPause(R.drawable.pause);
                        } else {
                            setPlayOrPause(R.drawable.playbutton);
                        }


                        Thread.sleep(1000);
                    } catch (InterruptedException e) {} catch (Exception e) { }
                }
            }
        }).start();
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

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }


    @Bindable
    public int getPlayOrPause() {
        return playOrPause;
    }

    public void setPlayOrPause(int playOrPause) {
        this.playOrPause = playOrPause;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.playOrPause);
    }
}
