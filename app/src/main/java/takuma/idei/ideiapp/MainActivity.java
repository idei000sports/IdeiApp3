package takuma.idei.ideiapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import takuma.idei.ideiapp.Home.HomeFragment;
import takuma.idei.ideiapp.MusicPlayer.MusicPlayerService;
import takuma.idei.ideiapp.MyLibrary.MyLibraryFragment;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;
    private FragmentManager fragmentManager;

    private final static int PERMISSON_REQUEST_CODE = 2;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_search:
                    fragment = new SearchFragment();
                    break;
                case R.id.navigation_selectFolder:
                    fragment = new MyLibraryFragment();
                    break;
            }

            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ストレージ利用の許諾得る
        requestReadStorage();
        //ストレージのファイルをSQLiteデータベースに登録
        InitialCreationOfDatabase initialCreationOfDatabase = new InitialCreationOfDatabase(getApplicationContext());

        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(this, MusicPlayerService.class);
        Objects.requireNonNull(this).startService(serviceIntent);

        fragmentManager = getSupportFragmentManager();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestReadStorage(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){

            }requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
            }, PERMISSON_REQUEST_CODE);
        }else{
            // ここに許可済みの時の動作を書く
        }
    }
}
