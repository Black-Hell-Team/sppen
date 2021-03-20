package android.support.v4.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

final class SwipeProgressBar {
	private static final int ANIMATION_DURATION_MS = 2000;
	private static final int COLOR1 = -1291845632;
	private static final int COLOR2 = -2147483648;
	private static final int COLOR3 = 1291845632;
	private static final int COLOR4 = 436207616;
	private static final int FINISH_ANIMATION_DURATION_MS = 1000;
	private static final Interpolator INTERPOLATOR;
	private Rect mBounds;
	private final RectF mClipRect;
	private int mColor1;
	private int mColor2;
	private int mColor3;
	private int mColor4;
	private long mFinishTime;
	private final Paint mPaint;
	private View mParent;
	private boolean mRunning;
	private long mStartTime;
	private float mTriggerPercentage;

	static {
		INTERPOLATOR = BakedBezierInterpolator.getInstance();
	}

	public SwipeProgressBar(View parent) {
		super();
		mPaint = new Paint();
		mClipRect = new RectF();
		mBounds = new Rect();
		mParent = parent;
		mColor1 = -1291845632;
		mColor2 = -2147483648;
		mColor3 = 1291845632;
		mColor4 = 436207616;
	}

	private void drawCircle(Canvas canvas, float cx, float cy, int color, float pct) {
		mPaint.setColor(color);
		canvas.save();
		canvas.translate(cx, cy);
		float radiusScale = INTERPOLATOR.getInterpolation(pct);
		canvas.scale(radiusScale, radiusScale);
		canvas.drawCircle(AutoScrollHelper.RELATIVE_UNSPECIFIED, AutoScrollHelper.RELATIVE_UNSPECIFIED, cx, mPaint);
		canvas.restore();
	}

	private void drawTrigger(Canvas canvas, int cx, int cy) {
		mPaint.setColor(mColor1);
		canvas.drawCircle((float) cx, (float) cy, ((float) cx) * mTriggerPercentage, mPaint);
	}

