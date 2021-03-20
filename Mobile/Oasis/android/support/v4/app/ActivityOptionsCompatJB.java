package android.support.v4.app;

import android.app.ActivityOptions;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

class ActivityOptionsCompatJB {
	private final ActivityOptions mActivityOptions;

	private ActivityOptionsCompatJB(ActivityOptions activityOptions) {
		super();
		mActivityOptions = activityOptions;
	}

	public static ActivityOptionsCompatJB makeCustomAnimation(Context context, int enterResId, int exitResId) {
		return new ActivityOptionsCompatJB(ActivityOptions.makeCustomAnimation(context, enterResId, exitResId));
	}

	public static ActivityOptionsCompatJB makeScaleUpAnimation(View source, int startX, int startY, int startWidth, int startHeight) {
		return new ActivityOptionsCompatJB(ActivityOptions.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight));
	}

	public static ActivityOptionsCompatJB makeThumbnailScaleUpAnimation(View source, Bitmap thumbnail, int startX, int startY) {
		return new ActivityOptionsCompatJB(ActivityOptions.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY));
	}

	public Bundle toBundle() {
		return mActivityOptions.toBundle();
	}

	public void update(ActivityOptionsCompatJB otherOptions) {
		mActivityOptions.update(otherOptions.mActivityOptions);
	}
}
