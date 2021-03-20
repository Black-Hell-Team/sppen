package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;

public class ViewPropertyAnimatorCompat {
	static final ViewPropertyAnimatorCompatImpl IMPL;
	private static final String TAG = "ViewAnimatorCompat";
	private WeakReference<View> mView;

	static interface ViewPropertyAnimatorCompatImpl {
		public void alpha(View r1_View, float r2f);

		public void alphaBy(View r1_View, float r2f);

		public void cancel(View r1_View);

		public long getDuration(View r1_View);

		public Interpolator getInterpolator(View r1_View);

		public long getStartDelay(View r1_View);

		public void rotation(View r1_View, float r2f);

		public void rotationBy(View r1_View, float r2f);

		public void rotationX(View r1_View, float r2f);

		public void rotationXBy(View r1_View, float r2f);

		public void rotationY(View r1_View, float r2f);

		public void rotationYBy(View r1_View, float r2f);

		public void scaleX(View r1_View, float r2f);

		public void scaleXBy(View r1_View, float r2f);

		public void scaleY(View r1_View, float r2f);

		public void scaleYBy(View r1_View, float r2f);

		public void setDuration(View r1_View, long r2j);

		public void setInterpolator(View r1_View, Interpolator r2_Interpolator);

		public void setListener(View r1_View, ViewPropertyAnimatorListener r2_ViewPropertyAnimatorListener);

		public void setStartDelay(View r1_View, long r2j);

		public void start(View r1_View);

		public void translationX(View r1_View, float r2f);

		public void translationXBy(View r1_View, float r2f);

		public void translationY(View r1_View, float r2f);

		public void translationYBy(View r1_View, float r2f);

		public void withEndAction(View r1_View, Runnable r2_Runnable);

		public void withLayer(View r1_View);

		public void withStartAction(View r1_View, Runnable r2_Runnable);

		public void x(View r1_View, float r2f);

		public void xBy(View r1_View, float r2f);

		public void y(View r1_View, float r2f);

		public void yBy(View r1_View, float r2f);
	}

	static class BaseViewPropertyAnimatorCompatImpl implements ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl {
		BaseViewPropertyAnimatorCompatImpl() {
			super();
		}

		public void alpha(View view, float value) {
		}

		public void alphaBy(View view, float value) {
		}

		public void cancel(View view) {
		}

		public long getDuration(View view) {
			return 0;
		}

		public Interpolator getInterpolator(View view) {
			return null;
		}

		public long getStartDelay(View view) {
			return 0;
		}

		public void rotation(View view, float value) {
		}

		public void rotationBy(View view, float value) {
		}

		public void rotationX(View view, float value) {
		}

		public void rotationXBy(View view, float value) {
		}

		public void rotationY(View view, float value) {
		}

		public void rotationYBy(View view, float value) {
		}

		public void scaleX(View view, float value) {
		}

		public void scaleXBy(View view, float value) {
		}

		public void scaleY(View view, float value) {
		}

		public void scaleYBy(View view, float value) {
		}

		public void setDuration(View view, long value) {
		}

		public void setInterpolator(View view, Interpolator value) {
		}

		public void setListener(View view, ViewPropertyAnimatorListener listener) {
		}

		public void setStartDelay(View view, long value) {
		}

		public void start(View view) {
		}

		public void translationX(View view, float value) {
		}

		public void translationXBy(View view, float value) {
		}

		public void translationY(View view, float value) {
		}

		public void translationYBy(View view, float value) {
		}

		public void withEndAction(View view, Runnable runnable) {
			runnable.run();
		}

		public void withLayer(View view) {
		}

		public void withStartAction(View view, Runnable runnable) {
			runnable.run();
		}

		public void x(View view, float value) {
		}

		public void xBy(View view, float value) {
		}

		public void y(View view, float value) {
		}

		public void yBy(View view, float value) {
		}
	}

	static class ICSViewPropertyAnimatorCompatImpl extends ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl {
		class AnonymousClass_1 implements ViewPropertyAnimatorListener {
			final /* synthetic */ ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl this$0;
			final /* synthetic */ Runnable val$runnable;

			AnonymousClass_1(ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl r1_ViewPropertyAnimatorCompat_ICSViewPropertyAnimatorCompatImpl, Runnable r2_Runnable) {
				super();
				this$0 = r1_ViewPropertyAnimatorCompat_ICSViewPropertyAnimatorCompatImpl;
				val$runnable = r2_Runnable;
			}

			public void onAnimationCancel(View view) {
			}

			public void onAnimationEnd(View view) {
				val$runnable.run();
				this$0.setListener(view, null);
			}

