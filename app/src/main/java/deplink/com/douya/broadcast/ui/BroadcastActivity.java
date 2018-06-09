package deplink.com.douya.broadcast.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import deplink.com.douya.R;
import deplink.com.douya.network.api.info.frodo.Broadcast;

public class BroadcastActivity extends AppCompatActivity {
    private static final String KEY_PREFIX = BroadcastActivity.class.getName() + '.';

    private static final String EXTRA_BROADCAST_ID = KEY_PREFIX + "broadcast_id";
    private static final String EXTRA_BROADCAST = KEY_PREFIX + "broadcast";
    private static final String EXTRA_SHOW_SEND_COMMENT = KEY_PREFIX + "show_send_comment";
    private static final String EXTRA_TITLE = KEY_PREFIX + "title";

    private BroadcastFragment mFragment;

    private boolean mShouldFinish;

    public static Intent makeIntent(long broadcastId, Context context) {
        return new Intent(context, BroadcastActivity.class)
                .putExtra(EXTRA_BROADCAST_ID, broadcastId);
    }

    public static Intent makeIntent(Broadcast broadcast, Context context) {
        return makeIntent(broadcast.id, context)
                .putExtra(EXTRA_BROADCAST, broadcast);
    }

    public static Intent makeIntent(Broadcast broadcast, boolean showSendComment, String title,
                                    Context context) {
        return makeIntent(broadcast, context)
                .putExtra(EXTRA_SHOW_SEND_COMMENT, showSendComment)
                .putExtra(EXTRA_TITLE, title);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
    }
}
