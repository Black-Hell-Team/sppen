package android.support.v4.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DialogFragment extends Fragment implements OnCancelListener, OnDismissListener {
	private static final String SAVED_BACK_STACK_ID = "android:backStackId";
	private static final String SAVED_CANCELABLE = "android:cancelable";
	private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
	private static final String SAVED_SHOWS_DIALOG = "android:showsDialog";
	private static final String SAVED_STYLE = "android:style";
	private static final String SAVED_THEME = "android:theme";
	public static final int STYLE_NORMAL = 0;
	public static final int STYLE_NO_FRAME = 2;
	public static final int STYLE_NO_INPUT = 3;
	public static final int STYLE_NO_TITLE = 1;
	int mBackStackId;
	boolean mCancelable;
	Dialog mDialog;
	boolean mDismissed;
	boolean mShownByMe;
	boolean mShowsDialog;
	int mStyle;
	int mTheme;
	boolean mViewDestroyed;

	@IntDef({0, 1, 2, 3})
	@Retention(RetentionPolicy.SOURCE)
	private static @interface DialogStyle {
	}


	public DialogFragment() {
		super();
		mStyle = 0;
		mTheme = 0;
		mCancelable = true;
		mShowsDialog = true;
		mBackStackId = -1;
	}

	public void dismiss() {
		dismissInternal(false);
	}

	public void dismissAllowingStateLoss() {
		dismissInternal(true);
	}

	void dismissInternal(boolean allowStateLoss) {
		if (mDismissed) {
		} else {
			mDismissed = true;
			mShownByMe = false;
			if (mDialog != null) {
				mDialog.dismiss();
				mDialog = null;
			}
			mViewDestroyed = true;
			if (mBackStackId >= 0) {
				getFragmentManager().popBackStack(mBackStackId, (int)STYLE_NO_TITLE);
				mBackStackId = -1;
			} else {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.remove(this);
				if (allowStateLoss) {
					ft.commitAllowingStateLoss();
				} else {
					ft.commit();
				}
			}
		}
	}

	public Dialog getDialog() {
		return mDialog;
	}

	public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
		if (!mShowsDialog) {
			return super.getLayoutInflater(savedInstanceState);
		} else {
			mDialog = onCreateDialog(savedInstanceState);
			switch(mStyle) {
			case STYLE_NO_TITLE:
			case STYLE_NO_FRAME:
				break;
			case STYLE_NO_INPUT:
				mDialog.getWindow().addFlags(24);
				break;
			default:
				if (mDialog == null) {
					return (LayoutInflater) mDialog.getContext().getSystemService("layout_inflater");
				} else {
					return (LayoutInflater) mActivity.getSystemService("layout_inflater");
				}
			}
			mDialog.requestWindowFeature(STYLE_NO_TITLE);
			if (mDialog == null) {
				return (LayoutInflater) mActivity.getSystemService("layout_inflater");
			} else {
				return (LayoutInflater) mDialog.getContext().getSystemService("layout_inflater");
			}
		}
	}

	public boolean getShowsDialog() {
		return mShowsDialog;
	}

	public int getTheme() {
		return mTheme;
	}

	public boolean isCancelable() {
		return mCancelable;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!mShowsDialog) {
		} else {
			View view = getView();
			if (view != null) {
				if (view.getParent() != null) {
					throw new IllegalStateException("DialogFragment can not be attached to a container view");
				} else {
					mDialog.setContentView(view);
				}
			}
			mDialog.setOwnerActivity(getActivity());
			mDialog.setCancelable(mCancelable);
			mDialog.setOnCancelListener(this);
			mDialog.setOnDismissListener(this);
			if (savedInstanceState != null) {
				Bundle dialogState = savedInstanceState.getBundle(SAVED_DIALOG_STATE_TAG);
				if (dialogState != null) {
					mDialog.onRestoreInstanceState(dialogState);
				}
			}
		}
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!mShownByMe) {
			mDismissed = false;
		}
	}

	public void onCancel(DialogInterface dialog) {
	}

	public void onCreate(Bundle savedInstanceState) {
		boolean r0z;
		boolean r1z = true;
		super.onCreate(savedInstanceState);
		if (mContainerId == 0) {
			r0z = true;
		} else {
			r0z = false;
		}
		mShowsDialog = r0z;
		if (savedInstanceState != null) {
			mStyle = savedInstanceState.getInt(SAVED_STYLE, STYLE_NORMAL);
			mTheme = savedInstanceState.getInt(SAVED_THEME, STYLE_NORMAL);
			mCancelable = savedInstanceState.getBoolean(SAVED_CANCELABLE, r1z);
			mShowsDialog = savedInstanceState.getBoolean(SAVED_SHOWS_DIALOG, mShowsDialog);
			mBackStackId = savedInstanceState.getInt(SAVED_BACK_STACK_ID, -1);
		}
	}

	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new Dialog(getActivity(), getTheme());
	}

	public void onDestroyView() {
		super.onDestroyView();
		if (mDialog != null) {
			mViewDestroyed = true;
			mDialog.dismiss();
			mDialog = null;
		}
	}

	public void onDetach() {
		super.onDetach();
		if (mShownByMe || mDismissed) {
		} else {
			mDismissed = true;
		}
	}

	public void onDismiss(DialogInterface dialog) {
		if (!mViewDestroyed) {
			dismissInternal(true);
		}
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mDialog != null) {
			Bundle dialogState = mDialog.onSaveInstanceState();
			if (dialogState != null) {
				outState.putBundle(SAVED_DIALOG_STATE_TAG, dialogState);
			}
		}
		if (mStyle != 0) {
			outState.putInt(SAVED_STYLE, mStyle);
		}
		if (mTheme != 0) {
			outState.putInt(SAVED_THEME, mTheme);
		}
		if (!mCancelable) {
			outState.putBoolean(SAVED_CANCELABLE, mCancelable);
		}
		if (!mShowsDialog) {
			outState.putBoolean(SAVED_SHOWS_DIALOG, mShowsDialog);
		}
		if (mBackStackId != -1) {
			outState.putInt(SAVED_BACK_STACK_ID, mBackStackId);
		}
	}

	public void onStart() {
		super.onStart();
		if (mDialog != null) {
			mViewDestroyed = false;
			mDialog.show();
		}
	}

	public void onStop() {
		super.onStop();
		if (mDialog != null) {
			mDialog.hide();
		}
	}

	public void setCancelable(boolean cancelable) {
		mCancelable = cancelable;
		if (mDialog != null) {
			mDialog.setCancelable(cancelable);
		}
	}

	public void setShowsDialog(boolean showsDialog) {
		mShowsDialog = showsDialog;
	}

	public void setStyle(int style, int theme) {
		mStyle = style;
		if (mStyle == 2 || mStyle == 3) {
			mTheme = 16973913;
		} else if (theme == 0) {
			mTheme = theme;
		}
		if (theme == 0) {
		} else {
			mTheme = theme;
		}
	}

	public int show(FragmentTransaction transaction, String tag) {
		mDismissed = false;
		mShownByMe = true;
		transaction.add((Fragment)this, tag);
		mViewDestroyed = false;
		mBackStackId = transaction.commit();
		return mBackStackId;
	}

	public void show(FragmentManager manager, String tag) {
		mDismissed = false;
		mShownByMe = true;
		FragmentTransaction ft = manager.beginTransaction();
		ft.add((Fragment)this, tag);
		ft.commit();
	}
}
