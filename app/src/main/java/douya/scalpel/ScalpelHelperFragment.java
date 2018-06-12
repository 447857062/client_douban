package douya.scalpel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import douya.app.RetainedFragment;
import douya.eventbus.EventBusUtils;

/**
 * Created by ${kelijun} on 2018/6/11.
 */

public class ScalpelHelperFragment extends RetainedFragment {
    private static final String FRAGMENT_TAG = ScalpelHelperFragment.class.getName();
    public static ScalpelHelperFragment attachTo(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        ScalpelHelperFragment fragment = (ScalpelHelperFragment) fragmentManager
                .findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new ScalpelHelperFragment();
            fragmentManager.beginTransaction()
                    .add(fragment, FRAGMENT_TAG)
                    .commit();
        }
        return fragment;
    }
    public static void setEnabled(boolean enabled) {
        EventBusUtils.postAsync(new SetEnabledEvent(enabled));
    }
    public static ScalpelHelperFragment attachTo(Fragment fragment) {
        //noinspection deprecation
        return attachTo(fragment.getActivity());
    }
    private static class SetEnabledEvent {

        public boolean enabled;

        public SetEnabledEvent(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
