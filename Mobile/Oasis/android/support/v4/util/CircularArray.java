package android.support.v4.util;

public class CircularArray<E> {
	private int mCapacityBitmask;
	private E[] mElements;
	private int mHead;
	private int mTail;

	public CircularArray() {
		this(8);
	}

	public CircularArray(int minCapacity) {
		super();
		if (minCapacity <= 0) {
			throw new IllegalArgumentException("capacity must be positive");
		} else {
			int arrayCapacity = minCapacity;
			if (Integer.bitCount(minCapacity) != 1) {
				arrayCapacity = 1 << (Integer.highestOneBit(minCapacity) + 1);
			}
			mCapacityBitmask = arrayCapacity - 1;
			mElements = new Object[arrayCapacity];
		}
	}

	private void doubleCapacity() {
		int n = mElements.length;
		int r = n - mHead;
		int newCapacity = n << 1;
		if (newCapacity < 0) {
			throw new RuntimeException("Too big");
		} else {
			Object a = new Object[newCapacity];
			System.arraycopy(mElements, mHead, a, 0, r);
			System.arraycopy(mElements, 0, a, r, mHead);
			mElements = (Object[]) a;
			mHead = 0;
			mTail = n;
			mCapacityBitmask = newCapacity - 1;
		}
	}

	public final void addFirst(E e) {
		mHead = (mHead - 1) & mCapacityBitmask;
		mElements[mHead] = e;
		if (mHead == mTail) {
			doubleCapacity();
		}
	}

	public final void addLast(E e) {
		mElements[mTail] = e;
		mTail = (mTail + 1) & mCapacityBitmask;
		if (mTail == mHead) {
			doubleCapacity();
		}
	}

	public final E get(int i) {
		if (i < 0 || i >= size()) {
			throw new ArrayIndexOutOfBoundsException();
		} else {
			return mElements[(mHead + i) & mCapacityBitmask];
		}
	}

	public final E getFirst() {
		if (mHead == mTail) {
			throw new ArrayIndexOutOfBoundsException();
		} else {
			return mElements[mHead];
		}
	}

	public final E getLast() {
		if (mHead == mTail) {
			throw new ArrayIndexOutOfBoundsException();
		} else {
			return mElements[(mTail - 1) & mCapacityBitmask];
		}
	}

	public final boolean isEmpty() {
		if (mHead == mTail) {
			return true;
		} else {
			return false;
		}
	}

	public final E popFirst() {
		if (mHead == mTail) {
			throw new ArrayIndexOutOfBoundsException();
		} else {
			mElements[mHead] = null;
			mHead = (mHead + 1) & mCapacityBitmask;
			return mElements[mHead];
		}
	}

	public final E popLast() {
		if (mHead == mTail) {
			throw new ArrayIndexOutOfBoundsException();
		} else {
			int t = (mTail - 1) & mCapacityBitmask;
			mElements[t] = null;
			mTail = t;
			return mElements[t];
		}
	}

	public final int size() {
		return (mTail - mHead) & mCapacityBitmask;
	}
}
