/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.content;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import douya.broadcast.content.DeleteBroadcastCommentManager;
import douya.broadcast.content.DeleteBroadcastManager;
import douya.broadcast.content.LikeBroadcastManager;
import douya.broadcast.content.RebroadcastBroadcastManager;
import douya.broadcast.content.SendBroadcastCommentManager;
import douya.broadcast.content.SendBroadcastManager;
import douya.followship.content.FollowUserManager;
import douya.item.content.CollectItemManager;
import douya.item.content.UncollectItemManager;
import douya.item.content.VoteItemCollectionManager;

public class ResourceWriterService extends Service {

    private List<ResourceWriterManager> mWriterManagers = new ArrayList<>();

    public static void start(Context context) {
        context.startService(new Intent(context, ResourceWriterService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        addWriterManager(FollowUserManager.getInstance());
        addWriterManager(SendBroadcastManager.getInstance());
        addWriterManager(LikeBroadcastManager.getInstance());
        addWriterManager(RebroadcastBroadcastManager.getInstance());
        addWriterManager(DeleteBroadcastManager.getInstance());
        addWriterManager(DeleteBroadcastCommentManager.getInstance());
        addWriterManager(SendBroadcastCommentManager.getInstance());
        addWriterManager(CollectItemManager.getInstance());
        addWriterManager(UncollectItemManager.getInstance());
        addWriterManager(VoteItemCollectionManager.getInstance());
    }

    private void addWriterManager(ResourceWriterManager writerManager) {
        mWriterManagers.add(writerManager);
        writerManager.onBind(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        removeWriterManagers();
    }

    private void removeWriterManagers() {
        Iterator<ResourceWriterManager> iterator = mWriterManagers.iterator();
        while (iterator.hasNext()) {
            ResourceWriterManager writerManager = iterator.next();
            writerManager.onUnbind();
            iterator.remove();
        }
    }
}
