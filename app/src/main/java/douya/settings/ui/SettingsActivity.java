package douya.settings.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import douya.util.FragmentUtils;

public class SettingsActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Calls ensureSubDecor().
        findViewById(android.R.id.content);

        if (savedInstanceState == null) {
            FragmentUtils.add(SettingsActivityFragment.newInstance(), this, android.R.id.content);
        }
    }
}
