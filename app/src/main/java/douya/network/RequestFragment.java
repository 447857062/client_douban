/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.network;


import douya.app.TargetedRetainedFragment;
import douya.network.api.ApiError;
import douya.network.api.ApiRequest;

/**
 * <p>Response will only be delivered when this Fragment is in resumed state.</p>
 *
 * @param <ResponseType> The type of parsed response the request expects.
 */
public abstract class RequestFragment<RequestStateType, ResponseType>
        extends TargetedRetainedFragment implements ApiRequest.Callback<ResponseType> {

    private boolean mRequesting;
    private ApiRequest<ResponseType> mRequest;
    private RequestStateType mRequestState;

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mRequest != null) {
            mRequest.cancel();
            mRequesting = false;
            clearRequest();
        }
    }

    public boolean isRequesting() {
        return mRequesting;
    }

    public ApiRequest<ResponseType> getRequest() {
        return mRequest;
    }

    public RequestStateType getRequestState() {
        return mRequestState;
    }

    protected void start(RequestStateType requestState) {

        if (mRequesting || shouldIgnoreStartRequest()) {
            return;
        }
        mRequesting = true;

        mRequest = onCreateRequest(requestState);
        mRequestState = requestState;
//        mRequest.enqueue(this);

        onRequestStarted();
    }

    protected boolean shouldIgnoreStartRequest() {
        return false;
    }

    protected abstract ApiRequest<ResponseType> onCreateRequest(RequestStateType requestState);

    protected abstract void onRequestStarted();

    @Override
    public final void onResponse(ResponseType response) {
        onCallResponse(true, response, null);
    }

    @Override
    public final void onErrorResponse(ApiError error) {
        onCallResponse(false, null, error);
    }

    private void onCallResponse(final boolean successful, final ResponseType response,
                                final ApiError error) {
        postOnResumed(new Runnable() {
            @Override
            public void run() {
                mRequesting = false;
                onRequestFinished(successful, mRequestState, response, error);
                clearRequest();
            }
        });
    }

    private void clearRequest() {
        mRequest = null;
        mRequestState = null;
    }

    protected abstract void onRequestFinished(boolean successful, RequestStateType requestState,
                                              ResponseType response, ApiError error);
}
