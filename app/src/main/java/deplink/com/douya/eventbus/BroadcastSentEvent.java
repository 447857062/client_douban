/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package deplink.com.douya.eventbus;


import deplink.com.douya.network.api.info.frodo.Broadcast;

public class BroadcastSentEvent extends Event {

    public long writerId;

    public Broadcast broadcast;

    public BroadcastSentEvent(long writerId, Broadcast broadcast, Object source) {
        super(source);

        this.writerId = writerId;
        this.broadcast = broadcast;
    }
}
