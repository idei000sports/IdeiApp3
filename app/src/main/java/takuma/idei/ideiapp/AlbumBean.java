package takuma.idei.ideiapp;

import java.io.Serializable;

public class AlbumBean implements Serializable {
    private String albumTitle;
    private String albumArt;
    private String artist;
    private String path;

    public AlbumBean(){

    }


    public AlbumBean(String artist, String albumTitle, String albumArt, String path) {
        this.albumTitle = albumTitle;
        this.albumArt = albumArt;
        this.artist = artist;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
