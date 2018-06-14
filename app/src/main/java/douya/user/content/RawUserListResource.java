/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.user.content;

import java.util.List;

import douya.network.api.ApiError;
import douya.network.api.info.frodo.SimpleUser;

public abstract class RawUserListResource extends BaseUserListResource<List<SimpleUser>> {

    @Override
    protected void onCallRawLoadFinished(boolean more, int count, boolean successful,
                                         List<SimpleUser> response, ApiError error) {
        onRawLoadFinished(more, count, successful, response, error);
    }
}
