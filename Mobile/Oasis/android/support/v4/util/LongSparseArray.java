package android.support.v4.util;

public class LongSparseArray<E> implements Cloneable {
	private static final Object DELETED;
	private boolean mGarbage;
	private long[] mKeys;
	private int mSize;
	private Object[] mValues;

	static {
		DELETED = new Object();
	}

	public LongSparseArray() {
		this(10);
	}

	public LongSparseArray(int initialCapacity) {
		super();
		mGarbage = false;
		if (initialCapacity == 0) {
			mKeys = ContainerHelpers.EMPTY_LONGS;
			mValues = ContainerHelpers.EMPTY_OBJECTS;
		} else {
			initialCapacity = ContainerHelpers.idealLongArraySize(initialCapacity);
			mKeys = new long[initialCapacity];
			mValues = new Object[initialCapacity];
		}
		mSize = 0;
	}

	private void gc() {
		int o = 0;
		long[] keys = mKeys;
		Object[] values = mValues;
		int i = 0;
		while (i < mSize) {
			Object val = values[i];
			if (val != DELETED) {
				if (i != o) {
					keys[o] = keys[i];
					values[o] = val;
					values[i] = null;
				}
				o++;
			}
			i++;
		}
		mGarbage = false;
		mSize = o;
	}

	public void append(long key, E value) {
		if (mSize == 0 || key > mKeys[mSize - 1]) {
			int pos;
			int n;
			Object nkeys;
			Object nvalues;
			if (!mGarbage || mSize < mKeys.length) {
				pos = mSize;
				if (pos < mKeys.length) {
					n = ContainerHelpers.idealLongArraySize(pos + 1);
					nkeys = new Object[n];
					nvalues = new Object[n];
					System.arraycopy(mKeys, 0, nkeys, 0, mKeys.length);
					System.arraycopy(mValues, 0, nvalues, 0, mValues.length);
					mKeys = nkeys;
					mValues = nvalues;
				}
				mKeys[pos] = key;
				mValues[pos] = value;
				mSize = pos + 1;
			} else {
				gc();
				pos = mSize;
				if (pos < mKeys.length) {
					mKeys[pos] = key;
					mValues[pos] = value;
					mSize = pos + 1;
				} else {
					n = ContainerHelpers.idealLongArraySize(pos + 1);
					nkeys = new Object[n];
					nvalues = new Object[n];
					System.arraycopy(mKeys, 0, nkeys, 0, mKeys.length);
					System.arraycopy(mValues, 0, nvalues, 0, mValues.length);
					mKeys = nkeys;
					mValues = nvalues;
					mKeys[pos] = key;
					mValues[pos] = value;
					mSize = pos + 1;
				}
			}
		} else {
			put(key, value);
		}
	}

	public void clear() {
		Object[] values = mValues;
		int i = 0;
		while (i < mSize) {
			values[i] = null;
			i++;
		}
		mSize = 0;
		mGarbage = false;
	}

	public LongSparseArray clone() throws CloneNotSupportedException {
		try {
			LongSparseArray<E> clone = (LongSparseArray) super.clone();
			clone.mKeys = (long[]) mKeys.clone();
			clone.mValues = (Object[]) mValues.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public void delete(long key) {
		int i = ContainerHelpers.binarySearch(mKeys, mSize, key);
		if (i < 0 || mValues[i] == DELETED) {
		} else {
			mValues[i] = DELETED;
			mGarbage = true;
		}
	}

	public E get(long key) {
		return get(key, null);
	}

	public E get(long key, E valueIfKeyNotFound) {
		int i = ContainerHelpers.binarySearch(mKeys, mSize, key);
		if (i >= 0) {
			if (mValues[i] == DELETED) {
				return valueIfKeyNotFound;
			} else {
				return mValues[i];
			}
		} else {
			return valueIfKeyNotFound;
		}
	}

	public int indexOfKey(long key) {
		if (mGarbage) {
			gc();
		}
		return ContainerHelpers.binarySearch(mKeys, mSize, key);
	}

	public int indexOfValue(E value) {
		if (mGarbage) {
			gc();
		}
		int i = 0;
		while (i < mSize) {
			if (mValues[i] == value) {
				return i;
			} else {
				i++;
			}
		}
		return -1;
	}

	public long keyAt(int index) {
		if (mGarbage) {
			gc();
		}
		return mKeys[index];
	}

	public void put(long key, E value) {
		int i = ContainerHelpers.binarySearch(mKeys, mSize, key);
		if (i >= 0) {
			mValues[i] = value;
		} else {
			i ^= -1;
			if (i >= mSize || mValues[i] != DELETED) {
				int n;
				Object nkeys;
				Object nvalues;
				if (!mGarbage || mSize < mKeys.length) {
					if (mSize < mKeys.length) {
						n = ContainerHelpers.idealLongArraySize(mSize + 1);
						nkeys = new Object[n];
						nvalues = new Object[n];
						System.arraycopy(mKeys, 0, nkeys, 0, mKeys.length);
						System.arraycopy(mValues, 0, nvalues, 0, mValues.length);
						mKeys = nkeys;
						mValues = nvalues;
					}
					if (mSize - i != 0) {
						System.arraycopy(mKeys, i, mKeys, i + 1, mSize - i);
						System.arraycopy(mValues, i, mValues, i + 1, mSize - i);
					}
					mKeys[i] = key;
					mValues[i] = value;
					mSize++;
				} else {
					gc();
					i = ContainerHelpers.binarySearch(mKeys, mSize, key) ^ -1;
					if (mSize < mKeys.length) {
						if (mSize - i != 0) {
							mKeys[i] = key;
							mValues[i] = value;
							mSize++;
						} else {
							System.arraycopy(mKeys, i, mKeys, i + 1, mSize - i);
							System.arraycopy(mValues, i, mValues, i + 1, mSize - i);
							mKeys[i] = key;
							mValues[i] = value;
							mSize++;
						}
					} else {
						n = ContainerHelpers.idealLongArraySize(mSize + 1);
						nkeys = new Object[n];
						nvalues = new Object[n];
						System.arraycopy(mKeys, 0, nkeys, 0, mKeys.length);
						System.arraycopy(mValues, 0, nvalues, 0, mValues.length);
						mKeys = nkeys;
						mValues = nvalues;
						if (mSize - i != 0) {
							System.arraycopy(mKeys, i, mKeys, i + 1, mSize - i);
							System.arraycopy(mValues, i, mValues, i + 1, mSize - i);
						}
						mKeys[i] = key;
						mValues[i] = value;
						mSize++;
					}
				}
			} else {
				mKeys[i] = key;
				mValues[i] = value;
			}
		}
	}

	public void remove(long key) {
		delete(key);
	}

	public void removeAt(int index) {
		if (mValues[index] != DELETED) {
			mValues[index] = DELETED;
			mGarbage = true;
		}
	}

	public void setValueAt(int index, E value) {
		if (mGarbage) {
			gc();
		}
		mValues[index] = value;
	}

	public int size() {
		if (mGarbage) {
			gc();
		}
		return mSize;
	}

	public String toString() {
		if (size() <= 0) {
			return "{}";
		} else {
			StringBuilder buffer = new StringBuilder(mSize * 28);
			buffer.append('{');
			int i = 0;
			while (i < mSize) {
				if (i > 0) {
					buffer.append(", ");
				}
				buffer.append(keyAt(i));
				buffer.append('=');
				LongSparseArray value = valueAt(i);
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

	public E valueAt(int index) {
		if (mGarbage) {
			gc();
		}
		return mValues[index];
	}
}
