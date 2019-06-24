package takuma.idei.ideiapp.Artist.AlbumTable;

import android.graphics.Bitmap;

public class AlbumTableItem {
    private Bitmap albumArt;
    private String albumTitle;

    public AlbumTableItem() {
    }

    public AlbumTableItem(Bitmap albumArt, String albumTitle) {
        this.albumArt = albumArt;
        this.albumTitle = albumTitle;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }
}
