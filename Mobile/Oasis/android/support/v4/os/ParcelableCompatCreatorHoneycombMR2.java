package android.support.v4.os;

import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;

class ParcelableCompatCreatorHoneycombMR2<T> implements ClassLoaderCreator<T> {
	// compiled from: ParcelableCompatHoneycombMR2.java
	private final ParcelableCompatCreatorCallbacks<T> mCallbacks;

	public ParcelableCompatCreatorHoneycombMR2(ParcelableCompatCreatorCallbacks<T> callbacks) {
		super();
		mCallbacks = callbacks;
	}

	public T createFromParcel(Parcel in) {
		return mCallbacks.createFromParcel(in, null);
	}

	public T createFromParcel(Parcel in, ClassLoader loader) {
		return mCallbacks.createFromParcel(in, loader);
	}

	public T[] newArray(int size) {
		return mCallbacks.newArray(size);
	}
}
