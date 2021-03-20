package android.support.v4.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.widget.CursorAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;

public class PagerTabStrip extends PagerTitleStrip {
	private static final int FULL_UNDERLINE_HEIGHT = 1;
	private static final int INDICATOR_HEIGHT = 3;
	private static final int MIN_PADDING_BOTTOM = 6;
	private static final int MIN_STRIP_HEIGHT = 32;
	private static final int MIN_TEXT_SPACING = 64;
	private static final int TAB_PADDING = 16;
	private static final int TAB_SPACING = 32;
	private static final String TAG = "PagerTabStrip";
	private boolean mDrawFullUnderline;
	private boolean mDrawFullUnderlineSet;
	private int mFullUnderlineHeight;
	private boolean mIgnoreTap;
	private int mIndicatorColor;
	private int mIndicatorHeight;
	private float mInitialMotionX;
	private float mInitialMotionY;
	private int mMinPaddingBottom;
	private int mMinStripHeight;
	private int mMinTextSpacing;
	private int mTabAlpha;
	private int mTabPadding;
	private final Paint mTabPaint;
	private final Rect mTempRect;
	private int mTouchSlop;

	class AnonymousClass_1 implements OnClickListener {
		final /* synthetic */ PagerTabStrip this$0;

		AnonymousClass_1(PagerTabStrip r1_PagerTabStrip) {
			super();
			this$0 = r1_PagerTabStrip;
		}

		public void onClick(View v) {
			this$0.mPager.setCurrentItem(this$0.mPager.getCurrentItem() - 1);
		}
	}

	class AnonymousClass_2 implements OnClickListener {
		final /* synthetic */ PagerTabStrip this$0;

		AnonymousClass_2(PagerTabStrip r1_PagerTabStrip) {
			super();
			this$0 = r1_PagerTabStrip;
		}

		public void onClick(View v) {
			this$0.mPager.setCurrentItem(this$0.mPager.getCurrentItem() + 1);
		}
	}


	public PagerTabStrip(Context context) {
		this(context, null);
	}

	public PagerTabStrip(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTabPaint = new Paint();
		mTempRect = new Rect();
		mTabAlpha = 255;
		mDrawFullUnderline = false;
		mDrawFullUnderlineSet = false;
		mIndicatorColor = mTextColor;
		mTabPaint.setColor(mIndicatorColor);
		float density = context.getResources().getDisplayMetrics().density;
		mIndicatorHeight = (int) ((3.0f * density) + 0.5f);
		mMinPaddingBottom = (int) ((6.0f * density) + 0.5f);
		mMinTextSpacing = (int) (64.0f * density);
		mTabPadding = (int) ((16.0f * density) + 0.5f);
		mFullUnderlineHeight = (int) ((1.0f * density) + 0.5f);
		mMinStripHeight = (int) ((32.0f * density) + 0.5f);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
		setTextSpacing(getTextSpacing());
		setWillNotDraw(false);
		mPrevText.setFocusable(true);
		mPrevText.setOnClickListener(new AnonymousClass_1(this));
		mNextText.setFocusable(true);
		mNextText.setOnClickListener(new AnonymousClass_2(this));
		if (getBackground() == null) {
			mDrawFullUnderline = true;
		}
	}

	public boolean getDrawFullUnderline() {
		return mDrawFullUnderline;
	}

	int getMinHeight() {
		return Math.max(super.getMinHeight(), mMinStripHeight);
	}

	public int getTabIndicatorColor() {
		return mIndicatorColor;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int bottom = height;
		mTabPaint.setColor((mTabAlpha << 24) | (mIndicatorColor & 16777215));
		canvas.drawRect((float) (mCurrText.getLeft() - mTabPadding), (float) (bottom - mIndicatorHeight), (float) (mCurrText.getRight() + mTabPadding), (float) bottom, mTabPaint);
		if (mDrawFullUnderline) {
			mTabPaint.setColor(-16777216 | (mIndicatorColor & 16777215));
			canvas.drawRect((float) getPaddingLeft(), (float) (height - mFullUnderlineHeight), (float) (getWidth() - getPaddingRight()), (float) height, mTabPaint);
		}
	}

	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		if (action == 0 || !mIgnoreTap) {
			float x = ev.getX();
			float y = ev.getY();
			switch(action) {
			case WearableExtender.SIZE_DEFAULT:
				mInitialMotionX = x;
				mInitialMotionY = y;
				mIgnoreTap = false;
				break;
			case FULL_UNDERLINE_HEIGHT:
				if (x < ((float) (mCurrText.getLeft() - mTabPadding))) {
					mPager.setCurrentItem(mPager.getCurrentItem() - 1);
				} else if (x > ((float) (mCurrText.getRight() + mTabPadding))) {
					mPager.setCurrentItem(mPager.getCurrentItem() + 1);
				}
				break;
			case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER:
				if (Math.abs(x - mInitialMotionX) > ((float) mTouchSlop) || Math.abs(y - mInitialMotionY) > ((float) mTouchSlop)) {
					mIgnoreTap = true;
				}
				break;
			}
			return true;
		} else {
			return false;
		}
	}

	public void setBackgroundColor(int color) {
		super.setBackgroundColor(color);
		if (!mDrawFullUnderlineSet) {
			boolean r0z;
			if ((-16777216 & color) == 0) {
				r0z = true;
			} else {
				r0z = false;
			}
			mDrawFullUnderline = r0z;
		}
	}

	public void setBackgroundDrawable(Drawable d) {
		super.setBackgroundDrawable(d);
		if (!mDrawFullUnderlineSet) {
			boolean r0z;
			if (d == null) {
				r0z = true;
			} else {
				r0z = false;
			}
			mDrawFullUnderline = r0z;
		}
	}

	public void setBackgroundResource(int resId) {
		super.setBackgroundResource(resId);
		if (!mDrawFullUnderlineSet) {
			boolean r0z;
			if (resId == 0) {
				r0z = true;
			} else {
				r0z = false;
			}
			mDrawFullUnderline = r0z;
		}
	}

	public void setDrawFullUnderline(boolean drawFull) {
		mDrawFullUnderline = drawFull;
		mDrawFullUnderlineSet = true;
		invalidate();
	}

	public void setPadding(int left, int top, int right, int bottom) {
		if (bottom < mMinPaddingBottom) {
			bottom = mMinPaddingBottom;
		}
		super.setPadding(left, top, right, bottom);
	}

	public void setTabIndicatorColor(int color) {
		mIndicatorColor = color;
		mTabPaint.setColor(mIndicatorColor);
		invalidate();
	}

	public void setTabIndicatorColorResource(int resId) {
		setTabIndicatorColor(getContext().getResources().getColor(resId));
	}

	public void setTextSpacing(int textSpacing) {
		if (textSpacing < mMinTextSpacing) {
			textSpacing = mMinTextSpacing;
		}
		super.setTextSpacing(textSpacing);
	}

	void updateTextPositions(int position, float positionOffset, boolean force) {
		Rect r = mTempRect;
		int bottom = getHeight();
		int top = bottom - mIndicatorHeight;
		r.set(mCurrText.getLeft() - mTabPadding, top, mCurrText.getRight() + mTabPadding, bottom);
		super.updateTextPositions(position, positionOffset, force);
		mTabAlpha = (int) ((Math.abs(positionOffset - 0.5f) * 2.0f) * 255.0f);
		r.union(mCurrText.getLeft() - mTabPadding, top, mCurrText.getRight() + mTabPadding, bottom);
		invalidate(r);
	}
}
