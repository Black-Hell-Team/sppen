package android.support.v4.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.view.ViewPager.Decor;
import android.support.v4.view.ViewPager.OnAdapterChangeListener;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ExploreByTouchHelper;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import java.lang.ref.WeakReference;

public class PagerTitleStrip extends ViewGroup implements Decor {
	private static final int[] ATTRS;
	private static final PagerTitleStripImpl IMPL;
	private static final float SIDE_ALPHA = 0.6f;
	private static final String TAG = "PagerTitleStrip";
	private static final int[] TEXT_ATTRS;
	private static final int TEXT_SPACING = 16;
	TextView mCurrText;
	private int mGravity;
	private int mLastKnownCurrentPage;
	private float mLastKnownPositionOffset;
	TextView mNextText;
	private int mNonPrimaryAlpha;
	private final PageListener mPageListener;
	ViewPager mPager;
	TextView mPrevText;
	private int mScaledTextSpacing;
	int mTextColor;
	private boolean mUpdatingPositions;
	private boolean mUpdatingText;
	private WeakReference<PagerAdapter> mWatchingAdapter;

	static /* synthetic */ class AnonymousClass_1 {
	}

	static interface PagerTitleStripImpl {
		public void setSingleLineAllCaps(TextView r1_TextView);
	}

	private class PageListener extends DataSetObserver implements OnPageChangeListener, OnAdapterChangeListener {
		private int mScrollState;
		final /* synthetic */ PagerTitleStrip this$0;

		private PageListener(PagerTitleStrip r1_PagerTitleStrip) {
			super();
			this$0 = r1_PagerTitleStrip;
		}

		/* synthetic */ PageListener(PagerTitleStrip x0, PagerTitleStrip.AnonymousClass_1 x1) {
			this(x0);
		}

		public void onAdapterChanged(PagerAdapter oldAdapter, PagerAdapter newAdapter) {
			this$0.updateAdapter(oldAdapter, newAdapter);
		}

		public void onChanged() {
			float offset = AutoScrollHelper.RELATIVE_UNSPECIFIED;
			this$0.updateText(this$0.mPager.getCurrentItem(), this$0.mPager.getAdapter());
			if (this$0.mLastKnownPositionOffset >= 0.0f) {
				offset = this$0.mLastKnownPositionOffset;
			}
			this$0.updateTextPositions(this$0.mPager.getCurrentItem(), offset, true);
		}

		public void onPageScrollStateChanged(int state) {
			mScrollState = state;
		}

		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			if (positionOffset > 0.5f) {
				position++;
			}
			this$0.updateTextPositions(position, positionOffset, false);
		}

