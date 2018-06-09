/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package deplink.com.douya.broadcast.content;

import android.content.Context;

import deplink.com.douya.content.ResourceWriterManager;
import deplink.com.douya.network.api.info.frodo.Broadcast;


public class DeleteBroadcastManager extends ResourceWriterManager<DeleteBroadcastWriter> {

    private static class InstanceHolder {
        public static final DeleteBroadcastManager VALUE = new DeleteBroadcastManager();
    }

    public static DeleteBroadcastManager getInstance() {
        return InstanceHolder.VALUE;
    }

    /**
     * @deprecated Use {@link #write(Broadcast, Context)} instead.
     */
    public void write(long broadcastId, Context context) {
        add(new DeleteBroadcastWriter(broadcastId, this), context);
    }

    public void write(Broadcast broadcast, Context context) {
        add(new DeleteBroadcastWriter(broadcast, this), context);
    }

    public boolean isWriting(long broadcastId) {
        return findWriter(broadcastId) != null;
    }

    private DeleteBroadcastWriter findWriter(long broadcastId) {
        for (DeleteBroadcastWriter writer : getWriters()) {
            if (writer.getBroadcastId() == broadcastId) {
                return writer;
            }
        }
        return null;
    }
}
