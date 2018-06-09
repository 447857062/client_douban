package deplink.com.douya.account.content;

import android.accounts.Account;
import android.os.Bundle;

import deplink.com.douya.account.util.AccountUtils;
import deplink.com.douya.network.api.info.apiv2.SimpleUser;
import deplink.com.douya.user.content.UserResource;
import deplink.com.douya.util.FragmentUtils;

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


}
