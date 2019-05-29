package takuma.idei.ideiapp;

import android.app.Activity;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;

public class Player extends Activity implements View.OnClickListener {
    private final static String TAG_SONG = "song";
    private MediaPlayer mediaPlayer;
    private int soundID;
    private ImageButton playButton;


    public void playSong(){
        stopSong();
        try {

            mediaPlayer = MediaPlayer.create(this, R.raw.eyeknoww);
            mediaPlayer.setLooping(true);

            mediaPlayer.seekTo(0);
            mediaPlayer.start();

        } catch (Exception e) {

        }
    }

    public void stopSong() {
        if (mediaPlayer == null) return;
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }catch (Exception e) {

        }
    }
    public void onClick(View view) {
    }
}
