package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.app.Fragment.SavedState;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public abstract class FragmentManager {
	public static final int POP_BACK_STACK_INCLUSIVE = 1;

	public static interface BackStackEntry {
		public CharSequence getBreadCrumbShortTitle();

		public int getBreadCrumbShortTitleRes();

		public CharSequence getBreadCrumbTitle();

		public int getBreadCrumbTitleRes();

		public int getId();

		public String getName();
	}

	public static interface OnBackStackChangedListener {
		public void onBackStackChanged();
	}


	public FragmentManager() {
		super();
	}

	public static void enableDebugLogging(boolean enabled) {
		FragmentManagerImpl.DEBUG = enabled;
	}

	public abstract void addOnBackStackChangedListener(OnBackStackChangedListener r1_OnBackStackChangedListener);

	public abstract FragmentTransaction beginTransaction();

	public abstract void dump(String r1_String, FileDescriptor r2_FileDescriptor, PrintWriter r3_PrintWriter, String[] r4_String_A);

	public abstract boolean executePendingTransactions();

	public abstract Fragment findFragmentById(int r1i);

	public abstract Fragment findFragmentByTag(String r1_String);

	public abstract BackStackEntry getBackStackEntryAt(int r1i);

	public abstract int getBackStackEntryCount();

	public abstract Fragment getFragment(Bundle r1_Bundle, String r2_String);

	public abstract List<Fragment> getFragments();

	public abstract boolean isDestroyed();

	@Deprecated
	public FragmentTransaction openTransaction() {
		return beginTransaction();
	}

	public abstract void popBackStack();

	public abstract void popBackStack(int r1i, int r2i);

	public abstract void popBackStack(String r1_String, int r2i);

	public abstract boolean popBackStackImmediate();

	public abstract boolean popBackStackImmediate(int r1i, int r2i);

	public abstract boolean popBackStackImmediate(String r1_String, int r2i);

	public abstract void putFragment(Bundle r1_Bundle, String r2_String, Fragment r3_Fragment);

	public abstract void removeOnBackStackChangedListener(OnBackStackChangedListener r1_OnBackStackChangedListener);

	public abstract SavedState saveFragmentInstanceState(Fragment r1_Fragment);
}
