package takuma.idei.ideiapp;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.Objects;

import takuma.idei.ideiapp.MusicPlayer.MusicPlayerService;

public class BindMachine extends Application {

    public static MusicPlayerAIDL binder;
    public static ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
                binder = MusicPlayerAIDL.Stub.asInterface(service);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };

    public BindMachine() {
        Intent serviceIntent = new Intent(getApplicationContext(), MusicPlayerService.class);
        Objects.requireNonNull(getApplicationContext()).startService(serviceIntent);
        getApplicationContext().bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    public static MusicPlayerAIDL getBinder() {
        return binder;
    }

    public static ServiceConnection getConnection() {
        return connection;
    }
}
