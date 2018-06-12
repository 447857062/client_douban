/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.broadcast.ui;

import android.os.Bundle;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import douya.broadcast.content.BroadcastLikerListResource;
import douya.content.MoreListResourceFragment;
import douya.eventbus.BroadcastUpdatedEvent;
import douya.eventbus.EventBusUtils;
import douya.network.api.info.frodo.Broadcast;
import douya.network.api.info.frodo.SimpleUser;
import douya.ui.SimpleAdapter;
import douya.user.ui.BaseUserListFragment;
import douya.user.ui.UserAdapter;
import douya.util.FragmentUtils;

public class BroadcastLikerListFragment extends BaseUserListFragment {

    private final String KEY_PREFIX = BroadcastLikerListFragment.class.getName() + '.';

    private final String EXTRA_BROADCAST = KEY_PREFIX + "broadcast";

    private Broadcast mBroadcast;

    public static BroadcastLikerListFragment newInstance(Broadcast broadcast) {
        //noinspection deprecation
        return new BroadcastLikerListFragment().setArguments(broadcast);
    }

    /**
     * @deprecated Use {@link #newInstance(Broadcast)} instead.
     */
    public BroadcastLikerListFragment() {}

    protected BroadcastLikerListFragment setArguments(Broadcast broadcast) {
        FragmentUtils.getArgumentsBuilder(this)
                .putParcelable(EXTRA_BROADCAST, broadcast);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBroadcast = getArguments().getParcelable(EXTRA_BROADCAST);

        EventBusUtils.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBusUtils.unregister(this);
    }

    @Override
    protected MoreListResourceFragment<?, List<SimpleUser>> onAttachResource() {
        return BroadcastLikerListResource.attachTo(mBroadcast.id, this);
    }

    @Override
    protected SimpleAdapter<SimpleUser, ?> onCreateAdapter() {
        return new UserAdapter();
    }

    @Override
    protected void onListUpdated(List<SimpleUser> userList) {
        if (mBroadcast.likeCount < userList.size()) {
            mBroadcast.likeCount = userList.size();
            EventBusUtils.postAsync(new BroadcastUpdatedEvent(mBroadcast, this));
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onBroadcastUpdated(BroadcastUpdatedEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        Broadcast updatedBroadcast = event.update(mBroadcast, this);
        if (updatedBroadcast != null) {
            mBroadcast = updatedBroadcast;
        }
    }
}
