package takuma.idei.ideiapp.Album;

public class AlbumListItem {
    private String artist_name;
    private String song_title;
    private String song_path;
    public AlbumListItem() {

    }

    public AlbumListItem(String artist_name, String song_title, String song_path) {
        this.artist_name = artist_name;
        this.song_title = song_title;
        this.song_path = song_path;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getSong_title() {
        return song_title;
    }

    public void setSong_title(String song_title) {
        this.song_title = song_title;
    }

    public String getSong_path() {
        return song_path;
    }

    public void setSong_path(String song_path) {
        this.song_path = song_path;
    }
}
