package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SimpleArrayMap;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class Fragment implements ComponentCallbacks, OnCreateContextMenuListener {
	static final int ACTIVITY_CREATED = 2;
	static final int CREATED = 1;
	static final int INITIALIZING = 0;
	static final int RESUMED = 5;
	static final int STARTED = 4;
	static final int STOPPED = 3;
	private static final SimpleArrayMap<String, Class<?>> sClassMap;
	FragmentActivity mActivity;
	boolean mAdded;
	View mAnimatingAway;
	Bundle mArguments;
	int mBackStackNesting;
	boolean mCalled;
	boolean mCheckedForLoaderManager;
	FragmentManagerImpl mChildFragmentManager;
	ViewGroup mContainer;
	int mContainerId;
	boolean mDeferStart;
	boolean mDetached;
	int mFragmentId;
	FragmentManagerImpl mFragmentManager;
	boolean mFromLayout;
	boolean mHasMenu;
	boolean mHidden;
	boolean mInLayout;
	int mIndex;
	View mInnerView;
	LoaderManagerImpl mLoaderManager;
	boolean mLoadersStarted;
	boolean mMenuVisible;
	int mNextAnim;
	Fragment mParentFragment;
	boolean mRemoving;
	boolean mRestored;
	boolean mResumed;
	boolean mRetainInstance;
	boolean mRetaining;
	Bundle mSavedFragmentState;
	SparseArray<Parcelable> mSavedViewState;
	int mState;
	int mStateAfterAnimating;
	String mTag;
	Fragment mTarget;
	int mTargetIndex;
	int mTargetRequestCode;
	boolean mUserVisibleHint;
	View mView;
	String mWho;

	public static class InstantiationException extends RuntimeException {
		public InstantiationException(String msg, Exception cause) {
			super(msg, cause);
		}
	}

	public static class SavedState implements Parcelable {
		public static final Creator<Fragment.SavedState> CREATOR;
		final Bundle mState;

		static class AnonymousClass_1 implements Creator<Fragment.SavedState> {
			AnonymousClass_1() {
				super();
			}

			public Fragment.SavedState createFromParcel(Parcel in) {
				return new Fragment.SavedState(in, null);
			}

			public Fragment.SavedState[] newArray(int size) {
				return new Fragment.SavedState[size];
			}
		}


		static {
			CREATOR = new AnonymousClass_1();
		}

		SavedState(Bundle state) {
			super();
			mState = state;
		}

		SavedState(Parcel in, ClassLoader loader) {
			super();
			mState = in.readBundle();
			if (loader == null || mState == null) {
			} else {
				mState.setClassLoader(loader);
			}
		}

		public int describeContents() {
			return INITIALIZING;
		}

		public void writeToParcel(Parcel dest, int flags) {
			dest.writeBundle(mState);
		}
	}

	class AnonymousClass_1 implements FragmentContainer {
		final /* synthetic */ Fragment this$0;

		AnonymousClass_1(Fragment r1_Fragment) {
			super();
			this$0 = r1_Fragment;
		}

		public View findViewById(int id) {
			if (this$0.mView == null) {
				throw new IllegalStateException("Fragment does not have a view");
			} else {
				return this$0.mView.findViewById(id);
			}
		}
	}


	static {
		sClassMap = new SimpleArrayMap();
	}

	public Fragment() {
		super();
		mState = 0;
		mIndex = -1;
		mTargetIndex = -1;
		mMenuVisible = true;
		mUserVisibleHint = true;
	}

	public static Fragment instantiate(Context context, String fname) {
		return instantiate(context, fname, null);
	}

	public static Fragment instantiate(Context context, String fname, Bundle args) {
		Class<?> clazz;
		Fragment f;
		try {
			clazz = (Class) sClassMap.get(fname);
			if (clazz == null) {
				clazz = context.getClassLoader().loadClass(fname);
				sClassMap.put(fname, clazz);
			}
			f = (Fragment) clazz.newInstance();
			if (args != null) {
				args.setClassLoader(f.getClass().getClassLoader());
				f.mArguments = args;
				return f;
			} else {
				return f;
			}
		} catch (ClassNotFoundException e) {
			throw new InstantiationException("Unable to instantiate fragment " + fname + ": make sure class name exists, is public, and has an" + " empty constructor that is public", e);
		} catch (InstantiationException e_2) {
			throw new InstantiationException("Unable to instantiate fragment " + fname + ": make sure class name exists, is public, and has an" + " empty constructor that is public", e_2);
		} catch (IllegalAccessException e_3) {
			throw new InstantiationException("Unable to instantiate fragment " + fname + ": make sure class name exists, is public, and has an" + " empty constructor that is public", e_3);
		}
	}

	static boolean isSupportFragmentClass(Context context, String fname) {
		Class<?> clazz;
		try {
			clazz = (Class) sClassMap.get(fname);
			if (clazz == null) {
				clazz = context.getClassLoader().loadClass(fname);
				sClassMap.put(fname, clazz);
			}
			return Fragment.class.isAssignableFrom(clazz);
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
		writer.print(prefix);
		writer.print("mFragmentId=#");
		writer.print(Integer.toHexString(mFragmentId));
		writer.print(" mContainerId=#");
		writer.print(Integer.toHexString(mContainerId));
		writer.print(" mTag=");
		writer.println(mTag);
		writer.print(prefix);
		writer.print("mState=");
		writer.print(mState);
		writer.print(" mIndex=");
		writer.print(mIndex);
		writer.print(" mWho=");
		writer.print(mWho);
		writer.print(" mBackStackNesting=");
		writer.println(mBackStackNesting);
		writer.print(prefix);
		writer.print("mAdded=");
		writer.print(mAdded);
		writer.print(" mRemoving=");
		writer.print(mRemoving);
		writer.print(" mResumed=");
		writer.print(mResumed);
		writer.print(" mFromLayout=");
		writer.print(mFromLayout);
		writer.print(" mInLayout=");
		writer.println(mInLayout);
		writer.print(prefix);
		writer.print("mHidden=");
		writer.print(mHidden);
		writer.print(" mDetached=");
		writer.print(mDetached);
		writer.print(" mMenuVisible=");
		writer.print(mMenuVisible);
		writer.print(" mHasMenu=");
		writer.println(mHasMenu);
		writer.print(prefix);
		writer.print("mRetainInstance=");
		writer.print(mRetainInstance);
		writer.print(" mRetaining=");
		writer.print(mRetaining);
		writer.print(" mUserVisibleHint=");
		writer.println(mUserVisibleHint);
		if (mFragmentManager != null) {
			writer.print(prefix);
			writer.print("mFragmentManager=");
			writer.println(mFragmentManager);
		}
		if (mActivity != null) {
			writer.print(prefix);
			writer.print("mActivity=");
			writer.println(mActivity);
		}
		if (mParentFragment != null) {
			writer.print(prefix);
			writer.print("mParentFragment=");
			writer.println(mParentFragment);
		}
		if (mArguments != null) {
			writer.print(prefix);
			writer.print("mArguments=");
			writer.println(mArguments);
		}
		if (mSavedFragmentState != null) {
			writer.print(prefix);
			writer.print("mSavedFragmentState=");
			writer.println(mSavedFragmentState);
		}
		if (mSavedViewState != null) {
			writer.print(prefix);
			writer.print("mSavedViewState=");
			writer.println(mSavedViewState);
		}
		if (mTarget != null) {
			writer.print(prefix);
			writer.print("mTarget=");
			writer.print(mTarget);
			writer.print(" mTargetRequestCode=");
			writer.println(mTargetRequestCode);
		}
		if (mNextAnim != 0) {
			writer.print(prefix);
			writer.print("mNextAnim=");
			writer.println(mNextAnim);
		}
		if (mContainer != null) {
			writer.print(prefix);
			writer.print("mContainer=");
			writer.println(mContainer);
		}
		if (mView != null) {
			writer.print(prefix);
			writer.print("mView=");
			writer.println(mView);
		}
		if (mInnerView != null) {
			writer.print(prefix);
			writer.print("mInnerView=");
			writer.println(mView);
		}
		if (mAnimatingAway != null) {
			writer.print(prefix);
			writer.print("mAnimatingAway=");
			writer.println(mAnimatingAway);
			writer.print(prefix);
			writer.print("mStateAfterAnimating=");
			writer.println(mStateAfterAnimating);
		}
		if (mLoaderManager != null) {
			writer.print(prefix);
			writer.println("Loader Manager:");
			mLoaderManager.dump(prefix + "  ", fd, writer, args);
		}
		if (mChildFragmentManager != null) {
			writer.print(prefix);
			writer.println("Child " + mChildFragmentManager + ":");
			mChildFragmentManager.dump(prefix + "  ", fd, writer, args);
		}
	}

	public final boolean equals(Object o) {
		return super.equals(o);
	}

	Fragment findFragmentByWho(String who) {
		if (who.equals(mWho)) {
			return this;
		} else if (mChildFragmentManager != null) {
			return mChildFragmentManager.findFragmentByWho(who);
		} else {
			return null;
		}
	}

	public final FragmentActivity getActivity() {
		return mActivity;
	}

	public final Bundle getArguments() {
		return mArguments;
	}

	public final FragmentManager getChildFragmentManager() {
		if (mChildFragmentManager == null) {
			instantiateChildFragmentManager();
			if (mState >= 5) {
				mChildFragmentManager.dispatchResume();
			} else if (mState >= 4) {
				mChildFragmentManager.dispatchStart();
			} else if (mState >= 2) {
				mChildFragmentManager.dispatchActivityCreated();
			} else if (mState >= 1) {
				mChildFragmentManager.dispatchCreate();
			}
		}
		return mChildFragmentManager;
	}

	public final FragmentManager getFragmentManager() {
		return mFragmentManager;
	}

	public final int getId() {
		return mFragmentId;
	}

	public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
		return mActivity.getLayoutInflater();
	}

	public LoaderManager getLoaderManager() {
		if (mLoaderManager != null) {
			return mLoaderManager;
		} else if (mActivity == null) {
			throw new IllegalStateException("Fragment " + this + " not attached to Activity");
		} else {
			mCheckedForLoaderManager = true;
			mLoaderManager = mActivity.getLoaderManager(mWho, mLoadersStarted, true);
			return mLoaderManager;
		}
	}

	public final Fragment getParentFragment() {
		return mParentFragment;
	}

	public final Resources getResources() {
		if (mActivity == null) {
			throw new IllegalStateException("Fragment " + this + " not attached to Activity");
		} else {
			return mActivity.getResources();
		}
	}

	public final boolean getRetainInstance() {
		return mRetainInstance;
	}

	public final String getString(int resId) {
		return getResources().getString(resId);
	}

	public final String getString(int resId, Object ... formatArgs) {
		return getResources().getString(resId, formatArgs);
	}

	public final String getTag() {
		return mTag;
	}

	public final Fragment getTargetFragment() {
		return mTarget;
	}

	public final int getTargetRequestCode() {
		return mTargetRequestCode;
	}

	public final CharSequence getText(int resId) {
		return getResources().getText(resId);
	}

	public boolean getUserVisibleHint() {
		return mUserVisibleHint;
	}

	@Nullable
	public View getView() {
		return mView;
	}

	public final boolean hasOptionsMenu() {
		return mHasMenu;
	}

	public final int hashCode() {
		return super.hashCode();
	}

	void initState() {
		mIndex = -1;
		mWho = null;
		mAdded = false;
		mRemoving = false;
		mResumed = false;
		mFromLayout = false;
		mInLayout = false;
		mRestored = false;
		mBackStackNesting = 0;
		mFragmentManager = null;
		mChildFragmentManager = null;
		mActivity = null;
		mFragmentId = 0;
		mContainerId = 0;
		mTag = null;
		mHidden = false;
		mDetached = false;
		mRetaining = false;
		mLoaderManager = null;
		mLoadersStarted = false;
		mCheckedForLoaderManager = false;
	}

	void instantiateChildFragmentManager() {
		mChildFragmentManager = new FragmentManagerImpl();
		mChildFragmentManager.attachActivity(mActivity, new AnonymousClass_1(this), this);
	}

	public final boolean isAdded() {
		if (mActivity == null || !mAdded) {
			return false;
		} else {
			return true;
		}
	}

	public final boolean isDetached() {
		return mDetached;
	}

	public final boolean isHidden() {
		return mHidden;
	}

	final boolean isInBackStack() {
		if (mBackStackNesting > 0) {
			return true;
		} else {
			return false;
		}
	}

	public final boolean isInLayout() {
		return mInLayout;
	}

	public final boolean isMenuVisible() {
		return mMenuVisible;
	}

	public final boolean isRemoving() {
		return mRemoving;
	}

	public final boolean isResumed() {
		return mResumed;
	}

	public final boolean isVisible() {
		if (!isAdded() || isHidden() || mView == null || mView.getWindowToken() == null || mView.getVisibility() != 0) {
			return false;
		} else {
			return true;
		}
	}

	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		mCalled = true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	}

	public void onAttach(Activity activity) {
		mCalled = true;
	}

	public void onConfigurationChanged(Configuration newConfig) {
		mCalled = true;
	}

	public boolean onContextItemSelected(MenuItem item) {
		return false;
	}

	public void onCreate(Bundle savedInstanceState) {
		mCalled = true;
	}

	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		return null;
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().onCreateContextMenu(menu, v, menuInfo);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	}

	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return null;
	}

	public void onDestroy() {
		boolean r1z = true;
		mCalled = true;
		if (!mCheckedForLoaderManager) {
			mCheckedForLoaderManager = r1z;
			mLoaderManager = mActivity.getLoaderManager(mWho, mLoadersStarted, false);
		}
		if (mLoaderManager != null) {
			mLoaderManager.doDestroy();
		}
	}

	public void onDestroyOptionsMenu() {
	}

	public void onDestroyView() {
		mCalled = true;
	}

	public void onDetach() {
		mCalled = true;
	}

	public void onHiddenChanged(boolean hidden) {
	}

	public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
		mCalled = true;
	}

	public void onLowMemory() {
		mCalled = true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}

	public void onOptionsMenuClosed(Menu menu) {
	}

	public void onPause() {
		mCalled = true;
	}

	public void onPrepareOptionsMenu(Menu menu) {
	}

	public void onResume() {
		mCalled = true;
	}

	public void onSaveInstanceState(Bundle outState) {
	}

	public void onStart() {
		boolean r1z = true;
		mCalled = true;
		if (!mLoadersStarted) {
			mLoadersStarted = true;
			if (!mCheckedForLoaderManager) {
				mCheckedForLoaderManager = r1z;
				mLoaderManager = mActivity.getLoaderManager(mWho, mLoadersStarted, false);
			}
			if (mLoaderManager != null) {
				mLoaderManager.doStart();
			}
		}
	}

	public void onStop() {
		mCalled = true;
	}

	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
	}

	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		mCalled = true;
	}

	void performActivityCreated(Bundle savedInstanceState) {
		if (mChildFragmentManager != null) {
			mChildFragmentManager.noteStateNotSaved();
		}
		mCalled = false;
		onActivityCreated(savedInstanceState);
		if (!mCalled) {
			throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onActivityCreated()");
		} else if (mChildFragmentManager != null) {
			mChildFragmentManager.dispatchActivityCreated();
		}
	}

	void performConfigurationChanged(Configuration newConfig) {
		onConfigurationChanged(newConfig);
		if (mChildFragmentManager != null) {
			mChildFragmentManager.dispatchConfigurationChanged(newConfig);
		}
	}

	boolean performContextItemSelected(MenuItem item) {
		if (!mHidden) {
			if (onContextItemSelected(item)) {
				return true;
			} else if (mChildFragmentManager == null || !mChildFragmentManager.dispatchContextItemSelected(item)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	void performCreate(Bundle savedInstanceState) {
		if (mChildFragmentManager != null) {
			mChildFragmentManager.noteStateNotSaved();
		}
		mCalled = false;
		onCreate(savedInstanceState);
		if (!mCalled) {
			throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onCreate()");
		} else if (savedInstanceState != null) {
			Parcelable p = savedInstanceState.getParcelable("android:support:fragments");
			if (p != null) {
				if (mChildFragmentManager == null) {
					instantiateChildFragmentManager();
				}
				mChildFragmentManager.restoreAllState(p, null);
				mChildFragmentManager.dispatchCreate();
			}
		}
	}

	boolean performCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		boolean show = false;
		if (!mHidden) {
			if (!mHasMenu || !mMenuVisible) {
				if (mChildFragmentManager == null) {
					show |= mChildFragmentManager.dispatchCreateOptionsMenu(menu, inflater);
				}
			} else {
				show = true;
				onCreateOptionsMenu(menu, inflater);
				if (mChildFragmentManager == null) {
					return show;
				} else {
					show |= mChildFragmentManager.dispatchCreateOptionsMenu(menu, inflater);
				}
			}
		}
		return show;
	}

	View performCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mChildFragmentManager != null) {
			mChildFragmentManager.noteStateNotSaved();
		}
		return onCreateView(inflater, container, savedInstanceState);
	}

	void performDestroy() {
		if (mChildFragmentManager != null) {
			mChildFragmentManager.dispatchDestroy();
		}
		mCalled = false;
		onDestroy();
		if (!mCalled) {
			throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDestroy()");
		}
	}

	void performDestroyView() {
		if (mChildFragmentManager != null) {
			mChildFragmentManager.dispatchDestroyView();
		}
		mCalled = false;
		onDestroyView();
		if (!mCalled) {
			throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDestroyView()");
		} else if (mLoaderManager != null) {
			mLoaderManager.doReportNextStart();
		}
	}

	void performLowMemory() {
		onLowMemory();
		if (mChildFragmentManager != null) {
			mChildFragmentManager.dispatchLowMemory();
		}
	}

	boolean performOptionsItemSelected(MenuItem item) {
		if (!mHidden) {
			if (!mHasMenu || !mMenuVisible || !onOptionsItemSelected(item)) {
				if (mChildFragmentManager == null || !mChildFragmentManager.dispatchOptionsItemSelected(item)) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	void performOptionsMenuClosed(Menu menu) {
		if (!mHidden) {
			if (!mHasMenu || !mMenuVisible) {
				if (mChildFragmentManager == null) {
					mChildFragmentManager.dispatchOptionsMenuClosed(menu);
				}
			} else {
				onOptionsMenuClosed(menu);
				if (mChildFragmentManager == null) {
				} else {
					mChildFragmentManager.dispatchOptionsMenuClosed(menu);
				}
			}
		}
	}

	void performPause() {
		if (mChildFragmentManager != null) {
			mChildFragmentManager.dispatchPause();
		}
		mCalled = false;
		onPause();
		if (!mCalled) {
			throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onPause()");
		}
	}

	boolean performPrepareOptionsMenu(Menu menu) {
		boolean show = false;
		if (!mHidden) {
			if (!mHasMenu || !mMenuVisible) {
				if (mChildFragmentManager == null) {
					show |= mChildFragmentManager.dispatchPrepareOptionsMenu(menu);
				}
			} else {
				show = true;
				onPrepareOptionsMenu(menu);
				if (mChildFragmentManager == null) {
					return show;
				} else {
					show |= mChildFragmentManager.dispatchPrepareOptionsMenu(menu);
				}
			}
		}
		return show;
	}

	void performReallyStop() {
		if (mChildFragmentManager != null) {
			mChildFragmentManager.dispatchReallyStop();
		}
		if (mLoadersStarted) {
			mLoadersStarted = false;
			if (!mCheckedForLoaderManager) {
				mCheckedForLoaderManager = true;
				mLoaderManager = mActivity.getLoaderManager(mWho, mLoadersStarted, false);
			}
			if (mLoaderManager != null) {
				if (!mActivity.mRetaining) {
					mLoaderManager.doStop();
				} else {
					mLoaderManager.doRetain();
				}
			}
		}
	}

	void performResume() {
		if (mChildFragmentManager != null) {
			mChildFragmentManager.noteStateNotSaved();
			mChildFragmentManager.execPendingActions();
		}
		mCalled = false;
		onResume();
		if (!mCalled) {
			throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onResume()");
		} else if (mChildFragmentManager != null) {
			mChildFragmentManager.dispatchResume();
			mChildFragmentManager.execPendingActions();
		}
	}

	void performSaveInstanceState(Bundle outState) {
		onSaveInstanceState(outState);
		if (mChildFragmentManager != null) {
			Parcelable p = mChildFragmentManager.saveAllState();
			if (p != null) {
				outState.putParcelable("android:support:fragments", p);
			}
		}
	}

	void performStart() {
		if (mChildFragmentManager != null) {
			mChildFragmentManager.noteStateNotSaved();
			mChildFragmentManager.execPendingActions();
		}
		mCalled = false;
		onStart();
		if (!mCalled) {
			throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onStart()");
		} else {
			if (mChildFragmentManager != null) {
				mChildFragmentManager.dispatchStart();
			}
			if (mLoaderManager != null) {
				mLoaderManager.doReportStart();
			}
		}
	}

	void performStop() {
		if (mChildFragmentManager != null) {
			mChildFragmentManager.dispatchStop();
		}
		mCalled = false;
		onStop();
		if (!mCalled) {
			throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onStop()");
		}
	}

	public void registerForContextMenu(View view) {
		view.setOnCreateContextMenuListener(this);
	}

	final void restoreViewState(Bundle savedInstanceState) {
		if (mSavedViewState != null) {
			mInnerView.restoreHierarchyState(mSavedViewState);
			mSavedViewState = null;
		}
		mCalled = false;
		onViewStateRestored(savedInstanceState);
		if (!mCalled) {
			throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onViewStateRestored()");
		}
	}

	public void setArguments(Bundle args) {
		if (mIndex >= 0) {
			throw new IllegalStateException("Fragment already active");
		} else {
			mArguments = args;
		}
	}

	public void setHasOptionsMenu(boolean hasMenu) {
		if (mHasMenu != hasMenu) {
			mHasMenu = hasMenu;
			if (!isAdded() || isHidden()) {
			} else {
				mActivity.supportInvalidateOptionsMenu();
			}
		}
	}

	final void setIndex(int index, Fragment parent) {
		mIndex = index;
		if (parent != null) {
			parent.mWho += ":" + mIndex;
		} else {
			mWho = "android:fragment:" + mIndex;
		}
	}

	public void setInitialSavedState(SavedState state) {
		if (mIndex >= 0) {
			throw new IllegalStateException("Fragment already active");
		} else {
			Bundle r0_Bundle;
			if (state == null || state.mState == null) {
				r0_Bundle = null;
			} else {
				r0_Bundle = state.mState;
			}
			mSavedFragmentState = r0_Bundle;
		}
	}

	public void setMenuVisibility(boolean menuVisible) {
		if (mMenuVisible != menuVisible) {
			mMenuVisible = menuVisible;
			if (!mHasMenu || !isAdded() || isHidden()) {
			} else {
				mActivity.supportInvalidateOptionsMenu();
			}
		}
	}

	public void setRetainInstance(boolean retain) {
		if (!retain || mParentFragment == null) {
			mRetainInstance = retain;
		} else {
			throw new IllegalStateException("Can't retain fragements that are nested in other fragments");
		}
	}

	public void setTargetFragment(Fragment fragment, int requestCode) {
		mTarget = fragment;
		mTargetRequestCode = requestCode;
	}

	public void setUserVisibleHint(boolean isVisibleToUser) {
		boolean r0z;
		if (mUserVisibleHint || !isVisibleToUser || mState >= 4) {
			mUserVisibleHint = isVisibleToUser;
			if (isVisibleToUser) {
				r0z = true;
			} else {
				r0z = false;
			}
			mDeferStart = r0z;
		} else {
			mFragmentManager.performPendingDeferredStart(this);
			mUserVisibleHint = isVisibleToUser;
			if (isVisibleToUser) {
				r0z = false;
			} else {
				r0z = true;
			}
			mDeferStart = r0z;
		}
	}

	public void startActivity(Intent intent) {
		if (mActivity == null) {
			throw new IllegalStateException("Fragment " + this + " not attached to Activity");
		} else {
			mActivity.startActivityFromFragment(this, intent, -1);
		}
	}

	public void startActivityForResult(Intent intent, int requestCode) {
		if (mActivity == null) {
			throw new IllegalStateException("Fragment " + this + " not attached to Activity");
		} else {
			mActivity.startActivityFromFragment(this, intent, requestCode);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		DebugUtils.buildShortClassTag(this, sb);
		if (mIndex >= 0) {
			sb.append(" #");
			sb.append(mIndex);
		}
		if (mFragmentId != 0) {
			sb.append(" id=0x");
			sb.append(Integer.toHexString(mFragmentId));
		}
		if (mTag != null) {
			sb.append(" ");
			sb.append(mTag);
		}
		sb.append('}');
		return sb.toString();
	}

	public void unregisterForContextMenu(View view) {
		view.setOnCreateContextMenuListener(null);
	}
}
