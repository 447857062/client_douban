/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package deplink.com.douya.broadcast.content;

import android.content.Context;

import deplink.com.douya.R;
import deplink.com.douya.content.RequestResourceWriter;
import deplink.com.douya.content.ResourceWriterManager;
import deplink.com.douya.eventbus.BroadcastUpdatedEvent;
import deplink.com.douya.eventbus.BroadcastWriteFinishedEvent;
import deplink.com.douya.eventbus.BroadcastWriteStartedEvent;
import deplink.com.douya.eventbus.EventBusUtils;
import deplink.com.douya.network.api.ApiError;
import deplink.com.douya.network.api.ApiRequest;
import deplink.com.douya.network.api.ApiService;
import deplink.com.douya.network.api.info.frodo.Broadcast;
import deplink.com.douya.util.LogUtils;
import deplink.com.douya.util.ToastUtils;

class LikeBroadcastWriter extends RequestResourceWriter<LikeBroadcastWriter, Broadcast> {

    private long mBroadcastId;
    private boolean mLike;

    LikeBroadcastWriter(long broadcastId, boolean like,
                        ResourceWriterManager<LikeBroadcastWriter> manager) {
        super(manager);

        mBroadcastId = broadcastId;
        mLike = like;
    }

    public long getBroadcastId() {
        return mBroadcastId;
    }

    public boolean isLike() {
        return mLike;
    }

    @Override
    protected ApiRequest<Broadcast> onCreateRequest() {
        return ApiService.getInstance().likeBroadcast(mBroadcastId, mLike);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBusUtils.postAsync(new BroadcastWriteStartedEvent(mBroadcastId, this));
    }

    @Override
    public void onResponse(Broadcast response) {

        ToastUtils.show(mLike ? R.string.broadcast_like_successful
                : R.string.broadcast_unlike_successful, getContext());

        EventBusUtils.postAsync(new BroadcastUpdatedEvent(response, this));

        stopSelf();
    }

    @Override
    public void onErrorResponse(ApiError error) {

        LogUtils.e(error.toString());
        Context context = getContext();
        ToastUtils.show(context.getString(mLike ? R.string.broadcast_like_failed_format
                        : R.string.broadcast_unlike_failed_format,
                ApiError.getErrorString(error, context)), context);

        // Must notify to reset pending status. Off-screen items also needs to be invalidated.
        EventBusUtils.postAsync(new BroadcastWriteFinishedEvent(mBroadcastId, this));

        stopSelf();
    }
}
