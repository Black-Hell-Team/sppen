package android.support.v4.os;

import android.os.Parcel;

public interface ParcelableCompatCreatorCallbacks<T> {
	public T createFromParcel(Parcel r1_Parcel, ClassLoader r2_ClassLoader);

	public T[] newArray(int r1i);
}
