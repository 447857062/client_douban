package deplink.com.douya.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import deplink.com.douya.R;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class InsetBackgroundFrameLayout extends FrameLayout {
    private Drawable mInsetBackground;

    private Rect mInsets;
    public InsetBackgroundFrameLayout(@NonNull Context context) {
        super(context);
        init(null, 0, 0);
    }

    public InsetBackgroundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public InsetBackgroundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    public InsetBackgroundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }
    @SuppressLint("RestrictedApi")
    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.InsetBackgroundFrameLayout, defStyleAttr, defStyleRes);
        mInsetBackground = a.getDrawable(R.styleable.InsetBackgroundFrameLayout_insetBackground);
        a.recycle();

        // Will not draw until insets are set.
        setWillNotDraw(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int count = canvas.save();
        canvas.translate(getScrollX(), getScrollY());

        int width = getWidth();
        int height = getHeight();

        if (mInsets != null && mInsetBackground != null) {

            // Top
            mInsetBackground.setBounds(0, 0, width, mInsets.top);
            mInsetBackground.draw(canvas);

            // Bottom
            mInsetBackground.setBounds(0, height - mInsets.bottom, width, height);
            mInsetBackground.draw(canvas);

            // Left
            mInsetBackground.setBounds(0, mInsets.top, mInsets.left, height - mInsets.bottom);
            mInsetBackground.draw(canvas);

            // Right
            mInsetBackground.setBounds(width - mInsets.right, mInsets.top, width,
                    height - mInsets.bottom);
            mInsetBackground.draw(canvas);
        }

        canvas.restoreToCount(count);

        super.onDraw(canvas);

    }
}
