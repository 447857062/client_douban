package douya.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class RetainedFragment extends Fragment {
    private List<Runnable> mPendingRunnables = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setUserVisibleHint(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        Iterator<Runnable> iterator = mPendingRunnables.iterator();
        while (iterator.hasNext()) {
            Runnable runnable = iterator.next();
            iterator.remove();
            runnable.run();
        }
    }

    protected void postOnResumed(Runnable runnable) {
        if (isResumed()) {
            runnable.run();
        } else {
            mPendingRunnables.add(runnable);
        }
    }
}
