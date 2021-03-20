package com.my.newproject2;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Process;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

public class SketchApplication extends Application {
	private UncaughtExceptionHandler uncaughtExceptionHandler;

	class AnonymousClass_1 implements UncaughtExceptionHandler {
		final /* synthetic */ SketchApplication this$0;

		AnonymousClass_1(SketchApplication r1_SketchApplication) {
			super();
			this$0 = r1_SketchApplication;
		}

		public void uncaughtException(Thread r6_Thread, Throwable r7_Throwable) {
			Intent r0_Intent = new Intent(this$0.getApplicationContext(), DebugActivity.class);
			r0_Intent.setFlags(32768);
			r0_Intent.putExtra("error", this$0.getStackTrace(r7_Throwable));
			((AlarmManager) this$0.getSystemService("alarm")).set(2, 1000, PendingIntent.getActivity(this$0.getApplicationContext(), 11111, r0_Intent, 1073741824));
			Process.killProcess(Process.myPid());
			System.exit(2);
			this$0.uncaughtExceptionHandler.uncaughtException(r6_Thread, r7_Throwable);
		}
	}


	public SketchApplication() {
		super();
	}

	private String getStackTrace(Throwable r3_Throwable) {
		Writer r0_Writer = new StringWriter();
		PrintWriter r1_PrintWriter = new PrintWriter(r0_Writer);
		while (r3_Throwable != null) {
			r3_Throwable.printStackTrace(r1_PrintWriter);
			r3_Throwable = r3_Throwable.getCause();
		}
		r1_PrintWriter.close();
		return r0_Writer.toString();
	}

	public void onCreate() {
		uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new AnonymousClass_1(this));
		super.onCreate();
	}
}
