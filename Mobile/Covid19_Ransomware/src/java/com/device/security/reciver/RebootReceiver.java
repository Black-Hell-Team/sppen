package com.device.security.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.device.security.activities.StartServiceActivity;
import com.device.security.util.SharedPreferencesUtil;

public class RebootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        SharedPreferencesUtil.setAuthorizedUser(context, "0");
        intent = new Intent(context, StartServiceActivity.class);
        intent.addFlags(268435456);
        context.startActivity(intent);
    }
}
