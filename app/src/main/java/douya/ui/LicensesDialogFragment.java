/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;

import com.douya.R;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.NoticesXmlParser;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;
import douya.util.ViewUtils;

public class LicensesDialogFragment extends AppCompatDialogFragment {

    private static final String KEY_PREFIX = LicensesDialogFragment.class.getName() + '.';

    private static final String STATE_NOTICES = KEY_PREFIX + "NOTICES";

    private Notices mNotices;

    public static LicensesDialogFragment newInstance() {
        //noinspection deprecation
        return new LicensesDialogFragment();
    }

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    public LicensesDialogFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mNotices = savedInstanceState.getParcelable(STATE_NOTICES);
        } else {
            try {
                mNotices = NoticesXmlParser.parse(getActivity().getResources().openRawResource(
                        R.raw.licenses));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(STATE_NOTICES, mNotices);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // setIncludeOwnLicense(true) will modify our notices instance.
        Notices notices = new Notices();
        for (Notice notice : mNotices.getNotices()) {
            notices.addNotice(notice);
        }
        int htmlStyleRes = ViewUtils.isLightTheme(getActivity()) ?
                R.string.settings_open_source_licenses_html_style_light
                : R.string.settings_open_source_licenses_html_style_dark;
        return new LicensesDialog.Builder(getActivity())
                .setThemeResourceId(getTheme())
                .setTitle(R.string.settings_open_source_licenses_title)
                .setCloseText(R.string.close)
                .setNotices(notices)
                .setIncludeOwnLicense(true)
                .setNoticesCssStyle(htmlStyleRes)
                .build()
                .createAppCompat();
    }
}
