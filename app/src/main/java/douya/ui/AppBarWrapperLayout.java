package douya.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindInt;
import butterknife.ButterKnife;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class AppBarWrapperLayout extends LinearLayout {
    @BindInt(android.R.integer.config_shortAnimTime)
    int mAnimationDuration;
    private View mAppbarView;
    private View mShadowCompatView;
    private boolean mShowing = true;
    private AnimatorSet mAnimator;
    public AppBarWrapperLayout(Context context) {
        super(context);
        init();
    }

    public AppBarWrapperLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppBarWrapperLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AppBarWrapperLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init() {

        setOrientation(VERTICAL);

        ButterKnife.bind(this);
    }
    public void show() {

        if (mShowing) {
            return;
        }
        mShowing = true;

        cancelAnimator();

        mAnimator = new AnimatorSet()
                .setDuration(mAnimationDuration);
        mAnimator.setInterpolator(new FastOutSlowInInterpolator());
        AnimatorSet.Builder builder = mAnimator.play(ObjectAnimator.ofFloat(this, TRANSLATION_Y,
                getTranslationY(), 0));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder.with(ObjectAnimator.ofFloat(mShadowCompatView, ALPHA,
                    mShadowCompatView.getAlpha(), 1));
        } else {
            builder.with(ObjectAnimator.ofFloat(mAppbarView, TRANSLATION_Z,
                    mAppbarView.getTranslationZ(), 0));
        }
        startAnimator();
    }
    public void hide() {

        if (!mShowing) {
            return;
        }
        mShowing = false;

        cancelAnimator();

        mAnimator = new AnimatorSet()
                .setDuration(mAnimationDuration);
        mAnimator.setInterpolator(new FastOutLinearInInterpolator());
        AnimatorSet.Builder builder = mAnimator.play(ObjectAnimator.ofFloat(this, TRANSLATION_Y,
                getTranslationY(), getHideTranslationY()));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder.before(ObjectAnimator.ofFloat(mShadowCompatView, ALPHA,
                    mShadowCompatView.getAlpha(), 0));
        } else {
            builder.before(ObjectAnimator.ofFloat(mAppbarView, TRANSLATION_Z,
                    mAppbarView.getTranslationZ(), -mAppbarView.getElevation()));
        }
        startAnimator();
    }
    private int getHideTranslationY() {
        return -(getBottom() - mShadowCompatView.getHeight());
    }
    private void startAnimator() {
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                mAnimator = null;
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator = null;
            }
        });
        mAnimator.start();
    }

    private void cancelAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }
}
