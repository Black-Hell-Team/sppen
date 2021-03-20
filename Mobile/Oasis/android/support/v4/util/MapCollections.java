package android.support.v4.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

abstract class MapCollections<K, V> {
	MapCollections<K, V> mEntrySet;
	MapCollections<K, V> mKeySet;
	MapCollections<K, V> mValues;

	final class ArrayIterator<T> implements Iterator<T> {
		boolean mCanRemove;
		int mIndex;
		final int mOffset;
		int mSize;
		final /* synthetic */ MapCollections this$0;

		ArrayIterator(MapCollections r2_MapCollections, int offset) {
			super();
			this$0 = r2_MapCollections;
			mCanRemove = false;
			mOffset = offset;
			mSize = r2_MapCollections.colGetSize();
		}

		public boolean hasNext() {
			if (mIndex < mSize) {
				return true;
			} else {
				return false;
			}
		}

		public T next() {
			mIndex++;
			mCanRemove = true;
			return this$0.colGetEntry(mIndex, mOffset);
		}

		public void remove() {
			if (!mCanRemove) {
				throw new IllegalStateException();
			} else {
				mIndex--;
				mSize--;
				mCanRemove = false;
				this$0.colRemoveAt(mIndex);
			}
		}
	}

	final class EntrySet implements Set<Entry<K, V>> {
		final /* synthetic */ MapCollections this$0;

		EntrySet(MapCollections r1_MapCollections) {
			super();
			this$0 = r1_MapCollections;
		}

		public boolean add(Entry<K, V> object) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(Collection<Entry<K, V>> collection) {
			int oldSize = this$0.colGetSize();
			Iterator i$ = collection.iterator();
			while (i$.hasNext()) {
				Entry<K, V> entry = (Entry) i$.next();
				this$0.colPut(entry.getKey(), entry.getValue());
			}
			if (oldSize != this$0.colGetSize()) {
				return true;
			} else {
				return false;
			}
		}

		public void clear() {
			this$0.colClear();
		}

		public boolean contains(Object o) {
			if (!(o instanceof Entry)) {
				return false;
			} else {
				Entry<> e = (Entry) o;
				int index = this$0.colIndexOfKey(e.getKey());
				if (index >= 0) {
					return ContainerHelpers.equal(this$0.colGetEntry(index, 1), e.getValue());
				} else {
					return false;
				}
			}
		}

		public boolean containsAll(Collection<?> collection) {
			Iterator<?> it = collection.iterator();
			while (it.hasNext()) {
				if (!contains(it.next())) {
					return false;
				}
			}
			return true;
		}

		public boolean equals(Object object) {
			return MapCollections.equalsSetHelper(this, object);
		}

		public int hashCode() {
			int result = 0;
			int i = this$0.colGetSize() - 1;
			while (i >= 0) {
				int r6i;
				int r4i;
				Object key = this$0.colGetEntry(i, 0);
				Object value = this$0.colGetEntry(i, 1);
				if (key == null) {
					r6i = 0;
				} else {
					r6i = key.hashCode();
				}
				if (value == null) {
					r4i = 0;
				} else {
					r4i = value.hashCode();
				}
				result += r4i ^ r6i;
				i--;
			}
			return result;
		}

		public boolean isEmpty() {
			if (this$0.colGetSize() == 0) {
				return true;
			} else {
				return false;
			}
		}

		public Iterator<Entry<K, V>> iterator() {
			return new MapCollections.MapIterator(this$0);
		}

		public boolean remove(Object object) {
			throw new UnsupportedOperationException();
		}

		public boolean removeAll(Collection<?> collection) {
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(Collection<?> collection) {
			throw new UnsupportedOperationException();
		}

		public int size() {
			return this$0.colGetSize();
		}

		public Object[] toArray() {
			throw new UnsupportedOperationException();
		}

		public <T> T[] toArray(T[] array) {
			throw new UnsupportedOperationException();
		}
	}

	final class KeySet implements Set<K> {
		final /* synthetic */ MapCollections this$0;

		KeySet(MapCollections r1_MapCollections) {
			super();
			this$0 = r1_MapCollections;
		}

		public boolean add(K object) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(Collection<K> collection) {
			throw new UnsupportedOperationException();
		}

		public void clear() {
			this$0.colClear();
		}

		public boolean contains(Object object) {
			if (this$0.colIndexOfKey(object) >= 0) {
				return true;
			} else {
				return false;
			}
		}

		public boolean containsAll(Collection<?> collection) {
			return MapCollections.containsAllHelper(this$0.colGetMap(), collection);
		}

		public boolean equals(Object object) {
			return MapCollections.equalsSetHelper(this, object);
		}

