package android.support.v4.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ArrayMap<K, V> extends SimpleArrayMap<K, V> implements Map<K, V> {
	MapCollections<K, V> mCollections;

	class AnonymousClass_1 extends MapCollections<K, V> {
		final /* synthetic */ ArrayMap this$0;

		AnonymousClass_1(ArrayMap r1_ArrayMap) {
			super();
			this$0 = r1_ArrayMap;
		}

		protected void colClear() {
			this$0.clear();
		}

		protected Object colGetEntry(int index, int offset) {
			return this$0.mArray[(index << 1) + offset];
		}

		protected Map<K, V> colGetMap() {
			return this$0;
		}

		protected int colGetSize() {
			return this$0.mSize;
		}

		protected int colIndexOfKey(Object key) {
			if (key == null) {
				return this$0.indexOfNull();
			} else {
				return this$0.indexOf(key, key.hashCode());
			}
		}

		protected int colIndexOfValue(Object value) {
			return this$0.indexOfValue(value);
		}

		protected void colPut(K key, V value) {
			this$0.put(key, value);
		}

		protected void colRemoveAt(int index) {
			this$0.removeAt(index);
		}

		protected V colSetValue(int index, V value) {
			return this$0.setValueAt(index, value);
		}
	}


	public ArrayMap() {
		super();
	}

	public ArrayMap(int capacity) {
		super(capacity);
	}

	public ArrayMap(SimpleArrayMap map) {
		super(map);
	}

	private MapCollections<K, V> getCollection() {
		if (mCollections == null) {
			mCollections = new AnonymousClass_1(this);
		}
		return mCollections;
	}

	public boolean containsAll(Collection<?> collection) {
		return MapCollections.containsAllHelper(this, collection);
	}

	public Set<Entry<K, V>> entrySet() {
		return getCollection().getEntrySet();
	}

	public Set<K> keySet() {
		return getCollection().getKeySet();
	}

	public void putAll(Map<K, V> map) {
		ensureCapacity(mSize + map.size());
		Iterator i$ = map.entrySet().iterator();
		while (i$.hasNext()) {
			Entry<K, V> entry = (Entry) i$.next();
			put(entry.getKey(), entry.getValue());
		}
	}

	public boolean removeAll(Collection<?> collection) {
		return MapCollections.removeAllHelper(this, collection);
	}

	public boolean retainAll(Collection<?> collection) {
		return MapCollections.retainAllHelper(this, collection);
	}

	public Collection<V> values() {
		return getCollection().getValues();
	}
}
