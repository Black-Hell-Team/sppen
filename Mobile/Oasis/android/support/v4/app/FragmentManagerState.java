package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

final class FragmentManagerState implements Parcelable {
	public static final Creator<FragmentManagerState> CREATOR;
	FragmentState[] mActive;
	int[] mAdded;
	BackStackState[] mBackStack;

	static class AnonymousClass_1 implements Creator<FragmentManagerState> {
		AnonymousClass_1() {
			super();
		}

		public FragmentManagerState createFromParcel(Parcel in) {
			return new FragmentManagerState(in);
		}

		public FragmentManagerState[] newArray(int size) {
			return new FragmentManagerState[size];
		}
	}


	static {
		CREATOR = new AnonymousClass_1();
	}

	public FragmentManagerState() {
		super();
	}

	public FragmentManagerState(Parcel in) {
		super();
		mActive = (FragmentState[]) in.createTypedArray(FragmentState.CREATOR);
		mAdded = in.createIntArray();
		mBackStack = (BackStackState[]) in.createTypedArray(BackStackState.CREATOR);
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedArray(mActive, flags);
		dest.writeIntArray(mAdded);
		dest.writeTypedArray(mBackStack, flags);
	}
}
