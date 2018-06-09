package deplink.com.douya.notification.ui;

import android.support.v4.app.Fragment;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class NotificationListFragment extends Fragment {
    public static NotificationListFragment newInstance() {
        //noinspection deprecation
        return new NotificationListFragment();
    }

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    public NotificationListFragment() {}
}
