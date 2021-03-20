package android.support.v4.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import java.util.ArrayList;

public class FragmentTabHost extends TabHost implements OnTabChangeListener {
	private boolean mAttached;
	private int mContainerId;
	private Context mContext;
	private FragmentManager mFragmentManager;
	private TabInfo mLastTab;
	private OnTabChangeListener mOnTabChangeListener;
	private FrameLayout mRealTabContent;
	private final ArrayList<TabInfo> mTabs;

	static /* synthetic */ class AnonymousClass_1 {
	}

	static class DummyTabFactory implements TabContentFactory {
		private final Context mContext;

		public DummyTabFactory(Context context) {
			super();
			mContext = context;
		}

		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	static class SavedState extends BaseSavedState {
		public static final Creator<FragmentTabHost.SavedState> CREATOR;
		String curTab;

		static class AnonymousClass_1 implements Creator<FragmentTabHost.SavedState> {
			AnonymousClass_1() {
				super();
			}

			public FragmentTabHost.SavedState createFromParcel(Parcel in) {
				return new FragmentTabHost.SavedState(in, null);
			}

			public FragmentTabHost.SavedState[] newArray(int size) {
				return new FragmentTabHost.SavedState[size];
			}
		}


		static {
			CREATOR = new AnonymousClass_1();
		}

		private SavedState(Parcel in) {
			super(in);
			curTab = in.readString();
		}

		/* synthetic */ SavedState(Parcel x0, FragmentTabHost.AnonymousClass_1 x1) {
			this(x0);
		}

		SavedState(Parcelable superState) {
			super(superState);
		}

		public String toString() {
			return "FragmentTabHost.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " curTab=" + curTab + "}";
		}

		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeString(curTab);
		}
	}

	static final class TabInfo {
		private final Bundle args;
		private final Class<?> clss;
		private Fragment fragment;
		private final String tag;

		TabInfo(String _tag, Class<?> _class, Bundle _args) {
			super();
			tag = _tag;
			clss = _class;
			args = _args;
		}

	}


	public FragmentTabHost(Context context) {
		super(context, null);
		mTabs = new ArrayList();
		initFragmentTabHost(context, null);
	}

	public FragmentTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTabs = new ArrayList();
		initFragmentTabHost(context, attrs);
	}

	/* JADX WARNING: inconsistent code */
	/*
	private android.support.v4.app.FragmentTransaction doTabChanged(java.lang.String r7_tabId, android.support.v4.app.FragmentTransaction r8_ft) {
		r6_this = this;
		r1 = 0;
		r0 = 0;
	L_0x0002:
		r3 = r6.mTabs;
		r3 = r3.size();
		if (r0_i >= r3) goto L_0x0020;
	L_0x000a:
		r3 = r6.mTabs;
		r2 = r3.get(r0_i);
		r2 = (android.support.v4.app.FragmentTabHost.TabInfo) r2;
		r3 = r2_tab.tag;
		r3 = r3.equals(r7_tabId);
		if (r3 == 0) goto L_0x001d;
	L_0x001c:
		r1_newTab = r2_tab;
	L_0x001d:
		r0_i++;
		goto L_0x0002;
	L_0x0020:
		if (r1_newTab != 0) goto L_0x003b;
	L_0x0022:
		r3 = new java.lang.IllegalStateException;
		r4 = new java.lang.StringBuilder;
		r4.<init>();
		r5 = "No tab known for tag ";
		r4 = r4.append(r5);
		r4 = r4.append(r7_tabId);
		r4 = r4.toString();
		r3.<init>(r4);
		throw r3;
	L_0x003b:
		r3 = r6.mLastTab;
		if (r3 == r1_newTab) goto L_0x0088;
	L_0x003f:
		if (r8_ft != 0) goto L_0x0047;
	L_0x0041:
		r3 = r6.mFragmentManager;
		r8_ft = r3.beginTransaction();
	L_0x0047:
		r3 = r6.mLastTab;
		if (r3 == 0) goto L_0x005c;
	L_0x004b:
		r3 = r6.mLastTab;
		r3 = r3.fragment;
		if (r3 == 0) goto L_0x005c;
	L_0x0053:
		r3 = r6.mLastTab;
		r3 = r3.fragment;
		r8_ft.detach(r3);
	L_0x005c:
		if (r1_newTab == 0) goto L_0x0086;
	L_0x005e:
		r3 = r1_newTab.fragment;
		if (r3 != 0) goto L_0x0089;
	L_0x0064:
		r3 = r6.mContext;
		r4 = r1_newTab.clss;
		r4 = r4.getName();
		r5 = r1_newTab.args;
		r3 = android.support.v4.app.Fragment.instantiate(r3, r4, r5);
		r1_newTab.fragment = r3;
		r3 = r6.mContainerId;
		r4 = r1_newTab.fragment;
		r5 = r1_newTab.tag;
		r8_ft.add(r3, r4, r5);
	L_0x0086:
		r6.mLastTab = r1_newTab;
	L_0x0088:
		return r8_ft;
	L_0x0089:
		r3 = r1_newTab.fragment;
		r8.attach(r3);
		goto L_0x0086;
	}
	*/
	private FragmentTransaction doTabChanged(String tabId, FragmentTransaction ft) {
		TabInfo newTab = null;
		int i = 0;
		while (i < mTabs.size()) {
			TabInfo tab = (TabInfo) mTabs.get(i);
			if (tab.tag.equals(tabId)) {
				newTab = tab;
			}
			i++;
		}
		if (newTab == null) {
			throw new IllegalStateException("No tab known for tag " + tabId);
		} else if (mLastTab != newTab) {
			if (ft == null) {
				ft = mFragmentManager.beginTransaction();
			}
			if (mLastTab == null || mLastTab.fragment == null) {
				mLastTab = newTab;
				return ft;
			} else {
				ft.detach(mLastTab.fragment);
				mLastTab = newTab;
				return ft;
			}
		} else {
			return ft;
		}
	}

	private void ensureContent() {
		if (mRealTabContent == null) {
			mRealTabContent = (FrameLayout) findViewById(mContainerId);
			if (mRealTabContent == null) {
				throw new IllegalStateException("No tab content FrameLayout found for id " + mContainerId);
			}
		}
	}

	private void ensureHierarchy(Context context) {
		int r4i = 16908307;
		if (findViewById(16908307) == null) {
			View ll = new LinearLayout(context);
			ll.setOrientation(1);
			addView(ll, new LayoutParams(-1, -1));
			View tw = new TabWidget(context);
			tw.setId(r4i);
			tw.setOrientation(0);
			ll.addView(tw, new android.widget.LinearLayout.LayoutParams(-1, -2, 0.0f));
			View fl = new FrameLayout(context);
			fl.setId(16908305);
			ll.addView(fl, new android.widget.LinearLayout.LayoutParams(0, 0, 0.0f));
			fl = new FrameLayout(context);
			mRealTabContent = fl;
			mRealTabContent.setId(mContainerId);
			ll.addView(fl, new android.widget.LinearLayout.LayoutParams(-1, 0, 1.0f));
		}
	}

	private void initFragmentTabHost(Context context, AttributeSet attrs) {
		int[] r1_int_A = new int[1];
		r1_int_A[0] = 16842995;
		TypedArray a = context.obtainStyledAttributes(attrs, r1_int_A, 0, 0);
		mContainerId = a.getResourceId(0, 0);
		a.recycle();
		super.setOnTabChangedListener(this);
	}

	public void addTab(TabSpec tabSpec, Class<?> clss, Bundle args) {
		tabSpec.setContent(new DummyTabFactory(mContext));
		String tag = tabSpec.getTag();
		TabInfo info = new TabInfo(tag, clss, args);
		if (mAttached) {
			info.fragment = mFragmentManager.findFragmentByTag(tag);
			if (info.fragment == null || info.fragment.isDetached()) {
				mTabs.add(info);
				addTab(tabSpec);
			} else {
				FragmentTransaction ft = mFragmentManager.beginTransaction();
				ft.detach(info.fragment);
				ft.commit();
			}
		}
		mTabs.add(info);
		addTab(tabSpec);
	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		String currentTab = getCurrentTabTag();
		FragmentTransaction ft = null;
		int i = 0;
		while (i < mTabs.size()) {
			TabInfo tab = (TabInfo) mTabs.get(i);
			tab.fragment = mFragmentManager.findFragmentByTag(tab.tag);
			if (tab.fragment == null || tab.fragment.isDetached()) {
				i++;
			} else if (tab.tag.equals(currentTab)) {
				mLastTab = tab;
				i++;
			} else {
				if (ft == null) {
					ft = mFragmentManager.beginTransaction();
				}
				ft.detach(tab.fragment);
				i++;
			}
		}
		mAttached = true;
		ft = doTabChanged(currentTab, ft);
		if (ft != null) {
			ft.commit();
			mFragmentManager.executePendingTransactions();
		}
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mAttached = false;
	}

	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		setCurrentTabByTag(ss.curTab);
	}

	protected Parcelable onSaveInstanceState() {
		SavedState ss = new SavedState(super.onSaveInstanceState());
		ss.curTab = getCurrentTabTag();
		return ss;
	}

	public void onTabChanged(String tabId) {
		if (mAttached) {
			FragmentTransaction ft = doTabChanged(tabId, null);
			if (ft != null) {
				ft.commit();
			}
		}
		if (mOnTabChangeListener != null) {
			mOnTabChangeListener.onTabChanged(tabId);
		}
	}

	public void setOnTabChangedListener(OnTabChangeListener l) {
		mOnTabChangeListener = l;
	}

	@Deprecated
	public void setup() {
		throw new IllegalStateException("Must call setup() that takes a Context and FragmentManager");
	}

	public void setup(Context context, FragmentManager manager) {
		ensureHierarchy(context);
		super.setup();
		mContext = context;
		mFragmentManager = manager;
		ensureContent();
	}

	public void setup(Context context, FragmentManager manager, int containerId) {
		ensureHierarchy(context);
		super.setup();
		mContext = context;
		mFragmentManager = manager;
		mContainerId = containerId;
		ensureContent();
		mRealTabContent.setId(containerId);
		if (getId() == -1) {
			setId(16908306);
		}
	}
}
