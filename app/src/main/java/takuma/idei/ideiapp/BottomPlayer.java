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
import android.widget.ImageButton;

import takuma.idei.ideiapp.databinding.FragmentBottomplayerBinding;

public class BottomPlayer extends Fragment implements View.OnClickListener{
    private ImageButton bottomPlayerPlay;
    private ImageButton upButton;
    private SongData songData;
    private FragmentBottomplayerBinding binding;
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
        serviceIntent = new Intent(getActivity(), MusicPlayerService.class);
        getActivity().startService(serviceIntent);
        getActivity().bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottomplayer,container, false);
        View rootView = binding.getRoot();

        songData = new SongData();
        binding.setSongData(songData);

        rootView.findViewById(R.id.bottom_player_play).setOnClickListener(this);
        rootView.findViewById(R.id.bottom_player_up).setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();

    }


    public void onClick(View v) {
        if(v != null) {
            try {
                switch (v.getId()) {
                    case R.id.bottom_player_play:
                        String PLAYORPAUSE = binder.playOrPauseSong();
                        break;
                    case R.id.bottom_player_up:
                        Intent i = new Intent(getActivity(),MusicPlayerActivity.class);
                        startActivity(i);
                        break;
                }
            }catch (Exception e) {

            }
        }
    }

}
