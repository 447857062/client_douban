/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.calendar.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import douya.util.FragmentUtils;


public class CalendarActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, CalendarActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Calls ensureSubDecor().
        findViewById(android.R.id.content);

        if (savedInstanceState == null) {
            FragmentUtils.add(CalendarFragment.newInstance(), this, android.R.id.content);
        }
    }
}
