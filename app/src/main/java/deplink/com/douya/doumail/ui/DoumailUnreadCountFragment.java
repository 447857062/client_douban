package deplink.com.douya.doumail.ui;

import android.support.v4.app.Fragment;

import deplink.com.douya.doumail.content.NotificationCountResource;
import deplink.com.douya.network.api.ApiError;
import deplink.com.douya.network.api.info.frodo.NotificationCount;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class DoumailUnreadCountFragment extends Fragment
        implements NotificationCountResource.Listener {
    private NotificationCountResource mNotificationCountResource;

    public static DoumailUnreadCountFragment newInstance() {
        //noinspection deprecation
        return new DoumailUnreadCountFragment();
    }

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    public DoumailUnreadCountFragment() {
    }

    public void refresh() {
       // mNotificationCountResource.load();
    }

    @Override
    public void onLoadNotificationCountStarted(int requestCode) {

    }

    @Override
    public void onLoadNotificationCountFinished(int requestCode) {

    }

    @Override
    public void onLoadNotificationCountError(int requestCode, ApiError error) {

    }

    @Override
    public void onNotificationCountChanged(int requestCode, NotificationCount newNotificationCount) {

    }
}
