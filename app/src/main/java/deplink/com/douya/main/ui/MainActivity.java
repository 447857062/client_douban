package deplink.com.douya.main.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import deplink.com.douya.R;
import deplink.com.douya.doumail.ui.DoumailUnreadCountFragment;
import deplink.com.douya.home.HomeFragment;
import deplink.com.douya.navigation.ui.NavigationFragment;
import deplink.com.douya.notification.ui.NotificationListFragment;
import deplink.com.douya.util.FragmentUtils;
import deplink.com.douya.util.TransitionUtils;

public class MainActivity extends AppCompatActivity  implements NavigationFragment.Host{

    private static final String FRAGMENT_TAG_DOUMAIL_UNREAD_COUNT =
            DoumailUnreadCountFragment.class.getName();

    private static final String KEY_PREFIX = MainActivity.class.getName() + '.';

    private static final String STATE_OPENED_DOUMAIL = KEY_PREFIX + "opened_doumail";
    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.notification_list_drawer)
    View mNotificationDrawer;
    @BindView(R.id.container)
    FrameLayout mContainerLayout;

    private MenuItem mNotificationMenuItem;
    private MenuItem mDoumailMenuItem;

    private NavigationFragment mNavigationFragment;
    private NotificationListFragment mNotificationListFragment;
    private DoumailUnreadCountFragment mDoumailUnreadCountFragment;

    private HomeFragment mMainFragment;

    private boolean mOpenedDoumail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Douya_MainActivity);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mNavigationFragment = FragmentUtils.findById(this, R.id.navigation_fragment);

        if (savedInstanceState == null) {
            addFragments();
        } else {
            mMainFragment = FragmentUtils.findById(this, R.id.container);
            mNotificationListFragment = FragmentUtils.findById(this, R.id.notification_list_drawer);
            mDoumailUnreadCountFragment = FragmentUtils.findByTag(this,
                    FRAGMENT_TAG_DOUMAIL_UNREAD_COUNT);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(STATE_OPENED_DOUMAIL, mOpenedDoumail);
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (mOpenedDoumail) {
            mDoumailUnreadCountFragment.refresh();
            mOpenedDoumail = false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main, menu);
        mNotificationMenuItem = menu.findItem(R.id.action_notification);
       /* ActionItemBadge.setup(mNotificationMenuItem, R.drawable.notifications_icon_white_24dp,
                mNotificationListFragment.getUnreadCount(), this);
        mDoumailMenuItem = menu.findItem(R.id.action_doumail);
        ActionItemBadge.setup(mDoumailMenuItem, R.drawable.mail_icon_white_24dp,
                mDoumailUnreadCountFragment.getUnreadCount(), this);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(mNavigationFragment.getView());
                return true;
            case R.id.action_notification:
              //  mNotificationListFragment.refresh();
                mDrawerLayout.openDrawer(mNotificationDrawer);
                return true;
            case R.id.action_doumail:
                mOpenedDoumail = true;
               // NotImplementedManager.openDoumail(this);
                return true;
            case R.id.action_search:
                // TODO
              //  NotImplementedManager.openSearch(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);

        TransitionUtils.setupTransitionForAppBar((Activity)this);
    }
    private void addFragments() {
        mMainFragment = HomeFragment.newInstance();
        FragmentUtils.add(mMainFragment, this, R.id.container);
        mNotificationListFragment = NotificationListFragment.newInstance();
        FragmentUtils.add(mNotificationListFragment, this, R.id.notification_list_drawer);
        mDoumailUnreadCountFragment = DoumailUnreadCountFragment.newInstance();
        FragmentUtils.add(mDoumailUnreadCountFragment, this, FRAGMENT_TAG_DOUMAIL_UNREAD_COUNT);
    }

    @Override
    public DrawerLayout getDrawer() {
        return null;
    }

    @Override
    public void reloadForActiveAccountChange() {

    }
}
