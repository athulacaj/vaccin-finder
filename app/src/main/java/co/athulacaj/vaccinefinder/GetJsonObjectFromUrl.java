package co.athulacaj.vaccinefinder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static co.athulacaj.vaccinefinder.App.CHANNEL_ID;

public class GetJsonObjectFromUrl{
    public static JSONObject getJson(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
//        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }

}
//class TimerFunction extends TimerTask {
//    public void run() {
//        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//        int month = Calendar.getInstance().get(Calendar.MONTH);
//        int year = Calendar.getInstance().get(Calendar.YEAR);
//        String dt=day+"-"+(month+1)+"-"+year;
//        Timer timer = new Timer();
////
////            if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>7) {
//        showNotifications=true;
////            }
//        int[] dIdList={297,295};
//        System.out.println("Hello World! "+dt);
//        try {
//            JSONArray centersList=new JSONArray();
//            for (int i=0;i<dIdList.length;i++){
//                JSONObject jsonObject = GetJsonObjectFromUrl.getJson("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="+dIdList[i]+"&date="+dt);
////                    centersList=jsonObject.getJSONArray("centers");
//                for (int j=0;j<jsonObject.getJSONArray("centers").length();j++){
//                    centersList.put(jsonObject.getJSONArray("centers").getJSONObject(j));
//                }
//            }
//            if(showNotifications) {
//                runNotification(new FilterDataClass().getDataString(centersList));
//                showNotifications=false;
//            }
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
//    void runNotification(String title){
//        Calendar calTd= Calendar.getInstance();
//        Calendar calTm = Calendar.getInstance();
//        Calendar calDm = Calendar.getInstance();
//        calTm.add(Calendar.DATE, 1);
//        calDm.add(Calendar.DATE, 2);
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);
//
//
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        int minutes = Calendar.getInstance().get(Calendar.MINUTE);
//        Log.d("my", String.valueOf(minutes));
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("vaccine Availability ( Date: "+calTd.get(Calendar.DAY_OF_MONTH)+" | "+calTm.get(Calendar.DAY_OF_MONTH)+" | "+calDm.get(Calendar.DAY_OF_MONTH)+" )")
//                .setContentText(title)
//                .setSmallIcon(R.drawable.ic_android_black_24dp)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setSound(alarmSound)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(title))
//                .build();
//
//        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//
////        NotificationManager notificationManager =
////                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(32323, notification);
//
//
//    }

