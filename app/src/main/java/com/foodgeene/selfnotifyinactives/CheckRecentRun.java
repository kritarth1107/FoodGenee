package com.foodgeene.selfnotifyinactives;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;

public class CheckRecentRun extends Service {

    private final static String TAG = "CheckRecentPlay";
    private static Long MILLISECS_PER_DAY = 86400000L;
    private static Long MILLISECS_PER_MIN = 60000L;
    SharedPreferences sessionManager;

      private static long delay = MILLISECS_PER_MIN * 3;   // 3 minutes (for testing)


    @Override
    public void onCreate() {
        super.onCreate();
        sessionManager = getSharedPreferences(SessionManager.SHARED_PREF_NAME, MODE_PRIVATE);
        if(sessionManager.getBoolean("enabled", true)){
            if (sessionManager.getLong("lastRun", Long.MAX_VALUE) < System.currentTimeMillis() - delay)
                sendNotification();

        }
        else{

        }

        setAlarm();

        Log.v(TAG, "Service stopped");
        stopSelf();
    }

    private void setAlarm() {

        Intent serviceIntent = new Intent(this, CheckRecentRun.class);
        PendingIntent pi = PendingIntent.getService(this, 131313, serviceIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pi);
        Log.v(TAG, "Alarm set");
    }



    private void sendNotification() {


        Intent mainIntent = new Intent(this, MainActivity.class);
        Notification noti = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("We Miss You!")
                .setContentText("There's alot of food missing you too.")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.logonew)
                .setTicker("We Miss You! There's alot of food missing you too.")
                .setWhen(System.currentTimeMillis())
                .getNotification();

        NotificationManager notificationManager
                = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(131315, noti);

        Log.v(TAG, "Notification sent");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
