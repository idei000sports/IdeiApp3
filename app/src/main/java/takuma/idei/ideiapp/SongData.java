package takuma.idei.ideiapp;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.PropertyChangeRegistry;

public class SongData extends BaseObservable {
    private static String artist;
    private static String album;
    private static String title;
    private boolean playingNow;
    private String albumArt;
    PropertyChangeRegistry propertyChangeRegistry;
    public SongData() {

    }
    public SongData(String artist, String album, String title){
        this.artist = artist;
        this.album = album;
        this.title = title;
    }

    @Bindable
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.artist);
    }

    @Bindable
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.album);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
        notifyPropertyChanged(takuma.idei.ideiapp.BR.albumArt);
    }
}
