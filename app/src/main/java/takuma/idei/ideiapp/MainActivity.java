package takuma.idei.ideiapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private Fragment fragment;
    private FragmentActivity fragmentActivity;
    private FragmentManager fragmentManager;
    private View activity;
    private final static int PERMISSON_REQUEST_CODE = 2;
    private Intent music_player2;
    private String memo = "githubテスト";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    fragment = new Home();
                    break;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    fragment = new Dashboard();
                    break;
                case R.id.navigation_notifications:
                    Intent i = new Intent(getApplicationContext(),MusicPlayerActivity.class);
                    startActivity(i);
                    //break;
                case R.id.navigation_selectFolder:
                    mTextMessage.setText("folder");
                    fragment = new SelectFolder();
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

        setContentView(R.layout.activity_main);
        requestReadStorage();

        fragmentManager = getSupportFragmentManager();
        fragment = new Home();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestReadStorage(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)){

            }

            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, PERMISSON_REQUEST_CODE);
        }else{
            // ここに許可済みの時の動作を書く
        }
    }
}
