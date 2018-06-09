package deplink.com.douya.broadcast.ui;

import java.util.List;

import butterknife.BindDimen;
import deplink.com.douya.R;
import deplink.com.douya.broadcast.content.HomeBroadcastListResource;
import deplink.com.douya.content.MoreListResourceFragment;
import deplink.com.douya.network.api.info.frodo.Broadcast;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class HomeBroadcastListFragment extends BaseTimelineBroadcastListFragment {
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
    protected MoreListResourceFragment<?, List<Broadcast>> onAttachResource() {
        return HomeBroadcastListResource.attachTo(this);
    }

    @Override
    protected int getExtraPaddingTop() {
        return mToolbarAndTabHeight;
    }
}
