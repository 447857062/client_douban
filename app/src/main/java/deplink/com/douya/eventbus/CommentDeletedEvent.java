/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package deplink.com.douya.eventbus;

public class CommentDeletedEvent extends Event {

    public long commentId;

    public CommentDeletedEvent(long commentId, Object source) {
        super(source);

        this.commentId = commentId;
    }
}
