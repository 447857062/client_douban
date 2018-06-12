/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.account.content;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import douya.network.RequestFragment;
import douya.network.api.ApiError;
import douya.network.api.ApiRequest;
import douya.network.api.ApiService;
import douya.network.api.info.AuthenticationResponse;
import douya.util.FragmentUtils;

public class AuthenticateRequest extends RequestFragment<AuthenticateRequest.RequestState,
        AuthenticationResponse> {

    private static final String FRAGMENT_TAG_DEFAULT = AuthenticateRequest.class.getName();

    public static AuthenticateRequest attachTo(Fragment fragment, String tag, int requestCode) {
        FragmentActivity activity = fragment.getActivity();
        AuthenticateRequest instance = FragmentUtils.findByTag(activity, tag);
        if (instance == null) {
            //noinspection deprecation
            instance = new AuthenticateRequest();
            FragmentUtils.add(instance, activity, tag);
        }
        instance.setTarget(fragment, requestCode);
        return instance;
    }

    public static AuthenticateRequest attachTo(Fragment fragment) {
        return attachTo(fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    /**
     * @deprecated Use {@code attachTo()} instead.
     */
    public AuthenticateRequest() {}

    public void start(String authTokenType, String username, String password) {
        start(new RequestState(authTokenType, username, password));
    }

    @Override
    protected ApiRequest<AuthenticationResponse> onCreateRequest(RequestState requestState) {
        return ApiService.getInstance().authenticate(requestState.authTokenType,
                requestState.username, requestState.password);
    }

    @Override
    protected void onRequestStarted() {
        getListener().onAuthenticateStarted(getRequestCode());
    }

    @Override
    protected void onRequestFinished(boolean successful, RequestState requestState,
                                     AuthenticationResponse response, ApiError error) {
        getListener().onAuthenticateFinished(getRequestCode());
        if (successful) {
            getListener().onAuthenticateSuccess(getRequestCode(), requestState, response);
        } else {
            getListener().onAuthenticateError(getRequestCode(), requestState, error);
        }
    }

    private Listener getListener() {
        return (Listener) getTarget();
    }

    public static class RequestState {

        public String authTokenType;
        public String username;
        public String password;

        public RequestState(String authTokenType, String username, String password) {
            this.authTokenType = authTokenType;
            this.username = username;
            this.password = password;
        }
    }

    public interface Listener {
        void onAuthenticateStarted(int requestCode);
        void onAuthenticateFinished(int requestCode);
        void onAuthenticateSuccess(int requestCode, RequestState requestState,
                                   AuthenticationResponse response);
        void onAuthenticateError(int requestCode, RequestState requestState, ApiError error);
    }
}
