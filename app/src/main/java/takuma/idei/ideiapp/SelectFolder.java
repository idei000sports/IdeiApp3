package takuma.idei.ideiapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.BIND_AUTO_CREATE;

public class SelectFolder extends Fragment implements View.OnClickListener{
    //表示
    private TextView folderPathTextView;
    private List<String> songAndFolderList;
    //songAndFolderListから選択したファイル以降を抽出
    private ArrayList<String> playList;


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
        //サービス接続
        Intent serviceIntent = new Intent(getActivity(), MusicPlayerService.class);
        Objects.requireNonNull(getActivity()).bindService(serviceIntent, connection, BIND_AUTO_CREATE);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_selectfolder, container, false);
        //最深部
        String storagePath = Environment.getExternalStorageDirectory().getPath();
        //storagePath/Music
        String musicFolderPath = storagePath + "/Music";

        folderPathTextView = rootView.findViewById(R.id.folderPath);
        folderPathTextView.setText(musicFolderPath);

        makeFolderList(musicFolderPath, rootView);

        return rootView;
    }
    public void makeFolderList(String musicFolderPath, View rootView){
        songAndFolderList = new ArrayList<>();
        //現在のフォルダーを表示
        folderPathTextView.setText(musicFolderPath);

        //フォルダーのファイルをリスト化
        //fileListからmp3とフォルダを抽出したのがsongAndFolderList
        File[] fileList = new File(musicFolderPath).listFiles();

        //ファイルが入っていれば
        if(fileList != null){
            for (File file : fileList) {
                if (file.isFile() && file.getName().endsWith(".mp3") || file.isDirectory()) {
                    //mp3ファイルorフォルダーなら
                    songAndFolderList.add(file.getName());
                }

            }

            ListView listView = rootView.findViewById(R.id.songlist);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_expandable_list_item_1, songAndFolderList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                ListView listView1 = (ListView) parent;
                String item = (String) listView1.getItemAtPosition(position);
                if(item.endsWith(".mp3")) {
                    //mp3だった場合
                    int trackNumber = songAndFolderList.indexOf(item);
                    playList = new ArrayList<>();
                    //選択したファイルのtrackNumber以降のファイルをプレイリスト化
                    for (int i = trackNumber; i < songAndFolderList.size(); i++) {
                        if(songAndFolderList.get(i).endsWith(".mp3")) {
                            playList.add(musicFolderPath + "/" + songAndFolderList.get(i));
                        }
                    }

                    try {
                        String albumArtPath = musicFolderPath + "/" + "folder.jpg";
                        binder.setPlayList(playList, albumArtPath);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //ディレクトリ名を足してフォルダーを開き直す
                    songAndFolderList.clear();
                    makeFolderList(musicFolderPath + "/" + item, rootView);
                }
            });
        }


    }


    @Override
    public void onClick (View v){
    }
}
