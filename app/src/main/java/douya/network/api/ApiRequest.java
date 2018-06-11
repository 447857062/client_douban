/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.network.api;

import android.support.annotation.NonNull;

import okhttp3.Request;

public interface ApiRequest<T> extends Cloneable {

    T execute() throws ApiError;

    void enqueue(@NonNull final Callback<T> callback);

    boolean isExecuted();

    void cancel();

    boolean isCanceled();

    ApiRequest<T> clone();

    Request request();

    interface Callback<T> {
        void onResponse(T response);
        void onErrorResponse(ApiError error);
    }
}
