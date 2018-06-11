/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.broadcast.content;

import android.content.Context;

import deplink.com.douya.R;
import douya.content.RequestResourceWriter;
import douya.eventbus.CommentDeletedEvent;
import douya.eventbus.EventBusUtils;
import douya.network.api.ApiError;
import douya.network.api.ApiRequest;
import douya.network.api.ApiService;
import douya.util.LogUtils;
import douya.util.ToastUtils;

class DeleteBroadcastCommentWriter extends RequestResourceWriter<DeleteBroadcastCommentWriter, Void> {

    private long mBroadcastId;
    private long mCommentId;

    DeleteBroadcastCommentWriter(long broadcastId, long commentId,
                                 DeleteBroadcastCommentManager manager) {
        super(manager);

        mBroadcastId = broadcastId;
        mCommentId = commentId;
    }

    public long getBroadcastId() {
        return mBroadcastId;
    }

    public long getCommentId() {
        return mCommentId;
    }

    @Override
    protected ApiRequest<Void> onCreateRequest() {
        return ApiService.getInstance().deleteBroadcastComment(mBroadcastId, mCommentId);
    }

    @Override
    public void onResponse(Void response) {

        ToastUtils.show(R.string.broadcast_comment_delete_successful, getContext());

        EventBusUtils.postAsync(new CommentDeletedEvent(mCommentId, this));

        stopSelf();
    }

    @Override
    public void onErrorResponse(ApiError error) {

        LogUtils.e(error.toString());
        Context context = getContext();
        ToastUtils.show(context.getString(R.string.broadcast_comment_delete_failed_format,
                ApiError.getErrorString(error, context)), context);

        stopSelf();
    }
}
