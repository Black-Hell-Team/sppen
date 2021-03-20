package adrt;

import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;

public class ADRTSender {
	private static Context context;
	private static String debuggerPackageName;

	public ADRTSender() {
		super();
		ADRTSender r0_ADRTSender = this;
		ADRTSender r1_ADRTSender = r0_ADRTSender;
	}

	public static void onContext(Context r3_Context, String r4_String) {
		context = r3_Context;
		debuggerPackageName = r4_String;
	}

	public static void sendBreakpointHit(String r12_String, ArrayList<String> r13_ArrayList_String, ArrayList<String> r14_ArrayList_String, ArrayList<String> r15_ArrayList_String, ArrayList<String> r16_ArrayList_String, ArrayList<String> r17_ArrayList_String, ArrayList<String> r18_ArrayList_String) {
		Intent r11_Intent = r8_Intent;
		Intent r7_Intent = r11_Intent;
		context.sendBroadcast(r7_Intent);
	}

	public static void sendConnect(String r6_String) {
		Intent r5_Intent = r2_Intent;
		Intent r1_Intent = r5_Intent;
		context.sendBroadcast(r1_Intent);
	}

	public static void sendFields(String r10_String, String r11_String, ArrayList<String> r12_ArrayList_String, ArrayList<String> r13_ArrayList_String, ArrayList<String> r14_ArrayList_String) {
		Intent r9_Intent = r6_Intent;
		Intent r5_Intent = r9_Intent;
		context.sendBroadcast(r5_Intent);
	}

	public static void sendLogcatLines(String[] r6_String_A) {
		Intent r5_Intent = r2_Intent;
		Intent r1_Intent = r5_Intent;
		context.sendBroadcast(r1_Intent);
	}

	public static void sendStop(String r6_String) {
		Intent r5_Intent = r2_Intent;
		Intent r1_Intent = r5_Intent;
		context.sendBroadcast(r1_Intent);
	}
}
