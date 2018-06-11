package douya.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class DispatchInsetsHelper {
    private Delegate mDelegate;
    private WindowInsets mInsets;

    public DispatchInsetsHelper(Delegate delegate) {
        mDelegate = delegate;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        mInsets = insets;

        ViewGroup viewGroup = mDelegate.getOwner();
        int layoutDirection = ViewCompat.getLayoutDirection(viewGroup);
        for (int i = 0, count = viewGroup.getChildCount(); i < count; ++i) {
            View child = viewGroup.getChildAt(i);
            dispatchInsetsToChild(layoutDirection, child, child.getLayoutParams());
        }
        return insets.consumeSystemWindowInsets();
    }
    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private void dispatchInsetsToChild(int layoutDirection, View child,
                                       ViewGroup.LayoutParams childLayoutParams) {

        int childGravity = GravityCompat.getAbsoluteGravity(
                mDelegate.getGravityFromLayoutParams(childLayoutParams), layoutDirection);

        // In fact equivalent to the algorithm in Gravity.apply().

        int childInsetLeft = mInsets.getSystemWindowInsetLeft();
        int childInsetRight = mInsets.getSystemWindowInsetRight();
        if (childLayoutParams.width != FrameLayout.LayoutParams.MATCH_PARENT) {
            if ((childGravity & (Gravity.AXIS_PULL_BEFORE << Gravity.AXIS_X_SHIFT)) == 0) {
                childInsetLeft = 0;
            }
            if ((childGravity & (Gravity.AXIS_PULL_AFTER << Gravity.AXIS_X_SHIFT)) == 0) {
                childInsetRight = 0;
            }
        }

        int childInsetTop = mInsets.getSystemWindowInsetTop();
        int childInsetBottom = mInsets.getSystemWindowInsetBottom();
        if (childLayoutParams.height != FrameLayout.LayoutParams.MATCH_PARENT) {
            if ((childGravity & (Gravity.AXIS_PULL_BEFORE << Gravity.AXIS_Y_SHIFT)) == 0) {
                childInsetTop = 0;
            }
            if ((childGravity & (Gravity.AXIS_PULL_AFTER << Gravity.AXIS_Y_SHIFT)) == 0) {
                childInsetBottom = 0;
            }
        }

        WindowInsets childInsets = mInsets.replaceSystemWindowInsets(childInsetLeft,
                childInsetTop, childInsetRight, childInsetBottom);
        child.dispatchApplyWindowInsets(childInsets);
    }
    public interface Delegate {
        int getGravityFromLayoutParams(ViewGroup.LayoutParams layoutParams);

        ViewGroup getOwner();

        void superAddView(View child, int index, ViewGroup.LayoutParams params);

        boolean superAddViewInLayout(View child, int index, ViewGroup.LayoutParams params,
                                     boolean preventRequestLayout);
    }
}
