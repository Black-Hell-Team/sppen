package android.support.v4.widget;

import android.content.Context;
import android.support.v4.media.TransportMediator;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ContentLoadingProgressBar extends ProgressBar {
	private static final int MIN_DELAY = 500;
	private static final int MIN_SHOW_TIME = 500;
	private final Runnable mDelayedHide;
	private final Runnable mDelayedShow;
	private boolean mDismissed;
	private boolean mPostedHide;
	private boolean mPostedShow;
	private long mStartTime;

	class AnonymousClass_1 implements Runnable {
		final /* synthetic */ ContentLoadingProgressBar this$0;

		AnonymousClass_1(ContentLoadingProgressBar r1_ContentLoadingProgressBar) {
			super();
			this$0 = r1_ContentLoadingProgressBar;
		}

		public void run() {
			this$0.mPostedHide = false;
			this$0.mStartTime = -1;
			this$0.setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
		}
	}

	class AnonymousClass_2 implements Runnable {
		final /* synthetic */ ContentLoadingProgressBar this$0;

		AnonymousClass_2(ContentLoadingProgressBar r1_ContentLoadingProgressBar) {
			super();
			this$0 = r1_ContentLoadingProgressBar;
		}

		public void run() {
			this$0.mPostedShow = false;
			if (!this$0.mDismissed) {
				this$0.mStartTime = System.currentTimeMillis();
				this$0.setVisibility(0);
			}
		}
	}


	public ContentLoadingProgressBar(Context context) {
		this(context, null);
	}

	public ContentLoadingProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		mStartTime = -1;
		mPostedHide = false;
		mPostedShow = false;
		mDismissed = false;
		mDelayedHide = new AnonymousClass_1(this);
		mDelayedShow = new AnonymousClass_2(this);
	}

	private void removeCallbacks() {
		removeCallbacks(mDelayedHide);
		removeCallbacks(mDelayedShow);
	}

	public void hide() {
		mDismissed = true;
		removeCallbacks(mDelayedShow);
		long diff = System.currentTimeMillis() - mStartTime;
		if (diff >= 500 || mStartTime == -1) {
			setVisibility(TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE);
		} else if (!mPostedHide) {
			postDelayed(mDelayedHide, 500 - diff);
			mPostedHide = true;
		}
	}

	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		removeCallbacks();
	}

	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		removeCallbacks();
	}

	public void show() {
		mStartTime = -1;
		mDismissed = false;
		removeCallbacks(mDelayedHide);
		if (!mPostedShow) {
			postDelayed(mDelayedShow, 500);
			mPostedShow = true;
		}
	}
}
