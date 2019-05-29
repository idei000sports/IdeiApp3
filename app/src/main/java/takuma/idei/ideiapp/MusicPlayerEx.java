package takuma.idei.ideiapp;

import android.app.Activity;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicPlayerEx extends Activity implements View.OnClickListener {

    private TextView title;
    private TextView artist;
    private TextView album;

    private MediaPlayer mediaPlayer;
    private ImageButton playButton;
    private SeekBar positionBar;
    private int totalTime;
    private String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.fragment_musicplayer);


        playButton = (ImageButton)this.findViewById(R.id.PlayButton);
        //mediaPlayer初期設定
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);

        totalTime = mediaPlayer.getDuration();

        positionBar = (SeekBar)this.findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        // Thread (positionBar・経過時間ラベル・残り時間ラベルを更新する)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();



        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
                    playBGM();
                } else {
                    stopBGM();
                }
            }
        });

    }


    private ArrayList<String> showMetaData(String path) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(path);
        } catch (Exception e) {
        }

        String title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);

        ArrayList<String> songData = new ArrayList<>();
        songData.add(title);
        songData.add(artist);
        songData.add(album);

        return songData;

    }









    //（オプション）Warning解消
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int currentPosition = msg.what;
            // 再生位置を更新
            positionBar.setProgress(currentPosition);

            return true;
        }
    });

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    /*
    public void setSelectSong() {
        String path = getArguments().getString("path");
        Toast.makeText(getContext(), "ファイル名:" + path, Toast.LENGTH_SHORT).show();
        try {

            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();;
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.pause);

            ArrayList<String> songData = showMetaData(path);
            title = (TextView)rootView.findViewById(R.id.title);
            artist = (TextView)rootView.findViewById(R.id.artist);
            album = (TextView)rootView.findViewById(R.id.album);
            title.setText(songData.get(0));
            artist.setText(songData.get(1));
            album.setText(songData.get(2));

        } catch (Exception e) {

        }
    }
    */

    private void playBGM () {
        stopBGM();
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.eyeknoww);
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.pause);
        } catch (Exception e) {

        }
    }

    private void stopBGM () {
        if (mediaPlayer == null) return;
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            playButton.setImageResource(R.drawable.playbutton);
        } catch (Exception e) {

        }
    }


    public void onClick(View view) {

    }


}
