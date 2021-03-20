package android.support.v4.util;

import android.util.Log;
import java.io.Writer;

public class LogWriter extends Writer {
	private StringBuilder mBuilder;
	private final String mTag;

	public LogWriter(String tag) {
		super();
		mBuilder = new StringBuilder(128);
		mTag = tag;
	}

	private void flushBuilder() {
		if (mBuilder.length() > 0) {
			Log.d(mTag, mBuilder.toString());
			mBuilder.delete(0, mBuilder.length());
		}
	}

	public void close() {
		flushBuilder();
	}

	public void flush() {
		flushBuilder();
	}

	public void write(char[] buf, int offset, int count) {
		int i = 0;
		while (i < count) {
			char c = buf[offset + i];
			if (c == '\n') {
				flushBuilder();
			} else {
				mBuilder.append(c);
			}
			i++;
		}
	}
}
