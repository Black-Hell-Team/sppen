package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class FragmentStatePagerAdapter extends PagerAdapter {
	private static final boolean DEBUG = false;
	private static final String TAG = "FragmentStatePagerAdapter";
	private FragmentTransaction mCurTransaction;
	private Fragment mCurrentPrimaryItem;
	private final FragmentManager mFragmentManager;
	private ArrayList<Fragment> mFragments;
	private ArrayList<SavedState> mSavedState;

	public FragmentStatePagerAdapter(FragmentManager fm) {
		super();
		mCurTransaction = null;
		mSavedState = new ArrayList();
		mFragments = new ArrayList();
		mCurrentPrimaryItem = null;
		mFragmentManager = fm;
	}

	public void destroyItem(ViewGroup container, int position, Object object) {
		Fragment fragment = (Fragment) object;
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}
		while (mSavedState.size() <= position) {
			mSavedState.add(null);
		}
		mSavedState.set(position, mFragmentManager.saveFragmentInstanceState(fragment));
		mFragments.set(position, null);
		mCurTransaction.remove(fragment);
	}

	public void finishUpdate(ViewGroup container) {
		if (mCurTransaction != null) {
			mCurTransaction.commitAllowingStateLoss();
			mCurTransaction = null;
			mFragmentManager.executePendingTransactions();
		}
	}

	public abstract Fragment getItem(int r1i);

	public Object instantiateItem(ViewGroup container, int position) {
		if (mFragments.size() > position) {
			Fragment f = (Fragment) mFragments.get(position);
			if (f != null) {
				return f;
			}
		}
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}
		Fragment fragment = getItem(position);
		if (mSavedState.size() > position) {
			SavedState fss = (SavedState) mSavedState.get(position);
			if (fss != null) {
				fragment.setInitialSavedState(fss);
			}
		}
		while (mFragments.size() <= position) {
			mFragments.add(null);
		}
		fragment.setMenuVisibility(DEBUG);
		fragment.setUserVisibleHint(DEBUG);
		mFragments.set(position, fragment);
		mCurTransaction.add(container.getId(), fragment);
		return fragment;
	}

	public boolean isViewFromObject(View view, Object object) {
		if (((Fragment) object).getView() == view) {
			return true;
		} else {
			return DEBUG;
		}
	}

	public void restoreState(Parcelable state, ClassLoader loader) {
		if (state != null) {
			Bundle bundle = (Bundle) state;
			bundle.setClassLoader(loader);
			Parcelable[] fss = bundle.getParcelableArray("states");
			mSavedState.clear();
			mFragments.clear();
			if (fss != null) {
				int i = 0;
				while (i < fss.length) {
					mSavedState.add((SavedState) fss[i]);
					i++;
				}
			}
			Iterator i$ = bundle.keySet().iterator();
			while (i$.hasNext()) {
				String key = (String) i$.next();
				if (key.startsWith("f")) {
					int index = Integer.parseInt(key.substring(1));
					Fragment f = mFragmentManager.getFragment(bundle, key);
					if (f != null) {
						while (mFragments.size() <= index) {
							mFragments.add(null);
						}
						f.setMenuVisibility(DEBUG);
						mFragments.set(index, f);
					} else {
						Log.w(TAG, "Bad fragment at key " + key);
					}
				}
			}
		}
	}

	public Parcelable saveState() {
		Bundle state = null;
		if (mSavedState.size() > 0) {
			state = new Bundle();
			SavedState[] fss = new SavedState[mSavedState.size()];
			mSavedState.toArray(fss);
			state.putParcelableArray("states", fss);
		}
		int i = 0;
		while (i < mFragments.size()) {
			Fragment f = (Fragment) mFragments.get(i);
			if (f != null) {
				if (state == null) {
					state = new Bundle();
				}
				mFragmentManager.putFragment(state, "f" + i, f);
			}
			i++;
		}
		return state;
	}

	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		Fragment fragment = (Fragment) object;
		if (fragment != mCurrentPrimaryItem) {
			if (mCurrentPrimaryItem != null) {
				mCurrentPrimaryItem.setMenuVisibility(DEBUG);
				mCurrentPrimaryItem.setUserVisibleHint(DEBUG);
			}
			if (fragment != null) {
				fragment.setMenuVisibility(true);
				fragment.setUserVisibleHint(true);
			}
			mCurrentPrimaryItem = fragment;
		}
	}

	public void startUpdate(ViewGroup container) {
	}
}
