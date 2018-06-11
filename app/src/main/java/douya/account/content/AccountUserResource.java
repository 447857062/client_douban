package douya.account.content;

import android.accounts.Account;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import douya.account.util.AccountUtils;
import douya.network.api.info.apiv2.SimpleUser;
import douya.network.api.info.apiv2.User;
import douya.user.content.UserResource;
import douya.util.FragmentUtils;

/**
 * Created by ${kelijun} on 2018/6/6.
 */

public class AccountUserResource extends UserResource {
    private static final String KEY_PREFIX = AccountUserResource.class.getName() + '.';

    private final String EXTRA_ACCOUNT = KEY_PREFIX + "account";

    private Account mAccount;

    private static final String FRAGMENT_TAG_DEFAULT = AccountUserResource.class.getName();

    private static AccountUserResource newInstance(Account account) {
        //noinspection deprecation
        return new AccountUserResource().setArguments(account);
    }
    public AccountUserResource() {}
    public static AccountUserResource attachTo(Account account, Fragment fragment, String tag,
                                               int requestCode) {
        FragmentActivity activity = fragment.getActivity();
        AccountUserResource instance = FragmentUtils.findByTag(activity, tag);
        if (instance == null) {
            instance = newInstance(account);
            FragmentUtils.add(instance, activity, tag);
        }
        instance.setTarget(fragment, requestCode);
        return instance;
    }

    public static AccountUserResource attachTo(Account account, Fragment fragment) {
        return attachTo(account, fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }
    protected AccountUserResource setArguments(Account account) {
        SimpleUser partialUser = makePartialUser(account);
        super.setArguments(partialUser.getIdOrUid(), partialUser, AccountUtils.getUser(account));
        FragmentUtils.getArgumentsBuilder(this)
                .putParcelable(EXTRA_ACCOUNT, account);
        return this;
    }

    private SimpleUser makePartialUser(Account account) {
        SimpleUser partialUser = new SimpleUser();
        //noinspection deprecation
        partialUser.id = AccountUtils.getUserId(account);
        //noinspection deprecation
        partialUser.uid = String.valueOf(partialUser.id);
        partialUser.name = AccountUtils.getUserName(account);
        return partialUser;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccount = getArguments().getParcelable(EXTRA_ACCOUNT);
    }

    @Override
    protected void loadOnStart() {
        super.loadOnStart();
        onLoadOnStart();
    }
    @Override
    protected void onLoadSuccess(User user) {
        super.onLoadSuccess(user);

        AccountUtils.setUserName(mAccount, user.name);
        AccountUtils.setUser(mAccount, user);
    }
    @Deprecated
    @Override
    public boolean hasSimpleUser() {
        throw new IllegalStateException("We always have a (partial) user");
    }

    @Deprecated
    @Override
    public SimpleUser getSimpleUser() {
        throw new IllegalStateException("Use getPartialUser() instead");
    }
    public SimpleUser getPartialUser() {
        return super.getSimpleUser();
    }

}
