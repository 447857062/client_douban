/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.util.PatternsCompat;
import android.text.TextUtils;

import com.douya.R;

import org.chromium.customtabsclient.CustomTabsActivityHelper;

import douya.ui.WebViewActivity;
import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;

public class UrlUtils {

    private UrlUtils() {}

    public static boolean isValidUrl(String text) {
        return !TextUtils.isEmpty(text) && PatternsCompat.WEB_URL.matcher(text).matches();
    }

    public static void openWithWebViewActivity(Uri uri, Context context) {
        context.startActivity(WebViewActivity.makeIntent(uri, context));
    }

    public static void openWithIntent(Uri uri, Context context) {
        Intent intent = IntentUtils.makeView(uri);
        AppUtils.startActivity(intent, context);
    }

    public static void openWithIntent(String url, Context context) {
        openWithIntent(Uri.parse(url), context);
    }

    public static void openWithCustomTabs(Uri uri,
                                          CustomTabsActivityHelper.CustomTabsFallback fallback,
                                          Activity activity) {
        CustomTabsHelperFragment.open(activity, makeCustomTabsIntent(activity), uri, fallback);
    }

    private static CustomTabsIntent makeCustomTabsIntent(Context context) {
        return new CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .enableUrlBarHiding()
                .setToolbarColor(ViewUtils.getColorFromAttrRes(R.attr.colorPrimary, 0, context))
                .setShowTitle(true)
                .build();
    }
}
