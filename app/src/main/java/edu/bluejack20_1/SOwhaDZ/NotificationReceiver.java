package edu.bluejack20_1.SOwhaDZ;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NotificationReceiver extends BroadcastReceiver {
    public static String word;

    private static final String FILE_NAME = "wotd.txt";

    @Override
    public void onReceive(Context context, Intent intent) {
//        String title = intent.getStringExtra("title2");
//        String desc = intent.getStringExtra("desc2");
//        Intent intent1 = new Intent(context, DetailTranslationActivity.class);
//        intent1.putExtra("data1", title );
//        intent1.putExtra("data2",  desc);
//        context.startActivity(intent1);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeatingIntent = new Intent(context, MainActivity.class);

        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String notifTitle = context.getResources().getText(R.string.word_of_the_day).toString();
        String notifDesc = context.getResources().getText(R.string.word_of_the_day_desc).toString();

        NotificationCompat.Builder builder = new  NotificationCompat.Builder(context, MyApplication.CHANNEL_1_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.google_logo)
                .setContentTitle(notifTitle)
                .setContentText(notifDesc)
                .setOnlyAlertOnce(true);

        if (intent.getAction() == "MY_NOTIFICATION_MESSAGE"){
            notificationManager.notify(100, builder.build());


        }
    }

    public void save(View v){

    }
}
