package deplink.com.douya.ui;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class DoubleClickToolbar extends Toolbar {
    private OnDoubleClickListener mOnDoubleClickListener;
    public DoubleClickToolbar(Context context) {
        super(context);
    }

    public DoubleClickToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DoubleClickToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public interface OnDoubleClickListener {
        boolean onDoubleClick(View view);
    }
    public void setOnDoubleClickListener(OnDoubleClickListener listener) {
        mOnDoubleClickListener = listener;
    }
}
