package douya.account.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import deplink.com.douya.R;
import douya.ui.SimpleDialogFragment;

public class SelectAccountActivity extends AppCompatActivity {
    private static final String KEY_PREFIX = SelectAccountActivity.class.getName() + '.';

    private static final String EXTRA_ON_SELECTED_INTENT = KEY_PREFIX + "on_selected_intent";

    private SimpleDialogFragment.Listener mDialogListener;

    public static Intent makeIntent(Intent onSelectedIntent, Context context) {
        return new Intent(context, SelectAccountActivity.class)
                .putExtra(SelectAccountActivity.EXTRA_ON_SELECTED_INTENT, onSelectedIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);
    }
}
