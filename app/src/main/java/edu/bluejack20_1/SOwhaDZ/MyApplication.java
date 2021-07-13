package edu.bluejack20_1.SOwhaDZ;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MyApplication extends Application {
    DatabaseReference mDatabase;
    public static String app_id;
    public static String app_key;
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    @Override
    public void onCreate() {

        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        createNotificationChannels();
        try{
            startService(new Intent(this, YourService.class));
        }catch (Exception e){

        }
        mDatabase = FirebaseDatabase.getInstance().getReference("appKey");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                app_key = snapshot.child("app_key").getValue().toString();
                app_id = snapshot.child("app_id").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        Calendar calendar = Calendar.getInstance();
//
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 30);
//        calendar.set(Calendar.SECOND, 00);
//
//        Intent intent = new Intent(MyApplication.this, NotificationReceiver.class);
//        intent.setAction("MY_NOTIFICATION_MESSAGE");
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT );
//
//        AlarmManager alarmManager = (AlarmManager) MyApplication.this.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(alarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);


        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme);
        } else setTheme(R.style.lightTheme);
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,"Channel 1", NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("THIS IS CHANNEL 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,"Channel 2", NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("THIS IS CHANNEL 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}

