package takuma.idei.ideiapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import takuma.idei.ideiapp.databinding.FragmentMusicplayerBinding;

import static android.content.Context.BIND_AUTO_CREATE;

public class MusicPlayer extends Fragment implements View.OnClickListener{
    private TextView title;
    private TextView artist;
    private TextView album;

    //private SongData songData = new SongData("ar", "albumaa", "titleee");
    private SongData songData;
    private TextView error;

    private String playOrPause = "nuru";

    private Handler mHandler;
    private static final int TEXT_UPDATE = 0;

    private MediaPlayer mediaPlayer;
    private ImageButton playButton;
    private SeekBar positionBar;
    private int totalTime;
    private String path;
    private Intent serviceIntent;
    private MusicPlayerAIDL binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = MusicPlayerAIDL.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };



    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        serviceIntent = new Intent(getActivity(), MusicPlayerService.class);
        getActivity().bindService(serviceIntent, connection, BIND_AUTO_CREATE);


    }


    public void setAAR(String test) {
        this.songData = new SongData(test, test, test);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMusicplayerBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_musicplayer, container, false);


        final View rootView = inflater.inflate(R.layout.fragment_musicplayer, container, false);
        playButton = rootView.findViewById(R.id.PlayButton);
        error = (TextView)rootView.findViewById(R.id.error);





        binding.setSongData(songData);




        //mediaPlayer初期設定
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);

        positionBar = rootView.findViewById(R.id.positionBar);
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
                try {
                    playOrPause = binder.playOrPauseSong();
                    if (playOrPause.equals("PLAY")){

                    }else if (playOrPause.equals("PAUSE")) {

                    }


                }catch (Exception e) {

                }
            }
        });



        //こっから新しい



        return binding.getRoot();
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


    public void setSelectSong(View rootView) {
        String path = getArguments().getString("path");
        Toast.makeText(getContext(), "ファイル名:" + path, Toast.LENGTH_SHORT).show();
        try {

            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();;
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.pause);

            ArrayList<String> songData = showMetaData(path);


        } catch (Exception e) {

        }
    }






    @Override
    public void onClick (View v){

    }


}



