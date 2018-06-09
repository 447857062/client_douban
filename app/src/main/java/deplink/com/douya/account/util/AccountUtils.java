package deplink.com.douya.account.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.text.TextUtils;

import com.google.gson.JsonParseException;

import deplink.com.douya.DouyaApplication;
import deplink.com.douya.account.app.AccountPreferences;
import deplink.com.douya.account.info.AccountContract;
import deplink.com.douya.network.api.info.apiv2.User;
import deplink.com.douya.settings.info.Settings;
import deplink.com.douya.util.GsonHelper;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class AccountUtils {
    public static AccountManager getAccountManager() {
        return AccountManager.get(DouyaApplication.getInstance());
    }
    public static long getUserId() {
        return getUserId(getActiveAccount());
    }
    public static long getUserId(Account account) {
        return AccountPreferences.forAccount(account).getLong(AccountContract.KEY_USER_ID,
                AccountContract.INVALID_USER_ID);
    }
    // User name is different from username: user name is the display name in User.name, but
    // username is the account name for logging in.
    public static String getUserName(Account account) {
        return AccountPreferences.forAccount(account).getString(AccountContract.KEY_USER_NAME,
                null);
    }

    public static String getUserName() {
        return getUserName(getActiveAccount());
    }

    public static void setUserName(Account account, String userName) {
        AccountPreferences.forAccount(account).putString(AccountContract.KEY_USER_NAME, userName);
    }
    public static void setUserId(Account account, long userId) {
        AccountPreferences.forAccount(account).putLong(AccountContract.KEY_USER_ID, userId);
    }
    // NOTICE:
    // Will clear the invalid setting and return null if no matching account with the name from
    // setting is found.
    public static Account getActiveAccount() {
        Account account = getAccountByName(getActiveAccountName());
        if (account != null) {
            return account;
        } else {
            removeActiveAccountName();
            return null;
        }
    }
    private static void removeActiveAccountName() {
        Settings.ACTIVE_ACCOUNT_NAME.remove();
    }
    // NOTE: Use getActiveAccount() instead for availability checking.
    private static String getActiveAccountName() {
        return Settings.ACTIVE_ACCOUNT_NAME.getValue();
    }
    private static Account getAccountByName(String accountName) {

        if (TextUtils.isEmpty(accountName)) {
            return null;
        }

        for (Account account : getAccounts()) {
            if (TextUtils.equals(account.name, accountName)) {
                return account;
            }
        }

        return null;
    }
    public static Account[] getAccounts() {
        return getAccountManager().getAccountsByType(AccountContract.ACCOUNT_TYPE);
    }
    public static User getUser(Account account) {
        String userInfoJson = AccountPreferences.forAccount(account).getString(
                AccountContract.KEY_USER_INFO, null);
        if (!TextUtils.isEmpty(userInfoJson)) {
            try {
                return GsonHelper.GSON.fromJson(userInfoJson, User.class);
            } catch (JsonParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void setUser(Account account, User user) {
        String userInfoJson = GsonHelper.GSON.toJson(user, User.class);
        AccountPreferences.forAccount(account).putString(AccountContract.KEY_USER_INFO,
                userInfoJson);
    }

    public static User getUser() {
        return getUser(getActiveAccount());
    }

    public static void setUser(User user) {
        setUser(getActiveAccount(), user);
    }
}
