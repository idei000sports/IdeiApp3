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
    private String folder;
    private String folderPath;
    private TextView folderPathTextView;
    private List<String> songList = new ArrayList<>();
    private List<String> folderList = new ArrayList<>();
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


    public void makeFolderList(String folderPath, View rootView){
        Toast.makeText(getContext(), "now : " + folderPath, Toast.LENGTH_LONG).show();
        folderPathTextView.setText(folderPath);
        files = new File(folderPath).listFiles();
        if(files != null){
            for(int i = 0; i < files.length; i++){
                if (files[i].isFile() && files[i].getName().endsWith(".mp3") || files[i].isDirectory()) {
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
                    Toast.makeText(getContext(), folderPath + "/" + item, Toast.LENGTH_SHORT).show();
                    if(item.endsWith(".mp3")) {
                        //mp3だった場合
                        int trackNumber = songList.indexOf(item);
                        ArrayList<String> playList = new ArrayList<>();

                        for (int i = trackNumber; i < songList.size(); i++) {
                            if(songList.get(i).endsWith(".mp3")) {
                                playList.add(folderPath + "/" + songList.get(i));
                            }
                        }

                        try {
                            String albumArtPath = folderPath + "/" + "folder.jpg";
                            binder.setAlbum(playList, albumArtPath);
                        }catch (Exception e) {

                        }
                    } else if (item.equals("folder.jpg")) {

                    } else {
                        songList.clear();
                        makeFolderList(folderPath + "/" + item, rootView);
                    }
                }
            });
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_selectfolder, container, false);
        folderPath = Environment.getExternalStorageDirectory().getPath();
        folderPath = folderPath + "/Music";

        folderPathTextView = (TextView)rootView.findViewById(R.id.folderPath);
        folderPathTextView.setText(folderPath);


        makeFolderList(folderPath, rootView);


        return rootView;
    }


    @Override
    public void onClick (View v){

    }
}
