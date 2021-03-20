package android.support.v4.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LruCache<K, V> {
	private int createCount;
	private int evictionCount;
	private int hitCount;
	private final LinkedHashMap<K, V> map;
	private int maxSize;
	private int missCount;
	private int putCount;
	private int size;

	public LruCache(int maxSize) {
		super();
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		} else {
			this.maxSize = maxSize;
			map = new LinkedHashMap(0, 0.75f, true);
		}
	}

	private int safeSizeOf(K key, V value) {
		int result = sizeOf(key, value);
		if (result < 0) {
			throw new IllegalStateException("Negative size: " + key + "=" + value);
		} else {
			return result;
		}
	}

	protected V create(K key) {
		return null;
	}

	public final synchronized int createCount() {
		synchronized(this) {
			return createCount;
		}
	}

	protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {
	}

	public final void evictAll() {
		trimToSize(-1);
	}

	public final synchronized int evictionCount() {
		synchronized(this) {
			return evictionCount;
		}
	}

	public final V get(K key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		} else {
			V createdValue;
			synchronized(this) {
				V mapValue;
				try {
					mapValue = map.get(key);
					if (mapValue != null) {
						hitCount++;
						createdValue = mapValue;
					} else {
						missCount++;
						createdValue = create(key);
						if (createdValue == null) {
							createdValue = null;
						} else {
							synchronized(this) {
								try {
									createCount++;
									mapValue = map.put(key, createdValue);
									if (mapValue != null) {
										map.put(key, mapValue);
									} else {
										size += safeSizeOf(key, createdValue);
									}
								} catch (Throwable th) {
									return th;
								}
							}
							if (mapValue != null) {
								entryRemoved(false, key, createdValue, mapValue);
								createdValue = mapValue;
							} else {
								trimToSize(maxSize);
							}
						}
					}
				} catch (Throwable th_2) {
					while (true) {
						return th_2;
					}
				}
			}
			return createdValue;
		}
	}

	public final synchronized int hitCount() {
		synchronized(this) {
			return hitCount;
		}
	}

	public final synchronized int maxSize() {
		synchronized(this) {
			return maxSize;
		}
	}

	public final synchronized int missCount() {
		synchronized(this) {
			return missCount;
		}
	}

	public final V put(K key, V value) {
		if (key == null || value == null) {
			throw new NullPointerException("key == null || value == null");
		} else {
			V previous;
			synchronized(this) {
				putCount++;
				size += safeSizeOf(key, value);
				previous = map.put(key, value);
				if (previous != null) {
					size -= safeSizeOf(key, previous);
				}
			}
			if (previous != null) {
				entryRemoved(false, key, previous, value);
			}
			trimToSize(maxSize);
			return previous;
		}
	}

	public final synchronized int putCount() {
		synchronized(this) {
			return putCount;
		}
	}

	public final V remove(K key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		} else {
			V previous;
			synchronized(this) {
				previous = map.remove(key);
				if (previous != null) {
					size -= safeSizeOf(key, previous);
				}
			}
			if (previous != null) {
				entryRemoved(false, key, previous, null);
				return previous;
			} else {
				return previous;
			}
		}
	}

	public final synchronized int size() {
		synchronized(this) {
			return size;
		}
	}

	protected int sizeOf(K key, V value) {
		return 1;
	}

	public final synchronized Map<K, V> snapshot() {
		synchronized(this) {
			return new LinkedHashMap(map);
		}
	}

	public final synchronized String toString() {
		int hitPercent = 0;
		synchronized(this) {
			int accesses = hitCount + missCount;
			if (accesses != 0) {
				hitPercent = (hitCount * 100) / accesses;
			}
			Object[] r3_Object_A = new Object[4];
			r3_Object_A[0] = Integer.valueOf(maxSize);
			r3_Object_A[1] = Integer.valueOf(hitCount);
			r3_Object_A[2] = Integer.valueOf(missCount);
			r3_Object_A[3] = Integer.valueOf(hitPercent);
			return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", r3_Object_A);
		}
	}

	public void trimToSize(int maxSize) {
		while (true) {
			synchronized(this) {
				try {
					if (size >= 0) {
						if (!map.isEmpty() || size == 0) {
							if (size <= maxSize || map.isEmpty()) {
								return;
							} else {
								Entry<K, V> toEvict = (Entry) map.entrySet().iterator().next();
								K key = toEvict.getKey();
								V value = toEvict.getValue();
								map.remove(key);
								size -= safeSizeOf(key, value);
								evictionCount++;
								entryRemoved(true, key, value, null);
							}
						}
					}
					throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
				} catch (Throwable th) {
					return th;
				}
			}
		}
	}
}
