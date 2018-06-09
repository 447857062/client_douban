package deplink.com.douya.doumail.content;

import deplink.com.douya.network.api.ApiError;
import deplink.com.douya.network.api.info.frodo.NotificationCount;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class NotificationCountResource {

    public interface Listener {
        void onLoadNotificationCountStarted(int requestCode);
        void onLoadNotificationCountFinished(int requestCode);
        void onLoadNotificationCountError(int requestCode, ApiError error);
        void onNotificationCountChanged(int requestCode, NotificationCount newNotificationCount);
    }
}
