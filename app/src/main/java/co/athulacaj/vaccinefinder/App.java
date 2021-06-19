package co.athulacaj.vaccinefinder;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
public class App extends Application {
    public static final String CHANNEL_ID = "exampleServiceChannel";
    public static final String NewCHANNEL_ID = "exampleServiceChannel";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel(CHANNEL_ID);
        createNotificationChannel(NewCHANNEL_ID);
    }
    private void createNotificationChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    channelId,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);

        }
    }
}