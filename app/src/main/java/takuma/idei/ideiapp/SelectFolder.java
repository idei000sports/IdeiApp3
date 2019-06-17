package takuma.idei.ideiapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        ArrayList<MyLibraryListItem> listItems = new ArrayList<>();

        //ファイルが入っていれば
        if(fileList != null){
            for (File file : fileList) {
                if (file.isFile() && file.getName().endsWith(".mp3") || file.isDirectory()) {
                    //mp3ファイルorフォルダーなら
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    songAndFolderList.add(file.getName());
                    if(file.isDirectory()) {
                        bmp = BitmapFactory.decodeFile(musicFolderPath + "/" + file.getName() + "/folder.jpg");
                    } else if (file.getName().endsWith(".mp3")){
                        bmp = BitmapFactory.decodeFile(musicFolderPath + "/folder.jpg");
                    }

                    MyLibraryListItem item = new MyLibraryListItem(bmp, file.getName());
                    listItems.add(item);


                }

            }

            //ここからリストビューをRecyclerViewに変更
            RecyclerView recyclerView = rootView.findViewById(R.id.songList);
            MyLibraryRecycleViewAdapter adapter = new MyLibraryRecycleViewAdapter(listItems) {
                @Override
                protected void onItemClicked(@NonNull String item) {
                    if (item.endsWith(".mp3")) {
                        //mp3だった場合
                        int trackNumber = songAndFolderList.indexOf(item);
                        playList = new ArrayList<>();
                        //選択したファイルのtrackNumber以降のファイルをプレイリスト化
                        for (int i = trackNumber; i < songAndFolderList.size(); i++) {
                            if (songAndFolderList.get(i).endsWith(".mp3")) {
                                playList.add(musicFolderPath + "/" + songAndFolderList.get(i));
                            }
                        }

                        try {
                            String albumArtPath = musicFolderPath + "/" + "folder.jpg";
                            binder.setPlayList(playList, albumArtPath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //ディレクトリ名を足してフォルダーを開き直す
                        songAndFolderList.clear();
                        SelectFolder.this.makeFolderList(musicFolderPath + "/" + item, rootView);
                    }
                }

            };
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick (View v){
    }
}
