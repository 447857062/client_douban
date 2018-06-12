/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import douya.content.RawListResourceFragment;
import douya.network.api.ApiError;
import douya.network.api.ApiRequest;
import douya.network.api.ApiService;
import douya.network.api.info.frodo.CollectableItem;
import douya.util.FragmentUtils;

public class ItemRecommendationListResource
        extends RawListResourceFragment<List<CollectableItem>, CollectableItem> {

    private static final String KEY_PREFIX = ItemRecommendationListResource.class.getName() + '.';

    private static final String EXTRA_ITEM_TYPE = KEY_PREFIX + "item_type";
    private static final String EXTRA_ITEM_ID = KEY_PREFIX + "item_id";

    private CollectableItem.Type mItemType;
    private long mItemId;

    private static final String FRAGMENT_TAG_DEFAULT =
            ItemRecommendationListResource.class.getName();

    private static ItemRecommendationListResource newInstance(CollectableItem.Type itemType,
                                                              long itemId) {
        //noinspection deprecation
        return new ItemRecommendationListResource().setArguments(itemType, itemId);
    }

    public static ItemRecommendationListResource attachTo(CollectableItem.Type itemType,
                                                          long itemId, Fragment fragment,
                                                          String tag, int requestCode) {
        FragmentActivity activity = fragment.getActivity();
        ItemRecommendationListResource instance = FragmentUtils.findByTag(activity, tag);
        if (instance == null) {
            instance = newInstance(itemType, itemId);
            FragmentUtils.add(instance, activity, tag);
        }
        instance.setTarget(fragment, requestCode);
        return instance;
    }

    public static ItemRecommendationListResource attachTo(CollectableItem.Type itemType,
                                                          long itemId, Fragment fragment) {
        return attachTo(itemType, itemId, fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    /**
     * @deprecated Use {@code attachTo()} instead.
     */
    public ItemRecommendationListResource() {}

    protected ItemRecommendationListResource setArguments(CollectableItem.Type itemType,
                                                          long itemId) {
        FragmentUtils.getArgumentsBuilder(this)
                .putSerializable(EXTRA_ITEM_TYPE, itemType)
                .putLong(EXTRA_ITEM_ID, itemId);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItemType = (CollectableItem.Type) getArguments().getSerializable(EXTRA_ITEM_TYPE);
        mItemId = getArguments().getLong(EXTRA_ITEM_ID);
    }

    @Override
    protected ApiRequest<List<CollectableItem>> onCreateRequest() {
        // TODO: Utilize count.
        return ApiService.getInstance().getItemRecommendationList(mItemType, mItemId, null);
    }

    @Override
    protected void onLoadStarted() {
        getListener().onLoadRecommendationListStarted(getRequestCode());
    }

    @Override
    protected void onLoadFinished(boolean successful, List<CollectableItem> response,
                                  ApiError error) {
        if (successful) {
            set(response);
            getListener().onLoadRecommendationListFinished(getRequestCode());
            getListener().onRecommendationListChanged(getRequestCode(), response);
        } else {
            getListener().onLoadRecommendationListFinished(getRequestCode());
            getListener().onLoadRecommendationListError(getRequestCode(), error);
        }
    }

    private Listener getListener() {
        return (Listener) getTarget();
    }

    public interface Listener {
        void onLoadRecommendationListStarted(int requestCode);
        void onLoadRecommendationListFinished(int requestCode);
        void onLoadRecommendationListError(int requestCode, ApiError error);
        void onRecommendationListChanged(int requestCode,
                                         List<CollectableItem> newRecommendationList);
    }
}
