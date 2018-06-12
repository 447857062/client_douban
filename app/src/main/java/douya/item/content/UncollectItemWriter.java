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
import douya.eventbus.ItemUncollectedEvent;
import douya.network.api.ApiError;
import douya.network.api.ApiRequest;
import douya.network.api.ApiService;
import douya.network.api.info.frodo.CollectableItem;
import douya.network.api.info.frodo.ItemCollection;
import douya.util.LogUtils;
import douya.util.ToastUtils;

class UncollectItemWriter extends RequestResourceWriter<UncollectItemWriter, ItemCollection> {

    private CollectableItem.Type mItemType;
    private long mItemId;

    UncollectItemWriter(CollectableItem.Type itemType, long itemId,
                        ResourceWriterManager<UncollectItemWriter> manager) {
        super(manager);

        mItemType = itemType;
        mItemId = itemId;
    }

    UncollectItemWriter(CollectableItem item, ResourceWriterManager<UncollectItemWriter> manager) {
        this(item.getType(), item.id, manager);
    }

    public CollectableItem.Type getItemType() {
        return mItemType;
    }

    public long getItemId() {
        return mItemId;
    }

    @Override
    protected ApiRequest<ItemCollection> onCreateRequest() {
        return ApiService.getInstance().uncollectItem(mItemType, mItemId);
    }

    @Override
    public void onResponse(ItemCollection response) {

        Context context = getContext();
        ToastUtils.show(context.getString(R.string.item_uncollect_successful_format,
                mItemType.getName(context)), context);

        EventBusUtils.postAsync(new ItemUncollectedEvent(mItemType, mItemId, this));

        stopSelf();
    }

    @Override
    public void onErrorResponse(ApiError error) {

        LogUtils.e(error.toString());
        Context context = getContext();
        ToastUtils.show(context.getString(R.string.item_uncollect_failed_format,
                mItemType.getName(context), ApiError.getErrorString(error, context)), context);

        stopSelf();
    }
}
