package android.support.v4.view;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

public abstract class PagerAdapter {
	public static final int POSITION_NONE = -2;
	public static final int POSITION_UNCHANGED = -1;
	private DataSetObservable mObservable;

	public PagerAdapter() {
		super();
		mObservable = new DataSetObservable();
	}

	public void destroyItem(View container, int position, Object object) {
		throw new UnsupportedOperationException("Required method destroyItem was not overridden");
	}

	public void destroyItem(ViewGroup container, int position, Object object) {
		destroyItem((View)container, position, object);
	}

	public void finishUpdate(View container) {
	}

	public void finishUpdate(ViewGroup container) {
		finishUpdate((View)container);
	}

	public abstract int getCount();

	public int getItemPosition(Object object) {
		return POSITION_UNCHANGED;
	}

	public CharSequence getPageTitle(int position) {
		return null;
	}

	public float getPageWidth(int position) {
		return 1.0f;
	}

	public Object instantiateItem(View container, int position) {
		throw new UnsupportedOperationException("Required method instantiateItem was not overridden");
	}

	public Object instantiateItem(ViewGroup container, int position) {
		return instantiateItem((View)container, position);
	}

	public abstract boolean isViewFromObject(View r1_View, Object r2_Object);

	public void notifyDataSetChanged() {
		mObservable.notifyChanged();
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		mObservable.registerObserver(observer);
	}

	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	public Parcelable saveState() {
		return null;
	}

	public void setPrimaryItem(View container, int position, Object object) {
	}

	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		setPrimaryItem((View)container, position, object);
	}

	public void startUpdate(View container) {
	}

	public void startUpdate(ViewGroup container) {
		startUpdate((View)container);
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		mObservable.unregisterObserver(observer);
	}
}
