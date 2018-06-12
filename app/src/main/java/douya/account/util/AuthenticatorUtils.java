/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.account.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import douya.ui.WebViewActivity;


public class AuthenticatorUtils {

    private AuthenticatorUtils() {}

    public static Intent makeSetApiKeyIntent(Context context) {
        return WebViewActivity.makeIntent(Uri.parse(
                "https://github.com/DreaminginCodeZH/DouyaApiKey/releases/latest"), context);
    }

    public static Intent makeWebsiteIntent(Context context) {
        return WebViewActivity.makeIntent(Uri.parse("https://accounts.douban.com"), context);
    }
}
