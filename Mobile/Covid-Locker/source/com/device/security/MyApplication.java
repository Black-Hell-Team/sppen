package com.device.security;

import android.content.Context;
import androidx.multidex.MultiDexApplication;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

public class MyApplication extends MultiDexApplication {
    private static final String TAG = "AppLocker";
    private static MyApplication mContext;

    public void onCreate() {
        super.onCreate();
        mContext = this;
        initializeLogger();
    }

    public static Context getContext() {
        return mContext.getApplicationContext();
    }

    private void initializeLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder().tag(TAG).build()));
        Logger.addLogAdapter(new AndroidLogAdapter() {
            /* class com.device.security.MyApplication.C02761 */

            @Override // com.orhanobut.logger.LogAdapter, com.orhanobut.logger.AndroidLogAdapter
            public boolean isLoggable(int i, String str) {
                return false;
            }
        });
    }
}
