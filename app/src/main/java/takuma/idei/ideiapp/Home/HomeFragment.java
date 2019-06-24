package takuma.idei.ideiapp.Home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import takuma.idei.ideiapp.MusicPlayerAIDL;
import takuma.idei.ideiapp.MusicPlayer.MusicPlayerService;
import takuma.idei.ideiapp.R;
import takuma.idei.ideiapp.SQL.SQLAlbumAndSongTableHelper;
import takuma.idei.ideiapp.SQL.SQLGetAllDatabase;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private String today = dateFormat.format(new Date());

    private HashMap<Integer, ArrayList<HomeListBean>> homeListMakerHashMap = new HashMap<Integer, ArrayList<HomeListBean>>();

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
    private Object Tag;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);




        Intent serviceIntent = new Intent(getActivity(), MusicPlayerService.class);
        Objects.requireNonNull(getActivity()).startService(serviceIntent);
        getActivity().bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Button button;
        button = rootView.findViewById(R.id.get_all_db);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SQLGetAllDatabase.class);
                startActivity(intent);
            }
        });



        setAndGetViewID(rootView);
        setAllListTitle(rootView);
        getAllList();

        return rootView;
    }
    public void getAllList() {

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.YEAR, -1);
        String oneYearBefore = dateFormat.format(cal1.getTime());

        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.MONTH, -1);
        String oneMonthBefore = dateFormat.format(cal2.getTime());




        String GET_RECENTLY_SQL = "SELECT song_title, album_art_path, id, song_path from count_table ORDER BY id DESC;";
        getSQL(GET_RECENTLY_SQL, homeListMakerHashMap.get(R.id.recent_history), "song_title");
        //String GET_THIS_MONTH_FAV_SQL = "SELECT album_title, album_art_path, count(*) AS COUNT, song_path FROM count_and_date_table  WHERE date BETWEEN '" + oneMonthBefore + "' AND '" + today + "' GROUP BY artist_name, album_title ORDER BY COUNT DESC;";
        String GET_THIS_MONTH_FAV_SQL = "SELECT album_title, album_art_path, count(*) AS COUNT, song_path FROM count_and_date_table  WHERE date BETWEEN '" + oneMonthBefore + "' AND '" + today + "' GROUP BY artist_name, album_title ORDER BY COUNT DESC;";
        getHomeListInDate(GET_THIS_MONTH_FAV_SQL, homeListMakerHashMap.get(R.id.this_month_fav));

        String GET_MY_FAVORITE_SQL = "SELECT album_title, album_art_path, count(*) AS COUNT, song_path FROM count_and_date_table GROUP BY artist_name, album_title ORDER BY COUNT DESC;";
        getHomeListInDate(GET_MY_FAVORITE_SQL, homeListMakerHashMap.get(R.id.my_favorite));

        String GET_PLAYBACK_LIST_SQL = "SELECT album_title, album_art_path, count(*) AS COUNT, song_path FROM count_and_date_table  WHERE date BETWEEN '" + oneYearBefore + "' AND '" + today + "' GROUP BY artist_name, album_title ORDER BY COUNT DESC;";
        getHomeListInDate(GET_PLAYBACK_LIST_SQL, homeListMakerHashMap.get(R.id.playback));
    }

    public void getHomeListInDate(String sql, ArrayList<HomeListBean> list) {
        SQLAlbumAndSongTableHelper sqlAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = sqlAlbumAndSongTableHelper.getReadableDatabase();
        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {

            if (cursor != null && cursor.getCount() > 0) {
                boolean next = cursor.moveToNext();
                int i = 0;
                while (next) {
                    if (i < 6) {
                        //album_title or song_title
                        String title = cursor.getString(cursor.getColumnIndex("album_title"));

                        String albumArtPath = cursor.getString(cursor.getColumnIndex("album_art_path"));
                        System.out.println(albumArtPath);

                        String songPath = cursor.getString(cursor.getColumnIndex("song_path"));

                        list.get(i).setAlbumArtAndInfo(title, albumArtPath, songPath);

                        int finalI = i;
                        list.get(i).getImageButton().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), list.get(finalI).getTitle(), Toast.LENGTH_SHORT).show();
                                playSong(list.get(finalI).getSongPath(), list.get(finalI).getAlbumArtPath());
                            }
                        });

                        i++;
                    }

                    next = cursor.moveToNext();
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        sqlAlbumAndSongTableHelper.close();

    }


    public void getSQL(String sql, ArrayList<HomeListBean> list, String wantColumnName) {
        String GET_RECENTLY_SQL = "SELECT song_title, album_art_path, song_path FROM count_table ORDER BY id DESC;";
        SQLAlbumAndSongTableHelper sqlAlbumAndSongTableHelper = new SQLAlbumAndSongTableHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = sqlAlbumAndSongTableHelper.getWritableDatabase();

        try (Cursor cursor = sqLiteDatabase.rawQuery(GET_RECENTLY_SQL, null)) {
            //cursor.moveToFirst();
            if (cursor != null && cursor.getCount() > 0) {
                boolean next = cursor.moveToNext();
                int i = 0;
                while (next) {
                    if (i < 6) {
                        //album_title or song_title
                        String title = cursor.getString(cursor.getColumnIndex("song_title"));
                        String albumArtPath = cursor.getString(cursor.getColumnIndex("album_art_path"));
                        String songPath = cursor.getString(cursor.getColumnIndex("song_path"));

                        list.get(i).setAlbumArtAndInfo(title, albumArtPath, songPath);

                        int finalI = i;
                        list.get(i).getImageButton().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), list.get(finalI).getTitle(), Toast.LENGTH_SHORT).show();
                                playSong(list.get(finalI).getSongPath(), list.get(finalI).getAlbumArtPath());
                            }
                        });

                        i++;
                    }

                    next = cursor.moveToNext();
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }


        sqlAlbumAndSongTableHelper.close();
    }





    private void setAndGetViewID (View rootView) {

        int[] list_name = {R.id.recent_history, R.id.this_month_fav, R.id.playback, R.id.my_favorite}      ;
        int[] list_txts = {R.id.list_txt1, R.id.list_txt2, R.id.list_txt3, R.id.list_txt4, R.id.list_txt5, R.id.list_txt6};
        int[] list_imgs = {R.id.list_img1, R.id.list_img2, R.id.list_img3, R.id.list_img4, R.id.list_img5, R.id.list_img6};

        for (int i = 0; i < list_name.length; i++) {
            ArrayList<HomeListBean> homeList = new ArrayList<>();
            for (int j = 0; j < list_txts.length && j < list_imgs.length; j++) {
                HomeListBean homeListBean = new HomeListBean();
                homeListBean.setTextView((TextView) rootView.findViewById(list_name[i]).findViewById(list_txts[j]));
                homeListBean.setImageButton((ImageButton) rootView.findViewById(list_name[i]).findViewById(list_imgs[j]));
                homeList.add(homeListBean);
            }
            homeListMakerHashMap.put(list_name[i], homeList);
        }
    }





    public void setAllListTitle(View rootView) {
        int[] title_list = {R.id.recent_history_title, R.id.this_month_fav_title, R.id.playback_title, R.id.my_favorite_title};
        String[] titles = {"最近再生した曲", "最近のお気に入り", "プレイバック", "あなたのお気に入りのアルバムと曲"};
        String[] infos = {"", "あなたが今月リピートで聴いた音楽", "あなたが過去数ヶ月間で一番聴いた音楽", "あなたのお気に入りのアルバムと曲"};

        for(int i = 0; i < title_list.length; i++) {
            TextView textView = (TextView)rootView.findViewById(title_list[i]).findViewById(R.id.list_title);
            TextView textView_info = (TextView)rootView.findViewById(title_list[i]).findViewById(R.id.list_info);
            textView.setText(titles[i]);
            textView_info.setText(infos[i]);
        }
    }

    public void playSong(String songPath, String albumArtPath){
        try {
            binder.playSongFromTop(songPath, albumArtPath);
        }catch(Exception e) {

        }


    }





    @Override
    public void onClick(View v) {

    }

}
