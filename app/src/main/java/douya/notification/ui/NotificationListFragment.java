package douya.notification.ui;

import android.support.v4.app.Fragment;

import douya.network.api.info.frodo.Notification;
import douya.notification.content.NotificationListResource;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class NotificationListFragment extends Fragment {
    private NotificationListResource mNotificationListResource;
    public static NotificationListFragment newInstance() {
        //noinspection deprecation
        return new NotificationListFragment();
    }

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    public NotificationListFragment() {}
    public void refresh() {
        mNotificationListResource.load(false);
    }
    public int getUnreadCount() {
        if (!mNotificationListResource.has()) {
            return 0;
        }
        int count = 0;
        for (Notification notification : mNotificationListResource.get()) {
            if (!notification.read) {
                ++count;
            }
        }
        return count;
    }
}
