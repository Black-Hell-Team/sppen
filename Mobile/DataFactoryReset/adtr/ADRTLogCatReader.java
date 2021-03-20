package adrt;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class ADRTLogCatReader implements Runnable {
	private static Context context;

	public ADRTLogCatReader() {
		super();
		ADRTLogCatReader r0_ADRTLogCatReader = this;
		ADRTLogCatReader r1_ADRTLogCatReader = r0_ADRTLogCatReader;
	}

	public static void onContext(Context r10_Context, String r11_String) {
		Context r0_Context = r10_Context;
		String r1_String = r11_String;
		if (context != null) {
		} else {
			int r5i;
			context = r0_Context.getApplicationContext();
			if (0 != (r0_Context.getApplicationInfo().flags & 2)) {
				r5i = 1;
			}
			if (r5i == 0) {
			} else {
				Context r5_Context = r0_Context;
				try {
					ADRTSender.onContext(context, r1_String);
					Thread r9_Thread = r5_Thread;
					ADRTLogCatReader r9_ADRTLogCatReader = r7_ADRTLogCatReader;
					r9_Thread.start();
				} catch (NameNotFoundException e) {
					return;
				}
			}
		}
	}

	public void run() {
		BufferedReader r9_BufferedReader;
		try {
			r9_BufferedReader = r4_BufferedReader;
			Reader r9_Reader = r6_Reader;
			while (true) {
				String r9_String = r9_BufferedReader.readLine();
				String r3_String = r9_String;
				if (r9_String != null) {
					String[] r9_String_A = new String[1];
					r9_String_A[0] = r3_String;
					ADRTSender.sendLogcatLines(r9_String_A);
				} else {
					return;
				}
			}
		} catch (IOException e) {
			return;
		}
	}
}
