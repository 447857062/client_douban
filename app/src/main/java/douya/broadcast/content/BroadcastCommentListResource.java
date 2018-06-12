/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.broadcast.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;

import douya.eventbus.BroadcastCommentSentEvent;
import douya.network.api.ApiRequest;
import douya.network.api.ApiService;
import douya.network.api.info.frodo.CommentList;
import douya.util.FragmentUtils;

public class BroadcastCommentListResource extends CommentListResource {

    private static final String KEY_PREFIX = BroadcastCommentListResource.class.getName() + '.';

    private static final String EXTRA_BROADCAST_ID = KEY_PREFIX + "broadcast_id";

    private long mBroadcastId;

    private static final String FRAGMENT_TAG_DEFAULT = BroadcastCommentListResource.class.getName();

    private static BroadcastCommentListResource newInstance(long broadcastId) {
        //noinspection deprecation
        return new BroadcastCommentListResource().setArguments(broadcastId);
    }

    public static BroadcastCommentListResource attachTo(long broadcastId, Fragment fragment,
                                                        String tag, int requestCode) {
        FragmentActivity activity = fragment.getActivity();
        BroadcastCommentListResource instance = FragmentUtils.findByTag(activity, tag);
        if (instance == null) {
            instance = newInstance(broadcastId);
            FragmentUtils.add(instance, activity, tag);
        }
        instance.setTarget(fragment, requestCode);
        return instance;
    }

    public static BroadcastCommentListResource attachTo(long broadcastId, Fragment fragment) {
        return attachTo(broadcastId, fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    /**
     * @deprecated Use {@code attachTo()} instead.
     */
    public BroadcastCommentListResource() {}

    protected BroadcastCommentListResource setArguments(long broadcastId) {
        FragmentUtils.getArgumentsBuilder(this)
                .putLong(EXTRA_BROADCAST_ID, broadcastId);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBroadcastId = getArguments().getLong(EXTRA_BROADCAST_ID);
    }

    @Override
    protected ApiRequest<CommentList> onCreateRequest(Integer start, Integer count) {
        return ApiService.getInstance().getBroadcastCommentList(mBroadcastId, start, count);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onBroadcastCommentSent(BroadcastCommentSentEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        if (event.broadcastId == mBroadcastId) {
            appendAndNotifyListener(Collections.singletonList(event.comment));
        }
    }
}
