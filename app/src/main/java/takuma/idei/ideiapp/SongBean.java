package takuma.idei.ideiapp;

public class SongBean {
    private String artist;
    private String title;
    private String album;
    private String path;
    private int count;

    public SongBean() {

    }
    public SongBean(String artist, String title, String album, String path) {
        this.artist = artist;
        this.title = title;
        this.album = album;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
