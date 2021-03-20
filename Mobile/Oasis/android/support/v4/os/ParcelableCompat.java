package android.support.v4.os;

import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class ParcelableCompat {
	static class CompatCreator<T> implements Creator<T> {
		final ParcelableCompatCreatorCallbacks<T> mCallbacks;

		public CompatCreator(ParcelableCompatCreatorCallbacks<T> callbacks) {
			super();
			mCallbacks = callbacks;
		}

		public T createFromParcel(Parcel source) {
			return mCallbacks.createFromParcel(source, null);
		}

		public T[] newArray(int size) {
			return mCallbacks.newArray(size);
		}
	}


	public ParcelableCompat() {
		super();
	}

	public static <T> Creator<T> newCreator(ParcelableCompatCreatorCallbacks<T> callbacks) {
		if (VERSION.SDK_INT >= 13) {
			ParcelableCompatCreatorHoneycombMR2Stub.instantiate(callbacks);
		}
		return new CompatCreator(callbacks);
	}
}
