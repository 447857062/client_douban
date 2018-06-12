/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.network.api;


import douya.network.AuthenticationInterceptor;

public class ApiAuthenticationInterceptor extends AuthenticationInterceptor {

    private String mAuthTokenType;

    public ApiAuthenticationInterceptor(String authTokenType) {
        super(ApiContract.Request.Base.MAX_NUM_RETRIES);

        mAuthTokenType = authTokenType;
    }

    @Override
    protected AuthenticationInterceptor.Authenticator getAuthenticator() {
        return ApiAuthenticators.getInstance().getAuthenticator(mAuthTokenType);
    }
}
