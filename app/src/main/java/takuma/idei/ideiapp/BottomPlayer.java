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

import java.util.ArrayList;

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





        bottomPlayerPlay = rootView.findViewById(R.id.bottom_player_play);
        upButton = rootView.findViewById(R.id.bottom_player_up);

        bottomPlayerPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String PLAYORPAUSE = binder.playOrPauseSong();
                }catch (Exception e){

                }
            }
        });

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MusicPlayerActivity.class);
                startActivity(i);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        ArrayList<String> song = (ArrayList<String>)binder.getNowSongData();
                        songData.setTitle(song.get(2));
                        songData.setArtist(song.get(0));
                        String playNow = binder.playNow();
                        if (playNow.equals("PLAY")) {
                            bottomPlayerPlay.setImageResource(R.drawable.pause);
                        } else if (playNow.equals("PAUSE")) {
                            bottomPlayerPlay.setImageResource(R.drawable.playbutton);
                        }


                        Thread.sleep(1000);
                    } catch (InterruptedException e) {} catch (Exception e) { }
                }
            }
        }).start();

        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();

    }


    @Override
    public void onClick (View v){

    }

}
