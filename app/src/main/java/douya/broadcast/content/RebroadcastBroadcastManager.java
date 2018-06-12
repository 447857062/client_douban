package douya.broadcast.content;

import android.content.Context;

import douya.content.ResourceWriterManager;
import douya.network.api.info.frodo.Broadcast;

/**
 * Created by ${kelijun} on 2018/6/6.
 */

public class RebroadcastBroadcastManager extends ResourceWriterManager<RebroadcastBroadcastWriter> {
    private static class InstanceHolder {
        public static final RebroadcastBroadcastManager VALUE = new RebroadcastBroadcastManager();
    }
    public String getText(long broadcastId) {
        RebroadcastBroadcastWriter writer = findWriter(broadcastId);
        return writer != null ? writer.getText() : null;
    }
    public static RebroadcastBroadcastManager getInstance() {
        return InstanceHolder.VALUE;
    }

    /**
     * @deprecated Use {@link #write(Broadcast, String, Context)} instead.
     */
    public void write(long broadcastId, String text, Context context) {
        add(new RebroadcastBroadcastWriter(broadcastId, text, this), context);
    }

    public void write(Broadcast broadcast, String text, Context context) {
        add(new RebroadcastBroadcastWriter(broadcast, text, this), context);
    }

    public boolean isWriting(long broadcastId) {
        return findWriter(broadcastId) != null;
    }

    public boolean isWritingQuickRebroadcast(long broadcastId) {
        RebroadcastBroadcastWriter writer = findWriter(broadcastId);
        return writer != null && writer.getText() == null;
    }
    private RebroadcastBroadcastWriter findWriter(long broadcastId) {
        for (RebroadcastBroadcastWriter writer : getWriters()) {
            if (writer.getBroadcastId() == broadcastId) {
                return writer;
            }
        }
        return null;
    }
}
