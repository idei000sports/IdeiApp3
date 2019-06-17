package takuma.idei.ideiapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;
import android.widget.TextView;

public class HomeListMaker {
    private ImageButton imageButton;
    private TextView textView;
    private String title;
    private String albumArtPath;
    private String songPath;




    public void setAlbumArtAndInfo(String title, String albumArtPath, String songPath) {
        this.title = title;
        this.albumArtPath = albumArtPath;
        this.songPath = songPath;

        this.textView.setText(this.title);

        Bitmap bmp = BitmapFactory.decodeFile(albumArtPath);
        this.imageButton.setImageBitmap(bmp);
    }


    public ImageButton getImageButton() {
        return imageButton;
    }

    public void setImageButton(ImageButton imageButton) {
        this.imageButton = imageButton;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }


    public String getAlbumArtPath() {
        return albumArtPath;
    }

    public void setAlbumArtPath(String albumArtPath) {
        this.albumArtPath = albumArtPath;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
