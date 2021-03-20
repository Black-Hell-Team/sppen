package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.v4.app.FragmentManagerImpl;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import com.elite.DeviceManager;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewPager extends ViewGroup {
	private static final int CLOSE_ENOUGH = 2;
	private static final Comparator<ItemInfo> COMPARATOR;
	private static final boolean DEBUG = false;
	private static final int DEFAULT_GUTTER_SIZE = 16;
	private static final int DEFAULT_OFFSCREEN_PAGES = 1;
	private static final int DRAW_ORDER_DEFAULT = 0;
	private static final int DRAW_ORDER_FORWARD = 1;
	private static final int DRAW_ORDER_REVERSE = 2;
	private static final int INVALID_POINTER = -1;
	private static final int[] LAYOUT_ATTRS;
	private static final int MAX_SETTLE_DURATION = 600;
	private static final int MIN_DISTANCE_FOR_FLING = 25;
	private static final int MIN_FLING_VELOCITY = 400;
	public static final int SCROLL_STATE_DRAGGING = 1;
	public static final int SCROLL_STATE_IDLE = 0;
	public static final int SCROLL_STATE_SETTLING = 2;
	private static final String TAG = "ViewPager";
	private static final boolean USE_CACHE = false;
	private static final Interpolator sInterpolator;
	private static final ViewPositionComparator sPositionComparator;
	private int mActivePointerId;
	private PagerAdapter mAdapter;
	private OnAdapterChangeListener mAdapterChangeListener;
	private int mBottomPageBounds;
	private boolean mCalledSuper;
	private int mChildHeightMeasureSpec;
	private int mChildWidthMeasureSpec;
	private int mCloseEnough;
	private int mCurItem;
	private int mDecorChildCount;
	private int mDefaultGutterSize;
	private int mDrawingOrder;
	private ArrayList<View> mDrawingOrderedChildren;
	private final Runnable mEndScrollRunnable;
	private int mExpectedAdapterCount;
	private long mFakeDragBeginTime;
	private boolean mFakeDragging;
	private boolean mFirstLayout;
	private float mFirstOffset;
	private int mFlingDistance;
	private int mGutterSize;
	private boolean mIgnoreGutter;
	private boolean mInLayout;
	private float mInitialMotionX;
	private float mInitialMotionY;
	private OnPageChangeListener mInternalPageChangeListener;
	private boolean mIsBeingDragged;
	private boolean mIsUnableToDrag;
	private final ArrayList<ItemInfo> mItems;
	private float mLastMotionX;
	private float mLastMotionY;
	private float mLastOffset;
	private EdgeEffectCompat mLeftEdge;
	private Drawable mMarginDrawable;
	private int mMaximumVelocity;
	private int mMinimumVelocity;
	private boolean mNeedCalculatePageOffsets;
	private PagerObserver mObserver;
	private int mOffscreenPageLimit;
	private OnPageChangeListener mOnPageChangeListener;
	private int mPageMargin;
	private PageTransformer mPageTransformer;
	private boolean mPopulatePending;
	private Parcelable mRestoredAdapterState;
	private ClassLoader mRestoredClassLoader;
	private int mRestoredCurItem;
	private EdgeEffectCompat mRightEdge;
	private int mScrollState;
	private Scroller mScroller;
	private boolean mScrollingCacheEnabled;
	private Method mSetChildrenDrawingOrderEnabled;
	private final ItemInfo mTempItem;
	private final Rect mTempRect;
	private int mTopPageBounds;
	private int mTouchSlop;
	private VelocityTracker mVelocityTracker;

	static class AnonymousClass_1 implements Comparator<ViewPager.ItemInfo> {
		AnonymousClass_1() {
			super();
		}

		public int compare(ViewPager.ItemInfo lhs, ViewPager.ItemInfo rhs) {
			return lhs.position - rhs.position;
		}
	}

	static class AnonymousClass_2 implements Interpolator {
		AnonymousClass_2() {
			super();
		}

		public float getInterpolation(float t) {
			t -= 1.0f;
			return ((((t * t) * t) * t) * t) + 1.0f;
		}
	}

	class AnonymousClass_3 implements Runnable {
		final /* synthetic */ ViewPager this$0;

		AnonymousClass_3(ViewPager r1_ViewPager) {
			super();
			this$0 = r1_ViewPager;
		}

		public void run() {
			this$0.setScrollState(SCROLL_STATE_IDLE);
			this$0.populate();
		}
	}

	static interface Decor {
	}

	static class ItemInfo {
		Object object;
		float offset;
		int position;
		boolean scrolling;
		float widthFactor;

		ItemInfo() {
			super();
		}
	}

	public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
		int childIndex;
		public int gravity;
		public boolean isDecor;
		boolean needsMeasure;
		int position;
		float widthFactor;

		public LayoutParams() {
			super(-1, -1);
			widthFactor = 0.0f;
		}

		public LayoutParams(Context context, AttributeSet attrs) {
			super(context, attrs);
			widthFactor = 0.0f;
			TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
			gravity = a.getInteger(SCROLL_STATE_IDLE, 48);
			a.recycle();
		}
	}

	static interface OnAdapterChangeListener {
		public void onAdapterChanged(PagerAdapter r1_PagerAdapter, PagerAdapter r2_PagerAdapter);
	}

	public static interface OnPageChangeListener {
		public void onPageScrollStateChanged(int r1i);

		public void onPageScrolled(int r1i, float r2f, int r3i);

		public void onPageSelected(int r1i);
	}

	public static interface PageTransformer {
		public void transformPage(View r1_View, float r2f);
	}

	private class PagerObserver extends DataSetObserver {
		final /* synthetic */ ViewPager this$0;

		private PagerObserver(ViewPager r1_ViewPager) {
			super();
			this$0 = r1_ViewPager;
		}

		/* synthetic */ PagerObserver(ViewPager x0, ViewPager.AnonymousClass_1 x1) {
			this(x0);
		}

		public void onChanged() {
			this$0.dataSetChanged();
		}

		public void onInvalidated() {
			this$0.dataSetChanged();
		}
	}

	public static class SavedState extends BaseSavedState {
		public static final Creator<ViewPager.SavedState> CREATOR;
		Parcelable adapterState;
		ClassLoader loader;
		int position;

		static class AnonymousClass_1 implements ParcelableCompatCreatorCallbacks<ViewPager.SavedState> {
			AnonymousClass_1() {
				super();
			}

			public ViewPager.SavedState createFromParcel(Parcel in, ClassLoader loader) {
				return new ViewPager.SavedState(in, loader);
			}

			public ViewPager.SavedState[] newArray(int size) {
				return new ViewPager.SavedState[size];
			}
		}


		static {
			CREATOR = ParcelableCompat.newCreator(new AnonymousClass_1());
		}

		SavedState(Parcel in, ClassLoader loader) {
			super(in);
			if (loader == null) {
				loader = getClass().getClassLoader();
			}
			position = in.readInt();
			adapterState = in.readParcelable(loader);
			this.loader = loader;
		}

		public SavedState(Parcelable superState) {
			super(superState);
		}

		public String toString() {
			return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + position + "}";
		}

		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(position);
			out.writeParcelable(adapterState, flags);
		}
	}

	static class ViewPositionComparator implements Comparator<View> {
		ViewPositionComparator() {
			super();
		}

		public int compare(View lhs, View rhs) {
			ViewPager.LayoutParams llp = (ViewPager.LayoutParams) lhs.getLayoutParams();
			ViewPager.LayoutParams rlp = (ViewPager.LayoutParams) rhs.getLayoutParams();
			if (llp.isDecor != rlp.isDecor) {
				if (llp.isDecor) {
					return SCROLL_STATE_DRAGGING;
				} else {
					return INVALID_POINTER;
				}
			} else {
				return llp.position - rlp.position;
			}
		}
	}

	class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
		final /* synthetic */ ViewPager this$0;

		MyAccessibilityDelegate(ViewPager r1_ViewPager) {
			super();
			this$0 = r1_ViewPager;
		}

		private boolean canScroll() {
			if (this$0.mAdapter == null || this$0.mAdapter.getCount() <= 1) {
				return DEBUG;
			} else {
				return true;
			}
		}

		public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
			super.onInitializeAccessibilityEvent(host, event);
			event.setClassName(ViewPager.class.getName());
			AccessibilityRecordCompat recordCompat = AccessibilityRecordCompat.obtain();
			recordCompat.setScrollable(canScroll());
			if (event.getEventType() != 4096 || this$0.mAdapter == null) {
			} else {
				recordCompat.setItemCount(this$0.mAdapter.getCount());
				recordCompat.setFromIndex(this$0.mCurItem);
				recordCompat.setToIndex(this$0.mCurItem);
			}
		}

		public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
			super.onInitializeAccessibilityNodeInfo(host, info);
			info.setClassName(ViewPager.class.getName());
			info.setScrollable(canScroll());
			if (this$0.canScrollHorizontally(SCROLL_STATE_DRAGGING)) {
				info.addAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
			}
			if (this$0.canScrollHorizontally(INVALID_POINTER)) {
				info.addAction(AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
			}
		}

		public boolean performAccessibilityAction(View host, int action, Bundle args) {
			if (super.performAccessibilityAction(host, action, args)) {
				return true;
			} else {
				switch(action) {
				case AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD:
					if (this$0.canScrollHorizontally(SCROLL_STATE_DRAGGING)) {
						this$0.setCurrentItem(this$0.mCurItem + 1);
						return true;
					} else {
						return false;
					}
				case AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD:
					if (this$0.canScrollHorizontally(INVALID_POINTER)) {
						this$0.setCurrentItem(this$0.mCurItem - 1);
						return true;
					} else {
						return false;
					}
				}
				return false;
			}
		}
	}

	public static class SimpleOnPageChangeListener implements ViewPager.OnPageChangeListener {
		public SimpleOnPageChangeListener() {
			super();
		}

		public void onPageScrollStateChanged(int state) {
		}

		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		public void onPageSelected(int position) {
		}
	}


	static {
		int[] r0_int_A = new int[1];
		r0_int_A[0] = 16842931;
		LAYOUT_ATTRS = r0_int_A;
		COMPARATOR = new AnonymousClass_1();
		sInterpolator = new AnonymousClass_2();
		sPositionComparator = new ViewPositionComparator();
	}

	public ViewPager(Context context) {
		super(context);
		mItems = new ArrayList();
		mTempItem = new ItemInfo();
		mTempRect = new Rect();
		mRestoredCurItem = -1;
		mRestoredAdapterState = null;
		mRestoredClassLoader = null;
		mFirstOffset = -3.4028235E38f;
		mLastOffset = 3.4028235E38f;
		mOffscreenPageLimit = 1;
		mActivePointerId = -1;
		mFirstLayout = true;
		mNeedCalculatePageOffsets = false;
		mEndScrollRunnable = new AnonymousClass_3(this);
		mScrollState = 0;
		initViewPager();
	}

	public ViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mItems = new ArrayList();
		mTempItem = new ItemInfo();
		mTempRect = new Rect();
		mRestoredCurItem = -1;
		mRestoredAdapterState = null;
		mRestoredClassLoader = null;
		mFirstOffset = -3.4028235E38f;
		mLastOffset = 3.4028235E38f;
		mOffscreenPageLimit = 1;
		mActivePointerId = -1;
		mFirstLayout = true;
		mNeedCalculatePageOffsets = false;
		mEndScrollRunnable = new AnonymousClass_3(this);
		mScrollState = 0;
		initViewPager();
	}

	/* JADX WARNING: inconsistent code */
	/*
	private void calculatePageOffsets(android.support.v4.view.ViewPager.ItemInfo r15_curItem, int r16_curIndex, android.support.v4.view.ViewPager.ItemInfo r17_oldCurInfo) {
		r14_this = this;
		r12 = r14.mAdapter;
		r1 = r12.getCount();
		r11 = r14.getClientWidth();
		if (r11_width <= 0) goto L_0x0058;
	L_0x000c:
		r12 = r14.mPageMargin;
		r12 = (float) r12;
		r13 = (float) r11_width;
		r6 = r12 / r13;
	L_0x0012:
		if (r17_oldCurInfo == 0) goto L_0x00bc;
	L_0x0014:
		r0 = r17_oldCurInfo;
		r8 = r0.position;
		r12 = r15_curItem.position;
		if (r8_oldCurPosition >= r12) goto L_0x0072;
	L_0x001c:
		r5 = 0;
		r3 = 0;
		r0 = r17_oldCurInfo;
		r12 = r0.offset;
		r0 = r17_oldCurInfo;
		r13 = r0.widthFactor;
		r12 += r13;
		r7_offset = r12 + r6_marginOffset;
		r9 = r8_oldCurPosition + 1;
	L_0x002b:
		r12 = r15_curItem.position;
		if (r9_pos > r12) goto L_0x00bc;
	L_0x002f:
		r12 = r14.mItems;
		r12 = r12.size();
		if (r5_itemIndex >= r12) goto L_0x00bc;
	L_0x0037:
		r12 = r14.mItems;
		r3_ii = r12.get(r5_itemIndex);
		r3_ii = (android.support.v4.view.ViewPager.ItemInfo) r3_ii;
	L_0x003f:
		r12 = r3_ii.position;
		if (r9_pos <= r12) goto L_0x005a;
	L_0x0043:
		r12 = r14.mItems;
		r12 = r12.size();
		r12++;
		if (r5_itemIndex >= r12) goto L_0x005a;
	L_0x004d:
		r5_itemIndex++;
		r12 = r14.mItems;
		r3_ii = r12.get(r5_itemIndex);
		r3_ii = (android.support.v4.view.ViewPager.ItemInfo) r3_ii;
		goto L_0x003f;
	L_0x0058:
		r6_marginOffset = 0;
		goto L_0x0012;
	L_0x005a:
		r12 = r3_ii.position;
		if (r9_pos >= r12) goto L_0x0069;
	L_0x005e:
		r12 = r14.mAdapter;
		r12 = r12.getPageWidth(r9_pos);
		r12 += r6_marginOffset;
		r7_offset += r12;
		r9_pos++;
		goto L_0x005a;
	L_0x0069:
		r3_ii.offset = r7_offset;
		r12 = r3_ii.widthFactor;
		r12 += r6_marginOffset;
		r7_offset += r12;
		r9_pos++;
		goto L_0x002b;
	L_0x0072:
		r12 = r15_curItem.position;
		if (r8_oldCurPosition <= r12) goto L_0x00bc;
	L_0x0076:
		r12 = r14.mItems;
		r12 = r12.size();
		r5_itemIndex = r12 + -1;
		r3_ii = 0;
		r0 = r17_oldCurInfo;
		r7_offset = r0.offset;
		r9_pos = r8_oldCurPosition + -1;
	L_0x0085:
		r12 = r15_curItem.position;
		if (r9_pos < r12) goto L_0x00bc;
	L_0x0089:
		if (r5_itemIndex < 0) goto L_0x00bc;
	L_0x008b:
		r12 = r14.mItems;
		r3_ii = r12.get(r5_itemIndex);
		r3_ii = (android.support.v4.view.ViewPager.ItemInfo) r3_ii;
	L_0x0093:
		r12 = r3_ii.position;
		if (r9_pos >= r12) goto L_0x00a4;
	L_0x0097:
		if (r5_itemIndex <= 0) goto L_0x00a4;
	L_0x0099:
		r5_itemIndex++;
		r12 = r14.mItems;
		r3_ii = r12.get(r5_itemIndex);
		r3_ii = (android.support.v4.view.ViewPager.ItemInfo) r3_ii;
		goto L_0x0093;
	L_0x00a4:
		r12 = r3_ii.position;
		if (r9_pos <= r12) goto L_0x00b3;
	L_0x00a8:
		r12 = r14.mAdapter;
		r12 = r12.getPageWidth(r9_pos);
		r12 += r6_marginOffset;
		r7_offset -= r12;
		r9_pos++;
		goto L_0x00a4;
	L_0x00b3:
		r12 = r3_ii.widthFactor;
		r12 += r6_marginOffset;
		r7_offset -= r12;
		r3_ii.offset = r7_offset;
		r9_pos++;
		goto L_0x0085;
	L_0x00bc:
		r12 = r14.mItems;
		r4 = r12.size();
		r7_offset = r15_curItem.offset;
		r12 = r15_curItem.position;
		r9_pos = r12 + -1;
		r12 = r15_curItem.position;
		if (r12 != 0) goto L_0x00fc;
	L_0x00cc:
		r12 = r15_curItem.offset;
	L_0x00ce:
		r14.mFirstOffset = r12;
		r12 = r15_curItem.position;
		r13 = r1_N + -1;
		if (r12 != r13) goto L_0x0100;
	L_0x00d6:
		r12 = r15_curItem.offset;
		r13 = r15_curItem.widthFactor;
		r12 += r13;
		r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
		r12 -= r13;
	L_0x00de:
		r14.mLastOffset = r12;
		r2 = r16_curIndex + -1;
	L_0x00e2:
		if (r2_i < 0) goto L_0x0115;
	L_0x00e4:
		r12 = r14.mItems;
		r3_ii = r12.get(r2_i);
		r3_ii = (android.support.v4.view.ViewPager.ItemInfo) r3_ii;
	L_0x00ec:
		r12 = r3_ii.position;
		if (r9_pos <= r12) goto L_0x0104;
	L_0x00f0:
		r12 = r14.mAdapter;
		r10 = r9_pos + -1;
		r12 = r12.getPageWidth(r9_pos);
		r12 += r6_marginOffset;
		r7_offset -= r12;
		r9_pos = r10_pos;
		goto L_0x00ec;
	L_0x00fc:
		r12 = -8388609; // 0xffffffffff7fffff float:-3.4028235E38 double:NaN;
		goto L_0x00ce;
	L_0x0100:
		r12 = 2139095039; // 0x7f7fffff float:3.4028235E38 double:1.056853372E-314;
		goto L_0x00de;
	L_0x0104:
		r12 = r3_ii.widthFactor;
		r12 += r6_marginOffset;
		r7_offset -= r12;
		r3_ii.offset = r7_offset;
		r12 = r3_ii.position;
		if (r12 != 0) goto L_0x0110;
	L_0x010e:
		r14.mFirstOffset = r7_offset;
	L_0x0110:
		r2_i++;
		r9_pos++;
		goto L_0x00e2;
	L_0x0115:
		r12 = r15_curItem.offset;
		r13 = r15_curItem.widthFactor;
		r12 += r13;
		r7_offset = r12 + r6_marginOffset;
		r12 = r15_curItem.position;
		r9_pos = r12 + 1;
		r2_i = r16_curIndex + 1;
	L_0x0122:
		if (r2_i >= r4_itemCount) goto L_0x0155;
	L_0x0124:
		r12 = r14.mItems;
		r3_ii = r12.get(r2_i);
		r3_ii = (android.support.v4.view.ViewPager.ItemInfo) r3_ii;
	L_0x012c:
		r12 = r3_ii.position;
		if (r9_pos >= r12) goto L_0x013c;
	L_0x0130:
		r12 = r14.mAdapter;
		r10_pos = r9_pos + 1;
		r12 = r12.getPageWidth(r9_pos);
		r12 += r6_marginOffset;
		r7_offset += r12;
		r9_pos = r10_pos;
		goto L_0x012c;
	L_0x013c:
		r12 = r3_ii.position;
		r13 = r1_N + -1;
		if (r12 != r13) goto L_0x014a;
	L_0x0142:
		r12 = r3_ii.widthFactor;
		r12 += r7_offset;
		r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
		r12 -= r13;
		r14.mLastOffset = r12;
	L_0x014a:
		r3_ii.offset = r7_offset;
		r12 = r3_ii.widthFactor;
		r12 += r6_marginOffset;
		r7_offset += r12;
		r2_i++;
		r9_pos++;
		goto L_0x0122;
	L_0x0155:
		r12 = 0;
		r14.mNeedCalculatePageOffsets = r12;
		return;
	}
	*/
	private void calculatePageOffsets(ItemInfo curItem, int curIndex, ItemInfo oldCurInfo) {
		float marginOffset;
		float offset;
		int pos;
		ItemInfo ii;
		float r12f;
		int N = mAdapter.getCount();
		int width = getClientWidth();
		if (width > 0) {
			marginOffset = ((float) mPageMargin) / ((float) width);
		} else {
			marginOffset = AutoScrollHelper.RELATIVE_UNSPECIFIED;
		}
		if (oldCurInfo != null) {
			int oldCurPosition = oldCurInfo.position;
			int itemIndex;
			if (oldCurPosition < curItem.position) {
				itemIndex = SCROLL_STATE_IDLE;
				offset = (oldCurInfo.offset + oldCurInfo.widthFactor) + marginOffset;
				pos = oldCurPosition + 1;
				while (pos <= curItem.position && itemIndex < mItems.size()) {
					ii = mItems.get(itemIndex);
					while (pos > ii.position && itemIndex < mItems.size() - 1) {
						itemIndex++;
						ii = mItems.get(itemIndex);
					}
				}
			} else if (oldCurPosition > curItem.position) {
				itemIndex = mItems.size() - 1;
				offset = oldCurInfo.offset;
				pos = oldCurPosition - 1;
				while (pos >= curItem.position && itemIndex >= 0) {
					ii = mItems.get(itemIndex);
					while (pos < ii.position && itemIndex > 0) {
						itemIndex--;
						ii = mItems.get(itemIndex);
					}
				}
			}
		}
		int itemCount = mItems.size();
		offset = curItem.offset;
		pos = curItem.position - 1;
		if (curItem.position == 0) {
			r12f = curItem.offset;
		} else {
			r12f = -3.4028235E38f;
		}
		mFirstOffset = r12f;
		if (curItem.position == N - 1) {
			r12f = (curItem.offset + curItem.widthFactor) - 1.0f;
		} else {
			r12f = AutoScrollHelper.NO_MAX;
		}
		mLastOffset = r12f;
		int i = curIndex - 1;
		while (i >= 0) {
			ii = mItems.get(i);
			while (pos > ii.position) {
				offset -= mAdapter.getPageWidth(pos) + marginOffset;
				pos = pos - 1;
			}
			offset -= ii.widthFactor + marginOffset;
			ii.offset = offset;
			if (ii.position == 0) {
				mFirstOffset = offset;
			}
			i--;
		}
		offset = (curItem.offset + curItem.widthFactor) + marginOffset;
		pos = curItem.position + 1;
		i = curIndex + 1;
		while (i < itemCount) {
			ii = mItems.get(i);
			while (pos < ii.position) {
				offset += mAdapter.getPageWidth(pos) + marginOffset;
				pos = pos + 1;
			}
			if (ii.position == N - 1) {
				mLastOffset = (ii.widthFactor + offset) - 1.0f;
			}
			ii.offset = offset;
			offset += ii.widthFactor + marginOffset;
			i++;
		}
		mNeedCalculatePageOffsets = false;
	}

	private void completeScroll(boolean postEvents) {
		boolean needPopulate;
		if (mScrollState == 2) {
			needPopulate = true;
		}
		if (needPopulate) {
			setScrollingCacheEnabled(DEBUG);
			mScroller.abortAnimation();
			int oldY = getScrollY();
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();
			if (getScrollX() != x || oldY != y) {
				scrollTo(x, y);
			}
		}
		mPopulatePending = false;
		int i = SCROLL_STATE_IDLE;
		while (i < mItems.size()) {
			ItemInfo ii = (ItemInfo) mItems.get(i);
			if (ii.scrolling) {
				needPopulate = true;
				ii.scrolling = false;
			}
			i++;
		}
		if (needPopulate) {
			if (postEvents) {
				ViewCompat.postOnAnimation(this, mEndScrollRunnable);
			} else {
				mEndScrollRunnable.run();
			}
		}
	}

	private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
		int targetPage;
		if (Math.abs(deltaX) <= mFlingDistance || Math.abs(velocity) <= mMinimumVelocity) {
			float truncator;
			if (currentPage >= mCurItem) {
				truncator = 0.4f;
			} else {
				truncator = 0.6f;
			}
			targetPage = (int) ((((float) currentPage) + pageOffset) + truncator);
		} else if (velocity > 0) {
			targetPage = currentPage;
		} else {
			targetPage = currentPage + 1;
		}
		if (mItems.size() > 0) {
			return Math.max(((ItemInfo) mItems.get(SCROLL_STATE_IDLE)).position, Math.min(targetPage, ((ItemInfo) mItems.get(mItems.size() - 1)).position));
		} else {
			return targetPage;
		}
	}

	private void enableLayers(boolean enable) {
		int i = SCROLL_STATE_IDLE;
		while (i < getChildCount()) {
			int layerType;
			if (enable) {
				layerType = SCROLL_STATE_SETTLING;
			} else {
				layerType = SCROLL_STATE_IDLE;
			}
			ViewCompat.setLayerType(getChildAt(i), layerType, null);
			i++;
		}
	}

	private void endDrag() {
		mIsBeingDragged = false;
		mIsUnableToDrag = false;
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
		if (outRect == null) {
			outRect = new Rect();
		}
		if (child == null) {
			outRect.set(SCROLL_STATE_IDLE, SCROLL_STATE_IDLE, SCROLL_STATE_IDLE, SCROLL_STATE_IDLE);
			return outRect;
		} else {
			outRect.left = child.getLeft();
			outRect.right = child.getRight();
			outRect.top = child.getTop();
			outRect.bottom = child.getBottom();
			ViewPager parent = child.getParent();
			while (parent instanceof ViewGroup) {
				if (parent != this) {
					ViewGroup group = parent;
					outRect.left += group.getLeft();
					outRect.right += group.getRight();
					outRect.top += group.getTop();
					outRect.bottom += group.getBottom();
					parent = group.getParent();
				} else {
					return outRect;
				}
			}
			return outRect;
		}
	}

	private int getClientWidth() {
		return (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
	}

	private ItemInfo infoForCurrentScrollPosition() {
		float scrollOffset;
		float marginOffset = AutoScrollHelper.RELATIVE_UNSPECIFIED;
		int width = getClientWidth();
		if (width > 0) {
			scrollOffset = ((float) getScrollX()) / ((float) width);
		} else {
			scrollOffset = 0.0f;
		}
		if (width > 0) {
			marginOffset = ((float) mPageMargin) / ((float) width);
		}
		int lastPos = INVALID_POINTER;
		float lastOffset = AutoScrollHelper.RELATIVE_UNSPECIFIED;
		float lastWidth = AutoScrollHelper.RELATIVE_UNSPECIFIED;
		boolean first = true;
		ItemInfo lastItem = null;
		int i = SCROLL_STATE_IDLE;
		while (i < mItems.size()) {
			ItemInfo ii = (ItemInfo) mItems.get(i);
			float offset;
			float rightBound;
			if (first || ii.position == lastPos + 1) {
				offset = ii.offset;
				leftBound = offset;
				rightBound = (ii.widthFactor + offset) + marginOffset;
				if (first || scrollOffset >= leftBound) {
					if (scrollOffset < rightBound || i == mItems.size() - 1) {
						return ii;
					} else {
						first = DEBUG;
						lastPos = ii.position;
						lastOffset = offset;
						lastWidth = ii.widthFactor;
						lastItem = ii;
					}
				} else {
					return lastItem;
				}
			} else {
				ii = mTempItem;
				ii.offset = (lastOffset + lastWidth) + marginOffset;
				ii.position = lastPos + 1;
				ii.widthFactor = mAdapter.getPageWidth(ii.position);
				i--;
				offset = ii.offset;
				leftBound = offset;
				rightBound = (ii.widthFactor + offset) + marginOffset;
				if (first || scrollOffset >= leftBound) {
					if (scrollOffset < rightBound || i == mItems.size() - 1) {
						return ii;
					} else {
						first = DEBUG;
						lastPos = ii.position;
						lastOffset = offset;
						lastWidth = ii.widthFactor;
						lastItem = ii;
					}
				} else {
					return lastItem;
				}
			}
		}
		return lastItem;
	}

	private boolean isGutterDrag(float x, float dx) {
		if (x >= ((float) mGutterSize) || dx <= 0.0f) {
			if (x <= ((float) (getWidth() - mGutterSize)) || dx >= 0.0f) {
				return DEBUG;
			}
		} else {
			return true;
		}
		return true;
	}

	private void onSecondaryPointerUp(MotionEvent ev) {
		int pointerIndex = MotionEventCompat.getActionIndex(ev);
		if (MotionEventCompat.getPointerId(ev, pointerIndex) == mActivePointerId) {
			int newPointerIndex;
			if (pointerIndex == 0) {
				newPointerIndex = SCROLL_STATE_DRAGGING;
			} else {
				newPointerIndex = SCROLL_STATE_IDLE;
			}
			mLastMotionX = MotionEventCompat.getX(ev, newPointerIndex);
			mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
			if (mVelocityTracker != null) {
				mVelocityTracker.clear();
			}
		}
	}

	private boolean pageScrolled(int xpos) {
		boolean r7z = DEBUG;
		if (mItems.size() == 0) {
			mCalledSuper = false;
			onPageScrolled(SCROLL_STATE_IDLE, AutoScrollHelper.RELATIVE_UNSPECIFIED, SCROLL_STATE_IDLE);
			if (!mCalledSuper) {
				throw new IllegalStateException("onPageScrolled did not call superclass implementation");
			}
		} else {
			ItemInfo ii = infoForCurrentScrollPosition();
			int width = getClientWidth();
			float pageOffset = ((((float) xpos) / ((float) width)) - ii.offset) / (ii.widthFactor + (((float) mPageMargin) / ((float) width)));
			mCalledSuper = r7z;
			onPageScrolled(ii.position, pageOffset, (int) (((float) (width + mPageMargin)) * pageOffset));
			if (!mCalledSuper) {
				throw new IllegalStateException("onPageScrolled did not call superclass implementation");
			} else {
				r7z = true;
			}
		}
		return r7z;
	}

	private boolean performDrag(float x) {
		boolean needsInvalidate = DEBUG;
		mLastMotionX = x;
		float scrollX = ((float) getScrollX()) + (mLastMotionX - x);
		int width = getClientWidth();
		float leftBound = ((float) width) * mFirstOffset;
		float rightBound = ((float) width) * mLastOffset;
		boolean leftAbsolute = true;
		boolean rightAbsolute = true;
		ItemInfo firstItem = (ItemInfo) mItems.get(SCROLL_STATE_IDLE);
		ItemInfo lastItem = (ItemInfo) mItems.get(mItems.size() - 1);
		if (firstItem.position != 0) {
			leftAbsolute = DEBUG;
			leftBound = firstItem.offset * ((float) width);
		}
		if (lastItem.position != mAdapter.getCount() - 1) {
			rightAbsolute = DEBUG;
			rightBound = lastItem.offset * ((float) width);
		}
		if (scrollX < leftBound) {
			if (leftAbsolute) {
				needsInvalidate = mLeftEdge.onPull(Math.abs(leftBound - scrollX) / ((float) width));
			}
			scrollX = leftBound;
		} else if (scrollX > rightBound) {
			if (rightAbsolute) {
				needsInvalidate = mRightEdge.onPull(Math.abs(scrollX - rightBound) / ((float) width));
			}
			scrollX = rightBound;
		}
		mLastMotionX += scrollX - ((float) ((int) scrollX));
		scrollTo((int) scrollX, getScrollY());
		pageScrolled((int) scrollX);
		return needsInvalidate;
	}

	private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
		if (oldWidth <= 0 || mItems.isEmpty()) {
			float scrollOffset;
			ItemInfo ii = infoForPosition(mCurItem);
			if (ii != null) {
				scrollOffset = Math.min(ii.offset, mLastOffset);
			} else {
				scrollOffset = AutoScrollHelper.RELATIVE_UNSPECIFIED;
			}
			int scrollPos = (int) (((float) ((width - getPaddingLeft()) - getPaddingRight())) * scrollOffset);
			if (scrollPos != getScrollX()) {
				completeScroll(DEBUG);
				scrollTo(scrollPos, getScrollY());
			}
		} else {
			int newOffsetPixels = (int) (((float) (((width - getPaddingLeft()) - getPaddingRight()) + margin)) * (((float) getScrollX()) / ((float) (((oldWidth - getPaddingLeft()) - getPaddingRight()) + oldMargin))));
			scrollTo(newOffsetPixels, getScrollY());
			if (!mScroller.isFinished()) {
				mScroller.startScroll(newOffsetPixels, SCROLL_STATE_IDLE, (int) (infoForPosition(mCurItem).offset * ((float) width)), SCROLL_STATE_IDLE, mScroller.getDuration() - mScroller.timePassed());
			}
		}
	}

	private void removeNonDecorViews() {
		int i = SCROLL_STATE_IDLE;
		while (i < getChildCount()) {
			if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
				removeViewAt(i);
				i--;
			}
		}
	}

	private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
		ViewParent parent = getParent();
		if (parent != null) {
			parent.requestDisallowInterceptTouchEvent(disallowIntercept);
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	private void scrollToItem(int r9_item, boolean r10_smoothScroll, int r11_velocity, boolean r12_dispatchSelected) {
		r8_this = this;
		r7 = 0;
		r0 = r8.infoForPosition(r9_item);
		r1 = 0;
		if (r0_curInfo == 0) goto L_0x001d;
	L_0x0008:
		r2 = r8.getClientWidth();
		r3 = (float) r2_width;
		r4 = r8.mFirstOffset;
		r5 = r0_curInfo.offset;
		r6 = r8.mLastOffset;
		r5 = java.lang.Math.min(r5, r6);
		r4 = java.lang.Math.max(r4, r5);
		r3 *= r4;
		r1_destX = (int) r3;
	L_0x001d:
		if (r10_smoothScroll == 0) goto L_0x0039;
	L_0x001f:
		r8.smoothScrollTo(r1_destX, r7, r11_velocity);
		if (r12_dispatchSelected == 0) goto L_0x002d;
	L_0x0024:
		r3 = r8.mOnPageChangeListener;
		if (r3 == 0) goto L_0x002d;
	L_0x0028:
		r3 = r8.mOnPageChangeListener;
		r3.onPageSelected(r9_item);
	L_0x002d:
		if (r12_dispatchSelected == 0) goto L_0x0038;
	L_0x002f:
		r3 = r8.mInternalPageChangeListener;
		if (r3 == 0) goto L_0x0038;
	L_0x0033:
		r3 = r8.mInternalPageChangeListener;
		r3.onPageSelected(r9_item);
	L_0x0038:
		return;
	L_0x0039:
		if (r12_dispatchSelected == 0) goto L_0x0044;
	L_0x003b:
		r3 = r8.mOnPageChangeListener;
		if (r3 == 0) goto L_0x0044;
	L_0x003f:
		r3 = r8.mOnPageChangeListener;
		r3.onPageSelected(r9_item);
	L_0x0044:
		if (r12_dispatchSelected == 0) goto L_0x004f;
	L_0x0046:
		r3 = r8.mInternalPageChangeListener;
		if (r3 == 0) goto L_0x004f;
	L_0x004a:
		r3 = r8.mInternalPageChangeListener;
		r3.onPageSelected(r9_item);
	L_0x004f:
		r8.completeScroll(r7);
		r8.scrollTo(r1_destX, r7);
		r8.pageScrolled(r1_destX);
		goto L_0x0038;
	}
	*/
	private void scrollToItem(int item, boolean smoothScroll, int velocity, boolean dispatchSelected) {
		ItemInfo curInfo = infoForPosition(item);
		int destX = SCROLL_STATE_IDLE;
		if (curInfo != null) {
			destX = (int) (((float) getClientWidth()) * Math.max(mFirstOffset, Math.min(curInfo.offset, mLastOffset)));
		}
		if (smoothScroll) {
			smoothScrollTo(destX, SCROLL_STATE_IDLE, velocity);
			if (!dispatchSelected || mOnPageChangeListener == null) {
			} else {
				mOnPageChangeListener.onPageSelected(item);
			}
		} else if (!dispatchSelected || mOnPageChangeListener == null) {
			completeScroll(DEBUG);
			scrollTo(destX, SCROLL_STATE_IDLE);
			pageScrolled(destX);
		} else {
			mOnPageChangeListener.onPageSelected(item);
			completeScroll(DEBUG);
			scrollTo(destX, SCROLL_STATE_IDLE);
			pageScrolled(destX);
		}
	}

	private void setScrollState(int newState) {
		if (mScrollState == newState) {
		} else {
			mScrollState = newState;
			if (mPageTransformer != null) {
				boolean r0z;
				if (newState != 0) {
					r0z = true;
				} else {
					r0z = DEBUG;
				}
				enableLayers(r0z);
			}
			if (mOnPageChangeListener != null) {
				mOnPageChangeListener.onPageScrollStateChanged(newState);
			}
		}
	}

	private void setScrollingCacheEnabled(boolean enabled) {
		if (mScrollingCacheEnabled != enabled) {
			mScrollingCacheEnabled = enabled;
		}
	}

	private void sortChildDrawingOrder() {
		if (mDrawingOrder != 0) {
			if (mDrawingOrderedChildren == null) {
				mDrawingOrderedChildren = new ArrayList();
			} else {
				mDrawingOrderedChildren.clear();
			}
			int i = SCROLL_STATE_IDLE;
			while (i < getChildCount()) {
				mDrawingOrderedChildren.add(getChildAt(i));
				i++;
			}
			Collections.sort(mDrawingOrderedChildren, sPositionComparator);
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	public void addFocusables(java.util.ArrayList<android.view.View> r8_views, int r9_direction, int r10_focusableMode) {
		r7_this = this;
		r2 = r8_views.size();
		r1 = r7.getDescendantFocusability();
		r5 = 393216; // 0x60000 float:5.51013E-40 double:1.942745E-318;
		if (r1_descendantFocusability == r5) goto L_0x002f;
	L_0x000c:
		r3 = 0;
	L_0x000d:
		r5 = r7.getChildCount();
		if (r3_i >= r5) goto L_0x002f;
	L_0x0013:
		r0 = r7.getChildAt(r3_i);
		r5 = r0_child.getVisibility();
		if (r5 != 0) goto L_0x002c;
	L_0x001d:
		r4 = r7.infoForChild(r0_child);
		if (r4_ii == 0) goto L_0x002c;
	L_0x0023:
		r5 = r4_ii.position;
		r6 = r7.mCurItem;
		if (r5 != r6) goto L_0x002c;
	L_0x0029:
		r0_child.addFocusables(r8_views, r9_direction, r10_focusableMode);
	L_0x002c:
		r3_i++;
		goto L_0x000d;
	L_0x002f:
		r5 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
		if (r1_descendantFocusability != r5) goto L_0x0039;
	L_0x0033:
		r5 = r8_views.size();
		if (r2_focusableCount != r5) goto L_0x003f;
	L_0x0039:
		r5 = r7.isFocusable();
		if (r5 != 0) goto L_0x0040;
	L_0x003f:
		return;
	L_0x0040:
		r5 = r10_focusableMode & 1;
		r6 = 1;
		if (r5 != r6) goto L_0x0051;
	L_0x0045:
		r5 = r7.isInTouchMode();
		if (r5 == 0) goto L_0x0051;
	L_0x004b:
		r5 = r7.isFocusableInTouchMode();
		if (r5 == 0) goto L_0x003f;
	L_0x0051:
		if (r8_views == 0) goto L_0x003f;
	L_0x0053:
		r8_views.add(r7);
		goto L_0x003f;
	}
	*/
	public void addFocusables(ArrayList<View> r8_ArrayList_View, int direction, int focusableMode) {
		int focusableCount = views.size();
		int descendantFocusability = getDescendantFocusability();
		if (descendantFocusability != 393216) {
			int i = SCROLL_STATE_IDLE;
			while (i < getChildCount()) {
				View child = getChildAt(i);
				if (child.getVisibility() == 0) {
					ItemInfo ii = infoForChild(child);
					if (ii == null || ii.position != mCurItem) {
						i++;
					} else {
						child.addFocusables(views, direction, focusableMode);
					}
				}
				i++;
			}
		}
		if ((descendantFocusability != 262144 || focusableCount == views.size()) && isFocusable()) {
			if (((focusableMode & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) && views != null) {
				views.add(this);
			}
		}
	}

	ItemInfo addNewItem(int position, int index) {
		ItemInfo ii = new ItemInfo();
		ii.position = position;
		ii.object = mAdapter.instantiateItem((ViewGroup)this, position);
		ii.widthFactor = mAdapter.getPageWidth(position);
		if (index < 0 || index >= mItems.size()) {
			mItems.add(ii);
			return ii;
		} else {
			mItems.add(index, ii);
			return ii;
		}
	}

	public void addTouchables(ArrayList<View> views) {
		int i = SCROLL_STATE_IDLE;
		while (i < getChildCount()) {
			View child = getChildAt(i);
			if (child.getVisibility() == 0) {
				ItemInfo ii = infoForChild(child);
				if (ii == null || ii.position != mCurItem) {
					i++;
				} else {
					child.addTouchables(views);
				}
			}
			i++;
		}
	}

	public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
		if (!checkLayoutParams(params)) {
			params = generateLayoutParams(params);
		}
		LayoutParams lp = (LayoutParams) params;
		lp.isDecor |= child instanceof Decor;
		if (mInLayout) {
			if (lp == null || !lp.isDecor) {
				lp.needsMeasure = true;
				addViewInLayout(child, index, params);
			} else {
				throw new IllegalStateException("Cannot add pager decor view during layout");
			}
		} else {
			super.addView(child, index, params);
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	public boolean arrowScroll(int r14_direction) {
		r13_this = this;
		r12 = 66;
		r11 = 17;
		r1 = r13.findFocus();
		if (r1_currentFocused != r13) goto L_0x003c;
	L_0x000a:
		r1_currentFocused = 0;
	L_0x000b:
		r2 = 0;
		r8 = android.view.FocusFinder.getInstance();
		r4 = r8.findNextFocus(r13, r1_currentFocused, r14_direction);
		if (r4_nextFocused == 0) goto L_0x00c5;
	L_0x0016:
		if (r4_nextFocused == r1_currentFocused) goto L_0x00c5;
	L_0x0018:
		if (r14_direction != r11) goto L_0x00a3;
	L_0x001a:
		r8 = r13.mTempRect;
		r8 = r13.getChildRectInPagerCoordinates(r8, r4_nextFocused);
		r5 = r8.left;
		r8 = r13.mTempRect;
		r8 = r13.getChildRectInPagerCoordinates(r8, r1_currentFocused);
		r0 = r8.left;
		if (r1_currentFocused == 0) goto L_0x009e;
	L_0x002c:
		if (r5_nextLeft < r0_currLeft) goto L_0x009e;
	L_0x002e:
		r2_handled = r13.pageLeft();
	L_0x0032:
		if (r2_handled == 0) goto L_0x003b;
	L_0x0034:
		r8 = android.view.SoundEffectConstants.getContantForFocusDirection(r14_direction);
		r13.playSoundEffect(r8);
	L_0x003b:
		return r2_handled;
	L_0x003c:
		if (r1_currentFocused == 0) goto L_0x000b;
	L_0x003e:
		r3 = 0;
		r6 = r1_currentFocused.getParent();
	L_0x0043:
		r8 = r6_parent instanceof android.view.ViewGroup;
		if (r8 == 0) goto L_0x004a;
	L_0x0047:
		if (r6_parent != r13) goto L_0x007a;
	L_0x0049:
		r3_isChild = 1;
	L_0x004a:
		if (r3_isChild != 0) goto L_0x000b;
	L_0x004c:
		r7 = new java.lang.StringBuilder;
		r7.<init>();
		r8 = r1_currentFocused.getClass();
		r8 = r8.getSimpleName();
		r7_sb.append(r8);
		r6_parent = r1_currentFocused.getParent();
	L_0x0060:
		r8 = r6_parent instanceof android.view.ViewGroup;
		if (r8 == 0) goto L_0x007f;
	L_0x0064:
		r8 = " => ";
		r8 = r7_sb.append(r8);
		r9 = r6_parent.getClass();
		r9 = r9.getSimpleName();
		r8.append(r9);
		r6_parent = r6_parent.getParent();
		goto L_0x0060;
	L_0x007a:
		r6_parent = r6_parent.getParent();
		goto L_0x0043;
	L_0x007f:
		r8 = "ViewPager";
		r9 = new java.lang.StringBuilder;
		r9.<init>();
		r10 = "arrowScroll tried to find focus based on non-child current focused view ";
		r9 = r9.append(r10);
		r10 = r7_sb.toString();
		r9 = r9.append(r10);
		r9 = r9.toString();
		android.util.Log.e(r8, r9);
		r1_currentFocused = 0;
		goto L_0x000b;
	L_0x009e:
		r2_handled = r4_nextFocused.requestFocus();
		goto L_0x0032;
	L_0x00a3:
		if (r14_direction != r12) goto L_0x0032;
	L_0x00a5:
		r8 = r13.mTempRect;
		r8 = r13.getChildRectInPagerCoordinates(r8, r4_nextFocused);
		r5_nextLeft = r8.left;
		r8 = r13.mTempRect;
		r8 = r13.getChildRectInPagerCoordinates(r8, r1_currentFocused);
		r0_currLeft = r8.left;
		if (r1_currentFocused == 0) goto L_0x00bf;
	L_0x00b7:
		if (r5_nextLeft > r0_currLeft) goto L_0x00bf;
	L_0x00b9:
		r2_handled = r13.pageRight();
		goto L_0x0032;
	L_0x00bf:
		r2_handled = r4_nextFocused.requestFocus();
		goto L_0x0032;
	L_0x00c5:
		if (r14_direction == r11) goto L_0x00ca;
	L_0x00c7:
		r8 = 1;
		if (r14_direction != r8) goto L_0x00d0;
	L_0x00ca:
		r2_handled = r13.pageLeft();
		goto L_0x0032;
	L_0x00d0:
		if (r14_direction == r12) goto L_0x00d5;
	L_0x00d2:
		r8 = 2;
		if (r14_direction != r8) goto L_0x0032;
	L_0x00d5:
		r2_handled = r13.pageRight();
		goto L_0x0032;
	}
	*/
	public boolean arrowScroll(int direction) {
		View currentFocused = findFocus();
		if (currentFocused == this) {
			currentFocused = null;
		} else if (currentFocused != null) {
			boolean isChild = DEBUG;
			ViewPager parent = currentFocused.getParent();
			while (parent instanceof ViewGroup) {
				if (parent == this) {
					isChild = true;
				} else {
					parent = parent.getParent();
				}
			}
			if (!isChild) {
				StringBuilder sb = new StringBuilder();
				sb.append(currentFocused.getClass().getSimpleName());
				ViewParent parent_2 = currentFocused.getParent();
				while (parent_2 instanceof ViewGroup) {
					sb.append(" => ").append(parent_2.getClass().getSimpleName());
					parent_2 = parent_2.getParent();
				}
				Log.e(TAG, "arrowScroll tried to find focus based on non-child current focused view " + sb.toString());
				currentFocused = null;
			}
		}
		boolean handled = DEBUG;
		View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
		if (nextFocused == null || nextFocused == currentFocused) {
			handled = pageLeft();
		} else if (direction == 17) {
			nextLeft = getChildRectInPagerCoordinates(mTempRect, nextFocused).left;
			currLeft = getChildRectInPagerCoordinates(mTempRect, currentFocused).left;
			if (currentFocused == null || nextLeft < currLeft) {
				handled = nextFocused.requestFocus();
			} else {
				handled = pageLeft();
			}
		} else if (direction == 66) {
			nextLeft = getChildRectInPagerCoordinates(mTempRect, nextFocused).left;
			currLeft = getChildRectInPagerCoordinates(mTempRect, currentFocused).left;
			if (currentFocused == null || nextLeft > currLeft) {
				handled = nextFocused.requestFocus();
			} else {
				handled = pageRight();
			}
		}
		if (handled) {
			playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
			return handled;
		} else {
			return handled;
		}
	}

	public boolean beginFakeDrag() {
		boolean r4z = DEBUG;
		if (mIsBeingDragged) {
			return DEBUG;
		} else {
			mFakeDragging = true;
			setScrollState(SCROLL_STATE_DRAGGING);
			mLastMotionX = 0.0f;
			mInitialMotionX = 0.0f;
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
			} else {
				mVelocityTracker.clear();
			}
			long time = SystemClock.uptimeMillis();
			MotionEvent ev = MotionEvent.obtain(time, time, r4z, AutoScrollHelper.RELATIVE_UNSPECIFIED, 0.0f, r4z);
			mVelocityTracker.addMovement(ev);
			ev.recycle();
			mFakeDragBeginTime = time;
			return true;
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	protected boolean canScroll(android.view.View r12_v, boolean r13_checkV, int r14_dx, int r15_x, int r16_y) {
		r11_this = this;
		r0 = r12_v instanceof android.view.ViewGroup;
		if (r0 == 0) goto L_0x0059;
	L_0x0004:
		r7 = r12_v;
		r7 = (android.view.ViewGroup) r7;
		r9 = r12_v.getScrollX();
		r10 = r12_v.getScrollY();
		r6 = r7_group.getChildCount();
		r8 = r6_count + -1;
	L_0x0015:
		if (r8_i < 0) goto L_0x0059;
	L_0x0017:
		r1 = r7_group.getChildAt(r8_i);
		r0 = r15_x + r9_scrollX;
		r2 = r1_child.getLeft();
		if (r0 < r2) goto L_0x0056;
	L_0x0023:
		r0 = r15_x + r9_scrollX;
		r2 = r1_child.getRight();
		if (r0 >= r2) goto L_0x0056;
	L_0x002b:
		r0 = r16_y + r10_scrollY;
		r2 = r1_child.getTop();
		if (r0 < r2) goto L_0x0056;
	L_0x0033:
		r0 = r16_y + r10_scrollY;
		r2 = r1_child.getBottom();
		if (r0 >= r2) goto L_0x0056;
	L_0x003b:
		r2 = 1;
		r0 = r15_x + r9_scrollX;
		r3 = r1_child.getLeft();
		r4 = r0 - r3;
		r0 = r16_y + r10_scrollY;
		r3 = r1_child.getTop();
		r5 = r0 - r3;
		r0 = r11;
		r3 = r14_dx;
		r0 = r0.canScroll(r1_child, r2, r3, r4, r5);
		if (r0 == 0) goto L_0x0056;
	L_0x0054:
		r0 = 1;
	L_0x0055:
		return r0;
	L_0x0056:
		r8_i++;
		goto L_0x0015;
	L_0x0059:
		if (r13_checkV == 0) goto L_0x0064;
	L_0x005b:
		r0 = -r14;
		r0 = android.support.v4.view.ViewCompat.canScrollHorizontally(r12, r0);
		if (r0 == 0) goto L_0x0064;
	L_0x0062:
		r0 = 1;
		goto L_0x0055;
	L_0x0064:
		r0 = 0;
		goto L_0x0055;
	}
	*/
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) v;
			int scrollX = v.getScrollX();
			int scrollY = v.getScrollY();
			int i = group.getChildCount() - 1;
			while (i >= 0) {
				View child = group.getChildAt(i);
				if (x + scrollX < child.getLeft() || x + scrollX >= child.getRight() || y + scrollY < child.getTop() || y + scrollY >= child.getBottom() || !canScroll(child, true, dx, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
					i--;
				}
			}
		}
		if (!checkV || !ViewCompat.canScrollHorizontally(v, -dx)) {
			return DEBUG;
		} else {
			return true;
		}
	}

	public boolean canScrollHorizontally(int direction) {
		boolean r2z = true;
		if (mAdapter == null) {
			return DEBUG;
		} else {
			int width = getClientWidth();
			int scrollX = getScrollX();
			if (direction < 0) {
				if (scrollX > ((int) (((float) width) * mFirstOffset))) {
					return r2z;
				} else {
					r2z = false;
					return r2z;
				}
			} else if (direction > 0) {
				if (scrollX < ((int) (((float) width) * mLastOffset))) {
					return r2z;
				} else {
					return r2z;
				}
			} else {
				return DEBUG;
			}
		}
	}

	protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
		if (!(p instanceof LayoutParams) || !super.checkLayoutParams(p)) {
			return DEBUG;
		} else {
			return true;
		}
	}

	public void computeScroll() {
		if (mScroller.isFinished() || !mScroller.computeScrollOffset()) {
			completeScroll(true);
		} else {
			int oldY = getScrollY();
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();
			if (getScrollX() != x || oldY != y) {
				scrollTo(x, y);
				if (!pageScrolled(x)) {
					mScroller.abortAnimation();
					scrollTo(SCROLL_STATE_IDLE, y);
				}
			} else {
				ViewCompat.postInvalidateOnAnimation(this);
			}
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	void dataSetChanged() {
		boolean needPopulate;
		int adapterCount = mAdapter.getCount();
		mExpectedAdapterCount = adapterCount;
		if (mItems.size() >= (mOffscreenPageLimit * 2) + 1 || mItems.size() >= adapterCount) {
			needPopulate = false;
		} else {
			needPopulate = true;
		}
		int newCurrItem = mCurItem;
		boolean isUpdating = DEBUG;
		int i = SCROLL_STATE_IDLE;
		while (i < mItems.size()) {
			ItemInfo ii = (ItemInfo) mItems.get(i);
			int newPos = mAdapter.getItemPosition(ii.object);
			if (newPos == INVALID_POINTER) {
				i++;
			} else if (newPos == -2) {
				mItems.remove(i);
				if (!isUpdating) {
					mAdapter.startUpdate((ViewGroup)this);
					isUpdating = true;
				}
				mAdapter.destroyItem((ViewGroup)this, ii.position, ii.object);
				needPopulate = true;
				if (mCurItem == ii.position) {
					newCurrItem = Math.max(SCROLL_STATE_IDLE, Math.min(mCurItem, adapterCount - 1));
					needPopulate = true;
				}
				i++;
			} else {
				if (ii.position != newPos) {
					if (ii.position == mCurItem) {
						newCurrItem = newPos;
					}
					ii.position = newPos;
					needPopulate = true;
				}
				i++;
			}
		}
		if (isUpdating) {
			mAdapter.finishUpdate((ViewGroup)this);
		}
		Collections.sort(mItems, COMPARATOR);
		if (needPopulate) {
			i = SCROLL_STATE_IDLE;
			while (i < getChildCount()) {
				LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
				if (!lp.isDecor) {
					lp.widthFactor = 0.0f;
				}
				i++;
			}
			setCurrentItemInternal(newCurrItem, DEBUG, true);
			requestLayout();
		}
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (super.dispatchKeyEvent(event) || executeKeyEvent(event)) {
			return true;
		} else {
			return DEBUG;
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	public boolean dispatchPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent r7_event) {
		r6_this = this;
		r4 = r7_event.getEventType();
		r5 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
		if (r4 != r5) goto L_0x000d;
	L_0x0008:
		r4 = super.dispatchPopulateAccessibilityEvent(r7_event);
	L_0x000c:
		return r4;
	L_0x000d:
		r1 = r6.getChildCount();
		r2 = 0;
	L_0x0012:
		if (r2_i >= r1_childCount) goto L_0x0035;
	L_0x0014:
		r0 = r6.getChildAt(r2_i);
		r4 = r0_child.getVisibility();
		if (r4 != 0) goto L_0x0032;
	L_0x001e:
		r3 = r6.infoForChild(r0_child);
		if (r3_ii == 0) goto L_0x0032;
	L_0x0024:
		r4 = r3_ii.position;
		r5 = r6.mCurItem;
		if (r4 != r5) goto L_0x0032;
	L_0x002a:
		r4 = r0_child.dispatchPopulateAccessibilityEvent(r7_event);
		if (r4 == 0) goto L_0x0032;
	L_0x0030:
		r4 = 1;
		goto L_0x000c;
	L_0x0032:
		r2_i++;
		goto L_0x0012;
	L_0x0035:
		r4 = 0;
		goto L_0x000c;
	}
	*/
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
		if (event.getEventType() == 4096) {
			return super.dispatchPopulateAccessibilityEvent(event);
		} else {
			int i = SCROLL_STATE_IDLE;
			while (i < getChildCount()) {
				View child = getChildAt(i);
				if (child.getVisibility() == 0) {
					ItemInfo ii = infoForChild(child);
					if (ii == null || ii.position != mCurItem || !child.dispatchPopulateAccessibilityEvent(event)) {
						i++;
					}
				}
				i++;
			}
			return DEBUG;
		}
	}

	float distanceInfluenceForSnapDuration(float f) {
		return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
	}

	public void draw(Canvas canvas) {
		super.draw(canvas);
		boolean needsInvalidate = DEBUG;
		int overScrollMode = ViewCompat.getOverScrollMode(this);
		int height;
		int width;
		if (overScrollMode != 0) {
			if (overScrollMode != 1 || mAdapter == null || mAdapter.getCount() <= 1) {
				mLeftEdge.finish();
				mRightEdge.finish();
			} else {
				if (mLeftEdge.isFinished()) {
					height = (getHeight() - getPaddingTop()) - getPaddingBottom();
					width = getWidth();
					canvas.rotate(270.0f);
					canvas.translate((float) ((-height) + getPaddingTop()), mFirstOffset * ((float) width));
					mLeftEdge.setSize(height, width);
					needsInvalidate |= mLeftEdge.draw(canvas);
					canvas.restoreToCount(canvas.save());
				}
				if (!mRightEdge.isFinished()) {
					width = getWidth();
					canvas.rotate(90.0f);
					canvas.translate((float) (-getPaddingTop()), (-(mLastOffset + 1.0f)) * ((float) width));
					mRightEdge.setSize((getHeight() - getPaddingTop()) - getPaddingBottom(), width);
					needsInvalidate |= mRightEdge.draw(canvas);
					canvas.restoreToCount(canvas.save());
				}
			}
		} else if (mLeftEdge.isFinished()) {
			if (!mRightEdge.isFinished()) {
				if (!needsInvalidate) {
					ViewCompat.postInvalidateOnAnimation(this);
				}
			} else {
				width = getWidth();
				canvas.rotate(90.0f);
				canvas.translate((float) (-getPaddingTop()), (-(mLastOffset + 1.0f)) * ((float) width));
				mRightEdge.setSize((getHeight() - getPaddingTop()) - getPaddingBottom(), width);
				needsInvalidate |= mRightEdge.draw(canvas);
				canvas.restoreToCount(canvas.save());
			}
		} else {
			height = (getHeight() - getPaddingTop()) - getPaddingBottom();
			width = getWidth();
			canvas.rotate(270.0f);
			canvas.translate((float) ((-height) + getPaddingTop()), mFirstOffset * ((float) width));
			mLeftEdge.setSize(height, width);
			needsInvalidate |= mLeftEdge.draw(canvas);
			canvas.restoreToCount(canvas.save());
			if (!mRightEdge.isFinished()) {
				width = getWidth();
				canvas.rotate(90.0f);
				canvas.translate((float) (-getPaddingTop()), (-(mLastOffset + 1.0f)) * ((float) width));
				mRightEdge.setSize((getHeight() - getPaddingTop()) - getPaddingBottom(), width);
				needsInvalidate |= mRightEdge.draw(canvas);
				canvas.restoreToCount(canvas.save());
			}
		}
		if (!needsInvalidate) {
		} else {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	protected void drawableStateChanged() {
		super.drawableStateChanged();
		Drawable d = mMarginDrawable;
		if (d == null || !d.isStateful()) {
		} else {
			d.setState(getDrawableState());
		}
	}

	public void endFakeDrag() {
		if (!mFakeDragging) {
			throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
		} else {
			VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(DeviceManager.REQUEST_CODE_ENABLE_ADMIN, (float) mMaximumVelocity);
			int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, mActivePointerId);
			mPopulatePending = true;
			ItemInfo ii = infoForCurrentScrollPosition();
			setCurrentItemInternal(determineTargetPage(ii.position, ((((float) getScrollX()) / ((float) getClientWidth())) - ii.offset) / ii.widthFactor, initialVelocity, (int) (mLastMotionX - mInitialMotionX)), true, true, initialVelocity);
			endDrag();
			mFakeDragging = false;
		}
	}

	public boolean executeKeyEvent(KeyEvent event) {
		if (event.getAction() == 0) {
			switch(event.getKeyCode()) {
			case 21:
				return arrowScroll(17);
			case 22:
				return arrowScroll(66);
			case 61:
				if (VERSION.SDK_INT >= 11) {
					if (KeyEventCompat.hasNoModifiers(event)) {
						return arrowScroll(SCROLL_STATE_SETTLING);
					} else if (KeyEventCompat.hasModifiers(event, SCROLL_STATE_DRAGGING)) {
						return arrowScroll(SCROLL_STATE_DRAGGING);
					} else {
						return DEBUG;
					}
				} else {
					return DEBUG;
				}
			}
			return DEBUG;
		} else {
			return DEBUG;
		}
	}

	public void fakeDragBy(float xOffset) {
		if (!mFakeDragging) {
			throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
		} else {
			mLastMotionX += xOffset;
			float scrollX = ((float) getScrollX()) - xOffset;
			int width = getClientWidth();
			float leftBound = ((float) width) * mFirstOffset;
			float rightBound = ((float) width) * mLastOffset;
			ItemInfo firstItem = (ItemInfo) mItems.get(SCROLL_STATE_IDLE);
			ItemInfo lastItem = (ItemInfo) mItems.get(mItems.size() - 1);
			if (firstItem.position != 0) {
				leftBound = firstItem.offset * ((float) width);
			}
			if (lastItem.position != mAdapter.getCount() - 1) {
				rightBound = lastItem.offset * ((float) width);
			}
			if (scrollX < leftBound) {
				scrollX = leftBound;
			} else if (scrollX > rightBound) {
				scrollX = rightBound;
			}
			mLastMotionX += scrollX - ((float) ((int) scrollX));
			scrollTo((int) scrollX, getScrollY());
			pageScrolled((int) scrollX);
			MotionEvent ev = MotionEvent.obtain(mFakeDragBeginTime, SystemClock.uptimeMillis(), SCROLL_STATE_SETTLING, mLastMotionX, AutoScrollHelper.RELATIVE_UNSPECIFIED, SCROLL_STATE_IDLE);
			mVelocityTracker.addMovement(ev);
			ev.recycle();
		}
	}

	protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams();
	}

	public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
		return generateDefaultLayoutParams();
	}

	public PagerAdapter getAdapter() {
		return mAdapter;
	}

	protected int getChildDrawingOrder(int childCount, int i) {
		int index;
		if (mDrawingOrder == 2) {
			index = (childCount - 1) - i;
		} else {
			index = i;
		}
		return ((LayoutParams) ((View) mDrawingOrderedChildren.get(index)).getLayoutParams()).childIndex;
	}

	public int getCurrentItem() {
		return mCurItem;
	}

	public int getOffscreenPageLimit() {
		return mOffscreenPageLimit;
	}

	public int getPageMargin() {
		return mPageMargin;
	}

	ItemInfo infoForAnyChild(View child) {
		while (true) {
			View parent = child.getParent();
			if (parent != this) {
				if (parent == null || !(parent instanceof View)) {
					return null;
				} else {
					child = parent;
				}
			} else {
				return infoForChild(child);
			}
		}
	}

	ItemInfo infoForChild(View child) {
		int i = SCROLL_STATE_IDLE;
		while (i < mItems.size()) {
			ItemInfo ii = (ItemInfo) mItems.get(i);
			if (mAdapter.isViewFromObject(child, ii.object)) {
				return ii;
			} else {
				i++;
			}
		}
		return null;
	}

	ItemInfo infoForPosition(int position) {
		int i = SCROLL_STATE_IDLE;
		while (i < mItems.size()) {
			ItemInfo ii = (ItemInfo) mItems.get(i);
			if (ii.position == position) {
				return ii;
			} else {
				i++;
			}
		}
		return null;
	}

	void initViewPager() {
		setWillNotDraw(DEBUG);
		setDescendantFocusability(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_START);
		setFocusable(true);
		Context context = getContext();
		mScroller = new Scroller(context, sInterpolator);
		ViewConfiguration configuration = ViewConfiguration.get(context);
		float density = context.getResources().getDisplayMetrics().density;
		mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
		mMinimumVelocity = (int) (400.0f * density);
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
		mLeftEdge = new EdgeEffectCompat(context);
		mRightEdge = new EdgeEffectCompat(context);
		mFlingDistance = (int) (25.0f * density);
		mCloseEnough = (int) (2.0f * density);
		mDefaultGutterSize = (int) (16.0f * density);
		ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate(this));
		if (ViewCompat.getImportantForAccessibility(this) == 0) {
			ViewCompat.setImportantForAccessibility(this, SCROLL_STATE_DRAGGING);
		}
	}

	public boolean isFakeDragging() {
		return mFakeDragging;
	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mFirstLayout = true;
	}

	protected void onDetachedFromWindow() {
		removeCallbacks(mEndScrollRunnable);
		super.onDetachedFromWindow();
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mPageMargin <= 0 || mMarginDrawable == null || mItems.size() <= 0 || mAdapter == null) {
		} else {
			int scrollX = getScrollX();
			int width = getWidth();
			float marginOffset = ((float) mPageMargin) / ((float) width);
			int itemIndex = SCROLL_STATE_IDLE;
			ItemInfo ii = (ItemInfo) mItems.get(SCROLL_STATE_IDLE);
			float offset = ii.offset;
			int itemCount = mItems.size();
			int pos = ii.position;
			while (pos < ((ItemInfo) mItems.get(itemCount - 1)).position) {
				float drawAt;
				while (pos > ii.position && itemIndex < itemCount) {
					itemIndex++;
					ii = mItems.get(itemIndex);
				}
				if (pos == ii.position) {
					drawAt = (ii.offset + ii.widthFactor) * ((float) width);
					offset = (ii.offset + ii.widthFactor) + marginOffset;
				} else {
					float widthFactor = mAdapter.getPageWidth(pos);
					drawAt = (offset + widthFactor) * ((float) width);
					offset += widthFactor + marginOffset;
				}
				if (((float) mPageMargin) + drawAt > ((float) scrollX)) {
					mMarginDrawable.setBounds((int) drawAt, mTopPageBounds, (int) ((((float) mPageMargin) + drawAt) + 0.5f), mBottomPageBounds);
					mMarginDrawable.draw(canvas);
				}
				if (drawAt > ((float) (scrollX + width))) {
					return;
				} else {
					pos++;
				}
			}
		}
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction() & 255;
		if (action == WearableExtender.SIZE_MEDIUM || action == 1) {
			mIsBeingDragged = false;
			mIsUnableToDrag = false;
			mActivePointerId = -1;
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			return DEBUG;
		} else {
			if (action != 0) {
				if (mIsBeingDragged) {
					return true;
				} else if (mIsUnableToDrag) {
					return DEBUG;
				}
			}
			float r0f;
			switch(action) {
			case SCROLL_STATE_IDLE:
				r0f = ev.getX();
				mInitialMotionX = r0f;
				mLastMotionX = r0f;
				r0f = ev.getY();
				mInitialMotionY = r0f;
				mLastMotionY = r0f;
				mActivePointerId = MotionEventCompat.getPointerId(ev, SCROLL_STATE_IDLE);
				mIsUnableToDrag = false;
				mScroller.computeScrollOffset();
				if (mScrollState != 2 || Math.abs(mScroller.getFinalX() - mScroller.getCurrX()) <= mCloseEnough) {
					completeScroll(DEBUG);
					mIsBeingDragged = false;
				} else {
					mScroller.abortAnimation();
					mPopulatePending = false;
					populate();
					mIsBeingDragged = true;
					requestParentDisallowInterceptTouchEvent(true);
					setScrollState(SCROLL_STATE_DRAGGING);
				}
				break;
			case SCROLL_STATE_SETTLING:
				int activePointerId = mActivePointerId;
				if (activePointerId != -1) {
					int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
					float x = MotionEventCompat.getX(ev, pointerIndex);
					float dx = x - mLastMotionX;
					float xDiff = Math.abs(dx);
					float y = MotionEventCompat.getY(ev, pointerIndex);
					float yDiff = Math.abs(y - mInitialMotionY);
					if (dx == 0.0f || isGutterDrag(mLastMotionX, dx) || !canScroll(this, DEBUG, (int) dx, (int) x, (int) y)) {
						if (xDiff <= ((float) mTouchSlop) || 0.5f * xDiff <= yDiff) {
							if (yDiff > ((float) mTouchSlop)) {
								mIsUnableToDrag = true;
							}
						} else {
							mIsBeingDragged = true;
							requestParentDisallowInterceptTouchEvent(true);
							setScrollState(SCROLL_STATE_DRAGGING);
							if (dx > 0.0f) {
								r0f = mInitialMotionX + ((float) mTouchSlop);
							} else {
								r0f = mInitialMotionX - ((float) mTouchSlop);
							}
							mLastMotionX = r0f;
							mLastMotionY = y;
							setScrollingCacheEnabled(true);
						}
						if (mIsBeingDragged) {
							if (performDrag(x)) {
								ViewCompat.postInvalidateOnAnimation(this);
							}
						}
					} else {
						mLastMotionX = x;
						mLastMotionY = y;
						mIsUnableToDrag = true;
						return DEBUG;
					}
				}
			case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
				onSecondaryPointerUp(ev);
				break;
			}
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
			}
			mVelocityTracker.addMovement(ev);
			return mIsBeingDragged;
		}
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		View child;
		LayoutParams lp;
		int childLeft;
		int childTop;
		int count = getChildCount();
		int width = r - l;
		int height = b - t;
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();
		int scrollX = getScrollX();
		int decorCount = SCROLL_STATE_IDLE;
		int i = SCROLL_STATE_IDLE;
		while (i < count) {
			child = getChildAt(i);
			if (child.getVisibility() != 8) {
				lp = (LayoutParams) child.getLayoutParams();
				if (lp.isDecor) {
					int vgrav = lp.gravity & 112;
					switch((lp.gravity & 7)) {
					case SCROLL_STATE_DRAGGING:
						childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
						switch(vgrav) {
						case DEFAULT_GUTTER_SIZE:
							childTop = Math.max((height - child.getMeasuredHeight()) / 2, paddingTop);
							childLeft += scrollX;
							child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
							decorCount++;
							break;
						case 48:
							childTop = paddingTop;
							paddingTop += child.getMeasuredHeight();
							childLeft += scrollX;
							child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
							decorCount++;
							break;
						case 80:
							childTop = (height - paddingBottom) - child.getMeasuredHeight();
							paddingBottom += child.getMeasuredHeight();
							childLeft += scrollX;
							child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
							decorCount++;
							break;
						}
						childTop = paddingTop;
						childLeft += scrollX;
						child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
						decorCount++;
						break;
					case WearableExtender.SIZE_MEDIUM:
						childLeft = paddingLeft;
						paddingLeft += child.getMeasuredWidth();
						switch(vgrav) {
						case DEFAULT_GUTTER_SIZE:
							childTop = Math.max((height - child.getMeasuredHeight()) / 2, paddingTop);
							childLeft += scrollX;
							child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
							decorCount++;
							break;
						case 48:
							childTop = paddingTop;
							paddingTop += child.getMeasuredHeight();
							childLeft += scrollX;
							child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
							decorCount++;
							break;
						case 80:
							childTop = (height - paddingBottom) - child.getMeasuredHeight();
							paddingBottom += child.getMeasuredHeight();
							childLeft += scrollX;
							child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
							decorCount++;
							break;
						}
						childTop = paddingTop;
						childLeft += scrollX;
						child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
						decorCount++;
						break;
					case WearableExtender.SIZE_FULL_SCREEN:
						childLeft = (width - paddingRight) - child.getMeasuredWidth();
						paddingRight += child.getMeasuredWidth();
						switch(vgrav) {
						case DEFAULT_GUTTER_SIZE:
							childTop = Math.max((height - child.getMeasuredHeight()) / 2, paddingTop);
							childLeft += scrollX;
							child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
							decorCount++;
							break;
						case 48:
							childTop = paddingTop;
							paddingTop += child.getMeasuredHeight();
							childLeft += scrollX;
							child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
							decorCount++;
							break;
						case 80:
							childTop = (height - paddingBottom) - child.getMeasuredHeight();
							paddingBottom += child.getMeasuredHeight();
							childLeft += scrollX;
							child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
							decorCount++;
							break;
						}
						childTop = paddingTop;
						childLeft += scrollX;
						child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
						decorCount++;
						break;
					}
					childLeft = paddingLeft;
					switch(vgrav) {
					case DEFAULT_GUTTER_SIZE:
						childTop = Math.max((height - child.getMeasuredHeight()) / 2, paddingTop);
						childLeft += scrollX;
						child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
						decorCount++;
						break;
					case 48:
						childTop = paddingTop;
						paddingTop += child.getMeasuredHeight();
						childLeft += scrollX;
						child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
						decorCount++;
						break;
					case 80:
						childTop = (height - paddingBottom) - child.getMeasuredHeight();
						paddingBottom += child.getMeasuredHeight();
						childLeft += scrollX;
						child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
						decorCount++;
						break;
					}
					childTop = paddingTop;
					childLeft += scrollX;
					child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
					decorCount++;
				}
			}
			i++;
		}
		int childWidth = (width - paddingLeft) - paddingRight;
		i = SCROLL_STATE_IDLE;
		while (i < count) {
			child = getChildAt(i);
			if (child.getVisibility() != 8) {
				lp = (LayoutParams) child.getLayoutParams();
				if (!lp.isDecor) {
					ItemInfo ii = infoForChild(child);
					if (ii != null) {
						childLeft = paddingLeft + ((int) (((float) childWidth) * ii.offset));
						childTop = paddingTop;
						if (lp.needsMeasure) {
							lp.needsMeasure = false;
							child.measure(MeasureSpec.makeMeasureSpec((int) (((float) childWidth) * lp.widthFactor), 1073741824), MeasureSpec.makeMeasureSpec((height - paddingTop) - paddingBottom, 1073741824));
						}
						child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
					}
				}
			}
			i++;
		}
		mTopPageBounds = paddingTop;
		mBottomPageBounds = height - paddingBottom;
		mDecorChildCount = decorCount;
		if (mFirstLayout) {
			scrollToItem(mCurItem, false, 0, false);
		}
		mFirstLayout = false;
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		View child;
		LayoutParams lp;
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
		int measuredWidth = getMeasuredWidth();
		mGutterSize = Math.min(measuredWidth / 10, mDefaultGutterSize);
		int childWidthSize = (measuredWidth - getPaddingLeft()) - getPaddingRight();
		int childHeightSize = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
		int size = getChildCount();
		int i = SCROLL_STATE_IDLE;
		while (i < size) {
			child = getChildAt(i);
			if (child.getVisibility() != 8) {
				lp = (LayoutParams) child.getLayoutParams();
				if (lp == null || !lp.isDecor) {
					i++;
				} else {
					boolean consumeVertical;
					boolean consumeHorizontal;
					int hgrav = lp.gravity & 7;
					int vgrav = lp.gravity & 112;
					int widthMode = ExploreByTouchHelper.INVALID_ID;
					int heightMode = ExploreByTouchHelper.INVALID_ID;
					if (vgrav == 48 || vgrav == 80) {
						consumeVertical = true;
					} else {
						consumeVertical = DEBUG;
					}
					if (hgrav == 3 || hgrav == 5) {
						consumeHorizontal = true;
					} else {
						consumeHorizontal = DEBUG;
					}
					if (consumeVertical) {
						widthMode = 1073741824;
					} else if (consumeHorizontal) {
					}
					int widthSize = childWidthSize;
					int heightSize = childHeightSize;
					if (lp.width != -2) {
						widthMode = 1073741824;
						if (lp.width != -1) {
							widthSize = lp.width;
						}
					}
					if (lp.height != -2) {
						heightMode = 1073741824;
						if (lp.height != -1) {
							heightSize = lp.height;
						}
					}
					child.measure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(heightSize, heightMode));
					if (consumeVertical) {
						childHeightSize -= child.getMeasuredHeight();
					} else if (consumeHorizontal) {
						childWidthSize -= child.getMeasuredWidth();
					}
				}
			}
			i++;
		}
		mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, 1073741824);
		mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, 1073741824);
		mInLayout = true;
		populate();
		mInLayout = false;
		size = getChildCount();
		i = SCROLL_STATE_IDLE;
		while (i < size) {
			child = getChildAt(i);
			if (child.getVisibility() != 8) {
				lp = (LayoutParams) child.getLayoutParams();
				if (lp == null || !lp.isDecor) {
					child.measure(MeasureSpec.makeMeasureSpec((int) (((float) childWidthSize) * lp.widthFactor), 1073741824), mChildHeightMeasureSpec);
				}
			}
			i++;
		}
	}

	protected void onPageScrolled(int position, float offset, int offsetPixels) {
		int scrollX;
		int i;
		View child;
		if (mDecorChildCount > 0) {
			scrollX = getScrollX();
			int paddingLeft = getPaddingLeft();
			int paddingRight = getPaddingRight();
			int width = getWidth();
			i = SCROLL_STATE_IDLE;
			while (i < getChildCount()) {
				child = getChildAt(i);
				LayoutParams lp = (LayoutParams) child.getLayoutParams();
				if (!lp.isDecor) {
					i++;
				} else {
					int childLeft;
					int childOffset;
					switch((lp.gravity & 7)) {
					case SCROLL_STATE_DRAGGING:
						childLeft = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
						childOffset = (childLeft + scrollX) - child.getLeft();
						if (childOffset == 0) {
							child.offsetLeftAndRight(childOffset);
						}
						i++;
						break;
					case WearableExtender.SIZE_MEDIUM:
						childLeft = paddingLeft;
						paddingLeft += child.getWidth();
						childOffset = (childLeft + scrollX) - child.getLeft();
						if (childOffset == 0) {
							i++;
						} else {
							child.offsetLeftAndRight(childOffset);
							i++;
						}
						break;
					case WearableExtender.SIZE_FULL_SCREEN:
						childLeft = (width - paddingRight) - child.getMeasuredWidth();
						paddingRight += child.getMeasuredWidth();
						childOffset = (childLeft + scrollX) - child.getLeft();
						if (childOffset == 0) {
							child.offsetLeftAndRight(childOffset);
						}
						i++;
						break;
					}
					childLeft = paddingLeft;
					childOffset = (childLeft + scrollX) - child.getLeft();
					if (childOffset == 0) {
						i++;
					} else {
						child.offsetLeftAndRight(childOffset);
						i++;
					}
				}
			}
		}
		if (mOnPageChangeListener != null) {
			mOnPageChangeListener.onPageScrolled(position, offset, offsetPixels);
		}
		if (mInternalPageChangeListener != null) {
			mInternalPageChangeListener.onPageScrolled(position, offset, offsetPixels);
		}
		if (mPageTransformer != null) {
			scrollX = getScrollX();
			i = SCROLL_STATE_IDLE;
			while (i < getChildCount()) {
				child = getChildAt(i);
				if (((LayoutParams) child.getLayoutParams()).isDecor) {
					i++;
				} else {
					mPageTransformer.transformPage(child, ((float) (child.getLeft() - scrollX)) / ((float) getClientWidth()));
					i++;
				}
			}
		}
		mCalledSuper = true;
	}

	/* JADX WARNING: inconsistent code */
	/*
	protected boolean onRequestFocusInDescendants(int r10_direction, android.graphics.Rect r11_previouslyFocusedRect) {
		r9_this = this;
		r1 = r9.getChildCount();
		r7 = r10_direction & 2;
		if (r7 == 0) goto L_0x002c;
	L_0x0008:
		r6 = 0;
		r5_increment = 1;
		r2 = r1_count;
	L_0x000b:
		r3 = r6_index;
	L_0x000c:
		if (r3_i == r2_end) goto L_0x0033;
	L_0x000e:
		r0 = r9.getChildAt(r3_i);
		r7 = r0_child.getVisibility();
		if (r7 != 0) goto L_0x0031;
	L_0x0018:
		r4 = r9.infoForChild(r0_child);
		if (r4_ii == 0) goto L_0x0031;
	L_0x001e:
		r7 = r4_ii.position;
		r8 = r9.mCurItem;
		if (r7 != r8) goto L_0x0031;
	L_0x0024:
		r7 = r0_child.requestFocus(r10_direction, r11_previouslyFocusedRect);
		if (r7 == 0) goto L_0x0031;
	L_0x002a:
		r7 = 1;
	L_0x002b:
		return r7;
	L_0x002c:
		r6_index = r1_count + -1;
		r5_increment = -1;
		r2_end = -1;
		goto L_0x000b;
	L_0x0031:
		r3_i += r5_increment;
		goto L_0x000c;
	L_0x0033:
		r7 = 0;
		goto L_0x002b;
	}
	*/
	protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
		int index;
		int increment;
		int end;
		int count = getChildCount();
		if ((direction & 2) != 0) {
			index = SCROLL_STATE_IDLE;
			increment = SCROLL_STATE_DRAGGING;
			end = count;
		} else {
			index = count - 1;
			increment = INVALID_POINTER;
			end = INVALID_POINTER;
		}
		int i = index;
		while (i != end) {
			View child = getChildAt(i);
			if (child.getVisibility() == 0) {
				ItemInfo ii = infoForChild(child);
				if (ii == null || ii.position != mCurItem || !child.requestFocus(direction, previouslyFocusedRect)) {
					i += increment;
				}
			}
			i += increment;
		}
		return DEBUG;
	}

	public void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
		} else {
			SavedState ss = (SavedState) state;
			super.onRestoreInstanceState(ss.getSuperState());
			if (mAdapter != null) {
				mAdapter.restoreState(ss.adapterState, ss.loader);
				setCurrentItemInternal(ss.position, DEBUG, true);
			} else {
				mRestoredCurItem = ss.position;
				mRestoredAdapterState = ss.adapterState;
				mRestoredClassLoader = ss.loader;
			}
		}
	}

	public Parcelable onSaveInstanceState() {
		SavedState ss = new SavedState(super.onSaveInstanceState());
		ss.position = mCurItem;
		if (mAdapter != null) {
			ss.adapterState = mAdapter.saveState();
		}
		return ss;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != oldw) {
			recomputeScrollPosition(w, oldw, mPageMargin, mPageMargin);
		}
	}

	public boolean onTouchEvent(MotionEvent ev) {
		if (mFakeDragging) {
			return true;
		} else if (ev.getAction() != 0 || ev.getEdgeFlags() == 0) {
			if (mAdapter == null || mAdapter.getCount() == 0) {
				return DEBUG;
			} else {
				if (mVelocityTracker == null) {
					mVelocityTracker = VelocityTracker.obtain();
				}
				mVelocityTracker.addMovement(ev);
				boolean needsInvalidate = DEBUG;
				float r24f;
				switch((ev.getAction() & 255)) {
				case SCROLL_STATE_IDLE:
					mScroller.abortAnimation();
					mPopulatePending = false;
					populate();
					r24f = ev.getX();
					mInitialMotionX = r24f;
					mLastMotionX = r24f;
					r24f = ev.getY();
					mInitialMotionY = r24f;
					mLastMotionY = r24f;
					mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
					break;
				case SCROLL_STATE_DRAGGING:
					if (mIsBeingDragged) {
						VelocityTracker velocityTracker = mVelocityTracker;
						velocityTracker.computeCurrentVelocity(1000, (float) mMaximumVelocity);
						int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, mActivePointerId);
						mPopulatePending = true;
						ItemInfo ii = infoForCurrentScrollPosition();
						setCurrentItemInternal(determineTargetPage(ii.position, ((((float) getScrollX()) / ((float) getClientWidth())) - ii.offset) / ii.widthFactor, initialVelocity, (int) (MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, mActivePointerId)) - mInitialMotionX)), true, true, initialVelocity);
						mActivePointerId = -1;
						endDrag();
						needsInvalidate = mLeftEdge.onRelease() | mRightEdge.onRelease();
					}
					break;
				case SCROLL_STATE_SETTLING:
					if (!mIsBeingDragged) {
						int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
						float x = MotionEventCompat.getX(ev, pointerIndex);
						float xDiff = Math.abs(x - mLastMotionX);
						float y = MotionEventCompat.getY(ev, pointerIndex);
						float yDiff = Math.abs(y - mLastMotionY);
						if (xDiff <= ((float) mTouchSlop) || xDiff <= yDiff) {
						} else {
							mIsBeingDragged = true;
							requestParentDisallowInterceptTouchEvent(true);
							if (x - mInitialMotionX > 0.0f) {
								r24f = mInitialMotionX + ((float) mTouchSlop);
							} else {
								r24f = mInitialMotionX - ((float) mTouchSlop);
							}
							mLastMotionX = r24f;
							mLastMotionY = y;
							setScrollState(1);
							setScrollingCacheEnabled(true);
							ViewParent parent = getParent();
							if (parent != null) {
								parent.requestDisallowInterceptTouchEvent(true);
							}
						}
					}
					if (mIsBeingDragged) {
						needsInvalidate |= performDrag(MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, mActivePointerId)));
					}
					break;
				case WearableExtender.SIZE_MEDIUM:
					if (mIsBeingDragged) {
						scrollToItem(mCurItem, true, 0, false);
						mActivePointerId = -1;
						endDrag();
						needsInvalidate = mLeftEdge.onRelease() | mRightEdge.onRelease();
					}
					break;
				case WearableExtender.SIZE_FULL_SCREEN:
					int index = MotionEventCompat.getActionIndex(ev);
					mLastMotionX = MotionEventCompat.getX(ev, index);
					mActivePointerId = MotionEventCompat.getPointerId(ev, index);
					break;
				case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
					onSecondaryPointerUp(ev);
					mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, mActivePointerId));
					break;
				}
				if (needsInvalidate) {
					ViewCompat.postInvalidateOnAnimation(this);
				}
				return true;
			}
		} else {
			return DEBUG;
		}
	}

	boolean pageLeft() {
		if (mCurItem > 0) {
			setCurrentItem(mCurItem - 1, true);
			return true;
		} else {
			return DEBUG;
		}
	}

	boolean pageRight() {
		if (mAdapter == null || mCurItem >= mAdapter.getCount() - 1) {
			return DEBUG;
		} else {
			setCurrentItem(mCurItem + 1, true);
			return true;
		}
	}

	void populate() {
		populate(mCurItem);
	}

	/* JADX WARNING: inconsistent code */
	/*
	void populate(int r31_newCurrentItem) {
		r30_this = this;
		r21 = 0;
		r15 = 2;
		r0 = r30;
		r0 = r0.mCurItem;
		r27 = r0;
		r0 = r27;
		r1 = r31_newCurrentItem;
		if (r0 == r1) goto L_0x0031;
	L_0x000f:
		r0 = r30;
		r0 = r0.mCurItem;
		r27 = r0;
		r0 = r27;
		r1 = r31_newCurrentItem;
		if (r0 >= r1) goto L_0x003d;
	L_0x001b:
		r15_focusDirection = 66;
	L_0x001d:
		r0 = r30;
		r0 = r0.mCurItem;
		r27 = r0;
		r0 = r30;
		r1 = r27;
		r21_oldCurInfo = r0.infoForPosition(r1);
		r0 = r31_newCurrentItem;
		r1 = r30;
		r1.mCurItem = r0;
	L_0x0031:
		r0 = r30;
		r0 = r0.mAdapter;
		r27 = r0;
		if (r27 != 0) goto L_0x0040;
	L_0x0039:
		r30.sortChildDrawingOrder();
	L_0x003c:
		return;
	L_0x003d:
		r15_focusDirection = 17;
		goto L_0x001d;
	L_0x0040:
		r0 = r30;
		r0 = r0.mPopulatePending;
		r27 = r0;
		if (r27 == 0) goto L_0x004c;
	L_0x0048:
		r30.sortChildDrawingOrder();
		goto L_0x003c;
	L_0x004c:
		r27 = r30.getWindowToken();
		if (r27 == 0) goto L_0x003c;
	L_0x0052:
		r0 = r30;
		r0 = r0.mAdapter;
		r27 = r0;
		r0 = r27;
		r1 = r30;
		r0.startUpdate(r1);
		r0 = r30;
		r0 = r0.mOffscreenPageLimit;
		r22 = r0;
		r27 = 0;
		r0 = r30;
		r0 = r0.mCurItem;
		r28 = r0;
		r28 -= r22_pageLimit;
		r26 = java.lang.Math.max(r27, r28);
		r0 = r30;
		r0 = r0.mAdapter;
		r27 = r0;
		r4 = r27.getCount();
		r27 = r4_N + -1;
		r0 = r30;
		r0 = r0.mCurItem;
		r28 = r0;
		r28 += r22_pageLimit;
		r12 = java.lang.Math.min(r27, r28);
		r0 = r30;
		r0 = r0.mExpectedAdapterCount;
		r27 = r0;
		r0 = r27;
		if (r4_N == r0) goto L_0x0106;
	L_0x0095:
		r27 = r30.getResources();	 //Catch:{ NotFoundException -> 0x00fc }
		r28 = r30.getId();	 //Catch:{ NotFoundException -> 0x00fc }
		r24 = r27.getResourceName(r28);	 //Catch:{ NotFoundException -> 0x00fc }
	L_0x00a1:
		r27 = new java.lang.IllegalStateException;
		r28 = new java.lang.StringBuilder;
		r28.<init>();
		r29 = "The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ";
		r28 = r28.append(r29);
		r0 = r30;
		r0 = r0.mExpectedAdapterCount;
		r29 = r0;
		r28 = r28.append(r29);
		r29 = ", found: ";
		r28 = r28.append(r29);
		r0 = r28;
		r28 = r0.append(r4_N);
		r29 = " Pager id: ";
		r28 = r28.append(r29);
		r0 = r28;
		r1 = r24_resName;
		r28 = r0.append(r1);
		r29 = " Pager class: ";
		r28 = r28.append(r29);
		r29 = r30.getClass();
		r28 = r28.append(r29);
		r29 = " Problematic adapter: ";
		r28 = r28.append(r29);
		r0 = r30;
		r0 = r0.mAdapter;
		r29 = r0;
		r29 = r29.getClass();
		r28 = r28.append(r29);
		r28 = r28.toString();
		r27.<init>(r28);
		throw r27;
	L_0x00fc:
		r11_e = move-exception;
		r27 = r30.getId();
		r24_resName = java.lang.Integer.toHexString(r27);
		goto L_0x00a1;
	L_0x0106:
		r8 = -1;
		r9 = 0;
		r8_curIndex = 0;
	L_0x0109:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r27 = r27.size();
		r0 = r27;
		if (r8_curIndex >= r0) goto L_0x014b;
	L_0x0117:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r0 = r27;
		r17 = r0.get(r8_curIndex);
		r17 = (android.support.v4.view.ViewPager.ItemInfo) r17;
		r0 = r17_ii;
		r0 = r0.position;
		r27 = r0;
		r0 = r30;
		r0 = r0.mCurItem;
		r28 = r0;
		r0 = r27;
		r1 = r28;
		if (r0 < r1) goto L_0x0260;
	L_0x0137:
		r0 = r17_ii;
		r0 = r0.position;
		r27 = r0;
		r0 = r30;
		r0 = r0.mCurItem;
		r28 = r0;
		r0 = r27;
		r1 = r28;
		if (r0 != r1) goto L_0x014b;
	L_0x0149:
		r9_curItem = r17_ii;
	L_0x014b:
		if (r9_curItem != 0) goto L_0x015d;
	L_0x014d:
		if (r4_N <= 0) goto L_0x015d;
	L_0x014f:
		r0 = r30;
		r0 = r0.mCurItem;
		r27 = r0;
		r0 = r30;
		r1 = r27;
		r9_curItem = r0.addNewItem(r1, r8_curIndex);
	L_0x015d:
		if (r9_curItem == 0) goto L_0x01e1;
	L_0x015f:
		r13 = 0;
		r18 = r8_curIndex + -1;
		if (r18_itemIndex < 0) goto L_0x0264;
	L_0x0164:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r0 = r27;
		r1 = r18_itemIndex;
		r27 = r0.get(r1);
		r27 = (android.support.v4.view.ViewPager.ItemInfo) r27;
		r17_ii = r27;
	L_0x0176:
		r7 = r30.getClientWidth();
		if (r7_clientWidth > 0) goto L_0x0268;
	L_0x017c:
		r19 = 0;
	L_0x017e:
		r0 = r30;
		r0 = r0.mCurItem;
		r27 = r0;
		r23 = r27 + -1;
	L_0x0186:
		if (r23_pos < 0) goto L_0x0194;
	L_0x0188:
		r27 = (r13_extraWidthLeft > r19_leftWidthNeeded ? 1 : (r13_extraWidthLeft == r19_leftWidthNeeded ? 0 : -1));
		if (r27 < 0) goto L_0x02d9;
	L_0x018c:
		r0 = r23_pos;
		r1 = r26_startPos;
		if (r0 >= r1) goto L_0x02d9;
	L_0x0192:
		if (r17_ii != 0) goto L_0x0282;
	L_0x0194:
		r14 = r9_curItem.widthFactor;
		r18_itemIndex = r8_curIndex + 1;
		r27 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r27 = (r14_extraWidthRight > r27 ? 1 : (r14_extraWidthRight == r27 ? 0 : -1));
		if (r27 >= 0) goto L_0x01da;
	L_0x019e:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r27 = r27.size();
		r0 = r18_itemIndex;
		r1 = r27;
		if (r0 >= r1) goto L_0x0337;
	L_0x01ae:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r0 = r27;
		r1 = r18_itemIndex;
		r27 = r0.get(r1);
		r27 = (android.support.v4.view.ViewPager.ItemInfo) r27;
		r17_ii = r27;
	L_0x01c0:
		if (r7_clientWidth > 0) goto L_0x033b;
	L_0x01c2:
		r25 = 0;
	L_0x01c4:
		r0 = r30;
		r0 = r0.mCurItem;
		r27 = r0;
		r23_pos = r27 + 1;
	L_0x01cc:
		r0 = r23_pos;
		if (r0 >= r4_N) goto L_0x01da;
	L_0x01d0:
		r27 = (r14_extraWidthRight > r25_rightWidthNeeded ? 1 : (r14_extraWidthRight == r25_rightWidthNeeded ? 0 : -1));
		if (r27 < 0) goto L_0x03b0;
	L_0x01d4:
		r0 = r23_pos;
		if (r0 <= r12_endPos) goto L_0x03b0;
	L_0x01d8:
		if (r17_ii != 0) goto L_0x034f;
	L_0x01da:
		r0 = r30;
		r1 = r21_oldCurInfo;
		r0.calculatePageOffsets(r9_curItem, r8_curIndex, r1);
	L_0x01e1:
		r0 = r30;
		r0 = r0.mAdapter;
		r28 = r0;
		r0 = r30;
		r0 = r0.mCurItem;
		r29 = r0;
		if (r9_curItem == 0) goto L_0x0428;
	L_0x01ef:
		r0 = r9_curItem.object;
		r27 = r0;
	L_0x01f3:
		r0 = r28;
		r1 = r30;
		r2 = r29;
		r3 = r27;
		r0.setPrimaryItem(r1, r2, r3);
		r0 = r30;
		r0 = r0.mAdapter;
		r27 = r0;
		r0 = r27;
		r1 = r30;
		r0.finishUpdate(r1);
		r6 = r30.getChildCount();
		r16 = 0;
	L_0x0211:
		r0 = r16_i;
		if (r0 >= r6_childCount) goto L_0x042c;
	L_0x0215:
		r0 = r30;
		r1 = r16_i;
		r5 = r0.getChildAt(r1);
		r20 = r5_child.getLayoutParams();
		r20 = (android.support.v4.view.ViewPager.LayoutParams) r20;
		r0 = r16_i;
		r1 = r20_lp;
		r1.childIndex = r0;
		r0 = r20_lp;
		r0 = r0.isDecor;
		r27 = r0;
		if (r27 != 0) goto L_0x025d;
	L_0x0231:
		r0 = r20_lp;
		r0 = r0.widthFactor;
		r27 = r0;
		r28 = 0;
		r27 = (r27 > r28 ? 1 : (r27 == r28 ? 0 : -1));
		if (r27 != 0) goto L_0x025d;
	L_0x023d:
		r0 = r30;
		r17_ii = r0.infoForChild(r5_child);
		if (r17_ii == 0) goto L_0x025d;
	L_0x0245:
		r0 = r17_ii;
		r0 = r0.widthFactor;
		r27 = r0;
		r0 = r27;
		r1 = r20_lp;
		r1.widthFactor = r0;
		r0 = r17_ii;
		r0 = r0.position;
		r27 = r0;
		r0 = r27;
		r1 = r20_lp;
		r1.position = r0;
	L_0x025d:
		r16_i++;
		goto L_0x0211;
	L_0x0260:
		r8_curIndex++;
		goto L_0x0109;
	L_0x0264:
		r17_ii = 0;
		goto L_0x0176;
	L_0x0268:
		r27 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r0 = r9_curItem.widthFactor;
		r28 = r0;
		r27 -= r28;
		r28 = r30.getPaddingLeft();
		r0 = r28;
		r0 = (float) r0;
		r28 = r0;
		r0 = (float) r7_clientWidth;
		r29 = r0;
		r28 /= r29;
		r19_leftWidthNeeded = r27 + r28;
		goto L_0x017e;
	L_0x0282:
		r0 = r17_ii;
		r0 = r0.position;
		r27 = r0;
		r0 = r23_pos;
		r1 = r27;
		if (r0 != r1) goto L_0x02d2;
	L_0x028e:
		r0 = r17_ii;
		r0 = r0.scrolling;
		r27 = r0;
		if (r27 != 0) goto L_0x02d2;
	L_0x0296:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r0 = r27;
		r1 = r18_itemIndex;
		r0.remove(r1);
		r0 = r30;
		r0 = r0.mAdapter;
		r27 = r0;
		r0 = r17_ii;
		r0 = r0.object;
		r28 = r0;
		r0 = r27;
		r1 = r30;
		r2 = r23_pos;
		r3 = r28;
		r0.destroyItem(r1, r2, r3);
		r18_itemIndex++;
		r8_curIndex++;
		if (r18_itemIndex < 0) goto L_0x02d6;
	L_0x02c0:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r0 = r27;
		r1 = r18_itemIndex;
		r27 = r0.get(r1);
		r27 = (android.support.v4.view.ViewPager.ItemInfo) r27;
		r17_ii = r27;
	L_0x02d2:
		r23_pos++;
		goto L_0x0186;
	L_0x02d6:
		r17_ii = 0;
		goto L_0x02d2;
	L_0x02d9:
		if (r17_ii == 0) goto L_0x0309;
	L_0x02db:
		r0 = r17_ii;
		r0 = r0.position;
		r27 = r0;
		r0 = r23_pos;
		r1 = r27;
		if (r0 != r1) goto L_0x0309;
	L_0x02e7:
		r0 = r17_ii;
		r0 = r0.widthFactor;
		r27 = r0;
		r13_extraWidthLeft += r27;
		r18_itemIndex++;
		if (r18_itemIndex < 0) goto L_0x0306;
	L_0x02f3:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r0 = r27;
		r1 = r18_itemIndex;
		r27 = r0.get(r1);
		r27 = (android.support.v4.view.ViewPager.ItemInfo) r27;
		r17_ii = r27;
	L_0x0305:
		goto L_0x02d2;
	L_0x0306:
		r17_ii = 0;
		goto L_0x0305;
	L_0x0309:
		r27 = r18_itemIndex + 1;
		r0 = r30;
		r1 = r23_pos;
		r2 = r27;
		r17_ii = r0.addNewItem(r1, r2);
		r0 = r17_ii;
		r0 = r0.widthFactor;
		r27 = r0;
		r13_extraWidthLeft += r27;
		r8_curIndex++;
		if (r18_itemIndex < 0) goto L_0x0334;
	L_0x0321:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r0 = r27;
		r1 = r18_itemIndex;
		r27 = r0.get(r1);
		r27 = (android.support.v4.view.ViewPager.ItemInfo) r27;
		r17_ii = r27;
	L_0x0333:
		goto L_0x02d2;
	L_0x0334:
		r17_ii = 0;
		goto L_0x0333;
	L_0x0337:
		r17_ii = 0;
		goto L_0x01c0;
	L_0x033b:
		r27 = r30.getPaddingRight();
		r0 = r27;
		r0 = (float) r0;
		r27 = r0;
		r0 = (float) r7_clientWidth;
		r28 = r0;
		r27 /= r28;
		r28 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
		r25_rightWidthNeeded = r27 + r28;
		goto L_0x01c4;
	L_0x034f:
		r0 = r17_ii;
		r0 = r0.position;
		r27 = r0;
		r0 = r23_pos;
		r1 = r27;
		if (r0 != r1) goto L_0x03a9;
	L_0x035b:
		r0 = r17_ii;
		r0 = r0.scrolling;
		r27 = r0;
		if (r27 != 0) goto L_0x03a9;
	L_0x0363:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r0 = r27;
		r1 = r18_itemIndex;
		r0.remove(r1);
		r0 = r30;
		r0 = r0.mAdapter;
		r27 = r0;
		r0 = r17_ii;
		r0 = r0.object;
		r28 = r0;
		r0 = r27;
		r1 = r30;
		r2 = r23_pos;
		r3 = r28;
		r0.destroyItem(r1, r2, r3);
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r27 = r27.size();
		r0 = r18_itemIndex;
		r1 = r27;
		if (r0 >= r1) goto L_0x03ad;
	L_0x0397:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r0 = r27;
		r1 = r18_itemIndex;
		r27 = r0.get(r1);
		r27 = (android.support.v4.view.ViewPager.ItemInfo) r27;
		r17_ii = r27;
	L_0x03a9:
		r23_pos++;
		goto L_0x01cc;
	L_0x03ad:
		r17_ii = 0;
		goto L_0x03a9;
	L_0x03b0:
		if (r17_ii == 0) goto L_0x03ee;
	L_0x03b2:
		r0 = r17_ii;
		r0 = r0.position;
		r27 = r0;
		r0 = r23_pos;
		r1 = r27;
		if (r0 != r1) goto L_0x03ee;
	L_0x03be:
		r0 = r17_ii;
		r0 = r0.widthFactor;
		r27 = r0;
		r14_extraWidthRight += r27;
		r18_itemIndex++;
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r27 = r27.size();
		r0 = r18_itemIndex;
		r1 = r27;
		if (r0 >= r1) goto L_0x03eb;
	L_0x03d8:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r0 = r27;
		r1 = r18_itemIndex;
		r27 = r0.get(r1);
		r27 = (android.support.v4.view.ViewPager.ItemInfo) r27;
		r17_ii = r27;
	L_0x03ea:
		goto L_0x03a9;
	L_0x03eb:
		r17_ii = 0;
		goto L_0x03ea;
	L_0x03ee:
		r0 = r30;
		r1 = r23_pos;
		r2 = r18_itemIndex;
		r17_ii = r0.addNewItem(r1, r2);
		r18_itemIndex++;
		r0 = r17_ii;
		r0 = r0.widthFactor;
		r27 = r0;
		r14_extraWidthRight += r27;
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r27 = r27.size();
		r0 = r18_itemIndex;
		r1 = r27;
		if (r0 >= r1) goto L_0x0425;
	L_0x0412:
		r0 = r30;
		r0 = r0.mItems;
		r27 = r0;
		r0 = r27;
		r1 = r18_itemIndex;
		r27 = r0.get(r1);
		r27 = (android.support.v4.view.ViewPager.ItemInfo) r27;
		r17_ii = r27;
	L_0x0424:
		goto L_0x03a9;
	L_0x0425:
		r17_ii = 0;
		goto L_0x0424;
	L_0x0428:
		r27 = 0;
		goto L_0x01f3;
	L_0x042c:
		r30.sortChildDrawingOrder();
		r27 = r30.hasFocus();
		if (r27 == 0) goto L_0x003c;
	L_0x0435:
		r10 = r30.findFocus();
		if (r10_currentFocused == 0) goto L_0x048c;
	L_0x043b:
		r0 = r30;
		r17_ii = r0.infoForAnyChild(r10_currentFocused);
	L_0x0441:
		if (r17_ii == 0) goto L_0x0455;
	L_0x0443:
		r0 = r17_ii;
		r0 = r0.position;
		r27 = r0;
		r0 = r30;
		r0 = r0.mCurItem;
		r28 = r0;
		r0 = r27;
		r1 = r28;
		if (r0 == r1) goto L_0x003c;
	L_0x0455:
		r16_i = 0;
	L_0x0457:
		r27 = r30.getChildCount();
		r0 = r16_i;
		r1 = r27;
		if (r0 >= r1) goto L_0x003c;
	L_0x0461:
		r0 = r30;
		r1 = r16_i;
		r5_child = r0.getChildAt(r1);
		r0 = r30;
		r17_ii = r0.infoForChild(r5_child);
		if (r17_ii == 0) goto L_0x0489;
	L_0x0471:
		r0 = r17_ii;
		r0 = r0.position;
		r27 = r0;
		r0 = r30;
		r0 = r0.mCurItem;
		r28 = r0;
		r0 = r27;
		r1 = r28;
		if (r0 != r1) goto L_0x0489;
	L_0x0483:
		r27 = r5_child.requestFocus(r15_focusDirection);
		if (r27 != 0) goto L_0x003c;
	L_0x0489:
		r16_i++;
		goto L_0x0457;
	L_0x048c:
		r17_ii = 0;
		goto L_0x0441;
	}
	*/
	void populate(int newCurrentItem) {
		ItemInfo oldCurInfo = null;
		int focusDirection = SCROLL_STATE_SETTLING;
		if (mCurItem != newCurrentItem) {
			if (mCurItem < newCurrentItem) {
				focusDirection = 66;
			} else {
				focusDirection = 17;
			}
			oldCurInfo = infoForPosition(mCurItem);
			mCurItem = newCurrentItem;
		}
		if (mAdapter == null) {
			sortChildDrawingOrder();
		} else if (mPopulatePending) {
			sortChildDrawingOrder();
		} else if (getWindowToken() != null) {
			mAdapter.startUpdate((ViewGroup)this);
			int pageLimit = mOffscreenPageLimit;
			int startPos = Math.max(SCROLL_STATE_IDLE, mCurItem - pageLimit);
			int N = mAdapter.getCount();
			int endPos = Math.min(N - 1, mCurItem + pageLimit);
			if (N != mExpectedAdapterCount) {
				String resName;
				try {
					resName = getResources().getResourceName(getId());
				} catch (NotFoundException e) {
				}
				throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + mExpectedAdapterCount + ", found: " + N + " Pager id: " + resName + " Pager class: " + getClass() + " Problematic adapter: " + mAdapter.getClass());
			} else {
				ItemInfo curItem = null;
				int curIndex = SCROLL_STATE_IDLE;
				while (true) {
					ItemInfo ii;
					if (curIndex < mItems.size()) {
						ii = (ItemInfo) mItems.get(curIndex);
						if (ii.position >= mCurItem) {
							if (ii.position == mCurItem) {
								curItem = ii;
							}
						} else {
							curIndex++;
						}
					}
					PagerAdapter r28_PagerAdapter;
					int r29i;
					Object r27_Object;
					int childCount;
					int i;
					View child;
					LayoutParams lp;
					View currentFocused;
					if (curItem != null || N <= 0) {
						r28_PagerAdapter = mAdapter;
						r29i = mCurItem;
						if (curItem == null) {
							r27_Object = curItem.object;
						} else {
							r27_Object = null;
						}
						r28_PagerAdapter.setPrimaryItem((ViewGroup)this, r29i, r27_Object);
						mAdapter.finishUpdate((ViewGroup)this);
						childCount = getChildCount();
						i = SCROLL_STATE_IDLE;
						while (i < childCount) {
							child = getChildAt(i);
							lp = (LayoutParams) child.getLayoutParams();
							lp.childIndex = i;
							if (lp.isDecor || lp.widthFactor != 0.0f) {
								i++;
							} else {
								ii = infoForChild(child);
								if (ii == null) {
									lp.widthFactor = ii.widthFactor;
									lp.position = ii.position;
								}
								i++;
							}
						}
						sortChildDrawingOrder();
						if (!hasFocus()) {
							currentFocused = findFocus();
							if (currentFocused == null) {
								ii = infoForAnyChild(currentFocused);
							}
							if (null == null || ii.position != mCurItem) {
								i = SCROLL_STATE_IDLE;
								while (true) {
									if (i >= getChildCount()) {
										child = getChildAt(i);
										ii = infoForChild(child);
										if (ii == null || ii.position != mCurItem || !child.requestFocus(focusDirection)) {
											i++;
										} else {
											return;
										}
									} else {
										return;
									}
								}
							} else {
								return;
							}
						} else {
							return;
						}
					} else {
						curItem = addNewItem(mCurItem, curIndex);
						r28_PagerAdapter = mAdapter;
						r29i = mCurItem;
						if (curItem == null) {
							r27_Object = null;
						} else {
							r27_Object = curItem.object;
						}
						r28_PagerAdapter.setPrimaryItem((ViewGroup)this, r29i, r27_Object);
						mAdapter.finishUpdate((ViewGroup)this);
						childCount = getChildCount();
						i = SCROLL_STATE_IDLE;
						while (i < childCount) {
							child = getChildAt(i);
							lp = (LayoutParams) child.getLayoutParams();
							lp.childIndex = i;
							if (lp.isDecor || lp.widthFactor != 0.0f) {
								i++;
							} else {
								ii = infoForChild(child);
								if (ii == null) {
									i++;
								} else {
									lp.widthFactor = ii.widthFactor;
									lp.position = ii.position;
									i++;
								}
							}
						}
						sortChildDrawingOrder();
						if (!hasFocus()) {
							return;
						} else {
							currentFocused = findFocus();
							if (currentFocused == null) {
							} else {
								ii = infoForAnyChild(currentFocused);
							}
							if (null == null || ii.position != mCurItem) {
								i = SCROLL_STATE_IDLE;
								while (true) {
									if (i >= getChildCount()) {
										return;
									} else {
										child = getChildAt(i);
										ii = infoForChild(child);
										if (ii == null || ii.position != mCurItem || !child.requestFocus(focusDirection)) {
											i++;
										} else {
											return;
										}
									}
								}
							} else {
								return;
							}
						}
					}
				}
			}
		}
	}

	public void removeView(View view) {
		if (mInLayout) {
			removeViewInLayout(view);
		} else {
			super.removeView(view);
		}
	}

	public void setAdapter(PagerAdapter adapter) {
		if (mAdapter != null) {
			mAdapter.unregisterDataSetObserver(mObserver);
			mAdapter.startUpdate((ViewGroup)this);
			int i = SCROLL_STATE_IDLE;
			while (i < mItems.size()) {
				ItemInfo ii = (ItemInfo) mItems.get(i);
				mAdapter.destroyItem((ViewGroup)this, ii.position, ii.object);
				i++;
			}
			mAdapter.finishUpdate((ViewGroup)this);
			mItems.clear();
			removeNonDecorViews();
			mCurItem = 0;
			scrollTo(SCROLL_STATE_IDLE, SCROLL_STATE_IDLE);
		}
		PagerAdapter oldAdapter = mAdapter;
		mAdapter = adapter;
		mExpectedAdapterCount = 0;
		if (mAdapter != null) {
			if (mObserver == null) {
				mObserver = new PagerObserver(this, null);
			}
			mAdapter.registerDataSetObserver(mObserver);
			mPopulatePending = false;
			boolean wasFirstLayout = mFirstLayout;
			mFirstLayout = true;
			mExpectedAdapterCount = mAdapter.getCount();
			if (mRestoredCurItem >= 0) {
				mAdapter.restoreState(mRestoredAdapterState, mRestoredClassLoader);
				setCurrentItemInternal(mRestoredCurItem, DEBUG, true);
				mRestoredCurItem = -1;
				mRestoredAdapterState = null;
				mRestoredClassLoader = null;
			} else if (!wasFirstLayout) {
				populate();
			} else {
				requestLayout();
			}
		}
		if (mAdapterChangeListener == null || oldAdapter == adapter) {
		} else {
			mAdapterChangeListener.onAdapterChanged(oldAdapter, adapter);
		}
	}

	void setChildrenDrawingOrderEnabledCompat(boolean enable) {
		if (VERSION.SDK_INT >= 7) {
			if (mSetChildrenDrawingOrderEnabled == null) {
				try {
					Class[] r3_Class_A = new Class[1];
					r3_Class_A[0] = Boolean.TYPE;
					mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", r3_Class_A);
				} catch (NoSuchMethodException e) {
					Log.e(TAG, "Can't find setChildrenDrawingOrderEnabled", e);
				}
			}
			try {
				Object[] r2_Object_A = new Object[1];
				r2_Object_A[0] = Boolean.valueOf(enable);
				mSetChildrenDrawingOrderEnabled.invoke(this, r2_Object_A);
			} catch (Exception e_2) {
				Log.e(TAG, "Error changing children drawing order", e_2);
				return;
			}
		}
	}

	public void setCurrentItem(int item) {
		boolean r0z;
		mPopulatePending = false;
		if (!mFirstLayout) {
			r0z = true;
		} else {
			r0z = false;
		}
		setCurrentItemInternal(item, r0z, DEBUG);
	}

	public void setCurrentItem(int item, boolean smoothScroll) {
		mPopulatePending = false;
		setCurrentItemInternal(item, smoothScroll, DEBUG);
	}

	void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
		setCurrentItemInternal(item, smoothScroll, always, SCROLL_STATE_IDLE);
	}

	/* JADX WARNING: inconsistent code */
	/*
	void setCurrentItemInternal(int r6_item, boolean r7_smoothScroll, boolean r8_always, int r9_velocity) {
		r5_this = this;
		r0 = 1;
		r4 = 0;
		r3 = r5.mAdapter;
		if (r3 == 0) goto L_0x000e;
	L_0x0006:
		r3 = r5.mAdapter;
		r3 = r3.getCount();
		if (r3 > 0) goto L_0x0012;
	L_0x000e:
		r5.setScrollingCacheEnabled(r4);
	L_0x0011:
		return;
	L_0x0012:
		if (r8_always != 0) goto L_0x0024;
	L_0x0014:
		r3 = r5.mCurItem;
		if (r3 != r6_item) goto L_0x0024;
	L_0x0018:
		r3 = r5.mItems;
		r3 = r3.size();
		if (r3 == 0) goto L_0x0024;
	L_0x0020:
		r5.setScrollingCacheEnabled(r4);
		goto L_0x0011;
	L_0x0024:
		if (r6_item >= 0) goto L_0x0049;
	L_0x0026:
		r6_item = 0;
	L_0x0027:
		r2 = r5.mOffscreenPageLimit;
		r3 = r5.mCurItem;
		r3 += r2_pageLimit;
		if (r6_item > r3) goto L_0x0033;
	L_0x002e:
		r3 = r5.mCurItem;
		r3 -= r2_pageLimit;
		if (r6_item >= r3) goto L_0x005a;
	L_0x0033:
		r1 = 0;
	L_0x0034:
		r3 = r5.mItems;
		r3 = r3.size();
		if (r1_i >= r3) goto L_0x005a;
	L_0x003c:
		r3 = r5.mItems;
		r3 = r3.get(r1_i);
		r3 = (android.support.v4.view.ViewPager.ItemInfo) r3;
		r3.scrolling = r0;
		r1_i++;
		goto L_0x0034;
	L_0x0049:
		r3 = r5.mAdapter;
		r3 = r3.getCount();
		if (r6_item < r3) goto L_0x0027;
	L_0x0051:
		r3 = r5.mAdapter;
		r3 = r3.getCount();
		r6_item = r3 + -1;
		goto L_0x0027;
	L_0x005a:
		r3 = r5.mCurItem;
		if (r3 == r6_item) goto L_0x007e;
	L_0x005e:
		r3 = r5.mFirstLayout;
		if (r3 == 0) goto L_0x0080;
	L_0x0062:
		r5.mCurItem = r6_item;
		if (r0_dispatchSelected == 0) goto L_0x006f;
	L_0x0066:
		r3 = r5.mOnPageChangeListener;
		if (r3 == 0) goto L_0x006f;
	L_0x006a:
		r3 = r5.mOnPageChangeListener;
		r3.onPageSelected(r6_item);
	L_0x006f:
		if (r0_dispatchSelected == 0) goto L_0x007a;
	L_0x0071:
		r3 = r5.mInternalPageChangeListener;
		if (r3 == 0) goto L_0x007a;
	L_0x0075:
		r3 = r5.mInternalPageChangeListener;
		r3.onPageSelected(r6_item);
	L_0x007a:
		r5.requestLayout();
		goto L_0x0011;
	L_0x007e:
		r0_dispatchSelected = r4;
		goto L_0x005e;
	L_0x0080:
		r5.populate(r6_item);
		r5.scrollToItem(r6_item, r7_smoothScroll, r9_velocity, r0_dispatchSelected);
		goto L_0x0011;
	}
	*/
	void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
		boolean dispatchSelected = true;
		if (mAdapter == null || mAdapter.getCount() <= 0) {
			setScrollingCacheEnabled(DEBUG);
		} else if (always || mCurItem != item || mItems.size() == 0) {
			if (item >= mAdapter.getCount()) {
				item = mAdapter.getCount() - 1;
			}
			int pageLimit = mOffscreenPageLimit;
			if (item > mCurItem + pageLimit || item < mCurItem - pageLimit) {
				int i = SCROLL_STATE_IDLE;
				while (i < mItems.size()) {
					((ItemInfo) mItems.get(i)).scrolling = true;
					i++;
				}
			}
			if (mCurItem != item) {
				if (!mFirstLayout) {
					mCurItem = item;
					if (!dispatchSelected || mOnPageChangeListener == null) {
						requestLayout();
					} else {
						mOnPageChangeListener.onPageSelected(item);
						requestLayout();
					}
				} else {
					populate(item);
					scrollToItem(item, smoothScroll, velocity, dispatchSelected);
				}
			} else {
				dispatchSelected = false;
				if (!mFirstLayout) {
					populate(item);
					scrollToItem(item, smoothScroll, velocity, dispatchSelected);
				} else {
					mCurItem = item;
					if (!dispatchSelected || mOnPageChangeListener == null) {
						requestLayout();
					} else {
						mOnPageChangeListener.onPageSelected(item);
						requestLayout();
					}
				}
			}
		} else {
			setScrollingCacheEnabled(DEBUG);
		}
	}

	OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener listener) {
		mInternalPageChangeListener = listener;
		return mInternalPageChangeListener;
	}

	public void setOffscreenPageLimit(int limit) {
		if (limit < 1) {
			Log.w(TAG, "Requested offscreen page limit " + SCROLL_STATE_DRAGGING + " too small; defaulting to " + SCROLL_STATE_DRAGGING);
			limit = SCROLL_STATE_DRAGGING;
		}
		if (limit != mOffscreenPageLimit) {
			mOffscreenPageLimit = limit;
			populate();
		}
	}

	void setOnAdapterChangeListener(OnAdapterChangeListener listener) {
		mAdapterChangeListener = listener;
	}

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mOnPageChangeListener = listener;
	}

	public void setPageMargin(int marginPixels) {
		mPageMargin = marginPixels;
		int width = getWidth();
		recomputeScrollPosition(width, width, marginPixels, mPageMargin);
		requestLayout();
	}

	public void setPageMarginDrawable(int resId) {
		setPageMarginDrawable(getContext().getResources().getDrawable(resId));
	}

	public void setPageMarginDrawable(Drawable d) {
		boolean r0z;
		mMarginDrawable = d;
		if (d != null) {
			refreshDrawableState();
		}
		if (d == null) {
			r0z = true;
		} else {
			r0z = DEBUG;
		}
		setWillNotDraw(r0z);
		invalidate();
	}

	public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
		int r2i = SCROLL_STATE_DRAGGING;
		if (VERSION.SDK_INT >= 11) {
			boolean hasTransformer;
			boolean r4z;
			boolean needsPopulate;
			if (transformer != null) {
				hasTransformer = true;
			} else {
				hasTransformer = false;
			}
			if (mPageTransformer != null) {
				r4z = true;
			} else {
				r4z = false;
			}
			if (hasTransformer != r4z) {
				needsPopulate = true;
			} else {
				needsPopulate = false;
			}
			mPageTransformer = transformer;
			setChildrenDrawingOrderEnabledCompat(hasTransformer);
			if (hasTransformer) {
				if (reverseDrawingOrder) {
					r2i = SCROLL_STATE_SETTLING;
				}
				mDrawingOrder = r2i;
			} else {
				mDrawingOrder = 0;
			}
			if (needsPopulate) {
				populate();
			}
		}
	}

	void smoothScrollTo(int x, int y) {
		smoothScrollTo(x, y, SCROLL_STATE_IDLE);
	}

	void smoothScrollTo(int x, int y, int velocity) {
		if (getChildCount() == 0) {
			setScrollingCacheEnabled(DEBUG);
		} else {
			int sx = getScrollX();
			int sy = getScrollY();
			int dx = x - sx;
			int dy = y - sy;
			if (dx != 0 || dy != 0) {
				int duration;
				setScrollingCacheEnabled(true);
				setScrollState(SCROLL_STATE_SETTLING);
				int width = getClientWidth();
				int halfWidth = width / 2;
				float distance = ((float) halfWidth) + (((float) halfWidth) * distanceInfluenceForSnapDuration(Math.min(1.0f, (1.0f * ((float) Math.abs(dx))) / ((float) width))));
				velocity = Math.abs(velocity);
				if (velocity > 0) {
					duration = Math.round(1000.0f * Math.abs(distance / ((float) velocity))) * 4;
				} else {
					duration = (int) ((1.0f + (((float) Math.abs(dx)) / (((float) mPageMargin) + (((float) width) * mAdapter.getPageWidth(mCurItem))))) * 100.0f);
				}
				mScroller.startScroll(sx, sy, dx, dy, Math.min(duration, MAX_SETTLE_DURATION));
				ViewCompat.postInvalidateOnAnimation(this);
			} else {
				completeScroll(DEBUG);
				populate();
				setScrollState(SCROLL_STATE_IDLE);
			}
		}
	}

	protected boolean verifyDrawable(Drawable who) {
		if (super.verifyDrawable(who) || who == mMarginDrawable) {
			return true;
		} else {
			return DEBUG;
		}
	}
}
