package douya.app;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class RetainedFragment extends Fragment {
    private List<Runnable> mPendingRunnables = new ArrayList<>();
    protected void postOnResumed(Runnable runnable) {
        if (isResumed()) {
            runnable.run();
        } else {
            mPendingRunnables.add(runnable);
        }
    }
}
