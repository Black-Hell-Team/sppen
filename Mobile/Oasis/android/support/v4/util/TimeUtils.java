package android.support.v4.util;

import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.widget.CursorAdapter;
import java.io.PrintWriter;

public class TimeUtils {
	public static final int HUNDRED_DAY_FIELD_LEN = 19;
	private static final int SECONDS_PER_DAY = 86400;
	private static final int SECONDS_PER_HOUR = 3600;
	private static final int SECONDS_PER_MINUTE = 60;
	private static char[] sFormatStr;
	private static final Object sFormatSync;

	static {
		sFormatSync = new Object();
		sFormatStr = new char[24];
	}

	public TimeUtils() {
		super();
	}

	/* JADX WARNING: inconsistent code */
	/*
	private static int accumField(int r1_amt, int r2_suffix, boolean r3_always, int r4_zeropad) {
		r0 = 99;
		if (r1_amt > r0) goto L_0x0009;
	L_0x0004:
		if (r3_always == 0) goto L_0x000c;
	L_0x0006:
		r0 = 3;
		if (r4_zeropad < r0) goto L_0x000c;
	L_0x0009:
		r0 = r2_suffix + 3;
	L_0x000b:
		return r0;
	L_0x000c:
		r0 = 9;
		if (r1_amt > r0) goto L_0x0015;
	L_0x0010:
		if (r3_always == 0) goto L_0x0018;
	L_0x0012:
		r0 = 2;
		if (r4_zeropad < r0) goto L_0x0018;
	L_0x0015:
		r0 = r2_suffix + 2;
		goto L_0x000b;
	L_0x0018:
		if (r3_always != 0) goto L_0x001c;
	L_0x001a:
		if (r1_amt <= 0) goto L_0x001f;
	L_0x001c:
		r0 = r2_suffix + 1;
		goto L_0x000b;
	L_0x001f:
		r0 = 0;
		goto L_0x000b;
	}
	*/
	private static int accumField(int amt, int suffix, boolean always, int zeropad) {
		if (amt <= 99) {
			if (!always || zeropad < 3) {
				if (amt <= 9) {
					if (!always || zeropad < 2) {
						return suffix + 1;
					}
				}
				return suffix + 2;
			}
		}
		return suffix + 3;
	}

	public static void formatDuration(long time, long now, PrintWriter pw) {
		if (time == 0) {
			pw.print("--");
		} else {
			formatDuration(time - now, pw, 0);
		}
	}

	public static void formatDuration(long duration, PrintWriter pw) {
		formatDuration(duration, pw, 0);
	}

	public static void formatDuration(long duration, PrintWriter pw, int fieldLen) {
		synchronized(sFormatSync) {
			pw.print(new String(sFormatStr, 0, formatDurationLocked(duration, fieldLen)));
		}
	}

	public static void formatDuration(long duration, StringBuilder builder) {
		synchronized(sFormatSync) {
			int r1i = 0;
			builder.append(sFormatStr, 0, formatDurationLocked(duration, r1i));
		}
	}

