package douya.ui;

import android.support.v7.widget.RecyclerView;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public abstract class OnVerticalScrollListener extends RecyclerView.OnScrollListener {
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!recyclerView.canScrollVertically(-1)) {
            onScrolledToTop();
        } else if (!recyclerView.canScrollVertically(1)) {
            onScrolledToBottom();
        }
        if (dy < 0) {
            onScrolledUp(dy);
        } else if (dy > 0) {
            onScrolledDown(dy);
        }
        onScrolled(dy);
    }
    public void onScrolled(int dy) {}

    public void onScrolledUp(int dy) {}

    public void onScrolledDown(int dy) {}

    public void onScrolledToTop() {}

    public void onScrolledToBottom() {}
}
