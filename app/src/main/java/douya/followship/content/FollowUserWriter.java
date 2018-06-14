/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.followship.content;

import android.content.Context;
import android.text.TextUtils;

import com.douya.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import douya.content.RequestResourceWriter;
import douya.eventbus.EventBusUtils;
import douya.eventbus.UserUpdatedEvent;
import douya.eventbus.UserWriteFinishedEvent;
import douya.eventbus.UserWriteStartedEvent;
import douya.network.api.ApiError;
import douya.network.api.ApiRequest;
import douya.network.api.ApiService;
import douya.network.api.info.apiv2.User;
import douya.util.LogUtils;
import douya.util.ToastUtils;
import douya.network.api.ApiContract.Response.Error.Codes;
class FollowUserWriter extends RequestResourceWriter<FollowUserWriter, User> {

    private String mUserIdOrUid;
    private User mUser;
    private boolean mFollow;

    private FollowUserWriter(String userIdOrUid, User user, boolean follow,
                             FollowUserManager manager) {
        super(manager);

        mUserIdOrUid = userIdOrUid;
        mUser = user;
        mFollow = follow;

        EventBusUtils.register(this);
    }

    FollowUserWriter(String userIdOrUid, boolean follow, FollowUserManager manager) {
        this(userIdOrUid, null, follow, manager);
    }

    FollowUserWriter(User user, boolean follow, FollowUserManager manager) {
        this(user.getIdOrUid(), user, follow, manager);
    }

    public String getUserIdOrUid() {
        return mUserIdOrUid;
    }

    public boolean hasUserIdOrUid(String userIdOrUid) {
        return mUser != null ? mUser.isIdOrUid(userIdOrUid)
                : TextUtils.equals(mUserIdOrUid, userIdOrUid);
    }

    public boolean isFollow() {
        return mFollow;
    }

    @Override
    protected ApiRequest<User> onCreateRequest() {
        return ApiService.getInstance().follow(mUserIdOrUid, mFollow);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBusUtils.postAsync(new UserWriteStartedEvent(mUserIdOrUid, this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBusUtils.unregister(this);
    }

    @Override
    public void onResponse(User response) {

        ToastUtils.show(mFollow ? R.string.user_follow_successful
                : R.string.user_unfollow_successful, getContext());

        EventBusUtils.postAsync(new UserUpdatedEvent(response, this));

        stopSelf();
    }

    @Override
    public void onErrorResponse(ApiError error) {

        LogUtils.e(error.toString());
        Context context = getContext();
        ToastUtils.show(context.getString(mFollow ? R.string.user_follow_failed_format
                        : R.string.user_unfollow_failed_format,
                ApiError.getErrorString(error, context)), context);

        boolean notified = false;
        if (mUser != null && error instanceof ApiError) {
            // Correct our local state if needed.
            ApiError apiError = (ApiError) error;
            Boolean shouldBeFollowed = null;
            if (apiError.code == Codes.Followship.ALREADY_FOLLOWED) {
                shouldBeFollowed = true;
            } else if (apiError.code == Codes.Followship.NOT_FOLLOWED_YET) {
                shouldBeFollowed = false;
            }
            if (shouldBeFollowed != null) {
                mUser.fixFollowed(shouldBeFollowed);
                EventBusUtils.postAsync(new UserUpdatedEvent(mUser, this));
                notified = true;
            }
        }
        if (!notified) {
            // Must notify to reset pending status. Off-screen items also needs to be invalidated.
            EventBusUtils.postAsync(new UserWriteFinishedEvent(mUserIdOrUid, this));
        }

        stopSelf();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onUserUpdated(UserUpdatedEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        //noinspection deprecation
        if (event.mUser.isIdOrUid(mUserIdOrUid)) {
            mUserIdOrUid = event.mUser.getIdOrUid();
            mUser = event.mUser;
        }
    }
}
