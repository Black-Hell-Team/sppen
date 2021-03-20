package android.support.v4.app;

import android.app.Activity;
import java.io.FileDescriptor;
import java.io.PrintWriter;

class ActivityCompatHoneycomb {
	ActivityCompatHoneycomb() {
		super();
	}

	static void dump(Activity activity, String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
		activity.dump(prefix, fd, writer, args);
	}

	static void invalidateOptionsMenu(Activity activity) {
		activity.invalidateOptionsMenu();
	}
}
