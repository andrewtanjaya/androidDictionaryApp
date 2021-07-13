package edu.bluejack20_1.SOwhaDZ;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ScheduleTask extends Worker {
    private Context context;
    WordLibrary wl = new WordLibrary();
    public ScheduleTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        Toast.makeText(context,"Ringing",Toast.LENGTH_SHORT).show();
        FragmentHome.titleWordOfTheDay.setText(wl.getRandomwordEnglish());
        return null;
    }
}