			public void onAnimationStart(View view) {
			}
		}

		class AnonymousClass_2 implements ViewPropertyAnimatorListener {
			final /* synthetic */ ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl this$0;
			final /* synthetic */ Runnable val$runnable;

			AnonymousClass_2(ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl r1_ViewPropertyAnimatorCompat_ICSViewPropertyAnimatorCompatImpl, Runnable r2_Runnable) {
				super();
				this$0 = r1_ViewPropertyAnimatorCompat_ICSViewPropertyAnimatorCompatImpl;
				val$runnable = r2_Runnable;
			}

			public void onAnimationCancel(View view) {
			}

			public void onAnimationEnd(View view) {
			}

			public void onAnimationStart(View view) {
				val$runnable.run();
				this$0.setListener(view, null);
			}
		}

		class AnonymousClass_3 implements ViewPropertyAnimatorListener {
			final /* synthetic */ ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl this$0;
			final /* synthetic */ int val$currentLayerType;

			AnonymousClass_3(ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl r1_ViewPropertyAnimatorCompat_ICSViewPropertyAnimatorCompatImpl, int r2i) {
				super();
				this$0 = r1_ViewPropertyAnimatorCompat_ICSViewPropertyAnimatorCompatImpl;
				val$currentLayerType = r2i;
			}

			public void onAnimationCancel(View view) {
			}

			public void onAnimationEnd(View view) {
				ViewCompat.setLayerType(view, val$currentLayerType, null);
				this$0.setListener(view, null);
			}

			public void onAnimationStart(View view) {
				ViewCompat.setLayerType(view, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER, null);
			}
		}


		ICSViewPropertyAnimatorCompatImpl() {
			super();
		}

		public void alpha(View view, float value) {
			ViewPropertyAnimatorCompatICS.alpha(view, value);
		}

		public void alphaBy(View view, float value) {
			ViewPropertyAnimatorCompatICS.alphaBy(view, value);
		}

		public void cancel(View view) {
			ViewPropertyAnimatorCompatICS.cancel(view);
		}

		public long getDuration(View view) {
			return ViewPropertyAnimatorCompatICS.getDuration(view);
		}

		public long getStartDelay(View view) {
			return ViewPropertyAnimatorCompatICS.getStartDelay(view);
		}

		public void rotation(View view, float value) {
			ViewPropertyAnimatorCompatICS.rotation(view, value);
		}

		public void rotationBy(View view, float value) {
			ViewPropertyAnimatorCompatICS.rotationBy(view, value);
		}

		public void rotationX(View view, float value) {
			ViewPropertyAnimatorCompatICS.rotationX(view, value);
		}

		public void rotationXBy(View view, float value) {
			ViewPropertyAnimatorCompatICS.rotationXBy(view, value);
		}

		public void rotationY(View view, float value) {
			ViewPropertyAnimatorCompatICS.rotationY(view, value);
		}

		public void rotationYBy(View view, float value) {
			ViewPropertyAnimatorCompatICS.rotationYBy(view, value);
		}

		public void scaleX(View view, float value) {
			ViewPropertyAnimatorCompatICS.scaleX(view, value);
		}

		public void scaleXBy(View view, float value) {
			ViewPropertyAnimatorCompatICS.scaleXBy(view, value);
		}

		public void scaleY(View view, float value) {
			ViewPropertyAnimatorCompatICS.scaleY(view, value);
		}

		public void scaleYBy(View view, float value) {
			ViewPropertyAnimatorCompatICS.scaleYBy(view, value);
		}

		public void setDuration(View view, long value) {
			ViewPropertyAnimatorCompatICS.setDuration(view, value);
		}

		public void setInterpolator(View view, Interpolator value) {
			ViewPropertyAnimatorCompatICS.setInterpolator(view, value);
		}

		public void setListener(View view, ViewPropertyAnimatorListener listener) {
			ViewPropertyAnimatorCompatICS.setListener(view, listener);
		}

		public void setStartDelay(View view, long value) {
			ViewPropertyAnimatorCompatICS.setStartDelay(view, value);
		}

		public void start(View view) {
			ViewPropertyAnimatorCompatICS.start(view);
		}

		public void translationX(View view, float value) {
			ViewPropertyAnimatorCompatICS.translationX(view, value);
		}

		public void translationXBy(View view, float value) {
			ViewPropertyAnimatorCompatICS.translationXBy(view, value);
		}

		public void translationY(View view, float value) {
			ViewPropertyAnimatorCompatICS.translationY(view, value);
		}

