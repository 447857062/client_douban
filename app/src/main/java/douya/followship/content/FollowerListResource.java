/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.followship.content;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import douya.network.api.ApiRequest;
import douya.network.api.ApiService;
import douya.network.api.info.frodo.UserList;
import douya.util.FragmentUtils;

public class FollowerListResource extends FollowshipUserListResource {

    private static final String FRAGMENT_TAG_DEFAULT = FollowerListResource.class.getName();

    private static FollowerListResource newInstance(String userIdOrUid) {
        //noinspection deprecation
        return new FollowerListResource().setArguments(userIdOrUid);
    }

    public static FollowerListResource attachTo(String userIdOrUid, Fragment fragment,
                                                      String tag, int requestCode) {
        FragmentActivity activity = fragment.getActivity();
        FollowerListResource instance = FragmentUtils.findByTag(activity, tag);
        if (instance == null) {
            instance = newInstance(userIdOrUid);
            FragmentUtils.add(instance, activity, tag);
        }
        instance.setTarget(fragment, requestCode);
        return instance;
    }

    public static FollowerListResource attachTo(String userIdOrUid, Fragment fragment) {
        return attachTo(userIdOrUid, fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    /**
     * @deprecated Use {@code attachTo()} instead.
     */
    public FollowerListResource() {}

    @Override
    protected FollowerListResource setArguments(String userIdOrUid) {
        super.setArguments(userIdOrUid);
        return this;
    }

    @Override
    protected ApiRequest<UserList> onCreateRequest(Integer start, Integer count) {
        return ApiService.getInstance().getFollowerList(getUserIdOrUid(), start, count);
    }
}
