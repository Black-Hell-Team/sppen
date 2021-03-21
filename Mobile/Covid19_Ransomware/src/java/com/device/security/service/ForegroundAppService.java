package com.device.security.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import androidx.core.app.NotificationCompat.Builder;
import com.device.security.R;
import com.device.security.activities.MainActivity;
import com.device.security.util.InstalledAppManager;
import com.device.security.util.InstalledAppUtil;
import com.orhanobut.logger.Logger;
import java.util.Timer;
import java.util.TimerTask;

public class ForegroundAppService extends Service {
    public static final String CHANNEL_ID = "DeviceSecurityServiceChannel";
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        startTimerTask();
        createNotificationChannel();
        PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        startForeground(1, new Builder(this, CHANNEL_ID).setContentText("Running in background...").setSmallIcon(R.mipmap.ic_launcher).build());
        return 1;
    }

    private void createNotificationChannel() {
        if (VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Designius App Channel", 3);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

    private void startTimerTask() {
        stopTimerTask();
        initializeTimerTask();
        Timer timer = new Timer();
        this.mTimer = timer;
        timer.schedule(this.mTimerTask, 0, 60000);
    }

    private void stopTimerTask() {
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer.purge();
            this.mTimer = null;
        }
    }

    private void initializeTimerTask() {
        this.mTimerTask = new TimerTask() {
            public void run() {
                new Thread(new -$$Lambda$ForegroundAppService$1$m95tx_mwv37HnrxD4DeSOyuVUHk(this)).start();
            }

            public /* synthetic */ void lambda$run$0$ForegroundAppService$1() {
                try {
                    InstalledAppManager.getInstance().addInstalledAppsList(InstalledAppUtil.queryInstalledApps(ForegroundAppService.this.getApplicationContext()));
                } catch (Exception e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error Getting Installed Apps List: ");
                    stringBuilder.append(e.getMessage());
                    Logger.d(stringBuilder.toString());
                }
            }
        };
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            stopTimerTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
