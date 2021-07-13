package edu.bluejack20_1.SOwhaDZ;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

//import static edu.bluejack20_1.SOwhaDZ.MyApplication.CHANNEL_1_ID;

public class myBackgroundProcess extends BroadcastReceiver {
    private String channelIDNotif = "SO";
    public String word;
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
//        Ringtone ringtone = RingtoneManager.getRingtone(context,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
//        ringtone.play();



//        Ringtone ringtone = RingtoneManager.getRingtone(context,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
//        ringtone.play();
//
//        Toast.makeText(context, "This is my background process: \n" + Calendar.getInstance().getTime().toString(), Toast.LENGTH_LONG).show();
//
//        SystemClock.sleep(2000);
//        ringtone.stop();

//        createNotificationChannel();
//            startForeground();


//        Toast.makeText(context, "This is my background process: \n" + Calendar.getInstance().getTime().toString(), Toast.LENGTH_LONG).show();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeatingIntent = new Intent(context, MainActivity.class);

        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String notifTitle = context.getResources().getText(R.string.word_of_the_day).toString();
        String notifDesc = context.getResources().getText(R.string.word_of_the_day_desc).toString();

        NotificationCompat.Builder builder = new  NotificationCompat.Builder(context, MyApplication.CHANNEL_1_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_so_whadz)
                .setContentTitle(notifTitle)
                .setContentText(notifDesc)
                .setOnlyAlertOnce(true)
                ;

            notificationManager.notify(100, builder.build());
//

//        SystemClock.sleep(1000);
//        ringtone.stop();
    }

//    private void createNotificationChannels() {
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
////            NotificationChannel channel1 = new NotificationChannel(
////                    CHANNEL_1_ID,"Channel 1", NotificationManager.IMPORTANCE_HIGH
////            );
////            channel1.setDescription("THIS IS CHANNEL 1");
////
////            NotificationManager manager = (NotificationManager) context.getSystemService(NotificationManager.class);
////            manager.createNotificationChannel(channel1);
////        }
////    }




}
