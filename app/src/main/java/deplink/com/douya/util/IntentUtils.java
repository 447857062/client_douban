/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package deplink.com.douya.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;

import java.io.File;

public class /**/IntentUtils {

    private static final String ACTION_INSTALL_SHORTCUT =
            "com.android.launcher.action.INSTALL_SHORTCUT";

    private static final String MIME_TYPE_TEXT_PLAIN = "text/plain";
    private static final String MIME_TYPE_IMAGE_ANY = "image/*";
    private static final String MIME_TYPE_ANY = "*/*";

    private IntentUtils() {}

    public static Intent withChooser(Intent intent) {
        return Intent.createChooser(intent, null);
    }

    public static Intent makeCaptureImage(Uri outputUri) {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
    }

    public static Intent makeCaptureImage(File outputFile, Context context) {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getContentUri(outputFile, context));
    }

    public static Intent makeInstallShortcut(int iconRes, int nameRes, Class<?> intentClass,
                                             Context context) {
        return new Intent()
                .setAction(ACTION_INSTALL_SHORTCUT)
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(context.getApplicationContext(),
                        intentClass))
                .putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(nameRes))
                .putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(context, iconRes));
    }

    public static Intent makeLaunchApp(String packageName, Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    public static Intent makeMediaScan(Uri uri) {
        return new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                .setData(uri);
    }

    public static Intent makeMediaScan(File file) {
        return makeMediaScan(Uri.fromFile(file));
    }

    private static Intent makePickFile(String mimeType, String[] mimeTypes, boolean allowMultiple) {
        // If not using ACTION_OPEN_DOCUMENT, URI permission can be lost after ~10 seconds.
        String action = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ?
                Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_GET_CONTENT;
        Intent intent = new Intent(action)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType(mimeType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mimeTypes != null && mimeTypes.length > 0) {
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (allowMultiple) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
        }
        return intent;
    }

    public static Intent makePickFile(boolean allowMultiple) {
        return makePickFile(MIME_TYPE_ANY, null, allowMultiple);
    }

    public static Intent makePickFile(String mimeType, boolean allowMultiple) {
        return makePickFile(mimeType, new String[] { mimeType }, allowMultiple);
    }

    public static Intent makePickFile(String[] mimeTypes, boolean allowMultiple) {
        String mimeType = mimeTypes != null && mimeTypes.length == 1 ? mimeTypes[0] : MIME_TYPE_ANY;
        return makePickFile(mimeType, mimeTypes, allowMultiple);
    }

    public static Intent makePickImage(boolean allowMultiple) {
        return makePickFile(MIME_TYPE_IMAGE_ANY, allowMultiple);
    }

    public static Intent makePickOrCaptureImageWithChooser(boolean allowPickMultiple,
                                                           Uri captureOutputUri) {
        Intent intent = withChooser(makePickImage(allowPickMultiple));
        if (captureOutputUri != null) {
            intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[] {
                    makeCaptureImage(captureOutputUri)
            });
        }
        return intent;
    }

    public static Intent makePickOrCaptureImageWithChooser(boolean allowPickMultiple,
                                                           File captureOutputFile,
                                                           Context context) {
        return makePickOrCaptureImageWithChooser(allowPickMultiple, FileUtils.getContentUri(
                captureOutputFile, context));
    }

    // TODO: Use android.support.v4.app.ShareCompat ?

    // NOTE: Before Build.VERSION_CODES.JELLY_BEAN htmlText will be no-op.
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static Intent makeSendText(CharSequence text, String htmlText) {
        Intent intent = new Intent()
                .setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && htmlText != null) {
            intent.putExtra(Intent.EXTRA_HTML_TEXT, htmlText);
        }
        return intent.setType(MIME_TYPE_TEXT_PLAIN);
    }

    public static Intent makeSendText(CharSequence text) {
        return makeSendText(text, null);
    }

    public static Intent makeSendImage(Uri uri, CharSequence text) {
        return new Intent()
                .setAction(Intent.ACTION_SEND)
                // For maximum compatibility.
                .putExtra(Intent.EXTRA_TEXT, text)
                .putExtra(Intent.EXTRA_TITLE, text)
                .putExtra(Intent.EXTRA_SUBJECT, text)
                // HACK: WeChat moments respects this extra only.
                .putExtra("Kdescription", text)
                .putExtra(Intent.EXTRA_STREAM, uri)
                .setType(MIME_TYPE_IMAGE_ANY);
    }

    public static Intent makeSendImage(Uri uri) {
        return makeSendImage(uri, null);
    }

    public static Intent makeSyncSettings(String[] authorities, String[] accountTypes) {
        Intent intent = new Intent(Settings.ACTION_SYNC_SETTINGS);
        if (!ArrayUtils.isEmpty(authorities)) {
            intent.putExtra(Settings.EXTRA_AUTHORITIES, authorities);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (!ArrayUtils.isEmpty(accountTypes)) {
                intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, accountTypes);
            }
        }
        return intent;
    }

    public static Intent makeSyncSettingsWithAuthority(String authority) {
        return makeSyncSettings(authority != null ? new String[] { authority } : null, null);
    }

    public static Intent makeSyncSettingsWithAccountType(String accountType) {
        return makeSyncSettings(null, accountType != null ? new String[] { accountType } : null);
    }

    public static Intent makeSyncSettings() {
        return makeSyncSettings(null, null);
    }

    public static Intent makeView(Uri uri) {
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public static Intent makeViewWithType(Uri uri, Context context) {
        return new Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, UriUtils.getType(uri, context));
    }

    public static Intent makeViewAppInMarket(String packageName) {
        return makeView(Uri.parse("market://details?id=" + packageName));
    }
}
