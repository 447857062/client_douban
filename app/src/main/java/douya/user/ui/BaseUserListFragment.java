/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.user.ui;

import java.util.List;

import douya.network.api.ApiError;
import douya.network.api.info.frodo.SimpleUser;
import douya.ui.BaseListFragment;
import douya.user.content.BaseUserListResource;

public abstract class BaseUserListFragment extends BaseListFragment<SimpleUser>
        implements BaseUserListResource.Listener {

    @Override
    public void onLoadUserListStarted(int requestCode) {
        onLoadListStarted();
    }

    @Override
    public void onLoadUserListFinished(int requestCode) {
        onLoadListFinished();
    }

    @Override
    public void onLoadUserListError(int requestCode, ApiError error) {
        onLoadListError(error);
    }

    @Override
    public void onUserListChanged(int requestCode, List<SimpleUser> newUserList) {
        onListChanged(newUserList);
    }

    @Override
    public void onUserListAppended(int requestCode, List<SimpleUser> appendedUserList) {
        onListAppended(appendedUserList);
    }
}
