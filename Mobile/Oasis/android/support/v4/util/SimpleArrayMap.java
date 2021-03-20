package android.support.v4.util;

import android.support.v4.media.TransportMediator;
import java.util.Map;

public class SimpleArrayMap<K, V> {
	private static final int BASE_SIZE = 4;
	private static final int CACHE_SIZE = 10;
	private static final boolean DEBUG = false;
	private static final String TAG = "ArrayMap";
	static Object[] mBaseCache;
	static int mBaseCacheSize;
	static Object[] mTwiceBaseCache;
	static int mTwiceBaseCacheSize;
	Object[] mArray;
	int[] mHashes;
	int mSize;

	public SimpleArrayMap() {
		super();
		mHashes = ContainerHelpers.EMPTY_INTS;
		mArray = ContainerHelpers.EMPTY_OBJECTS;
		mSize = 0;
	}

	public SimpleArrayMap(int capacity) {
		super();
		if (capacity == 0) {
			mHashes = ContainerHelpers.EMPTY_INTS;
			mArray = ContainerHelpers.EMPTY_OBJECTS;
		} else {
			allocArrays(capacity);
		}
		mSize = 0;
	}

	public SimpleArrayMap(SimpleArrayMap map) {
		this();
		if (map != null) {
			putAll(map);
		}
	}

	private void allocArrays(int size) {
		Class r2_Class;
		Object[] array;
		if (size == 8) {
			r2_Class = ArrayMap.class;
			synchronized(r2_Class) {
				if (mTwiceBaseCache != null) {
					array = mTwiceBaseCache;
					mArray = array;
					mTwiceBaseCache = (Object[]) array[0];
					mHashes = (int[]) array[1];
					array[1] = null;
					array[0] = null;
					mTwiceBaseCacheSize--;
				} else {
					mHashes = new int[size];
					mArray = new Object[(size << 1)];
				}
			}
		} else {
			if (size == 4) {
				r2_Class = ArrayMap.class;
				synchronized(r2_Class) {
					try {
						if (mBaseCache != null) {
							array = mBaseCache;
							mArray = array;
							mBaseCache = (Object[]) array[0];
							mHashes = (int[]) array[1];
							array[1] = null;
							array[0] = null;
							mBaseCacheSize--;
						}
					} catch (Throwable th) {
						return th;
					}
				}
			}
			mHashes = new int[size];
			mArray = new Object[(size << 1)];
		}
	}

	private static void freeArrays(int[] hashes, Object[] array, int size) {
		int i;
		if (hashes.length == 8) {
			synchronized(ArrayMap.class) {
				if (mTwiceBaseCacheSize < 10) {
					array[0] = mTwiceBaseCache;
					array[1] = hashes;
					i = (size << 1) - 1;
					while (i >= 2) {
						array[i] = null;
						i--;
					}
					mTwiceBaseCache = array;
					mTwiceBaseCacheSize++;
				}
			}
		} else if (hashes.length == 4) {
			synchronized(ArrayMap.class) {
				if (mBaseCacheSize < 10) {
					array[0] = mBaseCache;
					array[1] = hashes;
					i = (size << 1) - 1;
					while (i >= 2) {
						array[i] = null;
						i--;
					}
					mBaseCache = array;
					mBaseCacheSize++;
				}
			}
		}
	}

	public void clear() {
		if (mSize != 0) {
			freeArrays(mHashes, mArray, mSize);
			mHashes = ContainerHelpers.EMPTY_INTS;
			mArray = ContainerHelpers.EMPTY_OBJECTS;
			mSize = 0;
		}
	}

