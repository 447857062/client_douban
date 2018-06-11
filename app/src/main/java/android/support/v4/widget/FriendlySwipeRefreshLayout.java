package android.support.v4.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class FriendlySwipeRefreshLayout extends SwipeRefreshLayout {

    private int mSize = DEFAULT;
    private int mCircleDiameter;
    private int mDefaultCircleDistance;
    public FriendlySwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public FriendlySwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgressViewOffset(int offset, int distanceOffset) {
        int progressStart = offset - mCircleDiameter;
        int progressEnd = progressStart + mDefaultCircleDistance + distanceOffset;
        setProgressViewOffset(false, progressStart, progressEnd);
    }

    public void setProgressViewOffset(int offset) {
        setProgressViewOffset(offset, 0);
    }
}
