package deplink.com.douya.broadcast.ui;

import java.util.List;

import deplink.com.douya.broadcast.content.BaseBroadcastListResource;
import deplink.com.douya.network.api.ApiError;
import deplink.com.douya.network.api.info.frodo.Broadcast;
import deplink.com.douya.ui.BaseListFragment;
import deplink.com.douya.ui.SimpleAdapter;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public abstract class BaseBroadcastListFragment extends BaseListFragment<Broadcast> implements BaseBroadcastListResource.Listener{
    @Override
    protected SimpleAdapter<Broadcast, ?> onCreateAdapter() {
        return null;
    }
    @Override
    public void onLoadBroadcastListStarted(int requestCode) {
        onLoadListStarted();
    }

    @Override
    public void onLoadBroadcastListFinished(int requestCode) {
        onLoadListFinished();
    }

    @Override
    public void onLoadBroadcastListError(int requestCode, ApiError error) {
        onLoadListError(error);
    }

    @Override
    public void onBroadcastListChanged(int requestCode, List<Broadcast> newBroadcastList) {
        onListChanged(newBroadcastList);
    }

    @Override
    public void onBroadcastListAppended(int requestCode, List<Broadcast> appendedBroadcastList) {
        onListAppended(appendedBroadcastList);
    }

    @Override
    public void onBroadcastChanged(int requestCode, int position, Broadcast newBroadcast) {
        onItemChanged(position, newBroadcast);
    }

    @Override
    public void onBroadcastRemoved(int requestCode, int position) {
        onItemRemoved(position);
    }
}
