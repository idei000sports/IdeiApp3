package takuma.idei.ideiapp;

public class BindMachine {
    /*
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

    public void BindMachine(Activity activity) {
        Intent serviceIntent = new Intent(context, MusicPlayerService.class);
        Objects.requireNonNull(context).startService(serviceIntent);
        context.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    public MusicPlayerAIDL getBinder() {
        return binder;
    }
    */



}
