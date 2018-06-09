package deplink.com.douya.user.content;

import android.os.Bundle;

import deplink.com.douya.content.ResourceFragment;
import deplink.com.douya.eventbus.EventBusUtils;
import deplink.com.douya.eventbus.UserUpdatedEvent;
import deplink.com.douya.network.api.ApiError;
import deplink.com.douya.network.api.ApiRequest;
import deplink.com.douya.network.api.info.apiv2.SimpleUser;
import deplink.com.douya.network.api.info.apiv2.User;
import deplink.com.douya.util.FragmentUtils;

/**
 * Created by ${kelijun} on 2018/6/6.
 */

public class UserResource extends ResourceFragment<User, User> {
    // Not static because we are to be subclassed.
    private final String KEY_PREFIX = getClass().getName() + '.';

    private final String EXTRA_USER_ID_OR_UID = KEY_PREFIX + "user_id_or_uid";
    private final String EXTRA_SIMPLE_USER = KEY_PREFIX + "simple_user";
    private final String EXTRA_USER = KEY_PREFIX + "user";

    private String mUserIdOrUid;
    private SimpleUser mSimpleUser;
    private User mExtraUser;

    private static final String FRAGMENT_TAG_DEFAULT = UserResource.class.getName();

    private static UserResource newInstance(String userIdOrUid, SimpleUser simpleUser, User user) {
        //noinspection deprecation
        return new UserResource().setArguments(userIdOrUid, simpleUser, user);
    }
    /**
     * @deprecated Use {@code attachTo()} instead.
     */
    public UserResource() {}

    protected UserResource setArguments(String userIdOrUid, SimpleUser simpleUser, User user) {
        FragmentUtils.getArgumentsBuilder(this)
                .putString(EXTRA_USER_ID_OR_UID, userIdOrUid)
                .putParcelable(EXTRA_SIMPLE_USER, simpleUser)
                .putParcelable(EXTRA_USER, user);
        return this;
    }
    public String getUserIdOrUid() {
        ensureArguments();
        return mUserIdOrUid;
    }

    public SimpleUser getSimpleUser() {
        // Can be called before onCreate() is called.
        ensureArguments();
        return mSimpleUser;
    }

    public boolean hasSimpleUser() {
        return getSimpleUser() != null;
    }

    @Override
    public User get() {
        User user = super.get();
        if (user == null) {
            // Can be called before onCreate() is called.
            ensureArguments();
            user = mExtraUser;
        }
        return user;
    }
    private void ensureArguments() {
        if (mUserIdOrUid != null) {
            return;
        }
        Bundle arguments = getArguments();
        mExtraUser = arguments.getParcelable(EXTRA_USER);
        if (mExtraUser != null) {
            mSimpleUser = mExtraUser;
            mUserIdOrUid = mExtraUser.getIdOrUid();
        } else {
            mSimpleUser = arguments.getParcelable(EXTRA_SIMPLE_USER);
            if (mSimpleUser != null) {
                mUserIdOrUid = mSimpleUser.getIdOrUid();
            } else {
                mUserIdOrUid = arguments.getString(EXTRA_USER_ID_OR_UID);
            }
        }
    }
    @Override
    protected void set(User user) {
        super.set(user);

        user = get();
        if (user != null) {
            mSimpleUser = user;
            mUserIdOrUid = user.getIdOrUid();
        }
    }
    @Override
    protected ApiRequest<User> onCreateRequest() {
        return null;
    }

    @Override
    protected void onLoadStarted() {
        getListener().onLoadUserStarted(getRequestCode());
    }

    @Override
    protected void onLoadFinished(boolean successful, User response, ApiError error) {
        if (successful) {
            set(response);
            onLoadSuccess(response);
            getListener().onLoadUserFinished(getRequestCode());
            getListener().onUserChanged(getRequestCode(), get());
            EventBusUtils.postAsync(new UserUpdatedEvent(response, this));
        } else {
            getListener().onLoadUserFinished(getRequestCode());
            getListener().onLoadUserError(getRequestCode(), error);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (has()) {
            User user = get();
            setArguments(user.getIdOrUid(), user, user);
        }
    }

    protected void onLoadSuccess(User user) {}

    private Listener getListener() {
        return (Listener) getTarget();
    }
    public interface Listener {
        void onLoadUserStarted(int requestCode);
        void onLoadUserFinished(int requestCode);
        void onLoadUserError(int requestCode, ApiError error);
        void onUserChanged(int requestCode, User newUser);
        void onUserWriteStarted(int requestCode);
        void onUserWriteFinished(int requestCode);
    }
}
