package deplink.com.douya.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import deplink.com.douya.util.ViewUtils;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class DispatchInsetsDrawerLayout extends DrawerLayout {
    public DispatchInsetsDrawerLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public DispatchInsetsDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DispatchInsetsDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        setFitsSystemWindows(false);
        ViewUtils.setLayoutFullscreen(this);
    }
    private DispatchInsetsHelper mInsetsHelper=new DispatchInsetsHelper(new DispatchInsetsHelper.Delegate(){

        @Override
        public int getGravityFromLayoutParams(ViewGroup.LayoutParams layoutParams) {
            return 0;
        }

        @Override
        public ViewGroup getOwner() {
            return null;
        }

        @Override
        public void superAddView(View child, int index, ViewGroup.LayoutParams params) {

        }

        @Override
        public boolean superAddViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
            return false;
        }
    });
}
