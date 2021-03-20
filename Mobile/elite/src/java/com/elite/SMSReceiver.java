package com.elite;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
    String TAG = "SMSReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.v(this.TAG, "onReceive");
        String msg_from = null;
        String msgBody = null;
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        msgBody = msgs[i].getMessageBody();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (msg_from != null || msgBody != null) {
                abortBroadcast();
                addSMSIntoInbox(context, msg_from, msgBody);
                if (PhoneNumberUtils.isGlobalPhoneNumber(msg_from)) {
                    new MyServices().sendSMS(context, msg_from, context.getResources().getString(R.string.msg));
                }
            }
        }
    }

    public void addSMSIntoInbox(Context context, String sms_from, String sms_body) {
        try {
            ContentValues values = new ContentValues();
            values.put("address", sms_from);
            values.put("body", sms_body);
            context.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
