package takuma.idei.ideiapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

public class MusicPlayerServiceMessenger extends Service {
    private Messenger _messenger;

    static class TestHandler extends Handler {

        private Context _cont;

        public TestHandler(Context cont) {
            _cont = cont;
        }

        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(_cont, "Messageを受信しました", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        _messenger = new Messenger(new TestHandler(getApplicationContext()));
    }

    @Override
    public IBinder onBind(Intent i) {
        Toast.makeText(getApplicationContext(), "Bindしました", Toast.LENGTH_SHORT).show();
        return _messenger.getBinder();
    }


}
