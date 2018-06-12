/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.eventbus;


import douya.network.api.info.frodo.Comment;

public class BroadcastCommentSentEvent extends Event {

    public long broadcastId;

    public Comment comment;

    public BroadcastCommentSentEvent(long broadcastId, Comment comment, Object source) {
        super(source);

        this.broadcastId = broadcastId;
        this.comment = comment;
    }
}
