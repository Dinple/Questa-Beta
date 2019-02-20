package com.kris.questademo2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;
import java.util.Calendar;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;
import static android.content.Context.ALARM_SERVICE;

public class NotificationScheduler
{

    public static void setReminder(Context context,Class<?> cls,Calendar time, int requestCode,String content,boolean isDue)
    {

        time.set(Calendar.SECOND,0);

        // Enable a receiver
            ComponentName receiver = new ComponentName(context, cls);
            PackageManager pm = context.getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);


            Intent intent1 = new Intent(context, cls);
            intent1.putExtra("requestCode",requestCode);
            intent1.putExtra("QuestDetail",content);
            intent1.putExtra("isDue",isDue);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent1, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
    }


    public static void cancelReminder(Context context,Class<?> cls, int requestCode,String content)
    {
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        intent1.putExtra("requestCode",requestCode);
        intent1.putExtra("QuestDetail",content);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void showNotification(Context context,Class<?> cls,String title,String content,int requestCode,boolean isDue)
    {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        notificationIntent.putExtra("requestCode",requestCode);

        if(isDue){
            notificationIntent.putExtra("type","due");
        }else{
            notificationIntent.putExtra("type","unlock");
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Notification notification = builder.setContentTitle(title)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE) //Important for heads-up notification
                .setPriority(Notification.PRIORITY_MAX) //Important for heads-up notification
                .setContentIntent(pendingIntent).build();
        RemoteViews unlockContentView = new RemoteViews(context.getPackageName(), R.layout.notify_new);
        RemoteViews dueContentView = new RemoteViews(context.getPackageName(), R.layout.notify_due);

        if(isDue){
            dueContentView.setTextViewText(R.id.tv_notify_due, content);
            notification.contentView = dueContentView;
        }else{
            unlockContentView.setTextViewText(R.id.tv_notify_new, content);
            notification.contentView = unlockContentView;
        }


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(requestCode, notification);

            if(isDue){
                Main2Activity.questDue(requestCode);
            }else{
                Main2Activity.questUnlock(requestCode);}

    }

}
