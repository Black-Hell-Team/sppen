package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.app.BackStackRecord.Op;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;

final class BackStackState implements Parcelable {
	// compiled from: BackStackRecord.java
	public static final Creator<BackStackState> CREATOR;
	final int mBreadCrumbShortTitleRes;
	final CharSequence mBreadCrumbShortTitleText;
	final int mBreadCrumbTitleRes;
	final CharSequence mBreadCrumbTitleText;
	final int mIndex;
	final String mName;
	final int[] mOps;
	final int mTransition;
	final int mTransitionStyle;

	static class AnonymousClass_1 implements Creator<BackStackState> {
		// compiled from: BackStackRecord.java
		AnonymousClass_1() {
			super();
		}

		public BackStackState createFromParcel(Parcel in) {
			return new BackStackState(in);
		}

		public BackStackState[] newArray(int size) {
			return new BackStackState[size];
		}
	}


	static {
		CREATOR = new AnonymousClass_1();
	}

	public BackStackState(Parcel in) {
		super();
		mOps = in.createIntArray();
		mTransition = in.readInt();
		mTransitionStyle = in.readInt();
		mName = in.readString();
		mIndex = in.readInt();
		mBreadCrumbTitleRes = in.readInt();
		mBreadCrumbTitleText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
		mBreadCrumbShortTitleRes = in.readInt();
		mBreadCrumbShortTitleText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
	}

	public BackStackState(FragmentManagerImpl fm, BackStackRecord bse) {
		super();
		int numRemoved = 0;
		Op op = bse.mHead;
		while (op != null) {
			if (op.removed != null) {
				numRemoved += op.removed.size();
			}
			op = op.next;
		}
		mOps = new int[((bse.mNumOp * 7) + numRemoved)];
		if (!bse.mAddToBackStack) {
			throw new IllegalStateException("Not on back stack");
		} else {
			op = bse.mHead;
			int pos = 0;
			while (op != null) {
				int r6i;
				int pos_2 = pos + 1;
				mOps[pos] = op.cmd;
				int[] r7_int_A = mOps;
				pos = pos_2 + 1;
				if (op.fragment != null) {
					r6i = op.fragment.mIndex;
				} else {
					r6i = -1;
				}
				r7_int_A[pos_2] = r6i;
				pos = pos + 1;
				mOps[pos] = op.enterAnim;
				pos = pos + 1;
				mOps[pos] = op.exitAnim;
				pos = pos + 1;
				mOps[pos] = op.popEnterAnim;
				pos = pos + 1;
				mOps[pos] = op.popExitAnim;
				if (op.removed != null) {
					int N = op.removed.size();
					mOps[pos] = N;
					int i = 0;
					pos = pos + 1;
					while (i < N) {
						mOps[pos] = ((Fragment) op.removed.get(i)).mIndex;
						i++;
						pos = pos + 1;
					}
					pos = pos;
				} else {
					pos = pos + 1;
					mOps[pos] = 0;
				}
				op = op.next;
				pos = pos;
			}
			mTransition = bse.mTransition;
			mTransitionStyle = bse.mTransitionStyle;
			mName = bse.mName;
			mIndex = bse.mIndex;
			mBreadCrumbTitleRes = bse.mBreadCrumbTitleRes;
			mBreadCrumbTitleText = bse.mBreadCrumbTitleText;
			mBreadCrumbShortTitleRes = bse.mBreadCrumbShortTitleRes;
			mBreadCrumbShortTitleText = bse.mBreadCrumbShortTitleText;
		}
	}

	public int describeContents() {
		return 0;
	}

	public BackStackRecord instantiate(FragmentManagerImpl fm) {
		BackStackRecord bse = new BackStackRecord(fm);
		int pos = 0;
		int num = 0;
		while (pos < mOps.length) {
			Op op = new Op();
			int pos_2 = pos + 1;
			op.cmd = mOps[pos];
			if (FragmentManagerImpl.DEBUG) {
				Log.v("FragmentManager", "Instantiate " + bse + " op #" + num + " base fragment #" + mOps[pos_2]);
			}
			pos = pos_2 + 1;
			int findex = mOps[pos_2];
			if (findex >= 0) {
				op.fragment = (Fragment) fm.mActive.get(findex);
			} else {
				op.fragment = null;
			}
			pos = pos + 1;
			op.enterAnim = mOps[pos];
			pos = pos + 1;
			op.exitAnim = mOps[pos];
			pos = pos + 1;
			op.popEnterAnim = mOps[pos];
			pos = pos + 1;
			op.popExitAnim = mOps[pos];
			pos = pos + 1;
			int N = mOps[pos];
			if (N > 0) {
				op.removed = new ArrayList(N);
				int i = 0;
				while (i < N) {
					if (FragmentManagerImpl.DEBUG) {
						Log.v("FragmentManager", "Instantiate " + bse + " set remove fragment #" + mOps[pos]);
					}
					op.removed.add((Fragment) fm.mActive.get(mOps[pos]));
					i++;
					pos = pos + 1;
				}
			}
			pos = pos;
			bse.addOp(op);
			num++;
		}
		bse.mTransition = mTransition;
		bse.mTransitionStyle = mTransitionStyle;
		bse.mName = mName;
		bse.mIndex = mIndex;
		bse.mAddToBackStack = true;
		bse.mBreadCrumbTitleRes = mBreadCrumbTitleRes;
		bse.mBreadCrumbTitleText = mBreadCrumbTitleText;
		bse.mBreadCrumbShortTitleRes = mBreadCrumbShortTitleRes;
		bse.mBreadCrumbShortTitleText = mBreadCrumbShortTitleText;
		bse.bumpBackStackNesting(1);
		return bse;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeIntArray(mOps);
		dest.writeInt(mTransition);
		dest.writeInt(mTransitionStyle);
		dest.writeString(mName);
		dest.writeInt(mIndex);
		dest.writeInt(mBreadCrumbTitleRes);
		TextUtils.writeToParcel(mBreadCrumbTitleText, dest, 0);
		dest.writeInt(mBreadCrumbShortTitleRes);
		TextUtils.writeToParcel(mBreadCrumbShortTitleText, dest, 0);
	}
}
