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

import takuma.idei.ideiapp.databinding.FragmentMusicplayerBinding;

//プレーヤーの表示部分
public class MusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView albumArt;
    private SongData songData;
    private SeekBar positionBar;
    private SeekBarData seekBarData;
    private ImageButton playing;

    //サービスへのアクセス
    private Intent serviceIntent;
    private MusicPlayerAIDL binder;
    //DataBinding用
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


        songData = new SongData();
        binding.setSongData(songData);


        //サービス開き
        serviceIntent = new Intent(this, MusicPlayerService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);

    }
    @Override
    public void onStart() {
        super.onStart();
        playing = (ImageButton)findViewById(R.id.PlayButton);
        findViewById(R.id.PlayButton).setOnClickListener(this);
        findViewById(R.id.SkipNextButton).setOnClickListener(this);
        findViewById(R.id.SkipBackButton).setOnClickListener(this);
        findViewById(R.id.FinishActivity).setOnClickListener(this);
        findViewById(R.id.Repeat).setOnClickListener(this);
        albumArt = (ImageView)findViewById(R.id.AlbumArt);
        try {
            albumArt.setImageBitmap(BitmapFactory.decodeFile(binder.getAlbumArt()));
        }catch (Exception e) {

        }

        positionBar = (SeekBar)findViewById(R.id.positionBar);


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

                        //albumArt.setImageBitmap(BitmapFactory.decodeFile(binder.getAlbumArt()));

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
    public void onClick(View v) {
        if(v != null) {
            try {
                switch (v.getId()) {
                    case R.id.PlayButton:
                        String PLAYORPAUSE = binder.playOrPauseSong();
                        break;
                    case R.id.SkipNextButton:
                        binder.skipNext();
                        break;
                    case R.id.SkipBackButton:
                        binder.skipBack();
                        break;
                    case R.id.Repeat:
                        binder.setRepert();
                        Toast.makeText(getApplicationContext(), "リピートをONにしました", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.FinishActivity:
                        finish();
                        break;
                }
            }catch (Exception e) {

            }
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        //unbindService();
    }

}
