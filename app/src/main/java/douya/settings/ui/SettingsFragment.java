/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.settings.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.douya.R;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompatDividers;

import douya.settings.info.Settings;
import douya.util.NightModeHelper;
import douya.util.SharedPrefsUtils;

public class SettingsFragment extends PreferenceFragmentCompatDividers
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPrefsUtils.getSharedPrefs().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        SharedPrefsUtils.getSharedPrefs().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!TextUtils.equals(key, Settings.NIGHT_MODE.getKey())) {
            return;
        }
        NightModeHelper.updateNightMode((AppCompatActivity) getActivity());
    }
}