	private static int formatDurationLocked(long duration, int fieldLen) {
		if (sFormatStr.length < fieldLen) {
			sFormatStr = new char[fieldLen];
		}
		char[] formatStr = sFormatStr;
		if (duration == 0) {
			fieldLen--;
			while (0 < fieldLen) {
				formatStr[0] = ' ';
			}
			formatStr[0] = '0';
			return 1;
		} else {
			char prefix;
			boolean zeropad;
			boolean r10z;
			int r11i;
			if (duration > 0) {
				prefix = '+';
			} else {
				prefix = '-';
				duration = -duration;
			}
			int millis = (int) (duration % 1000);
			int seconds = (int) Math.floor((double) (duration / 1000));
			int days = 0;
			int hours = 0;
			int minutes = 0;
			if (seconds > 86400) {
				days = seconds / 86400;
				seconds -= 86400 * days;
			}
			if (seconds > 3600) {
				hours = seconds / 3600;
				seconds -= hours * 3600;
			}
			if (seconds > 60) {
				minutes = seconds / 60;
				seconds -= minutes * 60;
			}
			int pos = 0;
			if (fieldLen != 0) {
				boolean r4z;
				int r4i;
				int myLen = accumField(days, 1, false, 0);
				int r6i = 1;
				if (myLen > 0) {
					r4z = true;
				} else {
					r4z = false;
				}
				myLen += accumField(hours, r6i, r4z, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
				r6i = 1;
				if (myLen > 0) {
					r4z = true;
				} else {
					r4z = false;
				}
				myLen += accumField(minutes, r6i, r4z, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
				r6i = 1;
				if (myLen > 0) {
					r4z = true;
				} else {
					r4z = false;
				}
				myLen += accumField(seconds, r6i, r4z, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
				if (myLen > 0) {
					r4i = WearableExtender.SIZE_MEDIUM;
				} else {
					r4i = 0;
				}
				myLen += accumField(millis, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER, true, r4i) + 1;
				while (myLen < fieldLen) {
					formatStr[pos] = ' ';
					pos++;
					myLen++;
				}
			}
			formatStr[pos] = prefix;
			pos++;
			int start = pos;
			if (fieldLen != 0) {
				zeropad = true;
			} else {
				zeropad = false;
			}
			pos = printField(formatStr, days, 'd', pos, false, 0);
			char r8c = 'h';
			if (pos != start) {
				r10z = true;
			} else {
				r10z = false;
			}
			if (zeropad) {
				r11i = CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER;
			} else {
				r11i = 0;
			}
			pos = printField(formatStr, hours, r8c, pos, r10z, r11i);
			r8c = 'm';
			if (pos != start) {
				r10z = true;
			} else {
				r10z = false;
			}
			if (zeropad) {
				r11i = CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER;
			} else {
				r11i = 0;
			}
			pos = printField(formatStr, minutes, r8c, pos, r10z, r11i);
			r8c = 's';
			if (pos != start) {
				r10z = true;
			} else {
				r10z = false;
			}
			if (zeropad) {
				r11i = CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER;
			} else {
				r11i = 0;
			}
			pos = printField(formatStr, seconds, r8c, pos, r10z, r11i);
			if (!zeropad || pos == start) {
				r11i = 0;
			} else {
				r11i = WearableExtender.SIZE_MEDIUM;
			}
			pos = printField(formatStr, millis, 'm', pos, true, r11i);
			formatStr[pos] = 's';
			return pos + 1;
		}
	}

	private static int printField(char[] formatStr, int amt, char suffix, int pos, boolean always, int zeropad) {
		if (always || amt > 0) {
			int dig;
			int startPos = pos;
			if ((!always || zeropad < 3) && amt <= 99) {
				if ((!always || zeropad < 2) && amt <= 9 && startPos == pos) {
					formatStr[pos] = (char) (amt + 48);
					pos++;
					formatStr[pos] = suffix;
					pos++;
				} else {
					dig = amt / 10;
					formatStr[pos] = (char) (dig + 48);
					pos++;
					amt -= dig * 10;
				}
				dig = amt / 10;
				formatStr[pos] = (char) (dig + 48);
				pos++;
				amt -= dig * 10;
				formatStr[pos] = (char) (amt + 48);
				pos++;
				formatStr[pos] = suffix;
				pos++;
			} else {
				dig = amt / 100;
				formatStr[pos] = (char) (dig + 48);
				pos++;
				amt -= dig * 100;
			}
			dig = amt / 100;
			formatStr[pos] = (char) (dig + 48);
			pos++;
			amt -= dig * 100;
			if ((!always || zeropad < 2) && amt <= 9 && startPos == pos) {
				formatStr[pos] = (char) (amt + 48);
				pos++;
				formatStr[pos] = suffix;
				pos++;
			} else {
				dig = amt / 10;
				formatStr[pos] = (char) (dig + 48);
				pos++;
				amt -= dig * 10;
			}
			dig = amt / 10;
			formatStr[pos] = (char) (dig + 48);
			pos++;
			amt -= dig * 10;
			formatStr[pos] = (char) (amt + 48);
			pos++;
			formatStr[pos] = suffix;
			pos++;
		} else {
			return pos;
		}
		return pos;
	}
}
