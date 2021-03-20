package com.elite;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import java.util.Timer;
import java.util.TimerTask;

public class IntentServiceClass extends Service {
    private static Timer timer = new Timer();
    private Context context;
    private final Handler toastHandler = new Handler() {
        public void handleMessage(Message msg) {
            new MyServices().keepRunningActivity(IntentServiceClass.this.context);
        }
    };

    private class mainTask extends TimerTask {
        private mainTask() {
        }

        /* synthetic */ mainTask(IntentServiceClass intentServiceClass, mainTask maintask) {
            this();
        }

        public void run() {
            IntentServiceClass.this.toastHandler.sendEmptyMessage(0);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        startService();
    }

    private void startService() {
        timer.scheduleAtFixedRate(new mainTask(this, null), 0, 500);
        this.context = this;
    }
}
