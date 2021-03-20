package android.support.v4.app;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class FragmentTransaction {
	public static final int TRANSIT_ENTER_MASK = 4096;
	public static final int TRANSIT_EXIT_MASK = 8192;
	public static final int TRANSIT_FRAGMENT_CLOSE = 8194;
	public static final int TRANSIT_FRAGMENT_FADE = 4099;
	public static final int TRANSIT_FRAGMENT_OPEN = 4097;
	public static final int TRANSIT_NONE = 0;
	public static final int TRANSIT_UNSET = -1;

	@IntDef({0, 4097, 8194})
	@Retention(RetentionPolicy.SOURCE)
	private static @interface Transit {
	}


	public FragmentTransaction() {
		super();
	}

	public abstract FragmentTransaction add(int r1i, Fragment r2_Fragment);

	public abstract FragmentTransaction add(int r1i, Fragment r2_Fragment, @Nullable String r3_String);

	public abstract FragmentTransaction add(Fragment r1_Fragment, String r2_String);

	public abstract FragmentTransaction addToBackStack(@Nullable String r1_String);

	public abstract FragmentTransaction attach(Fragment r1_Fragment);

	public abstract int commit();

	public abstract int commitAllowingStateLoss();

	public abstract FragmentTransaction detach(Fragment r1_Fragment);

	public abstract FragmentTransaction disallowAddToBackStack();

	public abstract FragmentTransaction hide(Fragment r1_Fragment);

	public abstract boolean isAddToBackStackAllowed();

	public abstract boolean isEmpty();

	public abstract FragmentTransaction remove(Fragment r1_Fragment);

	public abstract FragmentTransaction replace(int r1i, Fragment r2_Fragment);

	public abstract FragmentTransaction replace(int r1i, Fragment r2_Fragment, @Nullable String r3_String);

	public abstract FragmentTransaction setBreadCrumbShortTitle(int r1i);

	public abstract FragmentTransaction setBreadCrumbShortTitle(CharSequence r1_CharSequence);

	public abstract FragmentTransaction setBreadCrumbTitle(int r1i);

	public abstract FragmentTransaction setBreadCrumbTitle(CharSequence r1_CharSequence);

	public abstract FragmentTransaction setCustomAnimations(int r1i, int r2i);

	public abstract FragmentTransaction setCustomAnimations(int r1i, int r2i, int r3i, int r4i);

	public abstract FragmentTransaction setTransition(int r1i);

	public abstract FragmentTransaction setTransitionStyle(int r1i);

	public abstract FragmentTransaction show(Fragment r1_Fragment);
}
