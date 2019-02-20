package com.kris.questademo2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by Jaison on 17/06/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
         int id = intent.getIntExtra("requestCode", 0);
                    boolean isDue = intent.getBooleanExtra("isDue", false);
                    String questDetail = intent.getStringExtra("QuestDetail");
                    if (intent.getAction() != null && context != null) {
                        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                            PlayerData playerData = new PlayerData(context);
                            Player player = playerData.readJSON();

                            NotificationScheduler.setReminder(context, AlarmReceiver.class, player.getUlTime(id), id, questDetail, isDue);

                            return;
                        }
                    }

            NotificationScheduler.showNotification(context, Main2Activity.class,
                    "", questDetail, id,isDue);
    }
}