		public int hashCode() {
			int result = 0;
			int i = this$0.colGetSize() - 1;
			while (i >= 0) {
				int r3i;
				Object obj = this$0.colGetEntry(i, 0);
				if (obj == null) {
					r3i = 0;
				} else {
					r3i = obj.hashCode();
				}
				result += r3i;
				i--;
			}
			return result;
		}

		public boolean isEmpty() {
			if (this$0.colGetSize() == 0) {
				return true;
			} else {
				return false;
			}
		}

		public Iterator<K> iterator() {
			return new MapCollections.ArrayIterator(this$0, 0);
		}

		public boolean remove(Object object) {
			int index = this$0.colIndexOfKey(object);
			if (index >= 0) {
				this$0.colRemoveAt(index);
				return true;
			} else {
				return false;
			}
		}

		public boolean removeAll(Collection<?> collection) {
			return MapCollections.removeAllHelper(this$0.colGetMap(), collection);
		}

		public boolean retainAll(Collection<?> collection) {
			return MapCollections.retainAllHelper(this$0.colGetMap(), collection);
		}

		public int size() {
			return this$0.colGetSize();
		}

		public Object[] toArray() {
			return this$0.toArrayHelper(0);
		}

		public <T> T[] toArray(T[] array) {
			return this$0.toArrayHelper(array, 0);
		}
	}

	final class MapIterator implements Iterator<Entry<K, V>>, Entry<K, V> {
		int mEnd;
		boolean mEntryValid;
		int mIndex;
		final /* synthetic */ MapCollections this$0;

		MapIterator(MapCollections r2_MapCollections) {
			super();
			this$0 = r2_MapCollections;
			mEntryValid = false;
			mEnd = r2_MapCollections.colGetSize() - 1;
			mIndex = -1;
		}

		public final boolean equals(Object o) {
			boolean r1z = true;
			if (!mEntryValid) {
				throw new IllegalStateException("This container does not support retaining Map.Entry objects");
			} else if (!(o instanceof Entry)) {
				return false;
			} else {
				Entry<> e = (Entry) o;
				if (!ContainerHelpers.equal(e.getKey(), this$0.colGetEntry(mIndex, 0)) || !ContainerHelpers.equal(e.getValue(), this$0.colGetEntry(mIndex, 1))) {
					r1z = false;
				} else {
					return r1z;
				}
				return r1z;
			}
		}

		public K getKey() {
			if (!mEntryValid) {
				throw new IllegalStateException("This container does not support retaining Map.Entry objects");
			} else {
				return this$0.colGetEntry(mIndex, 0);
			}
		}

		public V getValue() {
			if (!mEntryValid) {
				throw new IllegalStateException("This container does not support retaining Map.Entry objects");
			} else {
				return this$0.colGetEntry(mIndex, 1);
			}
		}

		public boolean hasNext() {
			if (mIndex < mEnd) {
				return true;
			} else {
				return false;
			}
		}

		public final int hashCode() {
			int r2i = 0;
			if (!mEntryValid) {
				throw new IllegalStateException("This container does not support retaining Map.Entry objects");
			} else {
				int r3i;
				Object key = this$0.colGetEntry(mIndex, 0);
				Object value = this$0.colGetEntry(mIndex, 1);
				if (key == null) {
					r3i = 0;
				} else {
					r3i = key.hashCode();
				}
				if (value == null) {
					return r2i ^ r3i;
				} else {
					r2i = value.hashCode();
					return r2i ^ r3i;
				}
			}
		}

		public Entry next() {
			mIndex++;
			mEntryValid = true;
			return this;
		}

		public void remove() {
			if (!mEntryValid) {
				throw new IllegalStateException();
			} else {
				this$0.colRemoveAt(mIndex);
				mIndex--;
				mEnd--;
				mEntryValid = false;
			}
		}

		public V setValue(V object) {
			if (!mEntryValid) {
				throw new IllegalStateException("This container does not support retaining Map.Entry objects");
			} else {
				return this$0.colSetValue(mIndex, object);
			}
		}

		public final String toString() {
			return getKey() + "=" + getValue();
		}
	}

	final class ValuesCollection implements Collection<V> {
		final /* synthetic */ MapCollections this$0;

		ValuesCollection(MapCollections r1_MapCollections) {
			super();
			this$0 = r1_MapCollections;
		}

		public boolean add(V object) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(Collection<V> collection) {
			throw new UnsupportedOperationException();
		}

		public void clear() {
			this$0.colClear();
		}

		public boolean contains(Object object) {
			if (this$0.colIndexOfValue(object) >= 0) {
				return true;
			} else {
				return false;
			}
		}