	void draw(Canvas canvas) {
		int width = mBounds.width();
		int height = mBounds.height();
		int cx = width / 2;
		int cy = height / 2;
		boolean drawTriggerWhileFinishing = false;
		int restoreCount = canvas.save();
		canvas.clipRect(mBounds);
		if (mRunning || mFinishTime > 0) {
			long now = AnimationUtils.currentAnimationTimeMillis();
			long iterations = (now - mStartTime) / 2000;
			float rawProgress = ((float) ((now - mStartTime) % 2000)) / 20.0f;
			if (!mRunning) {
				if (now - mFinishTime >= 1000) {
					mFinishTime = 0;
				} else {
					float clearRadius = ((float) (width / 2)) * INTERPOLATOR.getInterpolation((((float) ((now - mFinishTime) % 1000)) / 10.0f) / 100.0f);
					mClipRect.set(((float) cx) - clearRadius, AutoScrollHelper.RELATIVE_UNSPECIFIED, ((float) cx) + clearRadius, (float) height);
					canvas.saveLayerAlpha(mClipRect, 0, 0);
					drawTriggerWhileFinishing = true;
				}
			}
			if (iterations == 0) {
				canvas.drawColor(mColor1);
			} else if (rawProgress < 0.0f || rawProgress >= 25.0f) {
				if (rawProgress < 25.0f || rawProgress >= 50.0f) {
					if (rawProgress < 50.0f || rawProgress >= 75.0f) {
						canvas.drawColor(mColor3);
					} else {
						canvas.drawColor(mColor2);
					}
				} else {
					canvas.drawColor(mColor1);
				}
			} else {
				canvas.drawColor(mColor4);
			}
			if (rawProgress < 0.0f || rawProgress > 25.0f) {
				if (rawProgress < 0.0f || rawProgress > 50.0f) {
					if (rawProgress < 25.0f || rawProgress > 75.0f) {
						if (rawProgress < 50.0f || rawProgress > 100.0f) {
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						} else {
							drawCircle(canvas, (float) cx, (float) cy, mColor4, ((rawProgress - 50.0f) * 2.0f) / 100.0f);
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						}
					} else {
						drawCircle(canvas, (float) cx, (float) cy, mColor3, ((rawProgress - 25.0f) * 2.0f) / 100.0f);
						if (rawProgress < 50.0f || rawProgress > 100.0f) {
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						} else {
							drawCircle(canvas, (float) cx, (float) cy, mColor4, ((rawProgress - 50.0f) * 2.0f) / 100.0f);
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						}
					}
				} else {
					drawCircle(canvas, (float) cx, (float) cy, mColor2, (2.0f * rawProgress) / 100.0f);
					if (rawProgress < 25.0f || rawProgress > 75.0f) {
						if (rawProgress < 50.0f || rawProgress > 100.0f) {
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						} else {
							drawCircle(canvas, (float) cx, (float) cy, mColor4, ((rawProgress - 50.0f) * 2.0f) / 100.0f);
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						}
					} else {
						drawCircle(canvas, (float) cx, (float) cy, mColor3, ((rawProgress - 25.0f) * 2.0f) / 100.0f);
						if (rawProgress < 50.0f || rawProgress > 100.0f) {
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						} else {
							drawCircle(canvas, (float) cx, (float) cy, mColor4, ((rawProgress - 50.0f) * 2.0f) / 100.0f);
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						}
					}
				}
			} else {
				drawCircle(canvas, (float) cx, (float) cy, mColor1, ((25.0f + rawProgress) * 2.0f) / 100.0f);
				if (rawProgress < 0.0f || rawProgress > 50.0f) {
					if (rawProgress < 25.0f || rawProgress > 75.0f) {
						if (rawProgress < 50.0f || rawProgress > 100.0f) {
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						} else {
							drawCircle(canvas, (float) cx, (float) cy, mColor4, ((rawProgress - 50.0f) * 2.0f) / 100.0f);
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						}
					} else {
						drawCircle(canvas, (float) cx, (float) cy, mColor3, ((rawProgress - 25.0f) * 2.0f) / 100.0f);
						if (rawProgress < 50.0f || rawProgress > 100.0f) {
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						} else {
							drawCircle(canvas, (float) cx, (float) cy, mColor4, ((rawProgress - 50.0f) * 2.0f) / 100.0f);
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						}
					}
				} else {
					drawCircle(canvas, (float) cx, (float) cy, mColor2, (2.0f * rawProgress) / 100.0f);
					if (rawProgress < 25.0f || rawProgress > 75.0f) {
						if (rawProgress < 50.0f || rawProgress > 100.0f) {
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						} else {
							drawCircle(canvas, (float) cx, (float) cy, mColor4, ((rawProgress - 50.0f) * 2.0f) / 100.0f);
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						}
					} else {
						drawCircle(canvas, (float) cx, (float) cy, mColor3, ((rawProgress - 25.0f) * 2.0f) / 100.0f);
						if (rawProgress < 50.0f || rawProgress > 100.0f) {
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						} else {
							drawCircle(canvas, (float) cx, (float) cy, mColor4, ((rawProgress - 50.0f) * 2.0f) / 100.0f);
							if (rawProgress < 75.0f || rawProgress > 100.0f) {
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							} else {
								drawCircle(canvas, (float) cx, (float) cy, mColor1, ((rawProgress - 75.0f) * 2.0f) / 100.0f);
								if (mTriggerPercentage <= 0.0f || !drawTriggerWhileFinishing) {
									ViewCompat.postInvalidateOnAnimation(mParent);
								} else {
									canvas.restoreToCount(restoreCount);
									restoreCount = canvas.save();
									canvas.clipRect(mBounds);
									drawTrigger(canvas, cx, cy);
									ViewCompat.postInvalidateOnAnimation(mParent);
								}
							}
						}
					}
				}
			}
		} else if (mTriggerPercentage <= 0.0f || ((double) mTriggerPercentage) > 1.0d) {
			canvas.restoreToCount(restoreCount);
		} else {
			drawTrigger(canvas, cx, cy);
		}
		canvas.restoreToCount(restoreCount);
	}

	boolean isRunning() {
		if (mRunning || mFinishTime > 0) {
			return true;
		} else {
			return false;
		}
	}

	void setBounds(int left, int top, int right, int bottom) {
		mBounds.left = left;
		mBounds.top = top;
		mBounds.right = right;
		mBounds.bottom = bottom;
	}

	void setColorScheme(int color1, int color2, int color3, int color4) {
		mColor1 = color1;
		mColor2 = color2;
		mColor3 = color3;
		mColor4 = color4;
	}

	void setTriggerPercentage(float triggerPercentage) {
		mTriggerPercentage = triggerPercentage;
		mStartTime = 0;
		ViewCompat.postInvalidateOnAnimation(mParent);
	}

	void start() {
		if (!mRunning) {
			mTriggerPercentage = 0.0f;
			mStartTime = AnimationUtils.currentAnimationTimeMillis();
			mRunning = true;
			mParent.postInvalidate();
		}
	}

	void stop() {
		if (mRunning) {
			mTriggerPercentage = 0.0f;
			mFinishTime = AnimationUtils.currentAnimationTimeMillis();
			mRunning = false;
			mParent.postInvalidate();
		}
	}
}
