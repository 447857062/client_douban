package douya.broadcast.ui;

import com.douya.R;

import butterknife.BindDimen;
import douya.broadcast.content.HomeBroadcastListResource;
import douya.broadcast.content.TimelineBroadcastListResource;
import douya.main.ui.MainActivity;
import douya.network.api.info.frodo.Broadcast;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class HomeBroadcastListFragment extends BaseTimelineBroadcastListFragment  implements HomeBroadcastListResource.Listener {
    @BindDimen(R.dimen.toolbar_and_tab_height)
    int mToolbarAndTabHeight;

    public static HomeBroadcastListFragment newInstance() {
        //noinspection deprecation
        return new HomeBroadcastListFragment();
    }

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    public HomeBroadcastListFragment() {}


    @Override
    protected int getExtraPaddingTop() {
        return mToolbarAndTabHeight;
    }
    @Override
    protected TimelineBroadcastListResource onAttachResource() {
        return HomeBroadcastListResource.attachTo(this);
    }
    @Override
    public void onBroadcastInserted(int requestCode, int position, Broadcast insertedBroadcast) {
        boolean hasFirstItemView = mList.getLayoutManager().findViewByPosition(0) != null;
        onItemInserted(position, insertedBroadcast);
        if (position == 0 && hasFirstItemView) {
            mList.scrollToPosition(0);
        }
    }
    @Override
    protected void onSwipeRefresh() {
        super.onSwipeRefresh();

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.onRefresh();
    }
}
