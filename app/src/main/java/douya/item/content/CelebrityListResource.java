/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import douya.content.RawListResourceFragment;
import douya.network.api.ApiError;
import douya.network.api.ApiRequest;
import douya.network.api.ApiService;
import douya.network.api.info.frodo.CelebrityList;
import douya.network.api.info.frodo.CollectableItem;
import douya.network.api.info.frodo.SimpleCelebrity;
import douya.util.FragmentUtils;

public class CelebrityListResource extends RawListResourceFragment<CelebrityList, SimpleCelebrity> {

    private static final String KEY_PREFIX = CelebrityListResource.class.getName() + '.';

    private static final String EXTRA_ITEM_TYPE = KEY_PREFIX + "item_type";
    private static final String EXTRA_ITEM_ID = KEY_PREFIX + "item_id";

    private CollectableItem.Type mItemType;
    private long mItemId;

    private static final String FRAGMENT_TAG_DEFAULT = CelebrityListResource.class.getName();

    private static CelebrityListResource newInstance(CollectableItem.Type itemType, long itemId) {
        //noinspection deprecation
        return new CelebrityListResource().setArguments(itemType, itemId);
    }

    public static CelebrityListResource attachTo(CollectableItem.Type itemType, long itemId,
                                                 Fragment fragment, String tag, int requestCode) {
        FragmentActivity activity = fragment.getActivity();
        CelebrityListResource instance = FragmentUtils.findByTag(activity, tag);
        if (instance == null) {
            instance = newInstance(itemType, itemId);
            FragmentUtils.add(instance, activity, tag);
        }
        instance.setTarget(fragment, requestCode);
        return instance;
    }

    public static CelebrityListResource attachTo(CollectableItem.Type itemType, long itemId,
                                                 Fragment fragment) {
        return attachTo(itemType, itemId, fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    /**
     * @deprecated Use {@code attachTo()} instead.
     */
    public CelebrityListResource() {}

    protected CelebrityListResource setArguments(CollectableItem.Type itemType, long itemId) {
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
    protected ApiRequest<CelebrityList> onCreateRequest() {
        return ApiService.getInstance().getItemCelebrityList(mItemType, mItemId);
    }

    @Override
    protected void onLoadStarted() {
        getListener().onLoadCelebrityListStarted(getRequestCode());
    }

    @Override
    protected void onLoadFinished(boolean successful, CelebrityList response, ApiError error) {
        onLoadFinished(successful, successful ? transformResponse(response) : null, error);
    }

    private List<SimpleCelebrity> transformResponse(CelebrityList response) {
        List<SimpleCelebrity> celebrityList = new ArrayList<>();
        celebrityList.addAll(response.directors);
        for (SimpleCelebrity celebrity : celebrityList) {
            celebrity.isDirector = true;
        }
        celebrityList.addAll(response.actors);
        return celebrityList;
    }

    private void onLoadFinished(boolean successful, List<SimpleCelebrity> response,
                                ApiError error) {
        if (successful) {
            set(response);
            getListener().onLoadCelebrityListFinished(getRequestCode());
            getListener().onCelebrityListChanged(getRequestCode(), response);
        } else {
            getListener().onLoadCelebrityListFinished(getRequestCode());
            getListener().onLoadCelebrityListError(getRequestCode(), error);
        }
    }

    private Listener getListener() {
        return (Listener) getTarget();
    }

    public interface Listener {
        void onLoadCelebrityListStarted(int requestCode);
        void onLoadCelebrityListFinished(int requestCode);
        void onLoadCelebrityListError(int requestCode, ApiError error);
        void onCelebrityListChanged(int requestCode, List<SimpleCelebrity> newCelebrityList);
    }
}
