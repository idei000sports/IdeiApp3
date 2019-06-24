package takuma.idei.ideiapp.Bean;

public class SongBean {
    private String artist_name;
    private String song_title;
    private String album_title;
    private String song_path;
    private int playcount;
    private String track_number;
    private String album_art_path;

    public SongBean() {

    }
    public SongBean(String artist_name, String song_title, String album_title, String song_path) {
        this.artist_name = artist_name;
        this.song_title = song_title;
        this.album_title = album_title;
        this.song_path = song_path;

    }

    public String getSong_title() {
        return song_title;
    }

    public void setSong_title(String song_title) {
        this.song_title = song_title;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public String getSong_path() {
        return song_path;
    }

    public void setSong_path(String song_path) {
        this.song_path = song_path;
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public String getTrack_number() {
        return track_number;
    }

    public void setTrack_number(String track_number) {
        this.track_number = track_number;
    }

    public String getAlbum_art_path() {
        return album_art_path;
    }

    public void setAlbum_art_path(String album_art_path) {
        this.album_art_path = album_art_path;
    }
}
