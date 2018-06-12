/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.content;

import android.content.Context;

import com.douya.R;

import douya.content.RequestResourceWriter;
import douya.content.ResourceWriterManager;
import douya.eventbus.EventBusUtils;
import douya.eventbus.ItemCollectionUpdatedEvent;
import douya.eventbus.ItemCollectionWriteFinishedEvent;
import douya.eventbus.ItemCollectionWriteStartedEvent;
import douya.network.api.ApiError;
import douya.network.api.ApiRequest;
import douya.network.api.ApiService;
import douya.network.api.info.frodo.CollectableItem;
import douya.network.api.info.frodo.ItemCollection;
import douya.util.LogUtils;
import douya.util.ToastUtils;

class VoteItemCollectionWriter
        extends RequestResourceWriter<VoteItemCollectionWriter, ItemCollection> {

    private CollectableItem.Type mItemType;
    private long mItemId;
    private long mItemCollectionId;

    public VoteItemCollectionWriter(CollectableItem.Type itemType, long itemId,
                                    long itemCollectionId,
                                    ResourceWriterManager<VoteItemCollectionWriter> manager) {
        super(manager);

        mItemType = itemType;
        mItemId = itemId;
        mItemCollectionId = itemCollectionId;
    }

    public CollectableItem.Type getItemType() {
        return mItemType;
    }

    public long getItemId() {
        return mItemId;
    }

    public long getItemCollectionId() {
        return mItemCollectionId;
    }

    @Override
    protected ApiRequest<ItemCollection> onCreateRequest() {
        return ApiService.getInstance().voteItemCollection(mItemType, mItemId, mItemCollectionId);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBusUtils.postAsync(new ItemCollectionWriteStartedEvent(mItemCollectionId, this));
    }

    @Override
    public void onResponse(ItemCollection response) {

        ToastUtils.show(R.string.item_collection_vote_successful, getContext());

        EventBusUtils.postAsync(new ItemCollectionUpdatedEvent(response, this));

        stopSelf();
    }

    @Override
    public void onErrorResponse(ApiError error) {

        LogUtils.e(error.toString());
        Context context = getContext();
        ToastUtils.show(context.getString(R.string.item_collection_vote_failed_format,
                ApiError.getErrorString(error, context)), context);

        // Must notify to reset pending status. Off-screen items also needs to be invalidated.
        EventBusUtils.postAsync(new ItemCollectionWriteFinishedEvent(mItemCollectionId, this));

        stopSelf();
    }
}
