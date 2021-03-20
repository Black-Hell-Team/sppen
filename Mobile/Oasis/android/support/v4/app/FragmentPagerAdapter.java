package android.support.v4.app;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class FragmentPagerAdapter extends PagerAdapter {
	private static final boolean DEBUG = false;
	private static final String TAG = "FragmentPagerAdapter";
	private FragmentTransaction mCurTransaction;
	private Fragment mCurrentPrimaryItem;
	private final FragmentManager mFragmentManager;

	public FragmentPagerAdapter(FragmentManager fm) {
		super();
		mCurTransaction = null;
		mCurrentPrimaryItem = null;
		mFragmentManager = fm;
	}

	private static String makeFragmentName(int viewId, long id) {
		return "android:switcher:" + viewId + ":" + id;
	}

	public void destroyItem(ViewGroup container, int position, Object object) {
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}
		mCurTransaction.detach((Fragment) object);
	}

	public void finishUpdate(ViewGroup container) {
		if (mCurTransaction != null) {
			mCurTransaction.commitAllowingStateLoss();
			mCurTransaction = null;
			mFragmentManager.executePendingTransactions();
		}
	}

	public abstract Fragment getItem(int r1i);

	public long getItemId(int position) {
		return (long) position;
	}

	public Object instantiateItem(ViewGroup container, int position) {
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}
		long itemId = getItemId(position);
		Fragment fragment = mFragmentManager.findFragmentByTag(makeFragmentName(container.getId(), itemId));
		if (fragment != null) {
			mCurTransaction.attach(fragment);
		} else {
			fragment = getItem(position);
			mCurTransaction.add(container.getId(), fragment, makeFragmentName(container.getId(), itemId));
		}
		if (fragment != mCurrentPrimaryItem) {
			fragment.setMenuVisibility(DEBUG);
			fragment.setUserVisibleHint(DEBUG);
			return fragment;
		} else {
			return fragment;
		}
	}

	public boolean isViewFromObject(View view, Object object) {
		if (((Fragment) object).getView() == view) {
			return true;
		} else {
			return DEBUG;
		}
	}

	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	public Parcelable saveState() {
		return null;
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
