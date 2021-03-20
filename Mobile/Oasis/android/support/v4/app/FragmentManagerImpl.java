package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.media.TransportMediator;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.widget.AutoScrollHelper;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class FragmentManagerImpl extends FragmentManager {
	static final Interpolator ACCELERATE_CUBIC;
	static final Interpolator ACCELERATE_QUINT;
	static final int ANIM_DUR = 220;
	public static final int ANIM_STYLE_CLOSE_ENTER = 3;
	public static final int ANIM_STYLE_CLOSE_EXIT = 4;
	public static final int ANIM_STYLE_FADE_ENTER = 5;
	public static final int ANIM_STYLE_FADE_EXIT = 6;
	public static final int ANIM_STYLE_OPEN_ENTER = 1;
	public static final int ANIM_STYLE_OPEN_EXIT = 2;
	static boolean DEBUG = false;
	static final Interpolator DECELERATE_CUBIC;
	static final Interpolator DECELERATE_QUINT;
	static final boolean HONEYCOMB;
	static final String TAG = "FragmentManager";
	static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
	static final String TARGET_STATE_TAG = "android:target_state";
	static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
	static final String VIEW_STATE_TAG = "android:view_state";
	ArrayList<Fragment> mActive;
	FragmentActivity mActivity;
	ArrayList<Fragment> mAdded;
	ArrayList<Integer> mAvailBackStackIndices;
	ArrayList<Integer> mAvailIndices;
	ArrayList<BackStackRecord> mBackStack;
	ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
	ArrayList<BackStackRecord> mBackStackIndices;
	FragmentContainer mContainer;
	ArrayList<Fragment> mCreatedMenus;
	int mCurState;
	boolean mDestroyed;
	Runnable mExecCommit;
	boolean mExecutingActions;
	boolean mHavePendingDeferredStart;
	boolean mNeedMenuInvalidate;
	String mNoTransactionsBecause;
	Fragment mParent;
	ArrayList<Runnable> mPendingActions;
	SparseArray<Parcelable> mStateArray;
	Bundle mStateBundle;
	boolean mStateSaved;
	Runnable[] mTmpActions;

	class AnonymousClass_1 implements Runnable {
		final /* synthetic */ FragmentManagerImpl this$0;

		AnonymousClass_1(FragmentManagerImpl r1_FragmentManagerImpl) {
			super();
			this$0 = r1_FragmentManagerImpl;
		}

		public void run() {
			this$0.execPendingActions();
		}
	}

	class AnonymousClass_2 implements Runnable {
		final /* synthetic */ FragmentManagerImpl this$0;

		AnonymousClass_2(FragmentManagerImpl r1_FragmentManagerImpl) {
			super();
			this$0 = r1_FragmentManagerImpl;
		}

		public void run() {
			this$0.popBackStackState(this$0.mActivity.mHandler, null, -1, 0);
		}
	}

	class AnonymousClass_3 implements Runnable {
		final /* synthetic */ FragmentManagerImpl this$0;
		final /* synthetic */ int val$flags;
		final /* synthetic */ String val$name;

		AnonymousClass_3(FragmentManagerImpl r1_FragmentManagerImpl, String r2_String, int r3i) {
			super();
			this$0 = r1_FragmentManagerImpl;
			val$name = r2_String;
			val$flags = r3i;
		}

		public void run() {
			this$0.popBackStackState(this$0.mActivity.mHandler, val$name, -1, val$flags);
		}
	}

	class AnonymousClass_4 implements Runnable {
		final /* synthetic */ FragmentManagerImpl this$0;
		final /* synthetic */ int val$flags;
		final /* synthetic */ int val$id;

		AnonymousClass_4(FragmentManagerImpl r1_FragmentManagerImpl, int r2i, int r3i) {
			super();
			this$0 = r1_FragmentManagerImpl;
			val$id = r2i;
			val$flags = r3i;
		}

		public void run() {
			this$0.popBackStackState(this$0.mActivity.mHandler, null, val$id, val$flags);
		}
	}

	class AnonymousClass_5 implements AnimationListener {
		final /* synthetic */ FragmentManagerImpl this$0;
		final /* synthetic */ Fragment val$fragment;

		AnonymousClass_5(FragmentManagerImpl r1_FragmentManagerImpl, Fragment r2_Fragment) {
			super();
			this$0 = r1_FragmentManagerImpl;
			val$fragment = r2_Fragment;
		}

		public void onAnimationEnd(Animation animation) {
			if (val$fragment.mAnimatingAway != null) {
				val$fragment.mAnimatingAway = null;
				this$0.moveToState(val$fragment, val$fragment.mStateAfterAnimating, 0, 0, false);
			}
		}

		public void onAnimationRepeat(Animation animation) {
		}

		public void onAnimationStart(Animation animation) {
		}
	}


	static {
		boolean r0z = HONEYCOMB;
		DEBUG = false;
		if (VERSION.SDK_INT >= 11) {
			r0z = true;
		}
		HONEYCOMB = r0z;
		DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
		DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
		ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
		ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);
	}

	FragmentManagerImpl() {
		super();
		mCurState = 0;
		mStateBundle = null;
		mStateArray = null;
		mExecCommit = new AnonymousClass_1(this);
	}

	private void checkStateLoss() {
		if (mStateSaved) {
			throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
		} else if (mNoTransactionsBecause != null) {
			throw new IllegalStateException("Can not perform this action inside of " + mNoTransactionsBecause);
		}
	}

	static Animation makeFadeAnimation(Context context, float start, float end) {
		Animation anim = new AlphaAnimation(start, end);
		anim.setInterpolator(DECELERATE_CUBIC);
		anim.setDuration(220);
		return anim;
	}

	static Animation makeOpenCloseAnimation(Context context, float startScale, float endScale, float startAlpha, float endAlpha) {
		Animation set = new AnimationSet(false);
		Animation scale = new ScaleAnimation(startScale, endScale, startScale, endScale, 1, 0.5f, 1, 0.5f);
		scale.setInterpolator(DECELERATE_QUINT);
		scale.setDuration(220);
		set.addAnimation(scale);
		Animation alpha = new AlphaAnimation(startAlpha, endAlpha);
		alpha.setInterpolator(DECELERATE_CUBIC);
		alpha.setDuration(220);
		set.addAnimation(alpha);
		return set;
	}

	public static int reverseTransit(int transit) {
		switch(transit) {
		case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
			return FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
		case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
			return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
		case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
			return FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
		}
		return 0;
	}

	private void throwException(RuntimeException ex) {
		Log.e(TAG, ex.getMessage());
		Log.e(TAG, "Activity state:");
		PrintWriter pw = new PrintWriter(new LogWriter(TAG));
		if (mActivity != null) {
			try {
				mActivity.dump("  ", null, pw, new String[0]);
			} catch (Exception e) {
				Log.e(TAG, "Failed dumping state", e);
				return ex;
			}
		} else {
			try {
				dump("  ", null, pw, new String[0]);
			} catch (Exception e_2) {
				Log.e(TAG, "Failed dumping state", e_2);
				return ex;
			}
		}
	}

	public static int transitToStyleIndex(int transit, boolean enter) {
		int animAttr;
		switch(transit) {
		case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
			if (enter) {
				animAttr = ANIM_STYLE_OPEN_ENTER;
			} else {
				animAttr = ANIM_STYLE_OPEN_EXIT;
			}
			return animAttr;
		case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
			if (enter) {
				animAttr = ANIM_STYLE_FADE_ENTER;
			} else {
				animAttr = ANIM_STYLE_FADE_EXIT;
			}
			return animAttr;
		case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
			if (enter) {
				animAttr = ANIM_STYLE_CLOSE_ENTER;
			} else {
				animAttr = ANIM_STYLE_CLOSE_EXIT;
			}
			return animAttr;
		}
		return -1;
	}

	void addBackStackState(BackStackRecord state) {
		if (mBackStack == null) {
			mBackStack = new ArrayList();
		}
		mBackStack.add(state);
		reportBackStackChanged();
	}

	/* JADX WARNING: inconsistent code */
	/*
	public void addFragment(android.support.v4.app.Fragment r5_fragment, boolean r6_moveToStateNow) {
		r4_this = this;
		r3 = 1;
		r0 = r4.mAdded;
		if (r0 != 0) goto L_0x000c;
	L_0x0005:
		r0 = new java.util.ArrayList;
		r0.<init>();
		r4.mAdded = r0;
	L_0x000c:
		r0 = DEBUG;
		if (r0 == 0) goto L_0x0028;
	L_0x0010:
		r0 = "FragmentManager";
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "add: ";
		r1 = r1.append(r2);
		r1 = r1.append(r5_fragment);
		r1 = r1.toString();
		android.util.Log.v(r0, r1);
	L_0x0028:
		r4.makeActive(r5_fragment);
		r0 = r5_fragment.mDetached;
		if (r0 != 0) goto L_0x0069;
	L_0x002f:
		r0 = r4.mAdded;
		r0 = r0.contains(r5_fragment);
		if (r0 == 0) goto L_0x0050;
	L_0x0037:
		r0 = new java.lang.IllegalStateException;
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "Fragment already added: ";
		r1 = r1.append(r2);
		r1 = r1.append(r5_fragment);
		r1 = r1.toString();
		r0.<init>(r1);
		throw r0;
	L_0x0050:
		r0 = r4.mAdded;
		r0.add(r5_fragment);
		r5_fragment.mAdded = r3;
		r0 = 0;
		r5_fragment.mRemoving = r0;
		r0 = r5_fragment.mHasMenu;
		if (r0 == 0) goto L_0x0064;
	L_0x005e:
		r0 = r5_fragment.mMenuVisible;
		if (r0 == 0) goto L_0x0064;
	L_0x0062:
		r4.mNeedMenuInvalidate = r3;
	L_0x0064:
		if (r6_moveToStateNow == 0) goto L_0x0069;
	L_0x0066:
		r4.moveToState(r5_fragment);
	L_0x0069:
		return;
	}
	*/
	public void addFragment(Fragment fragment, boolean moveToStateNow) {
		if (mAdded == null) {
			mAdded = new ArrayList();
		}
		if (DEBUG) {
			Log.v(TAG, "add: " + fragment);
		}
		makeActive(fragment);
		if (!fragment.mDetached) {
			if (mAdded.contains(fragment)) {
				throw new IllegalStateException("Fragment already added: " + fragment);
			} else {
				mAdded.add(fragment);
				fragment.mAdded = true;
				fragment.mRemoving = false;
				if (!fragment.mHasMenu || !fragment.mMenuVisible) {
				} else {
					mNeedMenuInvalidate = true;
				}
			}
		}
	}

	public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
		if (mBackStackChangeListeners == null) {
			mBackStackChangeListeners = new ArrayList();
		}
		mBackStackChangeListeners.add(listener);
	}

	public int allocBackStackIndex(BackStackRecord bse) {
		int index;
		synchronized(this) {
			int index_2;
			if (mAvailBackStackIndices == null || mAvailBackStackIndices.size() <= 0) {
				if (mBackStackIndices == null) {
					mBackStackIndices = new ArrayList();
				}
				index_2 = mBackStackIndices.size();
				if (DEBUG) {
					Log.v(TAG, "Setting back stack index " + index_2 + " to " + bse);
				}
				mBackStackIndices.add(bse);
				index = index_2;
			} else {
				index = ((Integer) mAvailBackStackIndices.remove(mAvailBackStackIndices.size() - 1)).intValue();
				if (DEBUG) {
					Log.v(TAG, "Adding back stack index " + index + " with " + bse);
				}
				mBackStackIndices.set(index, bse);
				index = index;
			}
		}
		return index;
	}

	public void attachActivity(FragmentActivity activity, FragmentContainer container, Fragment parent) {
		if (mActivity != null) {
			throw new IllegalStateException("Already attached");
		} else {
			mActivity = activity;
			mContainer = container;
			mParent = parent;
		}
	}

	public void attachFragment(Fragment fragment, int transition, int transitionStyle) {
		if (DEBUG) {
			Log.v(TAG, "attach: " + fragment);
		}
		if (fragment.mDetached) {
			fragment.mDetached = false;
			if (!fragment.mAdded) {
				if (mAdded == null) {
					mAdded = new ArrayList();
				}
				if (mAdded.contains(fragment)) {
					throw new IllegalStateException("Fragment already added: " + fragment);
				} else {
					if (DEBUG) {
						Log.v(TAG, "add from attach: " + fragment);
					}
					mAdded.add(fragment);
					fragment.mAdded = true;
					if (!fragment.mHasMenu || !fragment.mMenuVisible) {
						moveToState(fragment, mCurState, transition, transitionStyle, HONEYCOMB);
					} else {
						mNeedMenuInvalidate = true;
						moveToState(fragment, mCurState, transition, transitionStyle, HONEYCOMB);
					}
				}
			}
		}
	}

	public FragmentTransaction beginTransaction() {
		return new BackStackRecord(this);
	}

	public void detachFragment(Fragment fragment, int transition, int transitionStyle) {
		if (DEBUG) {
			Log.v(TAG, "detach: " + fragment);
		}
		if (!fragment.mDetached) {
			fragment.mDetached = true;
			if (fragment.mAdded) {
				if (mAdded != null) {
					if (DEBUG) {
						Log.v(TAG, "remove from detach: " + fragment);
					}
					mAdded.remove(fragment);
				}
				if (!fragment.mHasMenu || !fragment.mMenuVisible) {
					fragment.mAdded = false;
					moveToState(fragment, ANIM_STYLE_OPEN_ENTER, transition, transitionStyle, HONEYCOMB);
				} else {
					mNeedMenuInvalidate = true;
					fragment.mAdded = false;
					moveToState(fragment, ANIM_STYLE_OPEN_ENTER, transition, transitionStyle, HONEYCOMB);
				}
			}
		}
	}

	public void dispatchActivityCreated() {
		mStateSaved = false;
		moveToState(ANIM_STYLE_OPEN_EXIT, HONEYCOMB);
	}

	public void dispatchConfigurationChanged(Configuration newConfig) {
		if (mAdded != null) {
			int i = 0;
			while (i < mAdded.size()) {
				Fragment f = (Fragment) mAdded.get(i);
				if (f != null) {
					f.performConfigurationChanged(newConfig);
				}
				i++;
			}
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	public boolean dispatchContextItemSelected(android.view.MenuItem r4_item) {
		r3_this = this;
		r2 = r3.mAdded;
		if (r2 == 0) goto L_0x0022;
	L_0x0004:
		r1 = 0;
	L_0x0005:
		r2 = r3.mAdded;
		r2 = r2.size();
		if (r1_i >= r2) goto L_0x0022;
	L_0x000d:
		r2 = r3.mAdded;
		r0 = r2.get(r1_i);
		r0 = (android.support.v4.app.Fragment) r0;
		if (r0_f == 0) goto L_0x001f;
	L_0x0017:
		r2 = r0_f.performContextItemSelected(r4_item);
		if (r2 == 0) goto L_0x001f;
	L_0x001d:
		r2 = 1;
	L_0x001e:
		return r2;
	L_0x001f:
		r1_i++;
		goto L_0x0005;
	L_0x0022:
		r2 = 0;
		goto L_0x001e;
	}
	*/
	public boolean dispatchContextItemSelected(MenuItem item) {
		if (mAdded != null) {
			int i = 0;
			while (i < mAdded.size()) {
				Fragment f = (Fragment) mAdded.get(i);
				if (f == null || !f.performContextItemSelected(item)) {
					i++;
				}
			}
		}
		return HONEYCOMB;
	}

	public void dispatchCreate() {
		mStateSaved = false;
		moveToState(ANIM_STYLE_OPEN_ENTER, HONEYCOMB);
	}

	public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		int i;
		Fragment f;
		boolean show = HONEYCOMB;
		ArrayList<Fragment> newMenus = null;
		if (mAdded != null) {
			i = 0;
			while (i < mAdded.size()) {
				f = (Fragment) mAdded.get(i);
				if (f == null || !f.performCreateOptionsMenu(menu, inflater)) {
					i++;
				} else {
					show = true;
					if (newMenus == null) {
						newMenus = new ArrayList();
					}
					newMenus.add(f);
					i++;
				}
			}
		}
		if (mCreatedMenus != null) {
			i = 0;
			while (i < mCreatedMenus.size()) {
				f = mCreatedMenus.get(i);
				if (newMenus == null || !newMenus.contains(f)) {
					f.onDestroyOptionsMenu();
				} else {
					i++;
				}
				i++;
			}
		}
		mCreatedMenus = newMenus;
		return show;
	}

	public void dispatchDestroy() {
		mDestroyed = true;
		execPendingActions();
		moveToState(0, HONEYCOMB);
		mActivity = null;
		mContainer = null;
		mParent = null;
	}

	public void dispatchDestroyView() {
		moveToState(ANIM_STYLE_OPEN_ENTER, HONEYCOMB);
	}

	public void dispatchLowMemory() {
		if (mAdded != null) {
			int i = 0;
			while (i < mAdded.size()) {
				Fragment f = (Fragment) mAdded.get(i);
				if (f != null) {
					f.performLowMemory();
				}
				i++;
			}
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	public boolean dispatchOptionsItemSelected(android.view.MenuItem r4_item) {
		r3_this = this;
		r2 = r3.mAdded;
		if (r2 == 0) goto L_0x0022;
	L_0x0004:
		r1 = 0;
	L_0x0005:
		r2 = r3.mAdded;
		r2 = r2.size();
		if (r1_i >= r2) goto L_0x0022;
	L_0x000d:
		r2 = r3.mAdded;
		r0 = r2.get(r1_i);
		r0 = (android.support.v4.app.Fragment) r0;
		if (r0_f == 0) goto L_0x001f;
	L_0x0017:
		r2 = r0_f.performOptionsItemSelected(r4_item);
		if (r2 == 0) goto L_0x001f;
	L_0x001d:
		r2 = 1;
	L_0x001e:
		return r2;
	L_0x001f:
		r1_i++;
		goto L_0x0005;
	L_0x0022:
		r2 = 0;
		goto L_0x001e;
	}
	*/
	public boolean dispatchOptionsItemSelected(MenuItem item) {
		if (mAdded != null) {
			int i = 0;
			while (i < mAdded.size()) {
				Fragment f = (Fragment) mAdded.get(i);
				if (f == null || !f.performOptionsItemSelected(item)) {
					i++;
				}
			}
		}
		return HONEYCOMB;
	}

	public void dispatchOptionsMenuClosed(Menu menu) {
		if (mAdded != null) {
			int i = 0;
			while (i < mAdded.size()) {
				Fragment f = (Fragment) mAdded.get(i);
				if (f != null) {
					f.performOptionsMenuClosed(menu);
				}
				i++;
			}
		}
	}

	public void dispatchPause() {
		moveToState(ANIM_STYLE_CLOSE_EXIT, HONEYCOMB);
	}

	public boolean dispatchPrepareOptionsMenu(Menu menu) {
		boolean show = HONEYCOMB;
		if (mAdded != null) {
			int i = 0;
			while (i < mAdded.size()) {
				Fragment f = (Fragment) mAdded.get(i);
				if (f == null || !f.performPrepareOptionsMenu(menu)) {
					i++;
				} else {
					show = true;
					i++;
				}
			}
		}
		return show;
	}

	public void dispatchReallyStop() {
		moveToState(ANIM_STYLE_OPEN_EXIT, HONEYCOMB);
	}

	public void dispatchResume() {
		mStateSaved = false;
		moveToState(ANIM_STYLE_FADE_ENTER, HONEYCOMB);
	}

	public void dispatchStart() {
		mStateSaved = false;
		moveToState(ANIM_STYLE_CLOSE_EXIT, HONEYCOMB);
	}

	public void dispatchStop() {
		mStateSaved = true;
		moveToState(ANIM_STYLE_CLOSE_ENTER, HONEYCOMB);
	}

	public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
		int N;
		int i;
		String innerPrefix = prefix + "    ";
		if (mActive != null) {
			N = mActive.size();
			if (N > 0) {
				writer.print(prefix);
				writer.print("Active Fragments in ");
				writer.print(Integer.toHexString(System.identityHashCode(this)));
				writer.println(":");
				i = 0;
				while (i < N) {
					Fragment f = (Fragment) mActive.get(i);
					writer.print(prefix);
					writer.print("  #");
					writer.print(i);
					writer.print(": ");
					writer.println(f);
					if (f != null) {
						f.dump(innerPrefix, fd, writer, args);
					}
					i++;
				}
			}
		}
		if (mAdded != null) {
			N = mAdded.size();
			if (N > 0) {
				writer.print(prefix);
				writer.println("Added Fragments:");
				i = 0;
				while (i < N) {
					writer.print(prefix);
					writer.print("  #");
					writer.print(i);
					writer.print(": ");
					writer.println(mAdded.get(i).toString());
					i++;
				}
			}
		}
		if (mCreatedMenus != null) {
			N = mCreatedMenus.size();
			if (N > 0) {
				writer.print(prefix);
				writer.println("Fragments Created Menus:");
				i = 0;
				while (i < N) {
					writer.print(prefix);
					writer.print("  #");
					writer.print(i);
					writer.print(": ");
					writer.println(mCreatedMenus.get(i).toString());
					i++;
				}
			}
		}
		if (mBackStack != null) {
			N = mBackStack.size();
			if (N > 0) {
				writer.print(prefix);
				writer.println("Back Stack:");
				i = 0;
				while (i < N) {
					BackStackRecord bs = (BackStackRecord) mBackStack.get(i);
					writer.print(prefix);
					writer.print("  #");
					writer.print(i);
					writer.print(": ");
					writer.println(bs.toString());
					bs.dump(innerPrefix, fd, writer, args);
					i++;
				}
			}
		}
		synchronized(this) {
			try {
				if (mBackStackIndices != null) {
					N = mBackStackIndices.size();
					if (N > 0) {
						writer.print(prefix);
						writer.println("Back Stack Indices:");
						i = 0;
						while (i < N) {
							writer.print(prefix);
							writer.print("  #");
							writer.print(i);
							writer.print(": ");
							writer.println(mBackStackIndices.get(i));
							i++;
						}
					}
				}
				if (mAvailBackStackIndices == null || mAvailBackStackIndices.size() <= 0) {
				} else {
					writer.print(prefix);
					writer.print("mAvailBackStackIndices: ");
					writer.println(Arrays.toString(mAvailBackStackIndices.toArray()));
				}
			} catch (Throwable th) {
				while (true) {
					return th;
				}
			}
		}
		if (mPendingActions != null) {
			N = mPendingActions.size();
			if (N > 0) {
				writer.print(prefix);
				writer.println("Pending Actions:");
				i = 0;
				while (i < N) {
					writer.print(prefix);
					writer.print("  #");
					writer.print(i);
					writer.print(": ");
					writer.println((Runnable) mPendingActions.get(i));
					i++;
				}
			}
		}
		writer.print(prefix);
		writer.println("FragmentManager misc state:");
		writer.print(prefix);
		writer.print("  mActivity=");
		writer.println(mActivity);
		writer.print(prefix);
		writer.print("  mContainer=");
		writer.println(mContainer);
		if (mParent != null) {
			writer.print(prefix);
			writer.print("  mParent=");
			writer.println(mParent);
		}
		writer.print(prefix);
		writer.print("  mCurState=");
		writer.print(mCurState);
		writer.print(" mStateSaved=");
		writer.print(mStateSaved);
		writer.print(" mDestroyed=");
		writer.println(mDestroyed);
		if (mNeedMenuInvalidate) {
			writer.print(prefix);
			writer.print("  mNeedMenuInvalidate=");
			writer.println(mNeedMenuInvalidate);
		}
		if (mNoTransactionsBecause != null) {
			writer.print(prefix);
			writer.print("  mNoTransactionsBecause=");
			writer.println(mNoTransactionsBecause);
		}
		if (mAvailIndices == null || mAvailIndices.size() <= 0) {
		} else {
			writer.print(prefix);
			writer.print("  mAvailIndices: ");
			writer.println(Arrays.toString(mAvailIndices.toArray()));
		}
	}

	public void enqueueAction(Runnable action, boolean allowStateLoss) {
		if (!allowStateLoss) {
			checkStateLoss();
		}
		synchronized(this) {
			try {
				if (mDestroyed || mActivity == null) {
					throw new IllegalStateException("Activity has been destroyed");
				} else {
					if (mPendingActions == null) {
						mPendingActions = new ArrayList();
					}
					mPendingActions.add(action);
					if (mPendingActions.size() == 1) {
						mActivity.mHandler.removeCallbacks(mExecCommit);
						mActivity.mHandler.post(mExecCommit);
					}
				}
			} catch (Throwable th) {
				return th;
			}
		}
	}

	public boolean execPendingActions() {
		if (mExecutingActions) {
			throw new IllegalStateException("Recursive entry to executePendingTransactions");
		} else if (Looper.myLooper() != mActivity.mHandler.getLooper()) {
			throw new IllegalStateException("Must be called from main thread of process");
		} else {
			boolean didSomething = HONEYCOMB;
			while (true) {
				synchronized(this) {
					try {
						int i;
						if (mPendingActions == null || mPendingActions.size() == 0) {
							if (mHavePendingDeferredStart) {
								boolean loadersRunning = HONEYCOMB;
								i = 0;
								while (i < mActive.size()) {
									Fragment f = (Fragment) mActive.get(i);
									if (f == null || f.mLoaderManager == null) {
										i++;
									} else {
										loadersRunning |= f.mLoaderManager.hasRunningLoaders();
										i++;
									}
								}
								if (!loadersRunning) {
									mHavePendingDeferredStart = false;
									startPendingDeferredFragments();
								}
							}
							return didSomething;
						} else {
							int numActions = mPendingActions.size();
							if (mTmpActions == null || mTmpActions.length < numActions) {
								mTmpActions = new Runnable[numActions];
							} else {
								mPendingActions.toArray(mTmpActions);
								mPendingActions.clear();
								mActivity.mHandler.removeCallbacks(mExecCommit);
							}
							mPendingActions.toArray(mTmpActions);
							mPendingActions.clear();
							mActivity.mHandler.removeCallbacks(mExecCommit);
							mExecutingActions = true;
							i = 0;
							while (i < numActions) {
								mTmpActions[i].run();
								mTmpActions[i] = null;
								i++;
							}
							mExecutingActions = false;
							didSomething = true;
						}
					} catch (Throwable th) {
						while (true) {
							return th;
						}
					}
				}
			}
		}
	}

	public boolean executePendingTransactions() {
		return execPendingActions();
	}

	/* JADX WARNING: inconsistent code */
	/*
	public android.support.v4.app.Fragment findFragmentById(int r4_id) {
		r3_this = this;
		r2 = r3.mAdded;
		if (r2 == 0) goto L_0x0020;
	L_0x0004:
		r2 = r3.mAdded;
		r2 = r2.size();
		r1 = r2 + -1;
	L_0x000c:
		if (r1_i < 0) goto L_0x0020;
	L_0x000e:
		r2 = r3.mAdded;
		r0 = r2.get(r1_i);
		r0 = (android.support.v4.app.Fragment) r0;
		if (r0_f == 0) goto L_0x001d;
	L_0x0018:
		r2 = r0_f.mFragmentId;
		if (r2 != r4_id) goto L_0x001d;
	L_0x001c:
		return r0_f;
	L_0x001d:
		r1_i++;
		goto L_0x000c;
	L_0x0020:
		r2 = r3.mActive;
		if (r2 == 0) goto L_0x003f;
	L_0x0024:
		r2 = r3.mActive;
		r2 = r2.size();
		r1_i = r2 + -1;
	L_0x002c:
		if (r1_i < 0) goto L_0x003f;
	L_0x002e:
		r2 = r3.mActive;
		r0_f = r2.get(r1_i);
		r0_f = (android.support.v4.app.Fragment) r0_f;
		if (r0_f == 0) goto L_0x003c;
	L_0x0038:
		r2 = r0_f.mFragmentId;
		if (r2 == r4_id) goto L_0x001c;
	L_0x003c:
		r1_i++;
		goto L_0x002c;
	L_0x003f:
		r0_f = 0;
		goto L_0x001c;
	}
	*/
	public Fragment findFragmentById(int id) {
		int i;
		Fragment f;
		if (mAdded != null) {
			i = mAdded.size() - 1;
			while (i >= 0) {
				f = (Fragment) mAdded.get(i);
				if (f == null || f.mFragmentId != id) {
					i--;
				}
			}
		}
		if (mActive != null) {
			i = mActive.size() - 1;
			while (i >= 0) {
				f = mActive.get(i);
				if (f == null || f.mFragmentId != id) {
					i--;
				}
			}
		}
		return null;
	}

	/* JADX WARNING: inconsistent code */
	/*
	public android.support.v4.app.Fragment findFragmentByTag(java.lang.String r4_tag) {
		r3_this = this;
		r2 = r3.mAdded;
		if (r2 == 0) goto L_0x0026;
	L_0x0004:
		if (r4_tag == 0) goto L_0x0026;
	L_0x0006:
		r2 = r3.mAdded;
		r2 = r2.size();
		r1 = r2 + -1;
	L_0x000e:
		if (r1_i < 0) goto L_0x0026;
	L_0x0010:
		r2 = r3.mAdded;
		r0 = r2.get(r1_i);
		r0 = (android.support.v4.app.Fragment) r0;
		if (r0_f == 0) goto L_0x0023;
	L_0x001a:
		r2 = r0_f.mTag;
		r2 = r4_tag.equals(r2);
		if (r2 == 0) goto L_0x0023;
	L_0x0022:
		return r0_f;
	L_0x0023:
		r1_i++;
		goto L_0x000e;
	L_0x0026:
		r2 = r3.mActive;
		if (r2 == 0) goto L_0x004b;
	L_0x002a:
		if (r4_tag == 0) goto L_0x004b;
	L_0x002c:
		r2 = r3.mActive;
		r2 = r2.size();
		r1_i = r2 + -1;
	L_0x0034:
		if (r1_i < 0) goto L_0x004b;
	L_0x0036:
		r2 = r3.mActive;
		r0_f = r2.get(r1_i);
		r0_f = (android.support.v4.app.Fragment) r0_f;
		if (r0_f == 0) goto L_0x0048;
	L_0x0040:
		r2 = r0_f.mTag;
		r2 = r4_tag.equals(r2);
		if (r2 != 0) goto L_0x0022;
	L_0x0048:
		r1_i++;
		goto L_0x0034;
	L_0x004b:
		r0_f = 0;
		goto L_0x0022;
	}
	*/
	public Fragment findFragmentByTag(String tag) {
		int i;
		Fragment f;
		if (mAdded == null || tag == null) {
			if (mActive == null || tag == null) {
				return null;
			} else {
				i = mActive.size() - 1;
				while (i >= 0) {
					f = mActive.get(i);
					if (f == null || !tag.equals(f.mTag)) {
						i--;
					}
				}
				return null;
			}
		} else {
			i = mAdded.size() - 1;
			while (i >= 0) {
				f = (Fragment) mAdded.get(i);
				if (f == null || !tag.equals(f.mTag)) {
					i--;
				}
			}
			if (mActive == null || tag == null) {
				return null;
			} else {
				i = mActive.size() - 1;
				while (i >= 0) {
					f = mActive.get(i);
					if (f == null || !tag.equals(f.mTag)) {
						i--;
					}
				}
				return null;
			}
		}
	}

	public Fragment findFragmentByWho(String who) {
		if (mActive == null || who == null) {
			return null;
		} else {
			int i = mActive.size() - 1;
			while (i >= 0) {
				Fragment f = (Fragment) mActive.get(i);
				if (f != null) {
					f = f.findFragmentByWho(who);
					if (f != null) {
						return f;
					}
				}
				i--;
			}
			return null;
		}
	}

	public void freeBackStackIndex(int index) {
		synchronized(this) {
			mBackStackIndices.set(index, null);
			if (mAvailBackStackIndices == null) {
				mAvailBackStackIndices = new ArrayList();
			}
			if (DEBUG) {
				Log.v(TAG, "Freeing back stack index " + index);
			}
			mAvailBackStackIndices.add(Integer.valueOf(index));
		}
	}

	public BackStackEntry getBackStackEntryAt(int index) {
		return (BackStackEntry) mBackStack.get(index);
	}

	public int getBackStackEntryCount() {
		if (mBackStack != null) {
			return mBackStack.size();
		} else {
			return 0;
		}
	}

	public Fragment getFragment(Bundle bundle, String key) {
		int index = bundle.getInt(key, -1);
		if (index == -1) {
			return null;
		} else {
			if (index >= mActive.size()) {
				throwException(new IllegalStateException("Fragment no longer exists for key " + key + ": index " + index));
			}
			Fragment f = (Fragment) mActive.get(index);
			if (f == null) {
				throwException(new IllegalStateException("Fragment no longer exists for key " + key + ": index " + index));
				return f;
			} else {
				return f;
			}
		}
	}

	public List<Fragment> getFragments() {
		return mActive;
	}

	public void hideFragment(Fragment fragment, int transition, int transitionStyle) {
		if (DEBUG) {
			Log.v(TAG, "hide: " + fragment);
		}
		if (!fragment.mHidden) {
			fragment.mHidden = true;
			if (fragment.mView != null) {
				Animation anim = loadAnimation(fragment, transition, HONEYCOMB, transitionStyle);
				if (anim != null) {
					fragment.mView.startAnimation(anim);
				}
				fragment.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
			}
			if (!fragment.mAdded || !fragment.mHasMenu || !fragment.mMenuVisible) {
				fragment.onHiddenChanged(true);
			} else {
				mNeedMenuInvalidate = true;
				fragment.onHiddenChanged(true);
			}
		}
	}

	public boolean isDestroyed() {
		return mDestroyed;
	}

	/* JADX WARNING: inconsistent code */
	/*
	android.view.animation.Animation loadAnimation(android.support.v4.app.Fragment r10_fragment, int r11_transit, boolean r12_enter, int r13_transitionStyle) {
		r9_this = this;
		r8 = 1064933786; // 0x3f79999a float:0.975 double:5.26147199E-315;
		r3 = 0;
		r7 = 0;
		r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
		r4 = r10_fragment.mNextAnim;
		r1 = r10_fragment.onCreateAnimation(r11_transit, r12_enter, r4);
		if (r1_animObj == 0) goto L_0x0010;
	L_0x000f:
		return r1_animObj;
	L_0x0010:
		r4 = r10_fragment.mNextAnim;
		if (r4 == 0) goto L_0x0020;
	L_0x0014:
		r4 = r9.mActivity;
		r5 = r10_fragment.mNextAnim;
		r0 = android.view.animation.AnimationUtils.loadAnimation(r4, r5);
		if (r0_anim == 0) goto L_0x0020;
	L_0x001e:
		r1_animObj = r0_anim;
		goto L_0x000f;
	L_0x0020:
		if (r11_transit != 0) goto L_0x0024;
	L_0x0022:
		r1_animObj = r3;
		goto L_0x000f;
	L_0x0024:
		r2 = transitToStyleIndex(r11_transit, r12_enter);
		if (r2_styleIndex >= 0) goto L_0x002c;
	L_0x002a:
		r1_animObj = r3;
		goto L_0x000f;
	L_0x002c:
		switch(r2_styleIndex) {
			case 1: goto L_0x0049;
			case 2: goto L_0x0052;
			case 3: goto L_0x0059;
			case 4: goto L_0x0060;
			case 5: goto L_0x006a;
			case 6: goto L_0x0071;
			default: goto L_0x002f;
		}
	L_0x002f:
		if (r13_transitionStyle != 0) goto L_0x0045;
	L_0x0031:
		r4 = r9.mActivity;
		r4 = r4.getWindow();
		if (r4 == 0) goto L_0x0045;
	L_0x0039:
		r4 = r9.mActivity;
		r4 = r4.getWindow();
		r4 = r4.getAttributes();
		r13_transitionStyle = r4.windowAnimations;
	L_0x0045:
		if (r13_transitionStyle != 0) goto L_0x0078;
	L_0x0047:
		r1_animObj = r3;
		goto L_0x000f;
	L_0x0049:
		r3 = r9.mActivity;
		r4 = 1066401792; // 0x3f900000 float:1.125 double:5.2687249E-315;
		r1_animObj = makeOpenCloseAnimation(r3, r4, r6, r7, r6);
		goto L_0x000f;
	L_0x0052:
		r3 = r9.mActivity;
		r1_animObj = makeOpenCloseAnimation(r3, r6, r8, r6, r7);
		goto L_0x000f;
	L_0x0059:
		r3 = r9.mActivity;
		r1_animObj = makeOpenCloseAnimation(r3, r8, r6, r7, r6);
		goto L_0x000f;
	L_0x0060:
		r3 = r9.mActivity;
		r4 = 1065982362; // 0x3f89999a float:1.075 double:5.26665264E-315;
		r1_animObj = makeOpenCloseAnimation(r3, r6, r4, r6, r7);
		goto L_0x000f;
	L_0x006a:
		r3 = r9.mActivity;
		r1_animObj = makeFadeAnimation(r3, r7, r6);
		goto L_0x000f;
	L_0x0071:
		r3 = r9.mActivity;
		r1_animObj = makeFadeAnimation(r3, r6, r7);
		goto L_0x000f;
	L_0x0078:
		r1_animObj = r3;
		goto L_0x000f;
	}
	*/
	Animation loadAnimation(Fragment fragment, int transit, boolean enter, int transitionStyle) {
		Animation animObj = fragment.onCreateAnimation(transit, enter, fragment.mNextAnim);
		if (animObj != null) {
			return animObj;
		} else {
			if (fragment.mNextAnim != 0) {
				Animation anim = AnimationUtils.loadAnimation(mActivity, fragment.mNextAnim);
				if (anim != null) {
					return anim;
				}
			}
			if (transit == 0) {
				return null;
			} else {
				int styleIndex = transitToStyleIndex(transit, enter);
				if (styleIndex < 0) {
					return null;
				} else {
					switch(styleIndex) {
					case ANIM_STYLE_OPEN_ENTER:
						return makeOpenCloseAnimation(mActivity, 1.125f, 1.0f, AutoScrollHelper.RELATIVE_UNSPECIFIED, 1.0f);
					case ANIM_STYLE_OPEN_EXIT:
						return makeOpenCloseAnimation(mActivity, 1.0f, 0.975f, 1.0f, AutoScrollHelper.RELATIVE_UNSPECIFIED);
					case ANIM_STYLE_CLOSE_ENTER:
						return makeOpenCloseAnimation(mActivity, 0.975f, 1.0f, AutoScrollHelper.RELATIVE_UNSPECIFIED, 1.0f);
					case ANIM_STYLE_CLOSE_EXIT:
						return makeOpenCloseAnimation(mActivity, 1.0f, 1.075f, 1.0f, AutoScrollHelper.RELATIVE_UNSPECIFIED);
					case ANIM_STYLE_FADE_ENTER:
						return makeFadeAnimation(mActivity, AutoScrollHelper.RELATIVE_UNSPECIFIED, 1.0f);
					case ANIM_STYLE_FADE_EXIT:
						return makeFadeAnimation(mActivity, 1.0f, AutoScrollHelper.RELATIVE_UNSPECIFIED);
					}
					if (transitionStyle != 0 || mActivity.getWindow() == null) {
						return null;
					} else {
						transitionStyle = mActivity.getWindow().getAttributes().windowAnimations;
						return null;
					}
				}
			}
		}
	}

	void makeActive(Fragment f) {
		if (f.mIndex >= 0) {
		} else {
			if (mAvailIndices == null || mAvailIndices.size() <= 0) {
				if (mActive == null) {
					mActive = new ArrayList();
				}
				f.setIndex(mActive.size(), mParent);
				mActive.add(f);
			} else {
				f.setIndex(((Integer) mAvailIndices.remove(mAvailIndices.size() - 1)).intValue(), mParent);
				mActive.set(f.mIndex, f);
			}
			if (DEBUG) {
				Log.v(TAG, "Allocated fragment index " + f);
			}
		}
	}

	void makeInactive(Fragment f) {
		if (f.mIndex < 0) {
		} else {
			if (DEBUG) {
				Log.v(TAG, "Freeing fragment index " + f);
			}
			mActive.set(f.mIndex, null);
			if (mAvailIndices == null) {
				mAvailIndices = new ArrayList();
			}
			mAvailIndices.add(Integer.valueOf(f.mIndex));
			mActivity.invalidateSupportFragment(f.mWho);
			f.initState();
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	void moveToState(int r9_newState, int r10_transit, int r11_transitStyle, boolean r12_always) {
		r8_this = this;
		r5 = 0;
		r0 = r8.mActivity;
		if (r0 != 0) goto L_0x000f;
	L_0x0005:
		if (r9_newState == 0) goto L_0x000f;
	L_0x0007:
		r0 = new java.lang.IllegalStateException;
		r2 = "No activity";
		r0.<init>(r2);
		throw r0;
	L_0x000f:
		if (r12_always != 0) goto L_0x0016;
	L_0x0011:
		r0 = r8.mCurState;
		if (r0 != r9_newState) goto L_0x0016;
	L_0x0015:
		return;
	L_0x0016:
		r8.mCurState = r9_newState;
		r0 = r8.mActive;
		if (r0 == 0) goto L_0x0015;
	L_0x001c:
		r7 = 0;
		r6 = 0;
	L_0x001e:
		r0 = r8.mActive;
		r0 = r0.size();
		if (r6_i >= r0) goto L_0x0045;
	L_0x0026:
		r0 = r8.mActive;
		r1 = r0.get(r6_i);
		r1 = (android.support.v4.app.Fragment) r1;
		if (r1_f == 0) goto L_0x0042;
	L_0x0030:
		r0 = r8;
		r2 = r9_newState;
		r3 = r10_transit;
		r4 = r11_transitStyle;
		r0.moveToState(r1_f, r2, r3, r4, r5);
		r0 = r1_f.mLoaderManager;
		if (r0 == 0) goto L_0x0042;
	L_0x003b:
		r0 = r1_f.mLoaderManager;
		r0 = r0.hasRunningLoaders();
		r7_loadersRunning |= r0;
	L_0x0042:
		r6_i++;
		goto L_0x001e;
	L_0x0045:
		if (r7_loadersRunning != 0) goto L_0x004a;
	L_0x0047:
		r8.startPendingDeferredFragments();
	L_0x004a:
		r0 = r8.mNeedMenuInvalidate;
		if (r0 == 0) goto L_0x0015;
	L_0x004e:
		r0 = r8.mActivity;
		if (r0 == 0) goto L_0x0015;
	L_0x0052:
		r0 = r8.mCurState;
		r2 = 5;
		if (r0 != r2) goto L_0x0015;
	L_0x0057:
		r0 = r8.mActivity;
		r0.supportInvalidateOptionsMenu();
		r8.mNeedMenuInvalidate = r5;
		goto L_0x0015;
	}
	*/
	void moveToState(int newState, int transit, int transitStyle, boolean always) {
		if (mActivity != null || newState == 0) {
			mCurState = newState;
			if (mActive != null) {
				boolean loadersRunning = HONEYCOMB;
				int i = 0;
				while (i < mActive.size()) {
					Fragment f = (Fragment) mActive.get(i);
					if (f != null) {
						moveToState(f, newState, transit, transitStyle, HONEYCOMB);
						if (f.mLoaderManager != null) {
							loadersRunning |= f.mLoaderManager.hasRunningLoaders();
						}
					}
					i++;
				}
				if (!loadersRunning) {
					startPendingDeferredFragments();
				}
				if (!mNeedMenuInvalidate || mActivity == null || mCurState != 5) {
				} else {
					mActivity.supportInvalidateOptionsMenu();
					mNeedMenuInvalidate = false;
				}
			}
		} else {
			throw new IllegalStateException("No activity");
		}
	}

	void moveToState(int newState, boolean always) {
		moveToState(newState, 0, 0, always);
	}

	void moveToState(Fragment f) {
		moveToState(f, mCurState, 0, 0, false);
	}

	/* JADX WARNING: inconsistent code */
	/*
	void moveToState(android.support.v4.app.Fragment r11_f, int r12_newState, int r13_transit, int r14_transitionStyle, boolean r15_keepActive) {
		r10_this = this;
		r0 = r11_f.mAdded;
		if (r0 == 0) goto L_0x0008;
	L_0x0004:
		r0 = r11_f.mDetached;
		if (r0 == 0) goto L_0x000c;
	L_0x0008:
		r0 = 1;
		if (r12_newState <= r0) goto L_0x000c;
	L_0x000b:
		r12_newState = 1;
	L_0x000c:
		r0 = r11_f.mRemoving;
		if (r0 == 0) goto L_0x0016;
	L_0x0010:
		r0 = r11_f.mState;
		if (r12_newState <= r0) goto L_0x0016;
	L_0x0014:
		r12_newState = r11_f.mState;
	L_0x0016:
		r0 = r11_f.mDeferStart;
		if (r0 == 0) goto L_0x0023;
	L_0x001a:
		r0 = r11_f.mState;
		r1 = 4;
		if (r0 >= r1) goto L_0x0023;
	L_0x001f:
		r0 = 3;
		if (r12_newState <= r0) goto L_0x0023;
	L_0x0022:
		r12_newState = 3;
	L_0x0023:
		r0 = r11_f.mState;
		if (r0 >= r12_newState) goto L_0x0253;
	L_0x0027:
		r0 = r11_f.mFromLayout;
		if (r0 == 0) goto L_0x0030;
	L_0x002b:
		r0 = r11_f.mInLayout;
		if (r0 != 0) goto L_0x0030;
	L_0x002f:
		return;
	L_0x0030:
		r0 = r11_f.mAnimatingAway;
		if (r0 == 0) goto L_0x0041;
	L_0x0034:
		r0 = 0;
		r11_f.mAnimatingAway = r0;
		r2 = r11_f.mStateAfterAnimating;
		r3 = 0;
		r4 = 0;
		r5 = 1;
		r0 = r10;
		r1 = r11_f;
		r0.moveToState(r1, r2, r3, r4, r5);
	L_0x0041:
		r0 = r11_f.mState;
		switch(r0) {
			case 0: goto L_0x0049;
			case 1: goto L_0x012e;
			case 2: goto L_0x01fb;
			case 3: goto L_0x01fb;
			case 4: goto L_0x021d;
			default: goto L_0x0046;
		}
	L_0x0046:
		r11_f.mState = r12_newState;
		goto L_0x002f;
	L_0x0049:
		r0 = DEBUG;
		if (r0 == 0) goto L_0x0065;
	L_0x004d:
		r0 = "FragmentManager";
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "moveto CREATED: ";
		r1 = r1.append(r2);
		r1 = r1.append(r11_f);
		r1 = r1.toString();
		android.util.Log.v(r0, r1);
	L_0x0065:
		r0 = r11_f.mSavedFragmentState;
		if (r0 == 0) goto L_0x00a2;
	L_0x0069:
		r0 = r11_f.mSavedFragmentState;
		r1 = "android:view_state";
		r0 = r0.getSparseParcelableArray(r1);
		r11_f.mSavedViewState = r0;
		r0 = r11_f.mSavedFragmentState;
		r1 = "android:target_state";
		r0 = r10.getFragment(r0, r1);
		r11_f.mTarget = r0;
		r0 = r11_f.mTarget;
		if (r0 == 0) goto L_0x008c;
	L_0x0081:
		r0 = r11_f.mSavedFragmentState;
		r1 = "android:target_req_state";
		r2 = 0;
		r0 = r0.getInt(r1, r2);
		r11_f.mTargetRequestCode = r0;
	L_0x008c:
		r0 = r11_f.mSavedFragmentState;
		r1 = "android:user_visible_hint";
		r2 = 1;
		r0 = r0.getBoolean(r1, r2);
		r11_f.mUserVisibleHint = r0;
		r0 = r11_f.mUserVisibleHint;
		if (r0 != 0) goto L_0x00a2;
	L_0x009b:
		r0 = 1;
		r11_f.mDeferStart = r0;
		r0 = 3;
		if (r12_newState <= r0) goto L_0x00a2;
	L_0x00a1:
		r12_newState = 3;
	L_0x00a2:
		r0 = r10.mActivity;
		r11_f.mActivity = r0;
		r0 = r10.mParent;
		r11_f.mParentFragment = r0;
		r0 = r10.mParent;
		if (r0 == 0) goto L_0x00df;
	L_0x00ae:
		r0 = r10.mParent;
		r0 = r0.mChildFragmentManager;
	L_0x00b2:
		r11_f.mFragmentManager = r0;
		r0 = 0;
		r11_f.mCalled = r0;
		r0 = r10.mActivity;
		r11_f.onAttach(r0);
		r0 = r11_f.mCalled;
		if (r0 != 0) goto L_0x00e4;
	L_0x00c0:
		r0 = new android.support.v4.app.SuperNotCalledException;
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "Fragment ";
		r1 = r1.append(r2);
		r1 = r1.append(r11_f);
		r2 = " did not call through to super.onAttach()";
		r1 = r1.append(r2);
		r1 = r1.toString();
		r0.<init>(r1);
		throw r0;
	L_0x00df:
		r0 = r10.mActivity;
		r0 = r0.mFragments;
		goto L_0x00b2;
	L_0x00e4:
		r0 = r11_f.mParentFragment;
		if (r0 != 0) goto L_0x00ed;
	L_0x00e8:
		r0 = r10.mActivity;
		r0.onAttachFragment(r11_f);
	L_0x00ed:
		r0 = r11_f.mRetaining;
		if (r0 != 0) goto L_0x00f6;
	L_0x00f1:
		r0 = r11_f.mSavedFragmentState;
		r11_f.performCreate(r0);
	L_0x00f6:
		r0 = 0;
		r11_f.mRetaining = r0;
		r0 = r11_f.mFromLayout;
		if (r0 == 0) goto L_0x012e;
	L_0x00fd:
		r0 = r11_f.mSavedFragmentState;
		r0 = r11_f.getLayoutInflater(r0);
		r1 = 0;
		r2 = r11_f.mSavedFragmentState;
		r0 = r11_f.performCreateView(r0, r1, r2);
		r11_f.mView = r0;
		r0 = r11_f.mView;
		if (r0 == 0) goto L_0x024a;
	L_0x0110:
		r0 = r11_f.mView;
		r11_f.mInnerView = r0;
		r0 = r11_f.mView;
		r0 = android.support.v4.app.NoSaveStateFrameLayout.wrap(r0);
		r11_f.mView = r0;
		r0 = r11_f.mHidden;
		if (r0 == 0) goto L_0x0127;
	L_0x0120:
		r0 = r11_f.mView;
		r1 = 8;
		r0.setVisibility(r1);
	L_0x0127:
		r0 = r11_f.mView;
		r1 = r11_f.mSavedFragmentState;
		r11_f.onViewCreated(r0, r1);
	L_0x012e:
		r0 = 1;
		if (r12_newState <= r0) goto L_0x01fb;
	L_0x0131:
		r0 = DEBUG;
		if (r0 == 0) goto L_0x014d;
	L_0x0135:
		r0 = "FragmentManager";
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "moveto ACTIVITY_CREATED: ";
		r1 = r1.append(r2);
		r1 = r1.append(r11_f);
		r1 = r1.toString();
		android.util.Log.v(r0, r1);
	L_0x014d:
		r0 = r11_f.mFromLayout;
		if (r0 != 0) goto L_0x01ea;
	L_0x0151:
		r7 = 0;
		r0 = r11_f.mContainerId;
		if (r0 == 0) goto L_0x01a5;
	L_0x0156:
		r0 = r10.mContainer;
		r1 = r11_f.mContainerId;
		r7_container = r0.findViewById(r1);
		r7_container = (android.view.ViewGroup) r7_container;
		if (r7_container != 0) goto L_0x01a5;
	L_0x0162:
		r0 = r11_f.mRestored;
		if (r0 != 0) goto L_0x01a5;
	L_0x0166:
		r0 = new java.lang.IllegalArgumentException;
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "No view found for id 0x";
		r1 = r1.append(r2);
		r2 = r11_f.mContainerId;
		r2 = java.lang.Integer.toHexString(r2);
		r1 = r1.append(r2);
		r2 = " (";
		r1 = r1.append(r2);
		r2 = r11_f.getResources();
		r3 = r11_f.mContainerId;
		r2 = r2.getResourceName(r3);
		r1 = r1.append(r2);
		r2 = ") for fragment ";
		r1 = r1.append(r2);
		r1 = r1.append(r11_f);
		r1 = r1.toString();
		r0.<init>(r1);
		r10.throwException(r0);
	L_0x01a5:
		r11_f.mContainer = r7_container;
		r0 = r11_f.mSavedFragmentState;
		r0 = r11_f.getLayoutInflater(r0);
		r1 = r11_f.mSavedFragmentState;
		r0 = r11_f.performCreateView(r0, r7_container, r1);
		r11_f.mView = r0;
		r0 = r11_f.mView;
		if (r0 == 0) goto L_0x024f;
	L_0x01b9:
		r0 = r11_f.mView;
		r11_f.mInnerView = r0;
		r0 = r11_f.mView;
		r0 = android.support.v4.app.NoSaveStateFrameLayout.wrap(r0);
		r11_f.mView = r0;
		if (r7_container == 0) goto L_0x01d8;
	L_0x01c7:
		r0 = 1;
		r6 = r10.loadAnimation(r11_f, r13_transit, r0, r14_transitionStyle);
		if (r6_anim == 0) goto L_0x01d3;
	L_0x01ce:
		r0 = r11_f.mView;
		r0.startAnimation(r6_anim);
	L_0x01d3:
		r0 = r11_f.mView;
		r7_container.addView(r0);
	L_0x01d8:
		r0 = r11_f.mHidden;
		if (r0 == 0) goto L_0x01e3;
	L_0x01dc:
		r0 = r11_f.mView;
		r1 = 8;
		r0.setVisibility(r1);
	L_0x01e3:
		r0 = r11_f.mView;
		r1 = r11_f.mSavedFragmentState;
		r11_f.onViewCreated(r0, r1);
	L_0x01ea:
		r0 = r11_f.mSavedFragmentState;
		r11_f.performActivityCreated(r0);
		r0 = r11_f.mView;
		if (r0 == 0) goto L_0x01f8;
	L_0x01f3:
		r0 = r11_f.mSavedFragmentState;
		r11_f.restoreViewState(r0);
	L_0x01f8:
		r0 = 0;
		r11_f.mSavedFragmentState = r0;
	L_0x01fb:
		r0 = 3;
		if (r12_newState <= r0) goto L_0x021d;
	L_0x01fe:
		r0 = DEBUG;
		if (r0 == 0) goto L_0x021a;
	L_0x0202:
		r0 = "FragmentManager";
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "moveto STARTED: ";
		r1 = r1.append(r2);
		r1 = r1.append(r11_f);
		r1 = r1.toString();
		android.util.Log.v(r0, r1);
	L_0x021a:
		r11_f.performStart();
	L_0x021d:
		r0 = 4;
		if (r12_newState <= r0) goto L_0x0046;
	L_0x0220:
		r0 = DEBUG;
		if (r0 == 0) goto L_0x023c;
	L_0x0224:
		r0 = "FragmentManager";
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "moveto RESUMED: ";
		r1 = r1.append(r2);
		r1 = r1.append(r11_f);
		r1 = r1.toString();
		android.util.Log.v(r0, r1);
	L_0x023c:
		r0 = 1;
		r11_f.mResumed = r0;
		r11_f.performResume();
		r0 = 0;
		r11_f.mSavedFragmentState = r0;
		r0 = 0;
		r11_f.mSavedViewState = r0;
		goto L_0x0046;
	L_0x024a:
		r0 = 0;
		r11_f.mInnerView = r0;
		goto L_0x012e;
	L_0x024f:
		r0 = 0;
		r11_f.mInnerView = r0;
		goto L_0x01ea;
	L_0x0253:
		r0 = r11_f.mState;
		if (r0 <= r12_newState) goto L_0x0046;
	L_0x0257:
		r0 = r11_f.mState;
		switch(r0) {
			case 1: goto L_0x025e;
			case 2: goto L_0x02e3;
			case 3: goto L_0x02c1;
			case 4: goto L_0x029f;
			case 5: goto L_0x027a;
			default: goto L_0x025c;
		}
	L_0x025c:
		goto L_0x0046;
	L_0x025e:
		r0 = 1;
		if (r12_newState >= r0) goto L_0x0046;
	L_0x0261:
		r0 = r10.mDestroyed;
		if (r0 == 0) goto L_0x0271;
	L_0x0265:
		r0 = r11_f.mAnimatingAway;
		if (r0 == 0) goto L_0x0271;
	L_0x0269:
		r9 = r11_f.mAnimatingAway;
		r0 = 0;
		r11_f.mAnimatingAway = r0;
		r9_v.clearAnimation();
	L_0x0271:
		r0 = r11_f.mAnimatingAway;
		if (r0 == 0) goto L_0x0356;
	L_0x0275:
		r11_f.mStateAfterAnimating = r12_newState;
		r12_newState = 1;
		goto L_0x0046;
	L_0x027a:
		r0 = 5;
		if (r12_newState >= r0) goto L_0x029f;
	L_0x027d:
		r0 = DEBUG;
		if (r0 == 0) goto L_0x0299;
	L_0x0281:
		r0 = "FragmentManager";
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "movefrom RESUMED: ";
		r1 = r1.append(r2);
		r1 = r1.append(r11_f);
		r1 = r1.toString();
		android.util.Log.v(r0, r1);
	L_0x0299:
		r11_f.performPause();
		r0 = 0;
		r11_f.mResumed = r0;
	L_0x029f:
		r0 = 4;
		if (r12_newState >= r0) goto L_0x02c1;
	L_0x02a2:
		r0 = DEBUG;
		if (r0 == 0) goto L_0x02be;
	L_0x02a6:
		r0 = "FragmentManager";
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "movefrom STARTED: ";
		r1 = r1.append(r2);
		r1 = r1.append(r11_f);
		r1 = r1.toString();
		android.util.Log.v(r0, r1);
	L_0x02be:
		r11_f.performStop();
	L_0x02c1:
		r0 = 3;
		if (r12_newState >= r0) goto L_0x02e3;
	L_0x02c4:
		r0 = DEBUG;
		if (r0 == 0) goto L_0x02e0;
	L_0x02c8:
		r0 = "FragmentManager";
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "movefrom STOPPED: ";
		r1 = r1.append(r2);
		r1 = r1.append(r11_f);
		r1 = r1.toString();
		android.util.Log.v(r0, r1);
	L_0x02e0:
		r11_f.performReallyStop();
	L_0x02e3:
		r0 = 2;
		if (r12_newState >= r0) goto L_0x025e;
	L_0x02e6:
		r0 = DEBUG;
		if (r0 == 0) goto L_0x0302;
	L_0x02ea:
		r0 = "FragmentManager";
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "movefrom ACTIVITY_CREATED: ";
		r1 = r1.append(r2);
		r1 = r1.append(r11_f);
		r1 = r1.toString();
		android.util.Log.v(r0, r1);
	L_0x0302:
		r0 = r11_f.mView;
		if (r0 == 0) goto L_0x0315;
	L_0x0306:
		r0 = r10.mActivity;
		r0 = r0.isFinishing();
		if (r0 != 0) goto L_0x0315;
	L_0x030e:
		r0 = r11_f.mSavedViewState;
		if (r0 != 0) goto L_0x0315;
	L_0x0312:
		r10.saveFragmentViewState(r11_f);
	L_0x0315:
		r11_f.performDestroyView();
		r0 = r11_f.mView;
		if (r0 == 0) goto L_0x034b;
	L_0x031c:
		r0 = r11_f.mContainer;
		if (r0 == 0) goto L_0x034b;
	L_0x0320:
		r6_anim = 0;
		r0 = r10.mCurState;
		if (r0 <= 0) goto L_0x032e;
	L_0x0325:
		r0 = r10.mDestroyed;
		if (r0 != 0) goto L_0x032e;
	L_0x0329:
		r0 = 0;
		r6_anim = r10.loadAnimation(r11_f, r13_transit, r0, r14_transitionStyle);
	L_0x032e:
		if (r6_anim == 0) goto L_0x0344;
	L_0x0330:
		r8 = r11_f;
		r0 = r11_f.mView;
		r11_f.mAnimatingAway = r0;
		r11_f.mStateAfterAnimating = r12_newState;
		r0 = new android.support.v4.app.FragmentManagerImpl$5;
		r0.<init>(r10, r8_fragment);
		r6_anim.setAnimationListener(r0);
		r0 = r11_f.mView;
		r0.startAnimation(r6_anim);
	L_0x0344:
		r0 = r11_f.mContainer;
		r1 = r11_f.mView;
		r0.removeView(r1);
	L_0x034b:
		r0 = 0;
		r11_f.mContainer = r0;
		r0 = 0;
		r11_f.mView = r0;
		r0 = 0;
		r11_f.mInnerView = r0;
		goto L_0x025e;
	L_0x0356:
		r0 = DEBUG;
		if (r0 == 0) goto L_0x0372;
	L_0x035a:
		r0 = "FragmentManager";
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "movefrom CREATED: ";
		r1 = r1.append(r2);
		r1 = r1.append(r11_f);
		r1 = r1.toString();
		android.util.Log.v(r0, r1);
	L_0x0372:
		r0 = r11_f.mRetaining;
		if (r0 != 0) goto L_0x0379;
	L_0x0376:
		r11_f.performDestroy();
	L_0x0379:
		r0 = 0;
		r11_f.mCalled = r0;
		r11_f.onDetach();
		r0 = r11_f.mCalled;
		if (r0 != 0) goto L_0x03a2;
	L_0x0383:
		r0 = new android.support.v4.app.SuperNotCalledException;
		r1 = new java.lang.StringBuilder;
		r1.<init>();
		r2 = "Fragment ";
		r1 = r1.append(r2);
		r1 = r1.append(r11_f);
		r2 = " did not call through to super.onDetach()";
		r1 = r1.append(r2);
		r1 = r1.toString();
		r0.<init>(r1);
		throw r0;
	L_0x03a2:
		if (r15_keepActive != 0) goto L_0x0046;
	L_0x03a4:
		r0 = r11_f.mRetaining;
		if (r0 != 0) goto L_0x03ad;
	L_0x03a8:
		r10.makeInactive(r11_f);
		goto L_0x0046;
	L_0x03ad:
		r0 = 0;
		r11_f.mActivity = r0;
		r0 = 0;
		r11_f.mParentFragment = r0;
		r0 = 0;
		r11_f.mFragmentManager = r0;
		r0 = 0;
		r11_f.mChildFragmentManager = r0;
		goto L_0x0046;
	}
	*/
	void moveToState(Fragment f, int newState, int transit, int transitionStyle, boolean keepActive) {
		if ((!f.mAdded || f.mDetached) && newState > 1) {
			newState = ANIM_STYLE_OPEN_ENTER;
		}
		FragmentManagerImpl r0_FragmentManagerImpl;
		if (!f.mRemoving || newState <= f.mState) {
			if (!f.mDeferStart || f.mState >= 4 || newState <= 3) {
				if (f.mState >= newState) {
					if (!f.mFromLayout || f.mInLayout) {
						if (f.mAnimatingAway == null) {
							f.mAnimatingAway = null;
							moveToState(f, f.mStateAfterAnimating, 0, 0, true);
						}
						switch(f.mState) {
						case WearableExtender.SIZE_DEFAULT:
							if (!DEBUG) {
								Log.v(TAG, "moveto CREATED: " + f);
							}
							if (f.mSavedFragmentState != null) {
								f.mSavedViewState = f.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
								f.mTarget = getFragment(f.mSavedFragmentState, TARGET_STATE_TAG);
								if (f.mTarget != null) {
									f.mTargetRequestCode = f.mSavedFragmentState.getInt(TARGET_REQUEST_CODE_STATE_TAG, 0);
								}
								f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
								if (!f.mUserVisibleHint) {
									f.mDeferStart = true;
									if (newState > 3) {
										newState = ANIM_STYLE_CLOSE_ENTER;
									}
								}
							}
							f.mActivity = mActivity;
							f.mParentFragment = mParent;
							if (mParent != null) {
								r0_FragmentManagerImpl = mParent.mChildFragmentManager;
							} else {
								r0_FragmentManagerImpl = mActivity.mFragments;
							}
							f.mFragmentManager = r0_FragmentManagerImpl;
							f.mCalled = false;
							f.onAttach(mActivity);
							if (!f.mCalled) {
								throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
							} else {
								if (f.mParentFragment == null) {
									mActivity.onAttachFragment(f);
								}
								if (!f.mRetaining) {
									f.performCreate(f.mSavedFragmentState);
								}
								f.mRetaining = false;
								if (f.mFromLayout) {
									f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
									if (f.mView != null) {
										f.mInnerView = f.mView;
										f.mView = NoSaveStateFrameLayout.wrap(f.mView);
										if (f.mHidden) {
											f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
										}
										f.onViewCreated(f.mView, f.mSavedFragmentState);
									} else {
										f.mInnerView = null;
									}
								}
							}
							break;
						case ANIM_STYLE_OPEN_ENTER:
							break;
						case ANIM_STYLE_OPEN_EXIT:
						case ANIM_STYLE_CLOSE_ENTER:
							break;
						case ANIM_STYLE_CLOSE_EXIT:
							break;
						}
					}
				} else if (f.mState <= newState) {
					switch(f.mState) {
					case ANIM_STYLE_OPEN_ENTER:
						break;
					case ANIM_STYLE_OPEN_EXIT:
						break;
					case ANIM_STYLE_CLOSE_ENTER:
						break;
					case ANIM_STYLE_CLOSE_EXIT:
						break;
					case ANIM_STYLE_FADE_ENTER:
						if (newState >= 5) {
							if (!DEBUG) {
								Log.v(TAG, "movefrom RESUMED: " + f);
							}
							f.performPause();
							f.mResumed = false;
						}
						break;
					default:
						break;
					}
				}
				f.mState = newState;
			} else {
				newState = ANIM_STYLE_CLOSE_ENTER;
				if (f.mState >= newState) {
					if (f.mState <= newState) {
						f.mState = newState;
					} else {
						switch(f.mState) {
						case ANIM_STYLE_OPEN_ENTER:
							break;
						case ANIM_STYLE_OPEN_EXIT:
							break;
						case ANIM_STYLE_CLOSE_ENTER:
							break;
						case ANIM_STYLE_CLOSE_EXIT:
							break;
						case ANIM_STYLE_FADE_ENTER:
							if (newState >= 5) {
							} else if (!DEBUG) {
								f.performPause();
								f.mResumed = false;
							} else {
								Log.v(TAG, "movefrom RESUMED: " + f);
								f.performPause();
								f.mResumed = false;
							}
							break;
						default:
							break;
						}
					}
				} else if (!f.mFromLayout || f.mInLayout) {
					if (f.mAnimatingAway == null) {
						switch(f.mState) {
						case WearableExtender.SIZE_DEFAULT:
							if (!DEBUG) {
								if (f.mSavedFragmentState != null) {
									f.mActivity = mActivity;
									f.mParentFragment = mParent;
									if (mParent != null) {
										r0_FragmentManagerImpl = mActivity.mFragments;
									} else {
										r0_FragmentManagerImpl = mParent.mChildFragmentManager;
									}
									f.mFragmentManager = r0_FragmentManagerImpl;
									f.mCalled = false;
									f.onAttach(mActivity);
									if (!f.mCalled) {
										if (f.mParentFragment == null) {
											if (!f.mRetaining) {
												f.mRetaining = false;
												if (f.mFromLayout) {
												} else {
													f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
													if (f.mView != null) {
														f.mInnerView = null;
													} else {
														f.mInnerView = f.mView;
														f.mView = NoSaveStateFrameLayout.wrap(f.mView);
														if (f.mHidden) {
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														}
													}
												}
											} else {
												f.performCreate(f.mSavedFragmentState);
												f.mRetaining = false;
												if (f.mFromLayout) {
													f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
													if (f.mView != null) {
														f.mInnerView = f.mView;
														f.mView = NoSaveStateFrameLayout.wrap(f.mView);
														if (f.mHidden) {
															f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
														}
														f.onViewCreated(f.mView, f.mSavedFragmentState);
													} else {
														f.mInnerView = null;
													}
												}
											}
										} else {
											mActivity.onAttachFragment(f);
											if (!f.mRetaining) {
												f.performCreate(f.mSavedFragmentState);
											}
											f.mRetaining = false;
											if (f.mFromLayout) {
											} else {
												f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
												if (f.mView != null) {
													f.mInnerView = null;
												} else {
													f.mInnerView = f.mView;
													f.mView = NoSaveStateFrameLayout.wrap(f.mView);
													if (f.mHidden) {
														f.onViewCreated(f.mView, f.mSavedFragmentState);
													} else {
														f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
														f.onViewCreated(f.mView, f.mSavedFragmentState);
													}
												}
											}
										}
									} else {
										throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
									}
								} else {
									f.mSavedViewState = f.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
									f.mTarget = getFragment(f.mSavedFragmentState, TARGET_STATE_TAG);
									if (f.mTarget != null) {
										f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
										if (!f.mUserVisibleHint) {
											f.mActivity = mActivity;
											f.mParentFragment = mParent;
											if (mParent != null) {
												r0_FragmentManagerImpl = mParent.mChildFragmentManager;
											} else {
												r0_FragmentManagerImpl = mActivity.mFragments;
											}
											f.mFragmentManager = r0_FragmentManagerImpl;
											f.mCalled = false;
											f.onAttach(mActivity);
											if (!f.mCalled) {
												throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
											} else {
												if (f.mParentFragment == null) {
													mActivity.onAttachFragment(f);
												}
												if (!f.mRetaining) {
													f.mRetaining = false;
													if (f.mFromLayout) {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															}
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mInnerView = null;
														}
													}
												} else {
													f.performCreate(f.mSavedFragmentState);
													f.mRetaining = false;
													if (f.mFromLayout) {
													} else {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = null;
														} else {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															} else {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															}
														}
													}
												}
											}
										} else {
											f.mDeferStart = true;
											if (newState > 3) {
												f.mActivity = mActivity;
												f.mParentFragment = mParent;
												if (mParent != null) {
													r0_FragmentManagerImpl = mActivity.mFragments;
												} else {
													r0_FragmentManagerImpl = mParent.mChildFragmentManager;
												}
												f.mFragmentManager = r0_FragmentManagerImpl;
												f.mCalled = false;
												f.onAttach(mActivity);
												if (!f.mCalled) {
													if (f.mParentFragment == null) {
														if (!f.mRetaining) {
															f.performCreate(f.mSavedFragmentState);
														}
														f.mRetaining = false;
														if (f.mFromLayout) {
															f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
															if (f.mView != null) {
																f.mInnerView = f.mView;
																f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																if (f.mHidden) {
																	f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																}
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															} else {
																f.mInnerView = null;
															}
														}
													} else {
														mActivity.onAttachFragment(f);
														if (!f.mRetaining) {
															f.mRetaining = false;
															if (f.mFromLayout) {
															} else {
																f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
																if (f.mView != null) {
																	f.mInnerView = null;
																} else {
																	f.mInnerView = f.mView;
																	f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																	if (f.mHidden) {
																		f.onViewCreated(f.mView, f.mSavedFragmentState);
																	} else {
																		f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																		f.onViewCreated(f.mView, f.mSavedFragmentState);
																	}
																}
															}
														} else {
															f.performCreate(f.mSavedFragmentState);
															f.mRetaining = false;
															if (f.mFromLayout) {
																f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
																if (f.mView != null) {
																	f.mInnerView = f.mView;
																	f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																	if (f.mHidden) {
																		f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																	}
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																} else {
																	f.mInnerView = null;
																}
															}
														}
													}
												} else {
													throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
												}
											} else {
												newState = ANIM_STYLE_CLOSE_ENTER;
												f.mActivity = mActivity;
												f.mParentFragment = mParent;
												if (mParent != null) {
													r0_FragmentManagerImpl = mParent.mChildFragmentManager;
												} else {
													r0_FragmentManagerImpl = mActivity.mFragments;
												}
												f.mFragmentManager = r0_FragmentManagerImpl;
												f.mCalled = false;
												f.onAttach(mActivity);
												if (!f.mCalled) {
													throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
												} else {
													if (f.mParentFragment == null) {
														mActivity.onAttachFragment(f);
													}
													if (!f.mRetaining) {
														f.performCreate(f.mSavedFragmentState);
													}
													f.mRetaining = false;
													if (f.mFromLayout) {
													} else {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = null;
														} else {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															} else {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															}
														}
													}
												}
											}
										}
									} else {
										f.mTargetRequestCode = f.mSavedFragmentState.getInt(TARGET_REQUEST_CODE_STATE_TAG, 0);
										f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
										if (!f.mUserVisibleHint) {
											f.mDeferStart = true;
											if (newState > 3) {
												newState = ANIM_STYLE_CLOSE_ENTER;
											}
										}
										f.mActivity = mActivity;
										f.mParentFragment = mParent;
										if (mParent != null) {
											r0_FragmentManagerImpl = mActivity.mFragments;
										} else {
											r0_FragmentManagerImpl = mParent.mChildFragmentManager;
										}
										f.mFragmentManager = r0_FragmentManagerImpl;
										f.mCalled = false;
										f.onAttach(mActivity);
										if (!f.mCalled) {
											if (f.mParentFragment == null) {
												if (!f.mRetaining) {
													f.mRetaining = false;
													if (f.mFromLayout) {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															}
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mInnerView = null;
														}
													}
												} else {
													f.performCreate(f.mSavedFragmentState);
													f.mRetaining = false;
													if (f.mFromLayout) {
													} else {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = null;
														} else {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															} else {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															}
														}
													}
												}
											} else {
												mActivity.onAttachFragment(f);
												if (!f.mRetaining) {
													f.performCreate(f.mSavedFragmentState);
												}
												f.mRetaining = false;
												if (f.mFromLayout) {
													f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
													if (f.mView != null) {
														f.mInnerView = f.mView;
														f.mView = NoSaveStateFrameLayout.wrap(f.mView);
														if (f.mHidden) {
															f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
														}
														f.onViewCreated(f.mView, f.mSavedFragmentState);
													} else {
														f.mInnerView = null;
													}
												}
											}
										} else {
											throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
										}
									}
								}
							} else {
								Log.v(TAG, "moveto CREATED: " + f);
								if (f.mSavedFragmentState != null) {
									f.mSavedViewState = f.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
									f.mTarget = getFragment(f.mSavedFragmentState, TARGET_STATE_TAG);
									if (f.mTarget != null) {
										f.mTargetRequestCode = f.mSavedFragmentState.getInt(TARGET_REQUEST_CODE_STATE_TAG, 0);
									}
									f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
									if (!f.mUserVisibleHint) {
										f.mActivity = mActivity;
										f.mParentFragment = mParent;
									} else {
										f.mDeferStart = true;
										if (newState > 3) {
											f.mActivity = mActivity;
											f.mParentFragment = mParent;
										} else {
											newState = ANIM_STYLE_CLOSE_ENTER;
										}
									}
								}
								f.mActivity = mActivity;
								f.mParentFragment = mParent;
								if (mParent != null) {
									r0_FragmentManagerImpl = mParent.mChildFragmentManager;
								} else {
									r0_FragmentManagerImpl = mActivity.mFragments;
								}
								f.mFragmentManager = r0_FragmentManagerImpl;
								f.mCalled = false;
								f.onAttach(mActivity);
								if (!f.mCalled) {
									throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
								} else {
									if (f.mParentFragment == null) {
										mActivity.onAttachFragment(f);
									}
									if (!f.mRetaining) {
										f.mRetaining = false;
										if (f.mFromLayout) {
										} else {
											f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
											if (f.mView != null) {
												f.mInnerView = null;
											} else {
												f.mInnerView = f.mView;
												f.mView = NoSaveStateFrameLayout.wrap(f.mView);
												if (f.mHidden) {
													f.onViewCreated(f.mView, f.mSavedFragmentState);
												} else {
													f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
													f.onViewCreated(f.mView, f.mSavedFragmentState);
												}
											}
										}
									} else {
										f.performCreate(f.mSavedFragmentState);
										f.mRetaining = false;
										if (f.mFromLayout) {
											f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
											if (f.mView != null) {
												f.mInnerView = f.mView;
												f.mView = NoSaveStateFrameLayout.wrap(f.mView);
												if (f.mHidden) {
													f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
												}
												f.onViewCreated(f.mView, f.mSavedFragmentState);
											} else {
												f.mInnerView = null;
											}
										}
									}
								}
							}
							break;
						case ANIM_STYLE_OPEN_ENTER:
							break;
						case ANIM_STYLE_OPEN_EXIT:
						case ANIM_STYLE_CLOSE_ENTER:
							break;
						case ANIM_STYLE_CLOSE_EXIT:
							break;
						}
					} else {
						f.mAnimatingAway = null;
						moveToState(f, f.mStateAfterAnimating, 0, 0, true);
						switch(f.mState) {
						case WearableExtender.SIZE_DEFAULT:
							if (!DEBUG) {
								Log.v(TAG, "moveto CREATED: " + f);
							}
							if (f.mSavedFragmentState != null) {
								f.mActivity = mActivity;
								f.mParentFragment = mParent;
								if (mParent != null) {
									r0_FragmentManagerImpl = mActivity.mFragments;
								} else {
									r0_FragmentManagerImpl = mParent.mChildFragmentManager;
								}
								f.mFragmentManager = r0_FragmentManagerImpl;
								f.mCalled = false;
								f.onAttach(mActivity);
								if (!f.mCalled) {
									if (f.mParentFragment == null) {
										if (!f.mRetaining) {
											f.performCreate(f.mSavedFragmentState);
										}
										f.mRetaining = false;
										if (f.mFromLayout) {
										} else {
											f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
											if (f.mView != null) {
												f.mInnerView = null;
											} else {
												f.mInnerView = f.mView;
												f.mView = NoSaveStateFrameLayout.wrap(f.mView);
												if (f.mHidden) {
													f.onViewCreated(f.mView, f.mSavedFragmentState);
												} else {
													f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
													f.onViewCreated(f.mView, f.mSavedFragmentState);
												}
											}
										}
									} else {
										mActivity.onAttachFragment(f);
										if (!f.mRetaining) {
											f.mRetaining = false;
											if (f.mFromLayout) {
												f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
												if (f.mView != null) {
													f.mInnerView = f.mView;
													f.mView = NoSaveStateFrameLayout.wrap(f.mView);
													if (f.mHidden) {
														f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
													}
													f.onViewCreated(f.mView, f.mSavedFragmentState);
												} else {
													f.mInnerView = null;
												}
											}
										} else {
											f.performCreate(f.mSavedFragmentState);
											f.mRetaining = false;
											if (f.mFromLayout) {
											} else {
												f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
												if (f.mView != null) {
													f.mInnerView = null;
												} else {
													f.mInnerView = f.mView;
													f.mView = NoSaveStateFrameLayout.wrap(f.mView);
													if (f.mHidden) {
														f.onViewCreated(f.mView, f.mSavedFragmentState);
													} else {
														f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
														f.onViewCreated(f.mView, f.mSavedFragmentState);
													}
												}
											}
										}
									}
								} else {
									throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
								}
							} else {
								f.mSavedViewState = f.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
								f.mTarget = getFragment(f.mSavedFragmentState, TARGET_STATE_TAG);
								if (f.mTarget != null) {
									f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
									if (!f.mUserVisibleHint) {
										f.mDeferStart = true;
										if (newState > 3) {
											newState = ANIM_STYLE_CLOSE_ENTER;
										}
									}
									f.mActivity = mActivity;
									f.mParentFragment = mParent;
									if (mParent != null) {
										r0_FragmentManagerImpl = mParent.mChildFragmentManager;
									} else {
										r0_FragmentManagerImpl = mActivity.mFragments;
									}
									f.mFragmentManager = r0_FragmentManagerImpl;
									f.mCalled = false;
									f.onAttach(mActivity);
									if (!f.mCalled) {
										throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
									} else {
										if (f.mParentFragment == null) {
											mActivity.onAttachFragment(f);
										}
										if (!f.mRetaining) {
											f.performCreate(f.mSavedFragmentState);
										}
										f.mRetaining = false;
										if (f.mFromLayout) {
											f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
											if (f.mView != null) {
												f.mInnerView = f.mView;
												f.mView = NoSaveStateFrameLayout.wrap(f.mView);
												if (f.mHidden) {
													f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
												}
												f.onViewCreated(f.mView, f.mSavedFragmentState);
											} else {
												f.mInnerView = null;
											}
										}
									}
								} else {
									f.mTargetRequestCode = f.mSavedFragmentState.getInt(TARGET_REQUEST_CODE_STATE_TAG, 0);
									f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
									if (!f.mUserVisibleHint) {
										f.mActivity = mActivity;
										f.mParentFragment = mParent;
										if (mParent != null) {
											r0_FragmentManagerImpl = mActivity.mFragments;
										} else {
											r0_FragmentManagerImpl = mParent.mChildFragmentManager;
										}
										f.mFragmentManager = r0_FragmentManagerImpl;
										f.mCalled = false;
										f.onAttach(mActivity);
										if (!f.mCalled) {
											if (f.mParentFragment == null) {
												if (!f.mRetaining) {
													f.mRetaining = false;
													if (f.mFromLayout) {
													} else {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = null;
														} else {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															} else {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															}
														}
													}
												} else {
													f.performCreate(f.mSavedFragmentState);
													f.mRetaining = false;
													if (f.mFromLayout) {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															}
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mInnerView = null;
														}
													}
												}
											} else {
												mActivity.onAttachFragment(f);
												if (!f.mRetaining) {
													f.performCreate(f.mSavedFragmentState);
												}
												f.mRetaining = false;
												if (f.mFromLayout) {
												} else {
													f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
													if (f.mView != null) {
														f.mInnerView = null;
													} else {
														f.mInnerView = f.mView;
														f.mView = NoSaveStateFrameLayout.wrap(f.mView);
														if (f.mHidden) {
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														}
													}
												}
											}
										} else {
											throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
										}
									} else {
										f.mDeferStart = true;
										if (newState > 3) {
											f.mActivity = mActivity;
											f.mParentFragment = mParent;
											if (mParent != null) {
												r0_FragmentManagerImpl = mParent.mChildFragmentManager;
											} else {
												r0_FragmentManagerImpl = mActivity.mFragments;
											}
											f.mFragmentManager = r0_FragmentManagerImpl;
											f.mCalled = false;
											f.onAttach(mActivity);
											if (!f.mCalled) {
												throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
											} else {
												if (f.mParentFragment == null) {
													mActivity.onAttachFragment(f);
												}
												if (!f.mRetaining) {
													f.mRetaining = false;
													if (f.mFromLayout) {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															}
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mInnerView = null;
														}
													}
												} else {
													f.performCreate(f.mSavedFragmentState);
													f.mRetaining = false;
													if (f.mFromLayout) {
													} else {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = null;
														} else {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															} else {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															}
														}
													}
												}
											}
										} else {
											newState = ANIM_STYLE_CLOSE_ENTER;
											f.mActivity = mActivity;
											f.mParentFragment = mParent;
											if (mParent != null) {
												r0_FragmentManagerImpl = mActivity.mFragments;
											} else {
												r0_FragmentManagerImpl = mParent.mChildFragmentManager;
											}
											f.mFragmentManager = r0_FragmentManagerImpl;
											f.mCalled = false;
											f.onAttach(mActivity);
											if (!f.mCalled) {
												if (f.mParentFragment == null) {
													if (!f.mRetaining) {
														f.performCreate(f.mSavedFragmentState);
													}
													f.mRetaining = false;
													if (f.mFromLayout) {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															}
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mInnerView = null;
														}
													}
												} else {
													mActivity.onAttachFragment(f);
													if (!f.mRetaining) {
														f.mRetaining = false;
														if (f.mFromLayout) {
														} else {
															f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
															if (f.mView != null) {
																f.mInnerView = null;
															} else {
																f.mInnerView = f.mView;
																f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																if (f.mHidden) {
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																} else {
																	f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																}
															}
														}
													} else {
														f.performCreate(f.mSavedFragmentState);
														f.mRetaining = false;
														if (f.mFromLayout) {
															f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
															if (f.mView != null) {
																f.mInnerView = f.mView;
																f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																if (f.mHidden) {
																	f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																}
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															} else {
																f.mInnerView = null;
															}
														}
													}
												}
											} else {
												throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
											}
										}
									}
								}
							}
						case ANIM_STYLE_OPEN_ENTER:
							break;
						case ANIM_STYLE_OPEN_EXIT:
						case ANIM_STYLE_CLOSE_ENTER:
							break;
						case ANIM_STYLE_CLOSE_EXIT:
							break;
						}
					}
				}
				f.mState = newState;
			}
		} else {
			newState = f.mState;
			if (!f.mDeferStart || f.mState >= 4 || newState <= 3) {
				if (f.mState >= newState) {
					if (!f.mFromLayout || f.mInLayout) {
						if (f.mAnimatingAway == null) {
							f.mAnimatingAway = null;
							moveToState(f, f.mStateAfterAnimating, 0, 0, true);
						}
						switch(f.mState) {
						case WearableExtender.SIZE_DEFAULT:
							if (!DEBUG) {
								if (f.mSavedFragmentState != null) {
									f.mSavedViewState = f.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
									f.mTarget = getFragment(f.mSavedFragmentState, TARGET_STATE_TAG);
									if (f.mTarget != null) {
										f.mTargetRequestCode = f.mSavedFragmentState.getInt(TARGET_REQUEST_CODE_STATE_TAG, 0);
									}
									f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
									if (!f.mUserVisibleHint) {
										f.mDeferStart = true;
										if (newState > 3) {
											newState = ANIM_STYLE_CLOSE_ENTER;
										}
									}
								}
								f.mActivity = mActivity;
								f.mParentFragment = mParent;
								if (mParent != null) {
									r0_FragmentManagerImpl = mParent.mChildFragmentManager;
								} else {
									r0_FragmentManagerImpl = mActivity.mFragments;
								}
								f.mFragmentManager = r0_FragmentManagerImpl;
								f.mCalled = false;
								f.onAttach(mActivity);
								if (!f.mCalled) {
									throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
								} else {
									if (f.mParentFragment == null) {
										mActivity.onAttachFragment(f);
									}
									if (!f.mRetaining) {
										f.performCreate(f.mSavedFragmentState);
									}
									f.mRetaining = false;
									if (f.mFromLayout) {
									} else {
										f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
										if (f.mView != null) {
											f.mInnerView = null;
										} else {
											f.mInnerView = f.mView;
											f.mView = NoSaveStateFrameLayout.wrap(f.mView);
											if (f.mHidden) {
												f.onViewCreated(f.mView, f.mSavedFragmentState);
											} else {
												f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
												f.onViewCreated(f.mView, f.mSavedFragmentState);
											}
										}
									}
								}
							} else {
								Log.v(TAG, "moveto CREATED: " + f);
								if (f.mSavedFragmentState != null) {
									f.mActivity = mActivity;
									f.mParentFragment = mParent;
									if (mParent != null) {
										r0_FragmentManagerImpl = mActivity.mFragments;
									} else {
										r0_FragmentManagerImpl = mParent.mChildFragmentManager;
									}
									f.mFragmentManager = r0_FragmentManagerImpl;
									f.mCalled = false;
									f.onAttach(mActivity);
									if (!f.mCalled) {
										if (f.mParentFragment == null) {
											if (!f.mRetaining) {
												f.mRetaining = false;
												if (f.mFromLayout) {
													f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
													if (f.mView != null) {
														f.mInnerView = f.mView;
														f.mView = NoSaveStateFrameLayout.wrap(f.mView);
														if (f.mHidden) {
															f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
														}
														f.onViewCreated(f.mView, f.mSavedFragmentState);
													} else {
														f.mInnerView = null;
													}
												}
											} else {
												f.performCreate(f.mSavedFragmentState);
												f.mRetaining = false;
												if (f.mFromLayout) {
												} else {
													f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
													if (f.mView != null) {
														f.mInnerView = null;
													} else {
														f.mInnerView = f.mView;
														f.mView = NoSaveStateFrameLayout.wrap(f.mView);
														if (f.mHidden) {
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														}
													}
												}
											}
										} else {
											mActivity.onAttachFragment(f);
											if (!f.mRetaining) {
												f.performCreate(f.mSavedFragmentState);
											}
											f.mRetaining = false;
											if (f.mFromLayout) {
												f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
												if (f.mView != null) {
													f.mInnerView = f.mView;
													f.mView = NoSaveStateFrameLayout.wrap(f.mView);
													if (f.mHidden) {
														f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
													}
													f.onViewCreated(f.mView, f.mSavedFragmentState);
												} else {
													f.mInnerView = null;
												}
											}
										}
									} else {
										throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
									}
								} else {
									f.mSavedViewState = f.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
									f.mTarget = getFragment(f.mSavedFragmentState, TARGET_STATE_TAG);
									if (f.mTarget != null) {
										f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
										if (!f.mUserVisibleHint) {
											f.mActivity = mActivity;
											f.mParentFragment = mParent;
											if (mParent != null) {
												r0_FragmentManagerImpl = mParent.mChildFragmentManager;
											} else {
												r0_FragmentManagerImpl = mActivity.mFragments;
											}
											f.mFragmentManager = r0_FragmentManagerImpl;
											f.mCalled = false;
											f.onAttach(mActivity);
											if (!f.mCalled) {
												throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
											} else {
												if (f.mParentFragment == null) {
													mActivity.onAttachFragment(f);
												}
												if (!f.mRetaining) {
													f.mRetaining = false;
													if (f.mFromLayout) {
													} else {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = null;
														} else {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															} else {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															}
														}
													}
												} else {
													f.performCreate(f.mSavedFragmentState);
													f.mRetaining = false;
													if (f.mFromLayout) {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															}
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mInnerView = null;
														}
													}
												}
											}
										} else {
											f.mDeferStart = true;
											if (newState > 3) {
												f.mActivity = mActivity;
												f.mParentFragment = mParent;
												if (mParent != null) {
													r0_FragmentManagerImpl = mActivity.mFragments;
												} else {
													r0_FragmentManagerImpl = mParent.mChildFragmentManager;
												}
												f.mFragmentManager = r0_FragmentManagerImpl;
												f.mCalled = false;
												f.onAttach(mActivity);
												if (!f.mCalled) {
													if (f.mParentFragment == null) {
														if (!f.mRetaining) {
															f.performCreate(f.mSavedFragmentState);
														}
														f.mRetaining = false;
														if (f.mFromLayout) {
														} else {
															f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
															if (f.mView != null) {
																f.mInnerView = null;
															} else {
																f.mInnerView = f.mView;
																f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																if (f.mHidden) {
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																} else {
																	f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																}
															}
														}
													} else {
														mActivity.onAttachFragment(f);
														if (!f.mRetaining) {
															f.mRetaining = false;
															if (f.mFromLayout) {
																f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
																if (f.mView != null) {
																	f.mInnerView = f.mView;
																	f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																	if (f.mHidden) {
																		f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																	}
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																} else {
																	f.mInnerView = null;
																}
															}
														} else {
															f.performCreate(f.mSavedFragmentState);
															f.mRetaining = false;
															if (f.mFromLayout) {
															} else {
																f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
																if (f.mView != null) {
																	f.mInnerView = null;
																} else {
																	f.mInnerView = f.mView;
																	f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																	if (f.mHidden) {
																		f.onViewCreated(f.mView, f.mSavedFragmentState);
																	} else {
																		f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																		f.onViewCreated(f.mView, f.mSavedFragmentState);
																	}
																}
															}
														}
													}
												} else {
													throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
												}
											} else {
												newState = ANIM_STYLE_CLOSE_ENTER;
												f.mActivity = mActivity;
												f.mParentFragment = mParent;
												if (mParent != null) {
													r0_FragmentManagerImpl = mParent.mChildFragmentManager;
												} else {
													r0_FragmentManagerImpl = mActivity.mFragments;
												}
												f.mFragmentManager = r0_FragmentManagerImpl;
												f.mCalled = false;
												f.onAttach(mActivity);
												if (!f.mCalled) {
													throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
												} else {
													if (f.mParentFragment == null) {
														mActivity.onAttachFragment(f);
													}
													if (!f.mRetaining) {
														f.performCreate(f.mSavedFragmentState);
													}
													f.mRetaining = false;
													if (f.mFromLayout) {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															}
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mInnerView = null;
														}
													}
												}
											}
										}
									} else {
										f.mTargetRequestCode = f.mSavedFragmentState.getInt(TARGET_REQUEST_CODE_STATE_TAG, 0);
										f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
										if (!f.mUserVisibleHint) {
											f.mDeferStart = true;
											if (newState > 3) {
												newState = ANIM_STYLE_CLOSE_ENTER;
											}
										}
										f.mActivity = mActivity;
										f.mParentFragment = mParent;
										if (mParent != null) {
											r0_FragmentManagerImpl = mActivity.mFragments;
										} else {
											r0_FragmentManagerImpl = mParent.mChildFragmentManager;
										}
										f.mFragmentManager = r0_FragmentManagerImpl;
										f.mCalled = false;
										f.onAttach(mActivity);
										if (!f.mCalled) {
											if (f.mParentFragment == null) {
												if (!f.mRetaining) {
													f.mRetaining = false;
													if (f.mFromLayout) {
													} else {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = null;
														} else {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															} else {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															}
														}
													}
												} else {
													f.performCreate(f.mSavedFragmentState);
													f.mRetaining = false;
													if (f.mFromLayout) {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															}
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mInnerView = null;
														}
													}
												}
											} else {
												mActivity.onAttachFragment(f);
												if (!f.mRetaining) {
													f.performCreate(f.mSavedFragmentState);
												}
												f.mRetaining = false;
												if (f.mFromLayout) {
												} else {
													f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
													if (f.mView != null) {
														f.mInnerView = null;
													} else {
														f.mInnerView = f.mView;
														f.mView = NoSaveStateFrameLayout.wrap(f.mView);
														if (f.mHidden) {
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														}
													}
												}
											}
										} else {
											throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
										}
									}
								}
							}
						case ANIM_STYLE_OPEN_ENTER:
							break;
						case ANIM_STYLE_OPEN_EXIT:
						case ANIM_STYLE_CLOSE_ENTER:
							break;
						case ANIM_STYLE_CLOSE_EXIT:
							break;
						}
					}
				} else if (f.mState <= newState) {
					switch(f.mState) {
					case ANIM_STYLE_OPEN_ENTER:
						break;
					case ANIM_STYLE_OPEN_EXIT:
						break;
					case ANIM_STYLE_CLOSE_ENTER:
						break;
					case ANIM_STYLE_CLOSE_EXIT:
						break;
					case ANIM_STYLE_FADE_ENTER:
						if (newState >= 5) {
							if (!DEBUG) {
								Log.v(TAG, "movefrom RESUMED: " + f);
							}
							f.performPause();
							f.mResumed = false;
						}
						break;
					default:
						break;
					}
				}
				f.mState = newState;
			} else {
				newState = ANIM_STYLE_CLOSE_ENTER;
				if (f.mState >= newState) {
					if (f.mState <= newState) {
						f.mState = newState;
					} else {
						switch(f.mState) {
						case ANIM_STYLE_OPEN_ENTER:
							break;
						case ANIM_STYLE_OPEN_EXIT:
							break;
						case ANIM_STYLE_CLOSE_ENTER:
							break;
						case ANIM_STYLE_CLOSE_EXIT:
							break;
						case ANIM_STYLE_FADE_ENTER:
							if (newState >= 5) {
							} else if (!DEBUG) {
								f.performPause();
								f.mResumed = false;
							} else {
								Log.v(TAG, "movefrom RESUMED: " + f);
								f.performPause();
								f.mResumed = false;
							}
							break;
						default:
							break;
						}
					}
				} else if (!f.mFromLayout || f.mInLayout) {
					if (f.mAnimatingAway == null) {
						switch(f.mState) {
						case WearableExtender.SIZE_DEFAULT:
							if (!DEBUG) {
								Log.v(TAG, "moveto CREATED: " + f);
							}
							if (f.mSavedFragmentState != null) {
								f.mSavedViewState = f.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
								f.mTarget = getFragment(f.mSavedFragmentState, TARGET_STATE_TAG);
								if (f.mTarget != null) {
									f.mTargetRequestCode = f.mSavedFragmentState.getInt(TARGET_REQUEST_CODE_STATE_TAG, 0);
								}
								f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
								if (!f.mUserVisibleHint) {
									f.mActivity = mActivity;
									f.mParentFragment = mParent;
								} else {
									f.mDeferStart = true;
									if (newState > 3) {
										f.mActivity = mActivity;
										f.mParentFragment = mParent;
									} else {
										newState = ANIM_STYLE_CLOSE_ENTER;
									}
								}
							}
							f.mActivity = mActivity;
							f.mParentFragment = mParent;
							if (mParent != null) {
								r0_FragmentManagerImpl = mParent.mChildFragmentManager;
							} else {
								r0_FragmentManagerImpl = mActivity.mFragments;
							}
							f.mFragmentManager = r0_FragmentManagerImpl;
							f.mCalled = false;
							f.onAttach(mActivity);
							if (!f.mCalled) {
								throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
							} else {
								if (f.mParentFragment == null) {
									mActivity.onAttachFragment(f);
								}
								if (!f.mRetaining) {
									f.mRetaining = false;
									if (f.mFromLayout) {
										f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
										if (f.mView != null) {
											f.mInnerView = f.mView;
											f.mView = NoSaveStateFrameLayout.wrap(f.mView);
											if (f.mHidden) {
												f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
											}
											f.onViewCreated(f.mView, f.mSavedFragmentState);
										} else {
											f.mInnerView = null;
										}
									}
								} else {
									f.performCreate(f.mSavedFragmentState);
									f.mRetaining = false;
									if (f.mFromLayout) {
									} else {
										f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
										if (f.mView != null) {
											f.mInnerView = null;
										} else {
											f.mInnerView = f.mView;
											f.mView = NoSaveStateFrameLayout.wrap(f.mView);
											if (f.mHidden) {
												f.onViewCreated(f.mView, f.mSavedFragmentState);
											} else {
												f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
												f.onViewCreated(f.mView, f.mSavedFragmentState);
											}
										}
									}
								}
							}
							break;
						case ANIM_STYLE_OPEN_ENTER:
							break;
						case ANIM_STYLE_OPEN_EXIT:
						case ANIM_STYLE_CLOSE_ENTER:
							break;
						case ANIM_STYLE_CLOSE_EXIT:
							break;
						}
					} else {
						f.mAnimatingAway = null;
						moveToState(f, f.mStateAfterAnimating, 0, 0, true);
						switch(f.mState) {
						case WearableExtender.SIZE_DEFAULT:
							if (!DEBUG) {
								if (f.mSavedFragmentState != null) {
									f.mActivity = mActivity;
									f.mParentFragment = mParent;
									if (mParent != null) {
										r0_FragmentManagerImpl = mActivity.mFragments;
									} else {
										r0_FragmentManagerImpl = mParent.mChildFragmentManager;
									}
									f.mFragmentManager = r0_FragmentManagerImpl;
									f.mCalled = false;
									f.onAttach(mActivity);
									if (!f.mCalled) {
										if (f.mParentFragment == null) {
											if (!f.mRetaining) {
												f.performCreate(f.mSavedFragmentState);
											}
											f.mRetaining = false;
											if (f.mFromLayout) {
												f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
												if (f.mView != null) {
													f.mInnerView = f.mView;
													f.mView = NoSaveStateFrameLayout.wrap(f.mView);
													if (f.mHidden) {
														f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
													}
													f.onViewCreated(f.mView, f.mSavedFragmentState);
												} else {
													f.mInnerView = null;
												}
											}
										} else {
											mActivity.onAttachFragment(f);
											if (!f.mRetaining) {
												f.mRetaining = false;
												if (f.mFromLayout) {
												} else {
													f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
													if (f.mView != null) {
														f.mInnerView = null;
													} else {
														f.mInnerView = f.mView;
														f.mView = NoSaveStateFrameLayout.wrap(f.mView);
														if (f.mHidden) {
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														}
													}
												}
											} else {
												f.performCreate(f.mSavedFragmentState);
												f.mRetaining = false;
												if (f.mFromLayout) {
													f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
													if (f.mView != null) {
														f.mInnerView = f.mView;
														f.mView = NoSaveStateFrameLayout.wrap(f.mView);
														if (f.mHidden) {
															f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
														}
														f.onViewCreated(f.mView, f.mSavedFragmentState);
													} else {
														f.mInnerView = null;
													}
												}
											}
										}
									} else {
										throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
									}
								} else {
									f.mSavedViewState = f.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
									f.mTarget = getFragment(f.mSavedFragmentState, TARGET_STATE_TAG);
									if (f.mTarget != null) {
										f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
										if (!f.mUserVisibleHint) {
											f.mDeferStart = true;
											if (newState > 3) {
												newState = ANIM_STYLE_CLOSE_ENTER;
											}
										}
										f.mActivity = mActivity;
										f.mParentFragment = mParent;
										if (mParent != null) {
											r0_FragmentManagerImpl = mParent.mChildFragmentManager;
										} else {
											r0_FragmentManagerImpl = mActivity.mFragments;
										}
										f.mFragmentManager = r0_FragmentManagerImpl;
										f.mCalled = false;
										f.onAttach(mActivity);
										if (!f.mCalled) {
											throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
										} else {
											if (f.mParentFragment == null) {
												mActivity.onAttachFragment(f);
											}
											if (!f.mRetaining) {
												f.performCreate(f.mSavedFragmentState);
											}
											f.mRetaining = false;
											if (f.mFromLayout) {
											} else {
												f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
												if (f.mView != null) {
													f.mInnerView = null;
												} else {
													f.mInnerView = f.mView;
													f.mView = NoSaveStateFrameLayout.wrap(f.mView);
													if (f.mHidden) {
														f.onViewCreated(f.mView, f.mSavedFragmentState);
													} else {
														f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
														f.onViewCreated(f.mView, f.mSavedFragmentState);
													}
												}
											}
										}
									} else {
										f.mTargetRequestCode = f.mSavedFragmentState.getInt(TARGET_REQUEST_CODE_STATE_TAG, 0);
										f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
										if (!f.mUserVisibleHint) {
											f.mActivity = mActivity;
											f.mParentFragment = mParent;
											if (mParent != null) {
												r0_FragmentManagerImpl = mActivity.mFragments;
											} else {
												r0_FragmentManagerImpl = mParent.mChildFragmentManager;
											}
											f.mFragmentManager = r0_FragmentManagerImpl;
											f.mCalled = false;
											f.onAttach(mActivity);
											if (!f.mCalled) {
												if (f.mParentFragment == null) {
													if (!f.mRetaining) {
														f.mRetaining = false;
														if (f.mFromLayout) {
															f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
															if (f.mView != null) {
																f.mInnerView = f.mView;
																f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																if (f.mHidden) {
																	f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																}
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															} else {
																f.mInnerView = null;
															}
														}
													} else {
														f.performCreate(f.mSavedFragmentState);
														f.mRetaining = false;
														if (f.mFromLayout) {
														} else {
															f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
															if (f.mView != null) {
																f.mInnerView = null;
															} else {
																f.mInnerView = f.mView;
																f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																if (f.mHidden) {
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																} else {
																	f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																}
															}
														}
													}
												} else {
													mActivity.onAttachFragment(f);
													if (!f.mRetaining) {
														f.performCreate(f.mSavedFragmentState);
													}
													f.mRetaining = false;
													if (f.mFromLayout) {
														f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
														if (f.mView != null) {
															f.mInnerView = f.mView;
															f.mView = NoSaveStateFrameLayout.wrap(f.mView);
															if (f.mHidden) {
																f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
															}
															f.onViewCreated(f.mView, f.mSavedFragmentState);
														} else {
															f.mInnerView = null;
														}
													}
												}
											} else {
												throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
											}
										} else {
											f.mDeferStart = true;
											if (newState > 3) {
												f.mActivity = mActivity;
												f.mParentFragment = mParent;
												if (mParent != null) {
													r0_FragmentManagerImpl = mParent.mChildFragmentManager;
												} else {
													r0_FragmentManagerImpl = mActivity.mFragments;
												}
												f.mFragmentManager = r0_FragmentManagerImpl;
												f.mCalled = false;
												f.onAttach(mActivity);
												if (!f.mCalled) {
													throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
												} else {
													if (f.mParentFragment == null) {
														mActivity.onAttachFragment(f);
													}
													if (!f.mRetaining) {
														f.mRetaining = false;
														if (f.mFromLayout) {
														} else {
															f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
															if (f.mView != null) {
																f.mInnerView = null;
															} else {
																f.mInnerView = f.mView;
																f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																if (f.mHidden) {
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																} else {
																	f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																}
															}
														}
													} else {
														f.performCreate(f.mSavedFragmentState);
														f.mRetaining = false;
														if (f.mFromLayout) {
															f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
															if (f.mView != null) {
																f.mInnerView = f.mView;
																f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																if (f.mHidden) {
																	f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																}
																f.onViewCreated(f.mView, f.mSavedFragmentState);
															} else {
																f.mInnerView = null;
															}
														}
													}
												}
											} else {
												newState = ANIM_STYLE_CLOSE_ENTER;
												f.mActivity = mActivity;
												f.mParentFragment = mParent;
												if (mParent != null) {
													r0_FragmentManagerImpl = mActivity.mFragments;
												} else {
													r0_FragmentManagerImpl = mParent.mChildFragmentManager;
												}
												f.mFragmentManager = r0_FragmentManagerImpl;
												f.mCalled = false;
												f.onAttach(mActivity);
												if (!f.mCalled) {
													if (f.mParentFragment == null) {
														if (!f.mRetaining) {
															f.performCreate(f.mSavedFragmentState);
														}
														f.mRetaining = false;
														if (f.mFromLayout) {
														} else {
															f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
															if (f.mView != null) {
																f.mInnerView = null;
															} else {
																f.mInnerView = f.mView;
																f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																if (f.mHidden) {
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																} else {
																	f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																}
															}
														}
													} else {
														mActivity.onAttachFragment(f);
														if (!f.mRetaining) {
															f.mRetaining = false;
															if (f.mFromLayout) {
																f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
																if (f.mView != null) {
																	f.mInnerView = f.mView;
																	f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																	if (f.mHidden) {
																		f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																	}
																	f.onViewCreated(f.mView, f.mSavedFragmentState);
																} else {
																	f.mInnerView = null;
																}
															}
														} else {
															f.performCreate(f.mSavedFragmentState);
															f.mRetaining = false;
															if (f.mFromLayout) {
															} else {
																f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
																if (f.mView != null) {
																	f.mInnerView = null;
																} else {
																	f.mInnerView = f.mView;
																	f.mView = NoSaveStateFrameLayout.wrap(f.mView);
																	if (f.mHidden) {
																		f.onViewCreated(f.mView, f.mSavedFragmentState);
																	} else {
																		f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
																		f.onViewCreated(f.mView, f.mSavedFragmentState);
																	}
																}
															}
														}
													}
												} else {
													throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
												}
											}
										}
									}
								}
							} else {
								Log.v(TAG, "moveto CREATED: " + f);
								if (f.mSavedFragmentState != null) {
									f.mSavedViewState = f.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
									f.mTarget = getFragment(f.mSavedFragmentState, TARGET_STATE_TAG);
									if (f.mTarget != null) {
										f.mTargetRequestCode = f.mSavedFragmentState.getInt(TARGET_REQUEST_CODE_STATE_TAG, 0);
									}
									f.mUserVisibleHint = f.mSavedFragmentState.getBoolean(USER_VISIBLE_HINT_TAG, true);
									if (!f.mUserVisibleHint) {
										f.mDeferStart = true;
										if (newState > 3) {
											newState = ANIM_STYLE_CLOSE_ENTER;
										}
									}
								}
								f.mActivity = mActivity;
								f.mParentFragment = mParent;
								if (mParent != null) {
									r0_FragmentManagerImpl = mParent.mChildFragmentManager;
								} else {
									r0_FragmentManagerImpl = mActivity.mFragments;
								}
								f.mFragmentManager = r0_FragmentManagerImpl;
								f.mCalled = false;
								f.onAttach(mActivity);
								if (!f.mCalled) {
									throw new SuperNotCalledException("Fragment " + f + " did not call through to super.onAttach()");
								} else {
									if (f.mParentFragment == null) {
										mActivity.onAttachFragment(f);
									}
									if (!f.mRetaining) {
										f.performCreate(f.mSavedFragmentState);
									}
									f.mRetaining = false;
									if (f.mFromLayout) {
										f.mView = f.performCreateView(f.getLayoutInflater(f.mSavedFragmentState), null, f.mSavedFragmentState);
										if (f.mView != null) {
											f.mInnerView = f.mView;
											f.mView = NoSaveStateFrameLayout.wrap(f.mView);
											if (f.mHidden) {
												f.mView.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
											}
											f.onViewCreated(f.mView, f.mSavedFragmentState);
										} else {
											f.mInnerView = null;
										}
									}
								}
							}
							break;
						case ANIM_STYLE_OPEN_ENTER:
							break;
						case ANIM_STYLE_OPEN_EXIT:
						case ANIM_STYLE_CLOSE_ENTER:
							break;
						case ANIM_STYLE_CLOSE_EXIT:
							break;
						}
					}
				}
				f.mState = newState;
			}
		}
	}

	public void noteStateNotSaved() {
		mStateSaved = false;
	}

	public void performPendingDeferredStart(Fragment f) {
		if (f.mDeferStart) {
			if (mExecutingActions) {
				mHavePendingDeferredStart = true;
			} else {
				f.mDeferStart = false;
				moveToState(f, mCurState, 0, 0, false);
			}
		}
	}

	public void popBackStack() {
		enqueueAction(new AnonymousClass_2(this), HONEYCOMB);
	}

	public void popBackStack(int id, int flags) {
		if (id < 0) {
			throw new IllegalArgumentException("Bad id: " + id);
		} else {
			enqueueAction(new AnonymousClass_4(this, id, flags), HONEYCOMB);
		}
	}

	public void popBackStack(String name, int flags) {
		enqueueAction(new AnonymousClass_3(this, name, flags), HONEYCOMB);
	}

	public boolean popBackStackImmediate() {
		checkStateLoss();
		executePendingTransactions();
		return popBackStackState(mActivity.mHandler, null, -1, 0);
	}

	public boolean popBackStackImmediate(int id, int flags) {
		checkStateLoss();
		executePendingTransactions();
		if (id < 0) {
			throw new IllegalArgumentException("Bad id: " + id);
		} else {
			return popBackStackState(mActivity.mHandler, null, id, flags);
		}
	}

	public boolean popBackStackImmediate(String name, int flags) {
		checkStateLoss();
		executePendingTransactions();
		return popBackStackState(mActivity.mHandler, name, -1, flags);
	}

	/* JADX WARNING: inconsistent code */
	/*
	boolean popBackStackState(android.os.Handler r12_handler, java.lang.String r13_name, int r14_id, int r15_flags) {
		r11_this = this;
		r8 = 1;
		r9 = 0;
		r6 = r11.mBackStack;
		if (r6 != 0) goto L_0x0007;
	L_0x0006:
		return r9;
	L_0x0007:
		if (r13_name != 0) goto L_0x0029;
	L_0x0009:
		if (r14_id >= 0) goto L_0x0029;
	L_0x000b:
		r6 = r15_flags & 1;
		if (r6 != 0) goto L_0x0029;
	L_0x000f:
		r6 = r11.mBackStack;
		r6 = r6.size();
		r4 = r6 + -1;
		if (r4_last < 0) goto L_0x0006;
	L_0x0019:
		r6 = r11.mBackStack;
		r1 = r6.remove(r4_last);
		r1 = (android.support.v4.app.BackStackRecord) r1;
		r1_bss.popFromBackStack(r8);
		r11.reportBackStackChanged();
	L_0x0027:
		r9 = r8;
		goto L_0x0006;
	L_0x0029:
		r3 = -1;
		if (r13_name != 0) goto L_0x002e;
	L_0x002c:
		if (r14_id < 0) goto L_0x007c;
	L_0x002e:
		r6 = r11.mBackStack;
		r6 = r6.size();
		r3_index = r6 + -1;
	L_0x0036:
		if (r3_index < 0) goto L_0x004c;
	L_0x0038:
		r6 = r11.mBackStack;
		r1_bss = r6.get(r3_index);
		r1_bss = (android.support.v4.app.BackStackRecord) r1_bss;
		if (r13_name == 0) goto L_0x0073;
	L_0x0042:
		r6 = r1_bss.getName();
		r6 = r13_name.equals(r6);
		if (r6 == 0) goto L_0x0073;
	L_0x004c:
		if (r3_index < 0) goto L_0x0006;
	L_0x004e:
		r6 = r15_flags & 1;
		if (r6 == 0) goto L_0x007c;
	L_0x0052:
		r3_index++;
	L_0x0054:
		if (r3_index < 0) goto L_0x007c;
	L_0x0056:
		r6 = r11.mBackStack;
		r1_bss = r6.get(r3_index);
		r1_bss = (android.support.v4.app.BackStackRecord) r1_bss;
		if (r13_name == 0) goto L_0x006a;
	L_0x0060:
		r6 = r1_bss.getName();
		r6 = r13_name.equals(r6);
		if (r6 != 0) goto L_0x0070;
	L_0x006a:
		if (r14_id < 0) goto L_0x007c;
	L_0x006c:
		r6 = r1_bss.mIndex;
		if (r14_id != r6) goto L_0x007c;
	L_0x0070:
		r3_index++;
		goto L_0x0054;
	L_0x0073:
		if (r14_id < 0) goto L_0x0079;
	L_0x0075:
		r6 = r1_bss.mIndex;
		if (r14_id == r6) goto L_0x004c;
	L_0x0079:
		r3_index++;
		goto L_0x0036;
	L_0x007c:
		r6 = r11.mBackStack;
		r6 = r6.size();
		r6++;
		if (r3_index == r6) goto L_0x0006;
	L_0x0086:
		r5 = new java.util.ArrayList;
		r5.<init>();
		r6 = r11.mBackStack;
		r6 = r6.size();
		r2 = r6 + -1;
	L_0x0093:
		if (r2_i <= r3_index) goto L_0x00a1;
	L_0x0095:
		r6 = r11.mBackStack;
		r6 = r6.remove(r2_i);
		r5_states.add(r6);
		r2_i++;
		goto L_0x0093;
	L_0x00a1:
		r6 = r5_states.size();
		r0 = r6 + -1;
		r2_i = 0;
	L_0x00a8:
		if (r2_i > r0_LAST) goto L_0x00db;
	L_0x00aa:
		r6 = DEBUG;
		if (r6 == 0) goto L_0x00ca;
	L_0x00ae:
		r6 = "FragmentManager";
		r7 = new java.lang.StringBuilder;
		r7.<init>();
		r10 = "Popping back stack state: ";
		r7 = r7.append(r10);
		r10 = r5_states.get(r2_i);
		r7 = r7.append(r10);
		r7 = r7.toString();
		android.util.Log.v(r6, r7);
	L_0x00ca:
		r6 = r5_states.get(r2_i);
		r6 = (android.support.v4.app.BackStackRecord) r6;
		if (r2_i != r0_LAST) goto L_0x00d9;
	L_0x00d2:
		r7 = r8;
	L_0x00d3:
		r6.popFromBackStack(r7);
		r2_i++;
		goto L_0x00a8;
	L_0x00d9:
		r7 = r9;
		goto L_0x00d3;
	L_0x00db:
		r11.reportBackStackChanged();
		goto L_0x0027;
	}
	*/
	boolean popBackStackState(Handler handler, String name, int id, int flags) {
		if (mBackStack == null) {
			return HONEYCOMB;
		} else {
			if (name != null || id >= 0 || (flags & 1) != 0) {
				int index = -1;
				if (name != null || id >= 0) {
					index = mBackStack.size() - 1;
					while (index >= 0) {
						bss = mBackStack.get(index);
						if (name == null || !name.equals(bss.getName())) {
						}
					}
					if (index >= 0) {
						if ((flags & 1) != 0) {
							index--;
							while (index >= 0) {
								bss = mBackStack.get(index);
								if (name == null || !name.equals(bss.getName())) {
								}
							}
						}
					} else {
						return HONEYCOMB;
					}
				}
				if (index != mBackStack.size() - 1) {
					ArrayList<BackStackRecord> states = new ArrayList();
					int i = mBackStack.size() - 1;
					while (i > index) {
						states.add(mBackStack.remove(i));
						i--;
					}
					int LAST = states.size() - 1;
					i = 0;
					while (i <= LAST) {
						boolean r7z;
						if (DEBUG) {
							Log.v(TAG, "Popping back stack state: " + states.get(i));
						}
						BackStackRecord r6_BackStackRecord = (BackStackRecord) states.get(i);
						if (i == LAST) {
							r7z = true;
						} else {
							r7z = false;
						}
						r6_BackStackRecord.popFromBackStack(r7z);
						i++;
					}
					reportBackStackChanged();
				} else {
					return HONEYCOMB;
				}
			} else {
				int last = mBackStack.size() - 1;
				if (last >= 0) {
					((BackStackRecord) mBackStack.remove(last)).popFromBackStack(true);
					reportBackStackChanged();
				} else {
					return HONEYCOMB;
				}
			}
			return true;
		}
	}

	public void putFragment(Bundle bundle, String key, Fragment fragment) {
		if (fragment.mIndex < 0) {
			throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
		}
		bundle.putInt(key, fragment.mIndex);
	}

	public void removeFragment(Fragment fragment, int transition, int transitionStyle) {
		boolean inactive;
		if (DEBUG) {
			Log.v(TAG, "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
		}
		if (!fragment.isInBackStack()) {
			inactive = true;
		} else {
			inactive = false;
		}
		if (!fragment.mDetached || inactive) {
			if (mAdded != null) {
				mAdded.remove(fragment);
			}
			int r2i;
			if (!fragment.mHasMenu || !fragment.mMenuVisible) {
				fragment.mAdded = false;
				fragment.mRemoving = true;
				if (!inactive) {
					r2i = 0;
				} else {
					r2i = 1;
				}
				moveToState(fragment, r2i, transition, transitionStyle, HONEYCOMB);
			} else {
				mNeedMenuInvalidate = true;
				fragment.mAdded = false;
				fragment.mRemoving = true;
				if (!inactive) {
					r2i = 1;
				} else {
					r2i = 0;
				}
				moveToState(fragment, r2i, transition, transitionStyle, HONEYCOMB);
			}
		}
	}

	public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
		if (mBackStackChangeListeners != null) {
			mBackStackChangeListeners.remove(listener);
		}
	}

	void reportBackStackChanged() {
		if (mBackStackChangeListeners != null) {
			int i = 0;
			while (i < mBackStackChangeListeners.size()) {
				((OnBackStackChangedListener) mBackStackChangeListeners.get(i)).onBackStackChanged();
				i++;
			}
		}
	}

	void restoreAllState(Parcelable state, ArrayList<Fragment> nonConfig) {
		if (state == null) {
		} else {
			FragmentManagerState fms = (FragmentManagerState) state;
			if (fms.mActive != null) {
				int i;
				Fragment f;
				FragmentState fs;
				if (nonConfig != null) {
					i = 0;
					while (i < nonConfig.size()) {
						f = (Fragment) nonConfig.get(i);
						if (DEBUG) {
							Log.v(TAG, "restoreAllState: re-attaching retained " + f);
						}
						fs = fms.mActive[f.mIndex];
						fs.mInstance = f;
						f.mSavedViewState = null;
						f.mBackStackNesting = 0;
						f.mInLayout = false;
						f.mAdded = false;
						f.mTarget = null;
						if (fs.mSavedFragmentState != null) {
							fs.mSavedFragmentState.setClassLoader(mActivity.getClassLoader());
							f.mSavedViewState = fs.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
						}
						i++;
					}
				}
				mActive = new ArrayList(fms.mActive.length);
				if (mAvailIndices != null) {
					mAvailIndices.clear();
				}
				i = 0;
				while (i < fms.mActive.length) {
					fs = fms.mActive[i];
					if (fs != null) {
						f = fs.instantiate(mActivity, mParent);
						if (DEBUG) {
							Log.v(TAG, "restoreAllState: active #" + i + ": " + f);
						}
						mActive.add(f);
						fs.mInstance = null;
					} else {
						mActive.add(null);
						if (mAvailIndices == null) {
							mAvailIndices = new ArrayList();
						}
						if (DEBUG) {
							Log.v(TAG, "restoreAllState: avail #" + i);
						}
						mAvailIndices.add(Integer.valueOf(i));
					}
					i++;
				}
				if (nonConfig != null) {
					i = 0;
					while (i < nonConfig.size()) {
						f = nonConfig.get(i);
						if (f.mTargetIndex >= 0) {
							if (f.mTargetIndex < mActive.size()) {
								f.mTarget = (Fragment) mActive.get(f.mTargetIndex);
							} else {
								Log.w(TAG, "Re-attaching retained fragment " + f + " target no longer exists: " + f.mTargetIndex);
								f.mTarget = null;
							}
						}
						i++;
					}
				}
				if (fms.mAdded != null) {
					mAdded = new ArrayList(fms.mAdded.length);
					i = 0;
					while (i < fms.mAdded.length) {
						f = mActive.get(fms.mAdded[i]);
						if (f == null) {
							throwException(new IllegalStateException("No instantiated fragment for index #" + fms.mAdded[i]));
						}
						f.mAdded = true;
						if (DEBUG) {
							Log.v(TAG, "restoreAllState: added #" + i + ": " + f);
						}
						if (mAdded.contains(f)) {
							throw new IllegalStateException("Already added!");
						} else {
							mAdded.add(f);
							i++;
						}
					}
				} else {
					mAdded = null;
				}
				if (fms.mBackStack != null) {
					mBackStack = new ArrayList(fms.mBackStack.length);
					i = 0;
					while (i < fms.mBackStack.length) {
						BackStackRecord bse = fms.mBackStack[i].instantiate(this);
						if (DEBUG) {
							Log.v(TAG, "restoreAllState: back stack #" + i + " (index " + bse.mIndex + "): " + bse);
							bse.dump("  ", new PrintWriter(new LogWriter(TAG)), HONEYCOMB);
						}
						mBackStack.add(bse);
						if (bse.mIndex >= 0) {
							setBackStackIndex(bse.mIndex, bse);
						}
						i++;
					}
				} else {
					mBackStack = null;
				}
			}
		}
	}

	ArrayList<Fragment> retainNonConfig() {
		ArrayList<Fragment> fragments = null;
		if (mActive != null) {
			int i = 0;
			while (i < mActive.size()) {
				Fragment f = (Fragment) mActive.get(i);
				if (f == null || !f.mRetainInstance) {
					i++;
				} else {
					int r3i;
					if (fragments == null) {
						fragments = new ArrayList();
					}
					fragments.add(f);
					f.mRetaining = true;
					if (f.mTarget != null) {
						r3i = f.mTarget.mIndex;
					} else {
						r3i = -1;
					}
					f.mTargetIndex = r3i;
					if (DEBUG) {
						Log.v(TAG, "retainNonConfig: keeping retained " + f);
					}
					i++;
				}
			}
		}
		return fragments;
	}

	Parcelable saveAllState() {
		execPendingActions();
		if (HONEYCOMB) {
			mStateSaved = true;
		}
		if (mActive != null) {
			if (mActive.size() <= 0) {
				return null;
			} else {
				int N = mActive.size();
				FragmentState[] active = new FragmentState[N];
				boolean haveFragments = HONEYCOMB;
				int i = 0;
				while (i < N) {
					Fragment f = (Fragment) mActive.get(i);
					if (f != null) {
						if (f.mIndex < 0) {
							throwException(new IllegalStateException("Failure saving state: active " + f + " has cleared index: " + f.mIndex));
						}
						haveFragments = true;
						FragmentState fs = new FragmentState(f);
						active[i] = fs;
						if (f.mState <= 0 || fs.mSavedFragmentState != null) {
							fs.mSavedFragmentState = f.mSavedFragmentState;
						} else {
							fs.mSavedFragmentState = saveFragmentBasicState(f);
							if (f.mTarget != null) {
								if (f.mTarget.mIndex < 0) {
									throwException(new IllegalStateException("Failure saving state: " + f + " has target not in fragment manager: " + f.mTarget));
								}
								if (fs.mSavedFragmentState == null) {
									fs.mSavedFragmentState = new Bundle();
								}
								putFragment(fs.mSavedFragmentState, TARGET_STATE_TAG, f.mTarget);
								if (f.mTargetRequestCode != 0) {
									fs.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, f.mTargetRequestCode);
								}
							}
						}
						if (DEBUG) {
							Log.v(TAG, "Saved state of " + f + ": " + fs.mSavedFragmentState);
						}
					}
					i++;
				}
				if (!haveFragments) {
					if (DEBUG) {
						Log.v(TAG, "saveAllState: no fragments!");
						return null;
					} else {
						return null;
					}
				} else {
					int[] added = null;
					BackStackState[] backStack = null;
					if (mAdded != null) {
						N = mAdded.size();
						if (N > 0) {
							added = new int[N];
							i = 0;
							while (i < N) {
								added[i] = ((Fragment) mAdded.get(i)).mIndex;
								if (added[i] < 0) {
									throwException(new IllegalStateException("Failure saving state: active " + mAdded.get(i) + " has cleared index: " + added[i]));
								}
								if (DEBUG) {
									Log.v(TAG, "saveAllState: adding fragment #" + i + ": " + mAdded.get(i));
								}
								i++;
							}
						}
					}
					if (mBackStack != null) {
						N = mBackStack.size();
						if (N > 0) {
							backStack = new BackStackState[N];
							i = 0;
							while (i < N) {
								backStack[i] = new BackStackState(this, (BackStackRecord) mBackStack.get(i));
								if (DEBUG) {
									Log.v(TAG, "saveAllState: adding back stack #" + i + ": " + mBackStack.get(i));
								}
								i++;
							}
						}
					}
					Parcelable fms = new FragmentManagerState();
					fms.mActive = active;
					fms.mAdded = added;
					fms.mBackStack = backStack;
					return fms;
				}
			}
		} else {
			return null;
		}
	}

	Bundle saveFragmentBasicState(Fragment f) {
		Bundle result = null;
		if (mStateBundle == null) {
			mStateBundle = new Bundle();
		}
		f.performSaveInstanceState(mStateBundle);
		if (!mStateBundle.isEmpty()) {
			result = mStateBundle;
			mStateBundle = null;
		}
		if (f.mView != null) {
			saveFragmentViewState(f);
		}
		if (f.mSavedViewState != null) {
			if (result == null) {
				result = new Bundle();
			}
			result.putSparseParcelableArray(VIEW_STATE_TAG, f.mSavedViewState);
		}
		if (!f.mUserVisibleHint) {
			if (result == null) {
				result = new Bundle();
			}
			result.putBoolean(USER_VISIBLE_HINT_TAG, f.mUserVisibleHint);
		}
		return result;
	}

	public SavedState saveFragmentInstanceState(Fragment fragment) {
		SavedState r1_SavedState = null;
		if (fragment.mIndex < 0) {
			throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
		}
		if (fragment.mState > 0) {
			Bundle result = saveFragmentBasicState(fragment);
			if (result != null) {
				r1_SavedState = new SavedState(result);
			}
		}
		return r1_SavedState;
	}

	void saveFragmentViewState(Fragment f) {
		if (f.mInnerView == null) {
		} else {
			if (mStateArray == null) {
				mStateArray = new SparseArray();
			} else {
				mStateArray.clear();
			}
			f.mInnerView.saveHierarchyState(mStateArray);
			if (mStateArray.size() > 0) {
				f.mSavedViewState = mStateArray;
				mStateArray = null;
			}
		}
	}

	public void setBackStackIndex(int index, BackStackRecord bse) {
		synchronized(this) {
			if (mBackStackIndices == null) {
				mBackStackIndices = new ArrayList();
			}
			int N = mBackStackIndices.size();
			if (index < N) {
				if (DEBUG) {
					Log.v(TAG, "Setting back stack index " + index + " to " + bse);
				}
				mBackStackIndices.set(index, bse);
			} else {
				while (N < index) {
					mBackStackIndices.add(null);
					if (mAvailBackStackIndices == null) {
						mAvailBackStackIndices = new ArrayList();
					}
					if (DEBUG) {
						Log.v(TAG, "Adding available back stack index " + N);
					}
					mAvailBackStackIndices.add(Integer.valueOf(N));
					N++;
				}
				if (DEBUG) {
					Log.v(TAG, "Adding back stack index " + index + " with " + bse);
				}
				mBackStackIndices.add(bse);
			}
		}
	}

	public void showFragment(Fragment fragment, int transition, int transitionStyle) {
		if (DEBUG) {
			Log.v(TAG, "show: " + fragment);
		}
		if (fragment.mHidden) {
			fragment.mHidden = false;
			if (fragment.mView != null) {
				Animation anim = loadAnimation(fragment, transition, true, transitionStyle);
				if (anim != null) {
					fragment.mView.startAnimation(anim);
				}
				fragment.mView.setVisibility(0);
			}
			if (!fragment.mAdded || !fragment.mHasMenu || !fragment.mMenuVisible) {
				fragment.onHiddenChanged(HONEYCOMB);
			} else {
				mNeedMenuInvalidate = true;
				fragment.onHiddenChanged(HONEYCOMB);
			}
		}
	}

	void startPendingDeferredFragments() {
		if (mActive == null) {
		} else {
			int i = 0;
			while (i < mActive.size()) {
				Fragment f = (Fragment) mActive.get(i);
				if (f != null) {
					performPendingDeferredStart(f);
				}
				i++;
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		sb.append("FragmentManager{");
		sb.append(Integer.toHexString(System.identityHashCode(this)));
		sb.append(" in ");
		if (mParent != null) {
			DebugUtils.buildShortClassTag(mParent, sb);
		} else {
			DebugUtils.buildShortClassTag(mActivity, sb);
		}
		sb.append("}}");
		return sb.toString();
	}
}
