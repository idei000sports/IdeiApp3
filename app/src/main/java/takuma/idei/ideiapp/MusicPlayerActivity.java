package takuma.idei.ideiapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import takuma.idei.ideiapp.databinding.FragmentMusicplayerBinding;

//プレーヤーの表示部分
public class MusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    public static SeekBar positionBar;
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
        //DataBinding用
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_musicplayer);
        binding.setSongData(new SongData());
        //サービス開き
        serviceIntent = new Intent(this, MusicPlayerService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);

    }
    @Override
    public void onStart() {
        super.onStart();
        //ボタン
        findViewById(R.id.PlayButton).setOnClickListener(this);
        findViewById(R.id.SkipNextButton).setOnClickListener(this);
        findViewById(R.id.SkipBackButton).setOnClickListener(this);
        findViewById(R.id.FinishActivity).setOnClickListener(this);
        findViewById(R.id.Repeat).setOnClickListener(this);

        positionBar = (SeekBar)findViewById(R.id.positionBar);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            try {
                                //サービスのプレーヤーの再生位置変更
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

    }
    public void onClick(View v) {
        if(v != null) {
            try {
                switch (v.getId()) {
                    case R.id.PlayButton:
                        binder.playOrPauseSong();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unbindService();
    }

}
