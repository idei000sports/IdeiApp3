package takuma.idei.ideiapp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;

import takuma.idei.ideiapp.databinding.FragmentMusicplayerBinding;

public class MusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView albumArt;

    private ImageButton playButton;
    private ImageButton skipNextButton;
    private ImageButton skipBackButton;
    private ImageButton finishActivity;
    private ImageButton repertButton;


    private SeekBar positionBar;
    private SeekBarData seekBarData;

    private SongData songData;
    private Intent serviceIntent;
    private MusicPlayerAIDL binder;
    private FragmentMusicplayerBinding binding;
    private UpdateReceiver receiver;
    private IntentFilter filter;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_musicplayer);
        serviceIntent = new Intent(this, MusicPlayerService.class);
        //二重でstartServiceしても問題ないらしい
        startService(serviceIntent);
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);


        receiver = new UpdateReceiver();
        filter = new IntentFilter();
        filter.addAction("DO_ACTION");
        registerReceiver(receiver, filter);
        unregisterReceiver(receiver);



    }
    @Override
    public void onStart() {
        super.onStart();
        playButton = (ImageButton)findViewById(R.id.PlayButton);
        skipNextButton = (ImageButton)findViewById(R.id.SkipNextButton);
        skipBackButton = (ImageButton)findViewById(R.id.SkipBackButton);
        finishActivity = (ImageButton)findViewById(R.id.FinishActivity);
        repertButton = (ImageButton)findViewById(R.id.Repeat);
        albumArt = (ImageView)findViewById(R.id.AlbumArt);
        positionBar = (SeekBar)findViewById(R.id.positionBar);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String PLAYORPAUSE = binder.playOrPauseSong();
                }catch (Exception e) {

                }

            }
        });

        skipNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    binder.skipNext();
                }catch (Exception e) {
                    //制作
                }
            }
        });
        skipBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    binder.skipBack();
                }catch (Exception e) {

                }
            }
        });
        repertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    binder.setRepert();
                    Toast.makeText(getApplicationContext(), "リピートをONにしました", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }
        });

        finishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            try {
                                binder.setSeek(progress);
                            } catch (Exception e) {

                            }
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        //スレッドでサービスの情報取得
                        //曲のメタデータ
                        ArrayList<String> song = (ArrayList<String>)binder.getNowSongData();
                        songData = new SongData(song.get(0), song.get(1), song.get(2));
                        binding.setSongData(songData);

                        String albumArtPath = binder.getAlbumArt();
                        Bitmap bmImg = BitmapFactory.decodeFile(albumArtPath);
                        albumArt.setImageBitmap(bmImg);


                        //再生中か
                        String playNow = binder.playNow();
                        if (playNow.equals("PLAY")) {
                            playButton.setImageResource(R.drawable.pause);
                        } else if (playNow.equals("PAUSE")) {
                            playButton.setImageResource(R.drawable.playbutton);
                        }

                        //曲の長さ
                        seekBarData = new SeekBarData(binder.getTotalTime());
                        binding.setSeekBarData(seekBarData);

                        Message msg = new Message();

                        msg.what = binder.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {} catch (Exception e) { }
                }
            }
        }).start();

    }


    protected class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            /*
            Bundle extras = intent.getExtras();
            String msg = extras.getString("message");
            String title = extras.getString("title");
            String album = extras.getString("album");
            String artist = extras.getString("artist");
            //playingNow = extras.getBoolean("playingNow");
            int totalTime = extras.getInt("totalTime");
            //getedCurrentPosition = extras.getInt("currentPosition");
            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

            songData = new SongData(artist, album, title);
            binding.setSongData(songData);
            seekBarData = new SeekBarData(totalTime);
            binding.setSeekBarData(seekBarData);
            */

        }
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int currentPosition = msg.what;
            // 再生位置を更新
            positionBar.setProgress(currentPosition);

            String elapsedTime = createTimeLabel(currentPosition);
            String remainingTime = createTimeLabel(seekBarData.getTotalTime()-currentPosition);

            seekBarData.setElapsedTimeLabel(elapsedTime);
            seekBarData.setRemainingTimeLabel(remainingTime);
            binding.setSeekBarData(seekBarData);

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


    public void onClick(View view) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unbindService();
    }

}