		public boolean containsAll(Collection<?> collection) {
			Iterator<?> it = collection.iterator();
			while (it.hasNext()) {
				if (!contains(it.next())) {
					return false;
				}
			}
			return true;
		}

		public boolean isEmpty() {
			if (this$0.colGetSize() == 0) {
				return true;
			} else {
				return false;
			}
		}

		public Iterator<V> iterator() {
			return new MapCollections.ArrayIterator(this$0, 1);
		}

		public boolean remove(Object object) {
			int index = this$0.colIndexOfValue(object);
			if (index >= 0) {
				this$0.colRemoveAt(index);
				return true;
			} else {
				return false;
			}
		}

		public boolean removeAll(Collection<?> collection) {
			int N = this$0.colGetSize();
			boolean changed = false;
			int i = 0;
			while (i < N) {
				if (collection.contains(this$0.colGetEntry(i, 1))) {
					this$0.colRemoveAt(i);
					i--;
					N--;
					changed = true;
				}
			}
			return changed;
		}

		public boolean retainAll(Collection<?> collection) {
			int N = this$0.colGetSize();
			boolean changed = false;
			int i = 0;
			while (i < N) {
				if (!collection.contains(this$0.colGetEntry(i, 1))) {
					this$0.colRemoveAt(i);
					i--;
					N--;
					changed = true;
				}
			}
			return changed;
		}

		public int size() {
			return this$0.colGetSize();
		}

		public Object[] toArray() {
			return this$0.toArrayHelper(1);
		}

		public <T> T[] toArray(T[] array) {
			return this$0.toArrayHelper(array, 1);
		}
	}


	MapCollections() {
		super();
	}

	public static <K, V> boolean containsAllHelper(Map<K, V> map, Collection<?> collection) {
		Iterator<?> it = collection.iterator();
		while (it.hasNext()) {
			if (!map.containsKey(it.next())) {
				return false;
			}
		}
		return true;
	}

	public static <T> boolean equalsSetHelper(Set<T> set, Object object) {
		boolean r2z = true;
		if (set == object) {
			return true;
		} else if (object instanceof Set) {
			Set<?> s = (Set) object;
			try {
				if (set.size() != s.size() || !set.containsAll(s)) {
					r2z = false;
				} else {
					return r2z;
				}
				return r2z;
			} catch (NullPointerException e) {
				return false;
			} catch (ClassCastException e_2) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static <K, V> boolean removeAllHelper(Map<K, V> map, Collection<?> collection) {
		int oldSize = map.size();
		Iterator<?> it = collection.iterator();
		while (it.hasNext()) {
			map.remove(it.next());
		}
		if (oldSize != map.size()) {
			return true;
		} else {
			return false;
		}
	}

	public static <K, V> boolean retainAllHelper(Map<K, V> map, Collection<?> collection) {
		int oldSize = map.size();
		Iterator<K> it = map.keySet().iterator();
		while (it.hasNext()) {
			if (!collection.contains(it.next())) {
				it.remove();
			}
		}
		if (oldSize != map.size()) {
			return true;
		} else {
			return false;
		}
	}

	protected abstract void colClear();

	protected abstract Object colGetEntry(int r1i, int r2i);

	protected abstract Map<K, V> colGetMap();

	protected abstract int colGetSize();

	protected abstract int colIndexOfKey(Object r1_Object);

	protected abstract int colIndexOfValue(Object r1_Object);

	protected abstract void colPut(K r1_K, V r2_V);

	protected abstract void colRemoveAt(int r1i);

	protected abstract V colSetValue(int r1i, V r2_V);

	public Set<Entry<K, V>> getEntrySet() {
		if (mEntrySet == null) {
			mEntrySet = new EntrySet(this);
		}
		return mEntrySet;
	}

	public Set<K> getKeySet() {
		if (mKeySet == null) {
			mKeySet = new KeySet(this);
		}
		return mKeySet;
	}

	public Collection<V> getValues() {
		if (mValues == null) {
			mValues = new ValuesCollection(this);
		}
		return mValues;
	}

	public Object[] toArrayHelper(int offset) {
		int N = colGetSize();
		Object[] result = new Object[N];
		int i = 0;
		while (i < N) {
			result[i] = colGetEntry(i, offset);
			i++;
		}
		return result;
	}

	public <T> T[] toArrayHelper(T[] array, int offset) {
		int N = colGetSize();
		if (array.length < N) {
			array = (Object[]) Array.newInstance(array.getClass().getComponentType(), N);
		}
		int i = 0;
		while (i < N) {
			array[i] = colGetEntry(i, offset);
			i++;
		}
		if (array.length > N) {
			array[N] = null;
		}
		return array;
	}
}
