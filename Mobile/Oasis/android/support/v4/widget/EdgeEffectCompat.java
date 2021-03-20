package android.support.v4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build.VERSION;

public class EdgeEffectCompat {
	private static final EdgeEffectImpl IMPL;
	private Object mEdgeEffect;

	static interface EdgeEffectImpl {
		public boolean draw(Object r1_Object, Canvas r2_Canvas);

		public void finish(Object r1_Object);

		public boolean isFinished(Object r1_Object);

		public Object newEdgeEffect(Context r1_Context);

		public boolean onAbsorb(Object r1_Object, int r2i);

		public boolean onPull(Object r1_Object, float r2f);

		public boolean onRelease(Object r1_Object);

		public void setSize(Object r1_Object, int r2i, int r3i);
	}

	static class BaseEdgeEffectImpl implements EdgeEffectCompat.EdgeEffectImpl {
		BaseEdgeEffectImpl() {
			super();
		}

		public boolean draw(Object edgeEffect, Canvas canvas) {
			return false;
		}

		public void finish(Object edgeEffect) {
		}

		public boolean isFinished(Object edgeEffect) {
			return true;
		}

		public Object newEdgeEffect(Context context) {
			return null;
		}

		public boolean onAbsorb(Object edgeEffect, int velocity) {
			return false;
		}

		public boolean onPull(Object edgeEffect, float deltaDistance) {
			return false;
		}

		public boolean onRelease(Object edgeEffect) {
			return false;
		}

		public void setSize(Object edgeEffect, int width, int height) {
		}
	}

	static class EdgeEffectIcsImpl implements EdgeEffectCompat.EdgeEffectImpl {
		EdgeEffectIcsImpl() {
			super();
		}

		public boolean draw(Object edgeEffect, Canvas canvas) {
			return EdgeEffectCompatIcs.draw(edgeEffect, canvas);
		}

		public void finish(Object edgeEffect) {
			EdgeEffectCompatIcs.finish(edgeEffect);
		}

		public boolean isFinished(Object edgeEffect) {
			return EdgeEffectCompatIcs.isFinished(edgeEffect);
		}

		public Object newEdgeEffect(Context context) {
			return EdgeEffectCompatIcs.newEdgeEffect(context);
		}

		public boolean onAbsorb(Object edgeEffect, int velocity) {
			return EdgeEffectCompatIcs.onAbsorb(edgeEffect, velocity);
		}

		public boolean onPull(Object edgeEffect, float deltaDistance) {
			return EdgeEffectCompatIcs.onPull(edgeEffect, deltaDistance);
		}

		public boolean onRelease(Object edgeEffect) {
			return EdgeEffectCompatIcs.onRelease(edgeEffect);
		}

		public void setSize(Object edgeEffect, int width, int height) {
			EdgeEffectCompatIcs.setSize(edgeEffect, width, height);
		}
	}


	static {
		if (VERSION.SDK_INT >= 14) {
			IMPL = new EdgeEffectIcsImpl();
		} else {
			IMPL = new BaseEdgeEffectImpl();
		}
	}

	public EdgeEffectCompat(Context context) {
		super();
		mEdgeEffect = IMPL.newEdgeEffect(context);
	}

	public boolean draw(Canvas canvas) {
		return IMPL.draw(mEdgeEffect, canvas);
	}

	public void finish() {
		IMPL.finish(mEdgeEffect);
	}

	public boolean isFinished() {
		return IMPL.isFinished(mEdgeEffect);
	}

	public boolean onAbsorb(int velocity) {
		return IMPL.onAbsorb(mEdgeEffect, velocity);
	}

	public boolean onPull(float deltaDistance) {
		return IMPL.onPull(mEdgeEffect, deltaDistance);
	}

	public boolean onRelease() {
		return IMPL.onRelease(mEdgeEffect);
	}

	public void setSize(int width, int height) {
		IMPL.setSize(mEdgeEffect, width, height);
	}
}
