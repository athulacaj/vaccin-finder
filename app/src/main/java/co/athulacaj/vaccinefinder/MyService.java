package co.athulacaj.vaccinefinder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;

import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.content.ContextCompat.getSystemService;
import static co.athulacaj.vaccinefinder.App.CHANNEL_ID;

public class MyService extends Service {
    static boolean showNotifications=true;
    @Override
    public void onCreate() {

        Toast.makeText(MyService.this
                ,"Created",Toast.LENGTH_SHORT).show();

        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String pinCode = intent.getStringExtra("pinCode");
        int refreshTime = intent.getIntExtra("refreshTime",8);
        Intent notificationIntent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Running Service")
                .setContentText("keep data live")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.notification)
                .setColor(ContextCompat.getColor(this, R.color.purple_500))
                .build();
        startForeground(1, notification);

        Timer timer = new Timer();
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minutes = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        Log.d("my","refresh time is"+refreshTime);

        timer.schedule(new TimerFunction(pinCode), 0, refreshTime * 60*1000);

        return START_NOT_STICKY;
    }



     class TimerFunction extends TimerTask {
        String pinCode;
        TimerFunction(String pinCode){this.pinCode=pinCode;};
        public void run() {
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int month = Calendar.getInstance().get(Calendar.MONTH);
            int year = Calendar.getInstance().get(Calendar.YEAR);
            String dt = day + "-" + (month + 1) + "-" + year;
            Timer timer = new Timer();
//
//            if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>7) {
            showNotifications = true;
//            }
            int[] dIdList = {297, 295};
            Log.d("my", "Hello service is running " + dt);
            if (isNetworkConnected() && FilterDataClass.isInternetAvailable()) {
                try {
                    JSONArray centersList = new JSONArray();
                    Log.d("my", "pincode is" + pinCode);
                    String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pinCode + "&date=" + dt;
                    JSONObject jsonObject = GetJsonObjectFromUrl.getJson(url);
//                    centersList=jsonObject.getJSONArray("centers");
                    for (int j = 0; j < jsonObject.getJSONArray("centers").length(); j++) {
                        centersList.put(jsonObject.getJSONArray("centers").getJSONObject(j));
                    }

                    if (showNotifications) {
                        runNotification(new FilterDataClass().getDataString(centersList));
                        showNotifications = false;
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    void runNotification(String title){
        Calendar calTd= Calendar.getInstance();
        Calendar calTm = Calendar.getInstance();
        Calendar calDm = Calendar.getInstance();
        Calendar calD4 = Calendar.getInstance();
        calTm.add(Calendar.DATE, 1);
        calDm.add(Calendar.DATE, 2);
        calD4.add(Calendar.DATE, 3);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int minutes = Calendar.getInstance().get(Calendar.MINUTE);
        Log.d("my", String.valueOf(minutes));
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("vaccine Availability ( Date: "+calTd.get(Calendar.DAY_OF_MONTH)+" | "+calTm.get(Calendar.DAY_OF_MONTH)+" | "+calDm.get(Calendar.DAY_OF_MONTH)+" | "+calD4.get(Calendar.DAY_OF_MONTH)+" |... )")
                .setContentText(title)
                .setSmallIcon(R.drawable.notification)
                .setColor(ContextCompat.getColor(this, R.color.purple_500))

                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(title))
                .build();

//        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        startForeground(1, notification);

//        notificationManager.notify(32323, notification);


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(MyService.this
                ,"service destroy",Toast.LENGTH_SHORT).show();

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

class FilterDataClass{
    boolean anyAvailable=false;

String getDataString(JSONArray centersList) throws IOException, JSONException{
    JSONArray centers=centersList;
//    places.contains("Thirumeni");
    String result="";
    for(int i=0;i<centers.length();i++){
        JSONObject singleCenterData=centers.getJSONObject(i);


            JSONArray sessions=singleCenterData.getJSONArray("sessions");

            String sessionString="";
            for(int j=0;j<sessions.length();j++) {
                JSONObject sessionData=sessions.getJSONObject(j);
                Log.d("my",sessionData.toString());

                sessionString+=sessionData.getString("available_capacity")+" | ";
            }

            result+=singleCenterData.getString("name")+": ( "+sessionString+" )\n";

    }
    String pul=centers.getJSONObject(0).getString("name");
//    int  cap=centers.getJSONObject(0).getInt("available_capacity");
    Log.d("my",result);

    return (result);
}

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

}