		public void translationYBy(View view, float value) {
			ViewPropertyAnimatorCompatICS.translationYBy(view, value);
		}

		public void withEndAction(View view, Runnable runnable) {
			setListener(view, new AnonymousClass_1(this, runnable));
		}

		public void withLayer(View view) {
			setListener(view, new AnonymousClass_3(this, ViewCompat.getLayerType(view)));
		}

		public void withStartAction(View view, Runnable runnable) {
			setListener(view, new AnonymousClass_2(this, runnable));
		}

		public void x(View view, float value) {
			ViewPropertyAnimatorCompatICS.x(view, value);
		}

		public void xBy(View view, float value) {
			ViewPropertyAnimatorCompatICS.xBy(view, value);
		}

		public void y(View view, float value) {
			ViewPropertyAnimatorCompatICS.y(view, value);
		}

		public void yBy(View view, float value) {
			ViewPropertyAnimatorCompatICS.yBy(view, value);
		}
	}

	static class JBViewPropertyAnimatorCompatImpl extends ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl {
		JBViewPropertyAnimatorCompatImpl() {
			super();
		}

		public void withEndAction(View view, Runnable runnable) {
			ViewPropertyAnimatorCompatJB.withEndAction(view, runnable);
		}

		public void withLayer(View view) {
			ViewPropertyAnimatorCompatJB.withLayer(view);
		}

		public void withStartAction(View view, Runnable runnable) {
			ViewPropertyAnimatorCompatJB.withStartAction(view, runnable);
		}
	}

	static class JBMr2ViewPropertyAnimatorCompatImpl extends ViewPropertyAnimatorCompat.JBViewPropertyAnimatorCompatImpl {
		JBMr2ViewPropertyAnimatorCompatImpl() {
			super();
		}

		public Interpolator getInterpolator(View view) {
			return ViewPropertyAnimatorCompatJellybeanMr2.getInterpolator(view);
		}
	}


	static {
		int version = VERSION.SDK_INT;
		if (version >= 18) {
			IMPL = new JBMr2ViewPropertyAnimatorCompatImpl();
		} else if (version >= 16) {
			IMPL = new JBViewPropertyAnimatorCompatImpl();
		} else if (version >= 14) {
			IMPL = new ICSViewPropertyAnimatorCompatImpl();
		} else {
			IMPL = new BaseViewPropertyAnimatorCompatImpl();
		}
	}

	ViewPropertyAnimatorCompat(View view) {
		super();
		mView = new WeakReference(view);
	}

	public ViewPropertyAnimatorCompat alpha(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.alpha(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat alphaBy(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.alphaBy(view, value);
		}
		return this;
	}

	public void cancel() {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.cancel(view);
		}
	}

	public long getDuration() {
		View view = (View) mView.get();
		if (view != null) {
			return IMPL.getDuration(view);
		} else {
			return 0;
		}
	}

	public Interpolator getInterpolator() {
		View view = (View) mView.get();
		if (view != null) {
			return IMPL.getInterpolator(view);
		} else {
			return null;
		}
	}

	public long getStartDelay() {
		View view = (View) mView.get();
		if (view != null) {
			return IMPL.getStartDelay(view);
		} else {
			return 0;
		}
	}

	public ViewPropertyAnimatorCompat rotation(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.rotation(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat rotationBy(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.rotationBy(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat rotationX(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.rotationX(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat rotationXBy(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.rotationXBy(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat rotationY(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.rotationY(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat rotationYBy(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.rotationYBy(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat scaleX(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.scaleX(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat scaleXBy(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.scaleXBy(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat scaleY(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.scaleY(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat scaleYBy(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.scaleYBy(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat setDuration(long value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.setDuration(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat setInterpolator(Interpolator value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.setInterpolator(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat setListener(ViewPropertyAnimatorListener listener) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.setListener(view, listener);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat setStartDelay(long value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.setStartDelay(view, value);
		}
		return this;
	}

	public void start() {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.start(view);
		}
	}

	public ViewPropertyAnimatorCompat translationX(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.translationX(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat translationXBy(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.translationXBy(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat translationY(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.translationY(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat translationYBy(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.translationYBy(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat withEndAction(Runnable runnable) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.withEndAction(view, runnable);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat withLayer() {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.withLayer(view);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat withStartAction(Runnable runnable) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.withStartAction(view, runnable);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat x(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.x(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat xBy(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.xBy(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat y(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.y(view, value);
		}
		return this;
	}

	public ViewPropertyAnimatorCompat yBy(float value) {
		View view = (View) mView.get();
		if (view != null) {
			IMPL.yBy(view, value);
		}
		return this;
	}
}
