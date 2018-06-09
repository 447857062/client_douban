package deplink.com.douya.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;

import java.util.ArrayList;

import deplink.com.douya.R;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class TransitionUtils {
    private static final String TRANSITION_NAME_APPBAR = "appbar";
    public static boolean shouldEnableTransition() {
        // I hprof-ed the app, and found that bitmaps are kept by
        // ExitTransitionCoordinator.mSharedElementsBundle, which is in turn kept by
        // ResultReceiver$MyResultReceiver, which is kept by a FinalizerReference.
        // But this fix (
        // https://android.googlesource.com/platform/frameworks/base/+/a0a0260e48e1ee4e9b5d98b49571e8d2a6fd6c3a
        // ) should have been incorporated into android-5.0.0_r1. So I really don't know the root
        // cause now.
        // New finding at 2015-12-06: OOM even happens on Android 5.1 (CM).
        // TODO: Allow disabling transition on some pre-marshmallow devices for OOM.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    // FIXME: Duplicate code.
    public static void setupTransitionForAppBar(Activity activity) {

        if (!shouldEnableTransition()) {
            return;
        }

        View appbar = activity.findViewById(R.id.appBarWrapper);
        if (appbar != null) {
            appbar.setTransitionName(TRANSITION_NAME_APPBAR);
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setupTransitionForAppBar(Fragment fragment) {

        if (!shouldEnableTransition()) {
            return;
        }

        View appbar = fragment.getView().findViewById(R.id.appBarWrapper);
        if (appbar != null) {
            appbar.setTransitionName(TRANSITION_NAME_APPBAR);
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Bundle makeActivityOptionsBundle(Activity activity, View... sharedViews) {

        if (!shouldEnableTransition()) {
            return null;
        }

        ArrayList<Pair<View, String>> sharedElementList = new ArrayList<>();

        for (View sharedView : sharedViews) {
            sharedElementList.add(Pair.create(sharedView, sharedView.getTransitionName()));
        }

        View appbar = activity.findViewById(R.id.appBarWrapper);
        if (appbar != null) {
            sharedElementList.add(Pair.create(appbar, appbar.getTransitionName()));
        }

        //noinspection unchecked
        Pair<View, String>[] sharedElements =
                sharedElementList.toArray(new Pair[sharedElementList.size()]);
        //noinspection unchecked
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements)
                .toBundle();
    }
}
