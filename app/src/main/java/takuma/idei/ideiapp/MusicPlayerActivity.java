package takuma.idei.ideiapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
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

//プレーヤーの表示部分
public class MusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView albumArt;
    private ImageButton playButton;
    private ImageButton skipNextButton;
    private ImageButton skipBackButton;
    private ImageButton finishActivity;
    private ImageButton repertButton;

    private SeekBar positionBar;
    private SeekBarData seekBarData;

    //サービスへのアクセス
    private Intent serviceIntent;
    private MusicPlayerAIDL binder;
    //DataBinding用
    private SongData songData;
    private FragmentMusicplayerBinding binding;

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

        //サービス開き
        serviceIntent = new Intent(this, MusicPlayerService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);

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
                        //秒ごとにサービスの状態を取得するスレッド

                        //曲のメタデータとアルバムアートをDataBindingに
                        ArrayList<String> song = (ArrayList<String>)binder.getNowSongData();
                        songData = new SongData(song.get(0), song.get(1), song.get(2));
                        binding.setSongData(songData);
                        //PathからBitmapへ変換してset
                        String albumArtPath = binder.getAlbumArt();
                        albumArt.setImageBitmap(BitmapFactory.decodeFile(albumArtPath));

                        //再生中か否か
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