		public void onPageSelected(int position) {
			float offset = AutoScrollHelper.RELATIVE_UNSPECIFIED;
			if (mScrollState == 0) {
				this$0.updateText(this$0.mPager.getCurrentItem(), this$0.mPager.getAdapter());
				if (this$0.mLastKnownPositionOffset >= 0.0f) {
					offset = this$0.mLastKnownPositionOffset;
				}
				this$0.updateTextPositions(this$0.mPager.getCurrentItem(), offset, true);
			}
		}
	}

	static class PagerTitleStripImplBase implements PagerTitleStrip.PagerTitleStripImpl {
		PagerTitleStripImplBase() {
			super();
		}

		public void setSingleLineAllCaps(TextView text) {
			text.setSingleLine();
		}
	}

	static class PagerTitleStripImplIcs implements PagerTitleStrip.PagerTitleStripImpl {
		PagerTitleStripImplIcs() {
			super();
		}

		public void setSingleLineAllCaps(TextView text) {
			PagerTitleStripIcs.setSingleLineAllCaps(text);
		}
	}


	static {
		ATTRS = new int[]{16842804, 16842901, 16842904, 16842927};
		int[] r0_int_A = new int[1];
		r0_int_A[0] = 16843660;
		TEXT_ATTRS = r0_int_A;
		if (VERSION.SDK_INT >= 14) {
			IMPL = new PagerTitleStripImplIcs();
		} else {
			IMPL = new PagerTitleStripImplBase();
		}
	}

	public PagerTitleStrip(Context context) {
		this(context, null);
	}

	public PagerTitleStrip(Context context, AttributeSet attrs) {
		super(context, attrs);
		mLastKnownCurrentPage = -1;
		mLastKnownPositionOffset = -1.0f;
		mPageListener = new PageListener(this, null);
		View r7_View = new TextView(context);
		mPrevText = r7_View;
		addView(r7_View);
		r7_View = new TextView(context);
		mCurrText = r7_View;
		addView(r7_View);
		r7_View = new TextView(context);
		mNextText = r7_View;
		addView(r7_View);
		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
		int textAppearance = a.getResourceId(0, 0);
		if (textAppearance != 0) {
			mPrevText.setTextAppearance(context, textAppearance);
			mCurrText.setTextAppearance(context, textAppearance);
			mNextText.setTextAppearance(context, textAppearance);
		}
		int textSize = a.getDimensionPixelSize(1, 0);
		if (textSize != 0) {
			setTextSize(0, (float) textSize);
		}
		if (a.hasValue(CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)) {
			int textColor = a.getColor(CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER, 0);
			mPrevText.setTextColor(textColor);
			mCurrText.setTextColor(textColor);
			mNextText.setTextColor(textColor);
		}
		mGravity = a.getInteger(WearableExtender.SIZE_MEDIUM, 80);
		a.recycle();
		mTextColor = mCurrText.getTextColors().getDefaultColor();
		setNonPrimaryAlpha(SIDE_ALPHA);
		mPrevText.setEllipsize(TruncateAt.END);
		mCurrText.setEllipsize(TruncateAt.END);
		mNextText.setEllipsize(TruncateAt.END);
		boolean allCaps = false;
		if (textAppearance != 0) {
			TypedArray ta = context.obtainStyledAttributes(textAppearance, TEXT_ATTRS);
			allCaps = ta.getBoolean(0, false);
			ta.recycle();
		}
		if (allCaps) {
			setSingleLineAllCaps(mPrevText);
			setSingleLineAllCaps(mCurrText);
			setSingleLineAllCaps(mNextText);
		} else {
			mPrevText.setSingleLine();
			mCurrText.setSingleLine();
			mNextText.setSingleLine();
		}
		mScaledTextSpacing = (int) (16.0f * context.getResources().getDisplayMetrics().density);
	}

	private static void setSingleLineAllCaps(TextView text) {
		IMPL.setSingleLineAllCaps(text);
	}

	int getMinHeight() {
		int minHeight = 0;
		Drawable bg = getBackground();
		if (bg != null) {
			minHeight = bg.getIntrinsicHeight();
		}
		return minHeight;
	}

	public int getTextSpacing() {
		return mScaledTextSpacing;
	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		ViewParent parent = getParent();
		if (!(parent instanceof ViewPager)) {
			throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
		} else {
			PagerAdapter r3_PagerAdapter;
			ViewPager pager = (ViewPager) parent;
			PagerAdapter adapter = pager.getAdapter();
			pager.setInternalPageChangeListener(mPageListener);
			pager.setOnAdapterChangeListener(mPageListener);
			mPager = pager;
			if (mWatchingAdapter != null) {
				r3_PagerAdapter = (PagerAdapter) mWatchingAdapter.get();
			} else {
				r3_PagerAdapter = null;
			}
			updateAdapter(r3_PagerAdapter, adapter);
		}
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mPager != null) {
			updateAdapter(mPager.getAdapter(), null);
			mPager.setInternalPageChangeListener(null);
			mPager.setOnAdapterChangeListener(null);
			mPager = null;
		}
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		float offset = AutoScrollHelper.RELATIVE_UNSPECIFIED;
		if (mPager != null) {
			if (mLastKnownPositionOffset >= 0.0f) {
				offset = mLastKnownPositionOffset;
			}
			updateTextPositions(mLastKnownCurrentPage, offset, true);
		}
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		if (MeasureSpec.getMode(widthMeasureSpec) != 1073741824) {
			throw new IllegalStateException("Must measure with an exact width");
		} else {
			int minHeight = getMinHeight();
			int padding = getPaddingTop() + getPaddingBottom();
			int childWidthSpec = MeasureSpec.makeMeasureSpec((int) (((float) widthSize) * 0.8f), ExploreByTouchHelper.INVALID_ID);
			int childHeightSpec = MeasureSpec.makeMeasureSpec(heightSize - padding, ExploreByTouchHelper.INVALID_ID);
			mPrevText.measure(childWidthSpec, childHeightSpec);
			mCurrText.measure(childWidthSpec, childHeightSpec);
			mNextText.measure(childWidthSpec, childHeightSpec);
			if (heightMode == 1073741824) {
				setMeasuredDimension(widthSize, heightSize);
			} else {
				setMeasuredDimension(widthSize, Math.max(minHeight, mCurrText.getMeasuredHeight() + padding));
			}
		}
	}

	public void requestLayout() {
		if (!mUpdatingText) {
			super.requestLayout();
		}
	}

	public void setGravity(int gravity) {
		mGravity = gravity;
		requestLayout();
	}

	public void setNonPrimaryAlpha(float alpha) {
		mNonPrimaryAlpha = ((int) (255.0f * alpha)) & 255;
		int transparentColor = (mNonPrimaryAlpha << 24) | (mTextColor & 16777215);
		mPrevText.setTextColor(transparentColor);
		mNextText.setTextColor(transparentColor);
	}

	public void setTextColor(int color) {
		mTextColor = color;
		mCurrText.setTextColor(color);
		int transparentColor = (mNonPrimaryAlpha << 24) | (mTextColor & 16777215);
		mPrevText.setTextColor(transparentColor);
		mNextText.setTextColor(transparentColor);
	}

	public void setTextSize(int unit, float size) {
		mPrevText.setTextSize(unit, size);
		mCurrText.setTextSize(unit, size);
		mNextText.setTextSize(unit, size);
	}

	public void setTextSpacing(int spacingPixels) {
		mScaledTextSpacing = spacingPixels;
		requestLayout();
	}

	void updateAdapter(PagerAdapter oldAdapter, PagerAdapter newAdapter) {
		if (oldAdapter != null) {
			oldAdapter.unregisterDataSetObserver(mPageListener);
			mWatchingAdapter = null;
		}
		if (newAdapter != null) {
			newAdapter.registerDataSetObserver(mPageListener);
			mWatchingAdapter = new WeakReference(newAdapter);
		}
		if (mPager != null) {
			mLastKnownCurrentPage = -1;
			mLastKnownPositionOffset = -1.0f;
			updateText(mPager.getCurrentItem(), newAdapter);
			requestLayout();
		}
	}

	void updateText(int currentItem, PagerAdapter adapter) {
		int itemCount;
		if (adapter != null) {
			itemCount = adapter.getCount();
		} else {
			itemCount = 0;
		}
		mUpdatingText = true;
		CharSequence text = null;
		TextView r8_TextView;
		CharSequence r7_CharSequence;
		int childWidthSpec;
		int childHeightSpec;
		if (currentItem < 1 || adapter == null) {
			mPrevText.setText(text);
			r8_TextView = mCurrText;
			if (adapter == null || currentItem >= itemCount) {
				r7_CharSequence = null;
			} else {
				r7_CharSequence = adapter.getPageTitle(currentItem);
			}
			r8_TextView.setText(r7_CharSequence);
			text = null;
			if (currentItem + 1 >= itemCount || adapter == null) {
				mNextText.setText(text);
				childWidthSpec = MeasureSpec.makeMeasureSpec((int) (((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) * 0.8f), ExploreByTouchHelper.INVALID_ID);
				childHeightSpec = MeasureSpec.makeMeasureSpec((getHeight() - getPaddingTop()) - getPaddingBottom(), ExploreByTouchHelper.INVALID_ID);
				mPrevText.measure(childWidthSpec, childHeightSpec);
				mCurrText.measure(childWidthSpec, childHeightSpec);
				mNextText.measure(childWidthSpec, childHeightSpec);
				mLastKnownCurrentPage = currentItem;
				if (mUpdatingPositions) {
					updateTextPositions(currentItem, mLastKnownPositionOffset, false);
				}
				mUpdatingText = false;
			} else {
				text = adapter.getPageTitle(currentItem + 1);
				mNextText.setText(text);
				childWidthSpec = MeasureSpec.makeMeasureSpec((int) (((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) * 0.8f), ExploreByTouchHelper.INVALID_ID);
				childHeightSpec = MeasureSpec.makeMeasureSpec((getHeight() - getPaddingTop()) - getPaddingBottom(), ExploreByTouchHelper.INVALID_ID);
				mPrevText.measure(childWidthSpec, childHeightSpec);
				mCurrText.measure(childWidthSpec, childHeightSpec);
				mNextText.measure(childWidthSpec, childHeightSpec);
				mLastKnownCurrentPage = currentItem;
				if (mUpdatingPositions) {
					mUpdatingText = false;
				} else {
					updateTextPositions(currentItem, mLastKnownPositionOffset, false);
					mUpdatingText = false;
				}
			}
		} else {
			text = adapter.getPageTitle(currentItem - 1);
			mPrevText.setText(text);
			r8_TextView = mCurrText;
			if (adapter == null || currentItem >= itemCount) {
				r7_CharSequence = null;
			} else {
				r7_CharSequence = adapter.getPageTitle(currentItem);
			}
			r8_TextView.setText(r7_CharSequence);
			text = null;
			if (currentItem + 1 >= itemCount || adapter == null) {
				mNextText.setText(text);
				childWidthSpec = MeasureSpec.makeMeasureSpec((int) (((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) * 0.8f), ExploreByTouchHelper.INVALID_ID);
				childHeightSpec = MeasureSpec.makeMeasureSpec((getHeight() - getPaddingTop()) - getPaddingBottom(), ExploreByTouchHelper.INVALID_ID);
				mPrevText.measure(childWidthSpec, childHeightSpec);
				mCurrText.measure(childWidthSpec, childHeightSpec);
				mNextText.measure(childWidthSpec, childHeightSpec);
				mLastKnownCurrentPage = currentItem;
				if (mUpdatingPositions) {
					updateTextPositions(currentItem, mLastKnownPositionOffset, false);
				}
				mUpdatingText = false;
			} else {
				text = adapter.getPageTitle(currentItem + 1);
				mNextText.setText(text);
				childWidthSpec = MeasureSpec.makeMeasureSpec((int) (((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) * 0.8f), ExploreByTouchHelper.INVALID_ID);
				childHeightSpec = MeasureSpec.makeMeasureSpec((getHeight() - getPaddingTop()) - getPaddingBottom(), ExploreByTouchHelper.INVALID_ID);
				mPrevText.measure(childWidthSpec, childHeightSpec);
				mCurrText.measure(childWidthSpec, childHeightSpec);
				mNextText.measure(childWidthSpec, childHeightSpec);
				mLastKnownCurrentPage = currentItem;
				if (mUpdatingPositions) {
					mUpdatingText = false;
				} else {
					updateTextPositions(currentItem, mLastKnownPositionOffset, false);
					mUpdatingText = false;
				}
			}
		}
	}

	void updateTextPositions(int position, float positionOffset, boolean force) {
		int prevWidth;
		int currWidth;
		int nextWidth;
		int halfCurrWidth;
		int stripWidth;
		int stripHeight;
		int paddingLeft;
		int paddingRight;
		int paddingTop;
		int paddingBottom;
		int textPaddedRight;
		int contentWidth;
		float currOffset;
		int prevTop;
		int currTop;
		int nextTop;
		int prevLeft;
		int nextLeft;
		if (position != mLastKnownCurrentPage) {
			updateText(position, mPager.getAdapter());
		} else if (force || positionOffset != mLastKnownPositionOffset) {
			mUpdatingPositions = true;
			prevWidth = mPrevText.getMeasuredWidth();
			currWidth = mCurrText.getMeasuredWidth();
			nextWidth = mNextText.getMeasuredWidth();
			halfCurrWidth = currWidth / 2;
			stripWidth = getWidth();
			stripHeight = getHeight();
			paddingLeft = getPaddingLeft();
			paddingRight = getPaddingRight();
			paddingTop = getPaddingTop();
			paddingBottom = getPaddingBottom();
			textPaddedRight = paddingRight + halfCurrWidth;
			contentWidth = (stripWidth - (paddingLeft + halfCurrWidth)) - textPaddedRight;
			currOffset = positionOffset + 0.5f;
		}
		mUpdatingPositions = true;
		prevWidth = mPrevText.getMeasuredWidth();
		currWidth = mCurrText.getMeasuredWidth();
		nextWidth = mNextText.getMeasuredWidth();
		halfCurrWidth = currWidth / 2;
		stripWidth = getWidth();
		stripHeight = getHeight();
		paddingLeft = getPaddingLeft();
		paddingRight = getPaddingRight();
		paddingTop = getPaddingTop();
		paddingBottom = getPaddingBottom();
		textPaddedRight = paddingRight + halfCurrWidth;
		contentWidth = (stripWidth - (paddingLeft + halfCurrWidth)) - textPaddedRight;
		currOffset = positionOffset + 0.5f;
		if (currOffset > 1.0f) {
			currOffset -= 1.0f;
		}
		int currLeft = ((stripWidth - textPaddedRight) - ((int) (((float) contentWidth) * currOffset))) - (currWidth / 2);
		int currRight = currLeft + currWidth;
		int prevBaseline = mPrevText.getBaseline();
		int currBaseline = mCurrText.getBaseline();
		int nextBaseline = mNextText.getBaseline();
		int maxBaseline = Math.max(Math.max(prevBaseline, currBaseline), nextBaseline);
		int prevTopOffset = maxBaseline - prevBaseline;
		int currTopOffset = maxBaseline - currBaseline;
		int nextTopOffset = maxBaseline - nextBaseline;
		int maxTextHeight = Math.max(Math.max(prevTopOffset + mPrevText.getMeasuredHeight(), currTopOffset + mCurrText.getMeasuredHeight()), nextTopOffset + mNextText.getMeasuredHeight());
		switch((mGravity & 112)) {
		case TEXT_SPACING:
			int centeredTop = (((stripHeight - paddingTop) - paddingBottom) - maxTextHeight) / 2;
			prevTop = centeredTop + prevTopOffset;
			currTop = centeredTop + currTopOffset;
			nextTop = centeredTop + nextTopOffset;
			mCurrText.layout(currLeft, currTop, currRight, mCurrText.getMeasuredHeight() + currTop);
			prevLeft = Math.min(paddingLeft, (currLeft - mScaledTextSpacing) - prevWidth);
			mPrevText.layout(prevLeft, prevTop, prevLeft + prevWidth, mPrevText.getMeasuredHeight() + prevTop);
			nextLeft = Math.max((stripWidth - paddingRight) - nextWidth, mScaledTextSpacing + currRight);
			mNextText.layout(nextLeft, nextTop, nextLeft + nextWidth, mNextText.getMeasuredHeight() + nextTop);
			mLastKnownPositionOffset = positionOffset;
			mUpdatingPositions = false;
		case 80:
			int bottomGravTop = (stripHeight - paddingBottom) - maxTextHeight;
			prevTop = bottomGravTop + prevTopOffset;
			currTop = bottomGravTop + currTopOffset;
			nextTop = bottomGravTop + nextTopOffset;
			mCurrText.layout(currLeft, currTop, currRight, mCurrText.getMeasuredHeight() + currTop);
			prevLeft = Math.min(paddingLeft, (currLeft - mScaledTextSpacing) - prevWidth);
			mPrevText.layout(prevLeft, prevTop, prevLeft + prevWidth, mPrevText.getMeasuredHeight() + prevTop);
			nextLeft = Math.max((stripWidth - paddingRight) - nextWidth, mScaledTextSpacing + currRight);
			mNextText.layout(nextLeft, nextTop, nextLeft + nextWidth, mNextText.getMeasuredHeight() + nextTop);
			mLastKnownPositionOffset = positionOffset;
			mUpdatingPositions = false;
		}
		prevTop = paddingTop + prevTopOffset;
		currTop = paddingTop + currTopOffset;
		nextTop = paddingTop + nextTopOffset;
		mCurrText.layout(currLeft, currTop, currRight, mCurrText.getMeasuredHeight() + currTop);
		prevLeft = Math.min(paddingLeft, (currLeft - mScaledTextSpacing) - prevWidth);
		mPrevText.layout(prevLeft, prevTop, prevLeft + prevWidth, mPrevText.getMeasuredHeight() + prevTop);
		nextLeft = Math.max((stripWidth - paddingRight) - nextWidth, mScaledTextSpacing + currRight);
		mNextText.layout(nextLeft, nextTop, nextLeft + nextWidth, mNextText.getMeasuredHeight() + nextTop);
		mLastKnownPositionOffset = positionOffset;
		mUpdatingPositions = false;
	}
}
