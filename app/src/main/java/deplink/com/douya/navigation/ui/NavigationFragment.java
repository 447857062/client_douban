package deplink.com.douya.navigation.ui;


import android.accounts.Account;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import deplink.com.douya.R;
import deplink.com.douya.account.content.AccountUserResource;
import deplink.com.douya.network.api.info.apiv2.SimpleUser;
import deplink.com.douya.network.api.info.frodo.User;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class NavigationFragment extends Fragment implements NavigationHeaderLayout.Adapter,
        NavigationHeaderLayout.Listener{

    private ArrayMap<Account, AccountUserResource> mUserResourceMap;
    @BindView(R.id.navigation)
    NavigationView mNavigationView;
    private NavigationHeaderLayout mHeaderLayout;
    public static NavigationFragment newInstance() {
        //noinspection deprecation
        return new NavigationFragment();
    }
    public NavigationFragment() {}
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        mHeaderLayout = (NavigationHeaderLayout) mNavigationView.getHeaderView(0);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navigation_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserResourceMap = new ArrayMap<>();
      /*  getDrawer().addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
              //  mHeaderLayout.setShowingAccountList(false);
            }
        });*/
        mHeaderLayout.setAdapter(this);
        mHeaderLayout.setListener(this);
        mHeaderLayout.bind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

      //  AccountUtils.removeOnAccountListUpdatedListener(this);

        for (AccountUserResource userResource : mUserResourceMap.values()) {
        //    userResource.detach();
        }
    }
    private DrawerLayout getDrawer() {
        return getNavigationHost().getDrawer();
    }

    private Host getNavigationHost() {
        return (Host) getActivity();
    }

    @Override
    public SimpleUser getPartialUser(Account account) {
        return null;
    }

    @Override
    public User getUser(Account account) {
        return null;
    }

    @Override
    public void openProfile(Account account) {

    }

    @Override
    public void showAccountList(boolean show) {

    }

    @Override
    public void onAccountTransitionStart() {

    }

    @Override
    public void onAccountTransitionEnd() {

    }

    public interface Host {
        DrawerLayout getDrawer();
        void reloadForActiveAccountChange();
    }
}
