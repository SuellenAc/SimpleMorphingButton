package br.com.suellencolangelo.buttonloaderexample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by suellencolangelo on 26/09/17.
 */

public class CircularProgressButton extends AppCompatButton {
    private GradientDrawable mGradientDrawable;
    private State mState;
    private Boolean mIsMorphingInProgress;
    private AnimatorSet mMorphingAnimatorSet;
    private Integer mWidth, mHeight;

    public CircularProgressButton(Context context) {
        super(context);
        init(context);
    }

    public CircularProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircularProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public enum State {
        PROGRESS,
        IDLE
    }

    private void init(Context context) {
        mState = State.IDLE;
        mGradientDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.button_shape);
        setBackground(mGradientDrawable);
    }


    public void startAnimation() {
        if (mState != State.IDLE) {
            return;
        }
        mWidth = getWidth();
        mHeight = getHeight();

        int initialCornerRadius = 0;
        int finalCornerRadius = 1000;

        mState = State.PROGRESS;
        mIsMorphingInProgress = true;

        this.setText("");
        this.setClickable(false);

        int toWidth = mHeight; // random value
        int toHeight = toWidth;

        ValueAnimator heightAnimator = getHeightAnimator(mHeight, toHeight);
        ValueAnimator widthAnimator = getWidthAnimator(mWidth, toWidth);
        ObjectAnimator cornerAnimator = getCornerAnimation(initialCornerRadius, finalCornerRadius);

        mMorphingAnimatorSet = new AnimatorSet();
        mMorphingAnimatorSet.setDuration(300);
        mMorphingAnimatorSet.playTogether(cornerAnimator, widthAnimator, heightAnimator);
        mMorphingAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsMorphingInProgress = false;
                setClickable(true);
            }
        });
        mMorphingAnimatorSet.start();
    }

    public void stopAnimation() {
        if (mState != State.PROGRESS) {
            return;
        }
        int initialWidth = getWidth();
        int initialHeight = getHeight();

        int initialCornerRadius = 1000;
        int finalCornerRadius = 0;

        mState = State.IDLE;
        mIsMorphingInProgress = true;

        this.setText("");
        this.setClickable(false);


        ValueAnimator heightAnimator = getHeightAnimator(initialHeight, mHeight);
        ValueAnimator widthAnimator = getWidthAnimator(initialWidth, mWidth);
        ObjectAnimator cornerAnimator = getCornerAnimation(initialCornerRadius, finalCornerRadius);

        mMorphingAnimatorSet = new AnimatorSet();
        mMorphingAnimatorSet.setDuration(300);
        mMorphingAnimatorSet.playTogether(cornerAnimator, widthAnimator, heightAnimator);
        mMorphingAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsMorphingInProgress = false;
                setClickable(true);
            }
        });
        mMorphingAnimatorSet.start();
    }

    private ValueAnimator getWidthAnimator(Integer initialWidth, Integer finalWidth) {
        final ValueAnimator animator = ValueAnimator.ofInt(initialWidth, finalWidth);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams params = getLayoutParams();
                params.width = val;
                setLayoutParams(params);
            }
        });
        return animator;
    }


    private ValueAnimator getHeightAnimator(Integer initialHeight, Integer finalHeight){
        final ValueAnimator animator = ValueAnimator.ofInt(initialHeight, finalHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = val;
                setLayoutParams(params);
            }
        });
        return animator;
    }

    private ObjectAnimator getCornerAnimation(Integer initialCornerRadius, Integer finalCornerRadius){
        return ObjectAnimator.ofFloat(mGradientDrawable,
                "cornerRadius", // Property Name
                initialCornerRadius,
                finalCornerRadius);

    }

    public State getState() {
        return mState;
    }

}
