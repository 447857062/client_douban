/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.broadcast.content;

import java.util.List;

import douya.eventbus.BroadcastUpdatedEvent;
import douya.eventbus.EventBusUtils;
import douya.network.api.info.frodo.Broadcast;
import douya.network.api.info.frodo.Comment;

public class BroadcastCommentCountFixer {

    private BroadcastCommentCountFixer() {}

    public static void onCommentRemoved(Broadcast broadcast, Object eventSource) {

        if (broadcast == null) {
            return;
        }

        --broadcast.commentCount;
        EventBusUtils.postAsync(new BroadcastUpdatedEvent(broadcast, eventSource));
    }

    public static void onCommentListChanged(Broadcast broadcast, List<Comment> commentList,
                                            Object eventSource) {

        if (broadcast == null || commentList == null) {
            return;
        }

        if (broadcast.commentCount < commentList.size()) {
            broadcast.commentCount = commentList.size();
            EventBusUtils.postAsync(new BroadcastUpdatedEvent(broadcast, eventSource));
        }
    }
}
