/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.fabric;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.douya.BuildConfig;

import io.fabric.sdk.android.Fabric;

public class FabricUtils {

    private FabricUtils() {}

    public static void init(Context context) {
        if (BuildConfig.DEBUG) {
            return;
        }
        Fabric.with(context, new Crashlytics(), new Answers());
    }
}
