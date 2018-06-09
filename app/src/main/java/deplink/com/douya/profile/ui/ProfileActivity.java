package deplink.com.douya.profile.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import deplink.com.douya.R;
import deplink.com.douya.network.api.info.frodo.SimpleUser;
import deplink.com.douya.network.api.info.frodo.User;

public class ProfileActivity extends AppCompatActivity {
    private static final String KEY_PREFIX = ProfileFragment.class.getName() + '.';

    private static final String EXTRA_USER_ID_OR_UID = KEY_PREFIX + "user_id_or_uid";
    private static final String EXTRA_SIMPLE_USER = KEY_PREFIX + "simple_user";
    private static final String EXTRA_USER_INFO = KEY_PREFIX + "user_info";

    private ProfileFragment mFragment;

    public static Intent makeIntent(String userIdOrUid, Context context) {
        return new Intent(context, ProfileActivity.class)
                .putExtra(EXTRA_USER_ID_OR_UID, userIdOrUid);
    }

    public static Intent makeIntent(deplink.com.douya.network.api.info.apiv2.SimpleUser simpleUser, Context context) {
        return new Intent(context, ProfileActivity.class)
                .putExtra(EXTRA_SIMPLE_USER, simpleUser);
    }

    public static Intent makeIntent(
          SimpleUser simpleUser,
            Context context) {
        return makeIntent(deplink.com.douya.network.api.info.apiv2.SimpleUser.fromFrodo(simpleUser), context);
    }

    public static Intent makeIntent(User user, Context context) {
        return new Intent(context, ProfileActivity.class)
                .putExtra(EXTRA_USER_INFO, user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}
