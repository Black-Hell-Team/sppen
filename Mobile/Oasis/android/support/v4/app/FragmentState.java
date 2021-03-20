package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

final class FragmentState implements Parcelable {
	public static final Creator<FragmentState> CREATOR;
	final Bundle mArguments;
	final String mClassName;
	final int mContainerId;
	final boolean mDetached;
	final int mFragmentId;
	final boolean mFromLayout;
	final int mIndex;
	Fragment mInstance;
	final boolean mRetainInstance;
	Bundle mSavedFragmentState;
	final String mTag;

	static class AnonymousClass_1 implements Creator<FragmentState> {
		AnonymousClass_1() {
			super();
		}

		public FragmentState createFromParcel(Parcel in) {
			return new FragmentState(in);
		}

		public FragmentState[] newArray(int size) {
			return new FragmentState[size];
		}
	}


	static {
		CREATOR = new AnonymousClass_1();
	}

	public FragmentState(Parcel in) {
		boolean r0z;
		super();
		boolean r1z = true;
		mClassName = in.readString();
		mIndex = in.readInt();
		if (in.readInt() != 0) {
			r0z = true;
		} else {
			r0z = false;
		}
		mFromLayout = r0z;
		mFragmentId = in.readInt();
		mContainerId = in.readInt();
		mTag = in.readString();
		if (in.readInt() != 0) {
			r0z = true;
		} else {
			r0z = false;
		}
		mRetainInstance = r0z;
		if (in.readInt() != 0) {
			mDetached = r1z;
			mArguments = in.readBundle();
			mSavedFragmentState = in.readBundle();
		} else {
			r1z = false;
			mDetached = r1z;
			mArguments = in.readBundle();
			mSavedFragmentState = in.readBundle();
		}
	}

	public FragmentState(Fragment frag) {
		super();
		mClassName = frag.getClass().getName();
		mIndex = frag.mIndex;
		mFromLayout = frag.mFromLayout;
		mFragmentId = frag.mFragmentId;
		mContainerId = frag.mContainerId;
		mTag = frag.mTag;
		mRetainInstance = frag.mRetainInstance;
		mDetached = frag.mDetached;
		mArguments = frag.mArguments;
	}

	public int describeContents() {
		return 0;
	}

	public Fragment instantiate(FragmentActivity activity, Fragment parent) {
		if (mInstance != null) {
			return mInstance;
		} else {
			if (mArguments != null) {
				mArguments.setClassLoader(activity.getClassLoader());
			}
			mInstance = Fragment.instantiate(activity, mClassName, mArguments);
			if (mSavedFragmentState != null) {
				mSavedFragmentState.setClassLoader(activity.getClassLoader());
				mInstance.mSavedFragmentState = mSavedFragmentState;
			}
			mInstance.setIndex(mIndex, parent);
			mInstance.mFromLayout = mFromLayout;
			mInstance.mRestored = true;
			mInstance.mFragmentId = mFragmentId;
			mInstance.mContainerId = mContainerId;
			mInstance.mTag = mTag;
			mInstance.mRetainInstance = mRetainInstance;
			mInstance.mDetached = mDetached;
			mInstance.mFragmentManager = activity.mFragments;
			if (FragmentManagerImpl.DEBUG) {
				Log.v("FragmentManager", "Instantiated fragment " + mInstance);
			}
			return mInstance;
		}
	}

	public void writeToParcel(Parcel dest, int flags) {
		int r0i;
		int r1i = 1;
		dest.writeString(mClassName);
		dest.writeInt(mIndex);
		if (mFromLayout) {
			r0i = 1;
		} else {
			r0i = 0;
		}
		dest.writeInt(r0i);
		dest.writeInt(mFragmentId);
		dest.writeInt(mContainerId);
		dest.writeString(mTag);
		if (mRetainInstance) {
			r0i = 1;
		} else {
			r0i = 0;
		}
		dest.writeInt(r0i);
		if (mDetached) {
			dest.writeInt(r1i);
			dest.writeBundle(mArguments);
			dest.writeBundle(mSavedFragmentState);
		} else {
			r1i = 0;
			dest.writeInt(r1i);
			dest.writeBundle(mArguments);
			dest.writeBundle(mSavedFragmentState);
		}
	}
}
