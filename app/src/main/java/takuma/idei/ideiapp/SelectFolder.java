package takuma.idei.ideiapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import takuma.idei.ideiapp.MusicPlayer.MusicPlayerService;
import takuma.idei.ideiapp.SQL.SQLAlbumAndSongTableHelper;

import static android.content.Context.BIND_AUTO_CREATE;

public class SelectFolder extends Fragment implements View.OnClickListener{
    private Button button;
    private String folderPath;
    private TextView folderPathTextView;
    private List<String> songList = new ArrayList<>();
    private ListView lv;
    private File[] files;
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


    public void makeFolderList(String folderPath, View rootView){

        files = new File(folderPath).listFiles();
        if(files != null){
            for(int i = 0; i < files.length; i++){
                if (files[i].isFile() && files[i].getName().endsWith(".mp3") || files[i].isDirectory()) {
                    songList.add(files[i].getName());
                }

            }

            lv = (ListView)rootView.findViewById(R.id.test_list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, songList);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListView listView = (ListView) parent;
                    String item = (String) listView.getItemAtPosition(position);
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
                            //binder.setPlayList(playList, albumArtPath);
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
        final View rootView = inflater.inflate(R.layout.test_layout, container, false);

        button = rootView.findViewById(R.id.reset);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLAlbumAndSongTableHelper sq = new SQLAlbumAndSongTableHelper(getActivity());
                SQLiteDatabase sqLiteDatabase = sq.getWritableDatabase();
                sq.reset(sqLiteDatabase);
                sqLiteDatabase.close();



            }
        });

        folderPath = Environment.getExternalStorageDirectory().getPath();
        folderPath = folderPath + "/Music";




        makeFolderList(folderPath, rootView);


        return rootView;
    }


    @Override
    public void onClick (View v){

    }
}
