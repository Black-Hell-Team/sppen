package android.support.v4.os;

import android.os.Environment;
import java.io.File;

class EnvironmentCompatKitKat {
	EnvironmentCompatKitKat() {
		super();
	}

	public static String getStorageState(File path) {
		return Environment.getStorageState(path);
	}
}
