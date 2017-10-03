package cn.cncgroup.tv.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by zhang on 2015/10/20.
 */
public class ShadowView extends View {
    // default animation time
    private int mDuration = 0;
    private int mOffset;
    private FocusAnimator mFocusAnimator;

    public ShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * init shadow view property
     *
     * @param offset shadow width
     */
    public void init(int offset) {
        init(offset, 0);
    }

    public void init(int offset, int duration) {
        this.mOffset = offset;
        mDuration = duration;
        mFocusAnimator = new FocusAnimator(this, duration);
    }

    public void moveTo(View view) {
        if (!isShown() || mDuration <= 0) {
            sudden(view);
        } else {
            anim(view);
        }
    }

    /**
     * 取消移动
     */
    public void cancelMove() {
        mFocusAnimator.cancel();
    }

    private int getTargetShadowWidth(View view) {
        return view.getWidth() + mOffset * 2 - 2;
    }

    private int getTargetShadowHeight(View view) {
        return view.getHeight() + mOffset * 2 - 2;
    }

    private void reset(View view) {
        try {
            //java.lang.IllegalArgumentException: parameter must be a descendant of this view
            Rect to = findLocation(view);
            setTranslationX(to.left - mOffset);
            setTranslationY(to.top - mOffset);
            ViewGroup.LayoutParams param = getLayoutParams();
            param.height = getTargetShadowHeight(view);
            param.width = getTargetShadowWidth(view);
            setLayoutParams(param);
        } catch (Exception e) {

        }
    }

    /**
     * move not with animation
     */
    private void sudden(final View view) {
        if (!isShown()) {
            post(new Runnable() {
                @Override
                public void run() {
                    reset(view);
                    setVisibility(VISIBLE);
                }
            });
        } else {
            reset(view);
        }
    }

    /**
     * move with animation
     */
    private void anim(View view) {
        int targetWidth = getTargetShadowWidth(view);
        int targetHeight = getTargetShadowHeight(view);
        int deltaWidth = targetWidth - getWidth();
        int deltaHeight = targetHeight - getHeight();
        Rect rect = findLocation(view);
        int targetX = rect.left - mOffset;
        int targetY = rect.top - mOffset;
        if (deltaWidth != 0 || deltaHeight != 0 || targetX != 0 || targetY != 0) {
            mFocusAnimator.animateFocus(targetWidth, targetHeight, targetX, targetY);
        }
    }

    private Rect findLocation(View view) {
        Rect rect = new Rect();
        ((ViewGroup) this.getParent()).offsetDescendantRectToMyCoords(view, rect);
        return rect;
    }

    static class FocusAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {
        private final View mShadowView;
        private int mStartWidth;
        private int mStartHeight;
        private int mDeltaWidth;
        private int mDeltaHeight;
        private float mStartX;
        private float mStartY;
        private float mDeltaX;
        private float mDeltaY;

        FocusAnimator(View shadowView, int duration) {
            this.mShadowView = shadowView;
            this.setIntValues(0, 1);
            this.setDuration(duration);
            this.setInterpolator(new AccelerateDecelerateInterpolator());
            this.addUpdateListener(this);
        }

        public void animateFocus(int targetWidth, int targetHeight, float targetX, float targetY) {
            this.mStartWidth = mShadowView.getWidth();
            this.mStartHeight = mShadowView.getHeight();
            this.mDeltaWidth = targetWidth - mStartWidth;
            this.mDeltaHeight = targetHeight - mStartHeight;
            this.mStartX = mShadowView.getX();
            this.mStartY = mShadowView.getY();
            this.mDeltaX = targetX - mStartX;
            this.mDeltaY = targetY - mStartY;
            cancel();
            if (mShadowView.getVisibility() != View.VISIBLE || getDuration() <= 0) {
                setFocusLevel(1);
            } else {
                start();
            }
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            setFocusLevel(animation.getAnimatedFraction());
        }

        private void setFocusLevel(float fraction) {
            ViewGroup.LayoutParams param = mShadowView.getLayoutParams();
            param.height = (int) (mStartHeight + mDeltaHeight * fraction);
            param.width = (int) (mStartWidth + mDeltaWidth * fraction);
            mShadowView.setLayoutParams(param);
            mShadowView.setTranslationX(mStartX + mDeltaX * fraction);
            mShadowView.setTranslationY(mStartY + mDeltaY * fraction);
        }
    }
}
