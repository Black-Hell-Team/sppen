package com.elite;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmManagerTXTShield {
    long alarmTimeInterval = 60000;

    public void setAlarm(Context context) {
        ((AlarmManager) context.getSystemService("alarm")).setRepeating(0, System.currentTimeMillis(), this.alarmTimeInterval, PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0));
    }

    public void cancelAlarm(Context context) {
        ((AlarmManager) context.getSystemService("alarm")).cancel(PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0));
    }
}
