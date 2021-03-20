package android.support.v4.util;

public final class Pools {
	public static interface Pool<T> {
		public T acquire();

		public boolean release(T r1_T);
	}

	public static class SimplePool<T> implements Pools.Pool<T> {
		private final Object[] mPool;
		private int mPoolSize;

		public SimplePool(int maxPoolSize) {
			super();
			if (maxPoolSize <= 0) {
				throw new IllegalArgumentException("The max pool size must be > 0");
			} else {
				mPool = new Object[maxPoolSize];
			}
		}

		private boolean isInPool(T instance) {
			int i = 0;
			while (i < mPoolSize) {
				if (mPool[i] == instance) {
					return true;
				} else {
					i++;
				}
			}
			return false;
		}

		public T acquire() {
			Object r2_Object = null;
			if (mPoolSize > 0) {
				int lastPooledIndex = mPoolSize - 1;
				mPool[lastPooledIndex] = r2_Object;
				mPoolSize--;
				return mPool[lastPooledIndex];
			} else {
				return null;
			}
		}

		public boolean release(T instance) {
			if (isInPool(instance)) {
				throw new IllegalStateException("Already in the pool!");
			} else if (mPoolSize < mPool.length) {
				mPool[mPoolSize] = instance;
				mPoolSize++;
				return true;
			} else {
				return false;
			}
		}
	}

	public static class SynchronizedPool<T> extends Pools.SimplePool<T> {
		private final Object mLock;

		public SynchronizedPool(int maxPoolSize) {
			super(maxPoolSize);
			mLock = new Object();
		}

		public T acquire() {
			T r0_T;
			synchronized(mLock) {
				r0_T = super.acquire();
			}
			return r0_T;
		}

		public boolean release(T element) {
			boolean r0z;
			synchronized(mLock) {
				r0z = super.release(element);
			}
			return r0z;
		}
	}


	private Pools() {
		super();
	}
}
