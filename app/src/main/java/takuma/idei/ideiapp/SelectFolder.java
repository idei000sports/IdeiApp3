package takuma.idei.ideiapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

public class SelectFolder extends Fragment implements View.OnClickListener{

    private List<String> songList = new ArrayList<>();
    private ListView lv;
    private File[] files;
    private FragmentManager fragmentManager;
    private Intent serviceIntent;
    private TextView mTextMessage;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_selectfolder, container, false);
        String folderPath = Environment.getExternalStorageDirectory().getPath();
        TextView folderPathTextView = (TextView)rootView.findViewById(R.id.folderPath);


        files = new File(folderPath + "/Music/").listFiles();
        folderPathTextView.setText(folderPath);


        if(files != null){
            for(int i = 0; i < files.length; i++){
                if (files[i].isFile() && files[i].getName().endsWith(".mp3")) {
                    songList.add(files[i].getName());
                }

            }

            lv = (ListView)rootView.findViewById(R.id.songlist);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, songList);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListView listView = (ListView) parent;
                    String item = (String) listView.getItemAtPosition(position);

                    //setSong(item, rootView);
                    setAlbum(songList, rootView);
                }
            });
        }
        return rootView;
    }

    public void setSong(String item, View v) {
        String folderPath = Environment.getExternalStorageDirectory().getPath();
        String path = folderPath + "/Music/" + item;
        try {
            binder.setSelectSong(path);
        }catch (Exception e) {

        }
    }

    public void setAlbum(List<String> songList, View v) {
        Toast.makeText(getActivity(), songList.get(1), Toast.LENGTH_SHORT);
        String folderPath = Environment.getExternalStorageDirectory().getPath();
        String path = folderPath + "/Music/";
        List<String> album = new ArrayList<>();
        for (int i = 0; i < songList.size(); i++) {
            String song;
            song = path + songList.get(i);
            album.add(song);
            Toast.makeText(getActivity(), song, Toast.LENGTH_SHORT).show();
        }


        try {
            binder.setAlbum(album);
        }catch (Exception e) {

        }
    }

    @Override
    public void onClick (View v){

    }
}
