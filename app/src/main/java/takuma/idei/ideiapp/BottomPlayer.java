package takuma.idei.ideiapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import takuma.idei.ideiapp.databinding.FragmentBottomplayerBinding;

public class BottomPlayer extends Fragment implements View.OnClickListener{
    private LinearLayout bottomPlayerBg;
    //DataBinding
    private FragmentBottomplayerBinding binding;
    //ServiceへのBinder
    private MusicPlayerAIDL binder;
    private Intent serviceIntent;
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
        //Serviceへ繋ぐ
        serviceIntent = new Intent(getActivity(), MusicPlayerService.class);
        getActivity().startService(serviceIntent);
        getActivity().bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottomplayer,container, false);
        View rootView = binding.getRoot();
        //DataBinding用
        binding.setSongData(new SongData());

        //ボタン類
        rootView.findViewById(R.id.bottom_player_play).setOnClickListener(this);
        rootView.findViewById(R.id.bottom_player_up).setOnClickListener(this);
        bottomPlayerBg = rootView.findViewById(R.id.bottom_player_bg);
        bottomPlayerBg.setClickable(true);
        bottomPlayerBg.setOnClickListener(this);

        return rootView;
    }

    public void onClick(View v) {
        if(v != null) {
            try {
                switch (v.getId()) {
                    case R.id.bottom_player_play:
                        //再生してたらポーズ、そうでなければ再生
                        binder.playOrPauseSong();
                        break;
                    case R.id.bottom_player_up:
                    case R.id.bottom_player_bg:
                        //プレーヤー（大）起動
                        Intent i = new Intent(getActivity(),MusicPlayerActivity.class);
                        startActivity(i);
                        break;
                }
            }catch (Exception e) {

            }
        }
    }

}
