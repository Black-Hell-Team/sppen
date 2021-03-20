package com.elite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    String TAG = "AlarmReceiver";

    public void onReceive(Context context, Intent arg1) {
        if (new MyServices().isMyServiceRunning(context)) {
            Log.v(this.TAG, "AlarmReceiver onReceive() service is running. ");
            return;
        }
        Log.v(this.TAG, "AlarmReceiver onReceive() service is not running, need to start");
        context.startService(new Intent(context, IntentServiceClass.class));
    }
}
