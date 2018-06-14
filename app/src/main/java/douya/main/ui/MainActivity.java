package douya.main.ui;

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

import com.douya.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import douya.account.util.AccountUtils;
import douya.doumail.ui.DoumailUnreadCountFragment;
import douya.home.HomeFragment;
import douya.link.NotImplementedManager;
import douya.navigation.ui.NavigationFragment;
import douya.notification.ui.NotificationListFragment;
import douya.scalpel.ScalpelHelperFragment;
import douya.ui.ActionItemBadge;
import douya.util.FragmentUtils;
import douya.util.TransitionUtils;

public class MainActivity extends AppCompatActivity implements NavigationFragment.Host {

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

        setTheme(R.style.Theme_Douya_MainActivity);

        TransitionUtils.setupTransitionBeforeDecorate(this);
        super.onCreate(savedInstanceState);
       if (!AccountUtils.ensureActiveAccountAvailability(this)) {
            return;
        }
        if (savedInstanceState != null) {
            mOpenedDoumail = savedInstanceState.getBoolean(STATE_OPENED_DOUMAIL);
        }


        setContentView(R.layout.activity_main);
        TransitionUtils.setupTransitionAfterSetContentView(this);
        ButterKnife.bind(this);

        ScalpelHelperFragment.attachTo(this);
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
        ActionItemBadge.setup(mNotificationMenuItem, R.drawable.notifications_icon_white_24dp,
                mNotificationListFragment.getUnreadCount(), this);
        mDoumailMenuItem = menu.findItem(R.id.action_doumail);
        ActionItemBadge.setup(mDoumailMenuItem, R.drawable.mail_icon_white_24dp,
                mDoumailUnreadCountFragment.getUnreadCount(), this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(mNavigationFragment.getView());
                return true;
            case R.id.action_notification:
                mNotificationListFragment.refresh();
                mDrawerLayout.openDrawer(mNotificationDrawer);
                return true;
            case R.id.action_doumail:
                mOpenedDoumail = true;
                NotImplementedManager.openDoumail(this);
                return true;
            case R.id.action_search:

                NotImplementedManager.openSearch(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavigationFragment.getView())) {
            mDrawerLayout.closeDrawer(mNavigationFragment.getView());
        } else if (mDrawerLayout.isDrawerOpen(mNotificationDrawer)) {
            mDrawerLayout.closeDrawer(mNotificationDrawer);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);

        TransitionUtils.setupTransitionForAppBar((Activity) this);
    }

    @Override
    public DrawerLayout getDrawer() {
        return mDrawerLayout;
    }

    @Override
    public void reloadForActiveAccountChange() {
        if (mMainFragment != null) {
            FragmentUtils.remove(mMainFragment);
        }
        if (mNotificationListFragment != null) {
            FragmentUtils.remove(mNotificationListFragment);
        }
        if (mDoumailUnreadCountFragment != null) {
            FragmentUtils.remove(mDoumailUnreadCountFragment);
        }
        FragmentUtils.executePendingTransactions(this);
        addFragments();
        FragmentUtils.executePendingTransactions(this);
    }

    private void addFragments() {
        mMainFragment = HomeFragment.newInstance();
        FragmentUtils.add(mMainFragment, this, R.id.container);
        mNotificationListFragment = NotificationListFragment.newInstance();
        FragmentUtils.add(mNotificationListFragment, this, R.id.notification_list_drawer);
        mDoumailUnreadCountFragment = DoumailUnreadCountFragment.newInstance();
        FragmentUtils.add(mDoumailUnreadCountFragment, this, FRAGMENT_TAG_DOUMAIL_UNREAD_COUNT);
    }

    public void onNotificationUnreadCountUpdate(int count) {
        if (mNotificationMenuItem != null) {
            ActionItemBadge.update(mNotificationMenuItem, count);
        }
    }

    public void onDoumailUnreadCountUpdate(int count) {
        if (mDoumailMenuItem != null) {
            ActionItemBadge.update(mDoumailMenuItem, count);
        }
    }

    public void onRefresh() {
        mNotificationListFragment.refresh();
        mDoumailUnreadCountFragment.refresh();
    }
}
