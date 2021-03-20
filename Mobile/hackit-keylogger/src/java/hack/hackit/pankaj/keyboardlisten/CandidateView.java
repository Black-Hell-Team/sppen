package hack.hackit.pankaj.keyboardlisten;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class CandidateView extends View {
    private static final List<String> EMPTY_LIST = new ArrayList();
    private static final int MAX_SUGGESTIONS = 32;
    private static final int OUT_OF_BOUNDS = -1;
    private static final int SCROLL_PIXELS = 20;
    private static final int X_GAP = 10;
    private Rect mBgPadding;
    private int mColorNormal;
    private int mColorOther;
    private int mColorRecommended;
    private GestureDetector mGestureDetector;
    private Paint mPaint;
    private boolean mScrolled;
    private int mSelectedIndex;
    private Drawable mSelectionHighlight;
    private HackingKeyBoard mService;
    private List<String> mSuggestions;
    private int mTargetScrollX;
    private int mTotalWidth;
    private int mTouchX = OUT_OF_BOUNDS;
    private boolean mTypedWordValid;
    private int mVerticalPadding;
    private int[] mWordWidth = new int[32];
    private int[] mWordX = new int[32];

    public CandidateView(Context context) {
        super(context);
        this.mSelectionHighlight = context.getResources().getDrawable(17301602);
        this.mSelectionHighlight.setState(new int[]{16842910, 16842908, 16842909, 16842919});
        Resources r = context.getResources();
        setBackgroundColor(r.getColor(R.color.candidate_background));
        this.mColorNormal = r.getColor(R.color.candidate_normal);
        this.mColorRecommended = r.getColor(R.color.candidate_recommended);
        this.mColorOther = r.getColor(R.color.candidate_other);
        this.mVerticalPadding = r.getDimensionPixelSize(R.dimen.candidate_vertical_padding);
        this.mPaint = new Paint();
        this.mPaint.setColor(this.mColorNormal);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextSize((float) r.getDimensionPixelSize(R.dimen.candidate_font_height));
        this.mPaint.setStrokeWidth(0.0f);
        this.mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                CandidateView.this.mScrolled = true;
                int sx = (int) (((float) CandidateView.this.getScrollX()) + distanceX);
                if (sx < 0) {
                    sx = 0;
                }
                if (CandidateView.this.getWidth() + sx > CandidateView.this.mTotalWidth) {
                    sx = (int) (((float) sx) - distanceX);
                }
                CandidateView.this.mTargetScrollX = sx;
                CandidateView.this.scrollTo(sx, CandidateView.this.getScrollY());
                CandidateView.this.invalidate();
                return true;
            }
        });
        setHorizontalFadingEdgeEnabled(true);
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
    }

    public void setService(HackingKeyBoard listener) {
        this.mService = listener;
    }

    public int computeHorizontalScrollRange() {
        return this.mTotalWidth;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = resolveSize(50, widthMeasureSpec);
        Rect padding = new Rect();
        this.mSelectionHighlight.getPadding(padding);
        setMeasuredDimension(measuredWidth, resolveSize(((((int) this.mPaint.getTextSize()) + this.mVerticalPadding) + padding.top) + padding.bottom, heightMeasureSpec));
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (canvas != null) {
            super.onDraw(canvas);
        }
        this.mTotalWidth = 0;
        if (this.mSuggestions != null) {
            if (this.mBgPadding == null) {
                this.mBgPadding = new Rect(0, 0, 0, 0);
                if (getBackground() != null) {
                    getBackground().getPadding(this.mBgPadding);
                }
            }
            int x = 0;
            int count = this.mSuggestions.size();
            int height = getHeight();
            Rect bgPadding = this.mBgPadding;
            Paint paint = this.mPaint;
            int touchX = this.mTouchX;
            int scrollX = getScrollX();
            boolean scrolled = this.mScrolled;
            boolean typedWordValid = this.mTypedWordValid;
            int y = (int) (((((float) height) - this.mPaint.getTextSize()) / 2.0f) - this.mPaint.ascent());
            int i = 0;
            while (i < count) {
                String suggestion = (String) this.mSuggestions.get(i);
                int wordWidth = ((int) paint.measureText(suggestion)) + 20;
                this.mWordX[i] = x;
                this.mWordWidth[i] = wordWidth;
                paint.setColor(this.mColorNormal);
                if (touchX + scrollX >= x && touchX + scrollX < x + wordWidth && !scrolled) {
                    if (canvas != null) {
                        canvas.translate((float) x, 0.0f);
                        this.mSelectionHighlight.setBounds(0, bgPadding.top, wordWidth, height);
                        this.mSelectionHighlight.draw(canvas);
                        canvas.translate((float) (-x), 0.0f);
                    }
                    this.mSelectedIndex = i;
                }
                if (canvas != null) {
                    if ((i == 1 && !typedWordValid) || (i == 0 && typedWordValid)) {
                        paint.setFakeBoldText(true);
                        paint.setColor(this.mColorRecommended);
                    } else if (i != 0) {
                        paint.setColor(this.mColorOther);
                    }
                    Canvas canvas2 = canvas;
                    canvas2.drawText(suggestion, (float) (x + 10), (float) y, paint);
                    paint.setColor(this.mColorOther);
                    canvas.drawLine(0.5f + ((float) (x + wordWidth)), (float) bgPadding.top, 0.5f + ((float) (x + wordWidth)), (float) (height + 1), paint);
                    paint.setFakeBoldText(false);
                }
                x += wordWidth;
                i++;
            }
            this.mTotalWidth = x;
            if (this.mTargetScrollX != getScrollX()) {
                scrollToTarget();
            }
        }
    }

    private void scrollToTarget() {
        int sx = getScrollX();
        if (this.mTargetScrollX > sx) {
            sx += 20;
            if (sx >= this.mTargetScrollX) {
                sx = this.mTargetScrollX;
                requestLayout();
            }
        } else {
            sx -= 20;
            if (sx <= this.mTargetScrollX) {
                sx = this.mTargetScrollX;
                requestLayout();
            }
        }
        scrollTo(sx, getScrollY());
        invalidate();
    }

    public void setSuggestions(List<String> suggestions, boolean completions, boolean typedWordValid) {
        clear();
        if (suggestions != null) {
            this.mSuggestions = new ArrayList(suggestions);
        }
        this.mTypedWordValid = typedWordValid;
        scrollTo(0, 0);
        this.mTargetScrollX = 0;
        invalidate();
        requestLayout();
    }

    public void clear() {
        this.mSuggestions = EMPTY_LIST;
        this.mTouchX = OUT_OF_BOUNDS;
        this.mSelectedIndex = OUT_OF_BOUNDS;
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent me) {
        if (!this.mGestureDetector.onTouchEvent(me)) {
            int action = me.getAction();
            int y = (int) me.getY();
            this.mTouchX = (int) me.getX();
            switch (action) {
                case 0:
                    this.mScrolled = false;
                    invalidate();
                    break;
                case 1:
                    if (!this.mScrolled && this.mSelectedIndex >= 0) {
                        this.mService.pickSuggestionManually(this.mSelectedIndex);
                    }
                    this.mSelectedIndex = OUT_OF_BOUNDS;
                    removeHighlight();
                    requestLayout();
                    break;
                case 2:
                    if (y <= 0 && this.mSelectedIndex >= 0) {
                        this.mService.pickSuggestionManually(this.mSelectedIndex);
                        this.mSelectedIndex = OUT_OF_BOUNDS;
                    }
                    invalidate();
                    break;
            }
        }
        return true;
    }

    public void takeSuggestionAt(float x) {
        this.mTouchX = (int) x;
        if (this.mSelectedIndex >= 0) {
            this.mService.pickSuggestionManually(this.mSelectedIndex);
        }
        invalidate();
    }

    private void removeHighlight() {
        this.mTouchX = OUT_OF_BOUNDS;
        invalidate();
    }
}