	public boolean containsKey(Object key) {
		if (key == null) {
			if (indexOfNull() >= 0) {
				return true;
			} else {
				return false;
			}
		} else if (indexOf(key, key.hashCode()) < 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean containsValue(Object value) {
		if (indexOfValue(value) >= 0) {
			return true;
		} else {
			return DEBUG;
		}
	}

	public void ensureCapacity(int minimumCapacity) {
		if (mHashes.length < minimumCapacity) {
			Object ohashes = mHashes;
			Object oarray = mArray;
			allocArrays(minimumCapacity);
			if (mSize > 0) {
				System.arraycopy(ohashes, 0, mHashes, 0, mSize);
				System.arraycopy(oarray, 0, mArray, 0, mSize << 1);
			}
			freeArrays(ohashes, oarray, mSize);
		}
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object instanceof Map) {
			Map<> map = (Map) object;
			if (size() != map.size()) {
				return false;
			} else {
				int i = 0;
				while (true) {
					try {
						if (i < mSize) {
							K key = keyAt(i);
							V mine = valueAt(i);
							Object theirs = map.get(key);
							if (mine == null) {
								if (theirs != null || !map.containsKey(key)) {
									return false;
								}
							} else if (!mine.equals(theirs)) {
								return false;
							}
							i++;
						} else {
							return true;
						}
					} catch (NullPointerException e) {
						return false;
					} catch (ClassCastException e_2) {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}

	public V get(Object key) {
		int index;
		if (key == null) {
			index = indexOfNull();
		} else {
			index = indexOf(key, key.hashCode());
		}
		if (index >= 0) {
			return mArray[(index << 1) + 1];
		} else {
			return null;
		}
	}

	public int hashCode() {
		int[] hashes = mHashes;
		Object[] array = mArray;
		int result = 0;
		int i = 0;
		int v = 1;
		while (i < mSize) {
			int r7i;
			Object value = array[v];
			int r8i = hashes[i];
			if (value == null) {
				r7i = 0;
			} else {
				r7i = value.hashCode();
			}
			result += r7i ^ r8i;
			i++;
			v += 2;
		}
		return result;
	}

	int indexOf(Object key, int hash) {
		int N = mSize;
		if (N == 0) {
			return -1;
		} else {
			int index = ContainerHelpers.binarySearch(mHashes, N, hash);
			if (index >= 0) {
				if (!key.equals(mArray[index << 1])) {
					int end = index + 1;
					while (end < N && mHashes[end] == hash) {
						if (key.equals(mArray[end << 1])) {
							return end;
						} else {
							end++;
						}
					}
					int i = index - 1;
					while (i >= 0 && mHashes[i] == hash) {
						if (key.equals(mArray[i << 1])) {
							return i;
						} else {
							i--;
						}
					}
					return end ^ -1;
				} else {
					return index;
				}
			} else {
				return index;
			}
		}
	}

	int indexOfNull() {
		int N = mSize;
		if (N == 0) {
			return -1;
		} else {
			int index = ContainerHelpers.binarySearch(mHashes, N, 0);
			if (index >= 0) {
				if (mArray[index << 1] != null) {
					int end = index + 1;
					while (end < N && mHashes[end] == 0) {
						if (mArray[end << 1] == null) {
							return end;
						} else {
							end++;
						}
					}
					int i = index - 1;
					while (i >= 0 && mHashes[i] == 0) {
						if (mArray[i << 1] == null) {
							return i;
						} else {
							i--;
						}
					}
					return end ^ -1;
				} else {
					return index;
				}
			} else {
				return index;
			}
		}
	}

	int indexOfValue(Object value) {
		int N = mSize * 2;
		Object[] array = mArray;
		int i;
		if (value == null) {
			i = 1;
			while (i < N) {
				if (array[i] == null) {
					return i >> 1;
				} else {
					i += 2;
				}
			}
		} else {
			i = 1;
			while (i < N) {
				if (value.equals(array[i])) {
					return i >> 1;
				} else {
					i += 2;
				}
			}
		}
		return -1;
	}

	public boolean isEmpty() {
		if (mSize <= 0) {
			return true;
		} else {
			return DEBUG;
		}
	}

	public K keyAt(int index) {
		return mArray[index << 1];
	}

	public V put(K key, V value) {
		int hash;
		int index;
		int n = TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE;
		if (key == null) {
			hash = 0;
			index = indexOfNull();
		} else {
			hash = key.hashCode();
			index = indexOf(key, hash);
		}
		if (index >= 0) {
			index = (index << 1) + 1;
			mArray[index] = value;
			return mArray[index];
		} else {
			index ^= -1;
			if (mSize >= mHashes.length) {
				if (mSize >= 8) {
					n = mSize + (mSize >> 1);
				} else if (mSize < 4) {
					n = 4;
				}
				Object ohashes = mHashes;
				Object oarray = mArray;
				allocArrays(n);
				if (mHashes.length > 0) {
					System.arraycopy(ohashes, 0, mHashes, 0, ohashes.length);
					System.arraycopy(oarray, 0, mArray, 0, oarray.length);
				}
				freeArrays(ohashes, oarray, mSize);
			}
			if (index < mSize) {
				System.arraycopy(mHashes, index, mHashes, index + 1, mSize - index);
				System.arraycopy(mArray, index << 1, mArray, (index + 1) << 1, (mSize - index) << 1);
			}
			mHashes[index] = hash;
			mArray[index << 1] = key;
			mArray[(index << 1) + 1] = value;
			mSize++;
			return null;
		}
	}

	public void putAll(SimpleArrayMap<K, V> array) {
		int N = array.mSize;
		ensureCapacity(mSize + N);
		if (mSize == 0) {
			if (N > 0) {
				System.arraycopy(array.mHashes, 0, mHashes, 0, N);
				System.arraycopy(array.mArray, 0, mArray, 0, N << 1);
				mSize = N;
			}
		} else {
			int i = 0;
			while (i < N) {
				put(array.keyAt(i), array.valueAt(i));
				i++;
			}
		}
	}

	public V remove(Object key) {
		int index;
		if (key == null) {
			index = indexOfNull();
		} else {
			index = indexOf(key, key.hashCode());
		}
		if (index >= 0) {
			return removeAt(index);
		} else {
			return null;
		}
	}

	public V removeAt(int index) {
		int n = TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE;
		V old = mArray[(index << 1) + 1];
		if (mSize <= 1) {
			freeArrays(mHashes, mArray, mSize);
			mHashes = ContainerHelpers.EMPTY_INTS;
			mArray = ContainerHelpers.EMPTY_OBJECTS;
			mSize = 0;
			return old;
		} else if (mHashes.length <= 8 || mSize >= mHashes.length / 3) {
			mSize--;
			if (index < mSize) {
				System.arraycopy(mHashes, index + 1, mHashes, index, mSize - index);
				System.arraycopy(mArray, (index + 1) << 1, mArray, index << 1, (mSize - index) << 1);
			}
			mArray[mSize << 1] = null;
			mArray[(mSize << 1) + 1] = null;
			return old;
		} else {
			if (mSize > 8) {
				n = mSize + (mSize >> 1);
			}
			Object ohashes = mHashes;
			Object oarray = mArray;
			allocArrays(n);
			mSize--;
			if (index > 0) {
				System.arraycopy(ohashes, 0, mHashes, 0, index);
				System.arraycopy(oarray, 0, mArray, 0, index << 1);
			}
			if (index < mSize) {
				System.arraycopy(ohashes, index + 1, mHashes, index, mSize - index);
				System.arraycopy(oarray, (index + 1) << 1, mArray, index << 1, (mSize - index) << 1);
				return old;
			} else {
				return old;
			}
		}
	}

	public V setValueAt(int index, V value) {
		index = (index << 1) + 1;
		mArray[index] = value;
		return mArray[index];
	}

	public int size() {
		return mSize;
	}

	public String toString() {
		if (isEmpty()) {
			return "{}";
		} else {
			StringBuilder buffer = new StringBuilder(mSize * 28);
			buffer.append('{');
			int i = 0;
			while (i < mSize) {
				if (i > 0) {
					buffer.append(", ");
				}
				SimpleArrayMap key = keyAt(i);
				if (key != this) {
					buffer.append(key);
				} else {
					buffer.append("(this Map)");
				}
				buffer.append('=');
				SimpleArrayMap value = valueAt(i);
				if (value != this) {
					buffer.append(value);
				} else {
					buffer.append("(this Map)");
				}
				i++;
			}
			buffer.append('}');
			return buffer.toString();
		}
	}

	public V valueAt(int index) {
		return mArray[(index << 1) + 1];
	}
}
