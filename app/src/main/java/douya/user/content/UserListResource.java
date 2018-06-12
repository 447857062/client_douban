/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.user.content;

import douya.network.api.ApiError;
import douya.network.api.info.frodo.UserList;

public abstract class UserListResource extends BaseUserListResource<UserList> {

    @Override
    protected void onCallRawLoadFinished(boolean more, int count, boolean successful,
                                         UserList response, ApiError error) {
        onRawLoadFinished(more, count, successful, response != null ? response.users : null, error);
    }
}
