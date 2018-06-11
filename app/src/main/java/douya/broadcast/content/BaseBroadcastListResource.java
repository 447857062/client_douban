package douya.broadcast.content;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

import douya.content.MoreBaseListResourceFragment;
import douya.eventbus.BroadcastDeletedEvent;
import douya.eventbus.BroadcastUpdatedEvent;
import douya.eventbus.EventBusUtils;
import douya.network.api.ApiError;
import douya.network.api.info.frodo.Broadcast;
import douya.network.api.info.frodo.BroadcastList;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public abstract class BaseBroadcastListResource
        extends MoreBaseListResourceFragment<BroadcastList, Broadcast> {
    @Override
    protected void onLoadStarted() {
        getListener().onLoadBroadcastListStarted(getRequestCode());
    }

    @Override
    protected void onLoadFinished(boolean more, int count, boolean successful,
                                  List<Broadcast> response, ApiError error) {
        if (successful) {
            if (more) {
                append(response);
                getListener().onLoadBroadcastListFinished(getRequestCode());
                getListener().onBroadcastListAppended(getRequestCode(),
                        Collections.unmodifiableList(response));
            } else {
                set(response);
                getListener().onLoadBroadcastListFinished(getRequestCode());
                getListener().onBroadcastListChanged(getRequestCode(),
                        Collections.unmodifiableList(get()));
            }
            if (shouldPostBroadcastUpdatedEvent()) {
                for (Broadcast broadcast : response) {
                    EventBusUtils.postAsync(new BroadcastUpdatedEvent(broadcast, this));
                }
            }
        } else {
            getListener().onLoadBroadcastListFinished(getRequestCode());
            getListener().onLoadBroadcastListError(getRequestCode(), error);
        }
    }

    protected boolean shouldPostBroadcastUpdatedEvent() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onBroadcastUpdated(BroadcastUpdatedEvent event) {

        if (event.isFromMyself(this) || isEmpty()) {
            return;
        }

        List<Broadcast> broadcastList = get();
        for (int i = 0, size = broadcastList.size(); i < size; ++i) {
            Broadcast updatedBroadcast = event.update(broadcastList.get(i), this);
            if (updatedBroadcast != null) {
                broadcastList.set(i, updatedBroadcast);
                getListener().onBroadcastChanged(getRequestCode(), i, updatedBroadcast);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onBroadcastDeleted(BroadcastDeletedEvent event) {

        if (event.isFromMyself(this) || isEmpty()) {
            return;
        }

        List<Broadcast> broadcastList = get();
        for (int i = 0, size = broadcastList.size(); i < size; ) {
            Broadcast broadcast = broadcastList.get(i);
            if (broadcast.id == event.broadcastId) {
                broadcastList.remove(i);
                getListener().onBroadcastRemoved(getRequestCode(), i);
                --size;
            } else {
                if (broadcast.parentBroadcast != null
                        && broadcast.parentBroadcast.id == event.broadcastId) {
                    // Same behavior as Frodo API.
                    // FIXME: Won't reach here if another list shares this broadcast instance.
                    broadcast.parentBroadcast = null;
                    getListener().onBroadcastChanged(getRequestCode(), i, broadcast);
                } else if (broadcast.rebroadcastedBroadcast != null
                        && broadcast.rebroadcastedBroadcast.id == event.broadcastId) {
                    broadcast.rebroadcastedBroadcast.isDeleted = true;
                    getListener().onBroadcastChanged(getRequestCode(), i, broadcast);
                }
                ++i;
            }
        }
    }
    private Listener getListener() {
        return (Listener) getTarget();
    }
    public interface Listener {
        void onLoadBroadcastListStarted(int requestCode);
        void onLoadBroadcastListFinished(int requestCode);
        void onLoadBroadcastListError(int requestCode, ApiError error);
        /**
         * @param newBroadcastList Unmodifiable.
         */
        void onBroadcastListChanged(int requestCode, List<Broadcast> newBroadcastList);
        /**
         * @param appendedBroadcastList Unmodifiable.
         */
        void onBroadcastListAppended(int requestCode, List<Broadcast> appendedBroadcastList);
        void onBroadcastChanged(int requestCode, int position, Broadcast newBroadcast);
        void onBroadcastRemoved(int requestCode, int position);
    }
}
