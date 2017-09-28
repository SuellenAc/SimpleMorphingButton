package br.com.suellencolangelo.buttonloaderexample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by suellencolangelo on 28/09/17.
 * Example From - https://android.jlelse.eu/make-a-great-android-ux-how-to-make-a-swipe-button-eefbf060326d
 */

public class SwipeButton extends RelativeLayout {
    //slidingButton -> This is the moving part of the component. It contains the icon.
    private ImageView mSlidingButton;
    //initialX -> It is the position of the moving part when the user is going to start to move it.
    private float initialX;
    //active -> It is the variable that says that the button is active or not.
    private boolean active;
    //initialButtonWidth -> This the initial width of the moving part. We need to save it so we can morph back to the initial position.
    private int initialButtonWidth;
    //centerText ->The text in the center of the button.
    private TextView mCenterText;

    public SwipeButton(Context context) {
        super(context);
        init(context);
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        initialX = 0;

        View view = inflate(context, R.layout.swipe_button, this);
        this.mSlidingButton = view.findViewById(R.id.moving_image_view);
        this.mCenterText = view.findViewById(R.id.text_center);
        setOnTouchListener(getOnTouchListener());
    }

    private OnTouchListener getOnTouchListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        onActionMove(motionEvent);
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (active) {
                            collapseBtn();
                        } else {
                            initialButtonWidth = mSlidingButton.getWidth();
                            if (mSlidingButton.getX() + mSlidingButton.getWidth() > getWidth() * 0.90) {
                                expandBtn();
                            } else {
                                moveButtonBack();
                            }
                        }

                        return true;
                }
                return false;
            }
        };
    }

    private void onActionMove(MotionEvent event) {
        if (initialX == 0) {
            initialX = mSlidingButton.getX();
        }
        int slidingBtnClickPos = mSlidingButton.getWidth() / 2;
        if (event.getX() > initialX + slidingBtnClickPos &&
                event.getX() + slidingBtnClickPos < getWidth()) {

            mSlidingButton.setX(event.getX() - slidingBtnClickPos);
            mCenterText.setAlpha(1 - 1.3f * (mSlidingButton.getX() + mSlidingButton.getWidth()) / getWidth());
        }

        if (event.getX() + slidingBtnClickPos > getWidth() &&
                mSlidingButton.getX() + slidingBtnClickPos < getWidth()) {

            mSlidingButton.setX(getWidth() - mSlidingButton.getWidth());
        }

        if (event.getX() < slidingBtnClickPos
                && mSlidingButton.getX() > 0) {
            mSlidingButton.setX(0);
        }

    }

    private void expandBtn() {
        //Position move
        final ValueAnimator positionAnimator = ValueAnimator.ofFloat(mSlidingButton.getX(), 0);
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float x = (float) valueAnimator.getAnimatedValue();
                mSlidingButton.setX(x);
            }
        });
        //Size
        final ValueAnimator widthAnimator = ValueAnimator.ofInt(mSlidingButton.getWidth(), getWidth());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = mSlidingButton.getLayoutParams();
                layoutParams.width = (int) valueAnimator.getAnimatedValue();
                mSlidingButton.setLayoutParams(layoutParams);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                active = true;
                mSlidingButton.setImageResource(R.drawable.ic_lock_outline_black_24dp);
            }
        });
        animatorSet.playTogether(positionAnimator, widthAnimator);
        animatorSet.start();
    }

    private void moveButtonBack() {
        final ValueAnimator positionAnimator = ValueAnimator.ofFloat(mSlidingButton.getX(), 0);
        positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSlidingButton.setX((Float) valueAnimator.getAnimatedValue());
            }
        });
        positionAnimator.setDuration(200);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mCenterText, "alpha", 1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(positionAnimator, alphaAnimator);
        animatorSet.start();
    }

    private void collapseBtn() {
        //Size
        final ValueAnimator widthAnimator = ValueAnimator.ofInt(mSlidingButton.getWidth(), initialButtonWidth);
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = mSlidingButton.getLayoutParams();
                layoutParams.width = (int) valueAnimator.getAnimatedValue();
                mSlidingButton.setLayoutParams(layoutParams);
            }
        });
        widthAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                active = false;
                mSlidingButton.setImageResource(R.drawable.ic_lock_open_black_24dp);
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mCenterText, "alpha", 1);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(objectAnimator, widthAnimator);
        animatorSet.start();

    }
}


