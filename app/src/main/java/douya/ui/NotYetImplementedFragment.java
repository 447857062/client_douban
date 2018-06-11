package douya.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import deplink.com.douya.R;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class NotYetImplementedFragment extends Fragment {
    public static NotYetImplementedFragment newInstance() {
        //noinspection deprecation
        return new NotYetImplementedFragment();
    }

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    public NotYetImplementedFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.not_yet_implemented_fragment, container, false);
    }
}
