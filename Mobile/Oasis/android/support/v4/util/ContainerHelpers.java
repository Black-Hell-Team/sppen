package android.support.v4.util;

import android.support.v4.media.TransportMediator;

class ContainerHelpers {
	static final int[] EMPTY_INTS;
	static final long[] EMPTY_LONGS;
	static final Object[] EMPTY_OBJECTS;

	static {
		EMPTY_INTS = new int[0];
		EMPTY_LONGS = new long[0];
		EMPTY_OBJECTS = new Object[0];
	}

	ContainerHelpers() {
		super();
	}

	static int binarySearch(int[] array, int size, int value) {
		int mid;
		int lo = 0;
		int hi = size - 1;
		while (lo <= hi) {
			mid = (lo + hi) >>> 1;
			int midVal = array[mid];
			if (midVal < value) {
				lo = mid + 1;
			} else {
				if (midVal > value) {
					hi = mid - 1;
				}
				return mid;
			}
		}
		mid = lo ^ -1;
		return mid;
	}

	static int binarySearch(long[] array, int size, long value) {
		int mid;
		int lo = 0;
		int hi = size - 1;
		while (lo <= hi) {
			mid = (lo + hi) >>> 1;
			long midVal = array[mid];
			if (midVal < value) {
				lo = mid + 1;
			} else {
				if (midVal > value) {
					hi = mid - 1;
				}
				return mid;
			}
		}
		mid = lo ^ -1;
		return mid;
	}

	public static boolean equal(Object a, Object b) {
		if (a != b) {
			if (a == null || !a.equals(b)) {
				return false;
			}
		}
		return true;
	}

	public static int idealByteArraySize(int need) {
		int i = TransportMediator.FLAG_KEY_MEDIA_PLAY;
		while (i < 32) {
			if (need <= (1 << i) - 12) {
				return (1 << i) - 12;
			} else {
				i++;
			}
		}
		return need;
	}

	public static int idealIntArraySize(int need) {
		return idealByteArraySize(need * 4) / 4;
	}

	public static int idealLongArraySize(int need) {
		return idealByteArraySize(need * 8) / 8;
	}
}
