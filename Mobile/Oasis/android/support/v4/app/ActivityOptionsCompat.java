package android.support.v4.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;

public class ActivityOptionsCompat {
	private static class ActivityOptionsImplJB extends ActivityOptionsCompat {
		private final ActivityOptionsCompatJB mImpl;

		ActivityOptionsImplJB(ActivityOptionsCompatJB impl) {
			super();
			mImpl = impl;
		}

		public Bundle toBundle() {
			return mImpl.toBundle();
		}

		public void update(ActivityOptionsCompat otherOptions) {
			if (otherOptions instanceof ActivityOptionsCompat.ActivityOptionsImplJB) {
				mImpl.update(((ActivityOptionsCompat.ActivityOptionsImplJB) otherOptions).mImpl);
			}
		}
	}


	protected ActivityOptionsCompat() {
		super();
	}

	public static ActivityOptionsCompat makeCustomAnimation(Context context, int enterResId, int exitResId) {
		if (VERSION.SDK_INT >= 16) {
			return new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeCustomAnimation(context, enterResId, exitResId));
		} else {
			return new ActivityOptionsCompat();
		}
	}

	public static ActivityOptionsCompat makeScaleUpAnimation(View source, int startX, int startY, int startWidth, int startHeight) {
		if (VERSION.SDK_INT >= 16) {
			return new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight));
		} else {
			return new ActivityOptionsCompat();
		}
	}

	public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View source, Bitmap thumbnail, int startX, int startY) {
		if (VERSION.SDK_INT >= 16) {
			return new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY));
		} else {
			return new ActivityOptionsCompat();
		}
	}

	public Bundle toBundle() {
		return null;
	}

	public void update(ActivityOptionsCompat otherOptions) {
	}
}
