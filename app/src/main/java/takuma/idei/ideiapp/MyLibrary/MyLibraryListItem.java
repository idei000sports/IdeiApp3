package takuma.idei.ideiapp.MyLibrary;

import android.graphics.Bitmap;

import java.io.Serializable;

public class MyLibraryListItem implements Serializable {
    private static final long serialVersionUID = 1L;


    private Bitmap mThumbnail = null;
    private String mAlbumTitle = null;
    private String mArtistName = null;


    public MyLibraryListItem() {};


    public MyLibraryListItem(Bitmap thumbnail, String albumTitle, String artistName) {
        mThumbnail = thumbnail;
        mAlbumTitle = albumTitle;
        mArtistName = artistName;
    }


    public void setThumbnail(Bitmap thumbnail) {
        mThumbnail = thumbnail;
    }


    public void setAlbumTitle(String albumTitle) {
        mAlbumTitle = albumTitle;
    }

    public void setArtistName(String artistName) {
        mArtistName = artistName;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }


    public String getAlbumTitle() {
        return mAlbumTitle;
    }

    public String getArtistName() {
        return mArtistName;
    }


}