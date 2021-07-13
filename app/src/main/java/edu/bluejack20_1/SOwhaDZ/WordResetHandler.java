package edu.bluejack20_1.SOwhaDZ;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class WordResetHandler {
    private Context context;

    public WordResetHandler(Context context){
        this.context = context;
    }
    public void setAlarmManager(){
        Intent intent = new Intent(context, ScheduleTask.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,2,intent,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(am != null){
            long triggerAfter =  1000;
            long triggerEvery =  1000;
            am.setRepeating(AlarmManager.RTC_WAKEUP,triggerAfter,triggerEvery,sender);
        }
    }

    public void cancelAlarm(){
        Intent intent = new Intent(context,ScheduleTask.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,2, intent,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(am != null){
            am.cancel(sender);
        }
    }
}
