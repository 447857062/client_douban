/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.broadcast.content;

import android.accounts.Account;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import douya.account.util.AccountUtils;
import douya.eventbus.BroadcastRebroadcastedEvent;
import douya.eventbus.BroadcastSentEvent;
import douya.network.api.ApiRequest;
import douya.network.api.info.frodo.Broadcast;
import douya.network.api.info.frodo.TimelineList;
import douya.settings.info.Settings;
import douya.util.FragmentUtils;

public class HomeBroadcastListResource extends TimelineBroadcastListResource {

    private static final String FRAGMENT_TAG_DEFAULT = HomeBroadcastListResource.class.getName();

    private final Handler mHandler = new Handler();
    private boolean mStopped;

    private Account mAccount;
    private boolean mLoadingFromCache;

    private static HomeBroadcastListResource newInstance() {
        //noinspection deprecation
        return new HomeBroadcastListResource().setArguments();
    }

    public static HomeBroadcastListResource attachTo(Fragment fragment, String tag,
                                                     int requestCode) {
        FragmentActivity activity = fragment.getActivity();
        HomeBroadcastListResource instance = FragmentUtils.findByTag(activity, tag);
        if (instance == null) {
            instance = newInstance();
            FragmentUtils.add(instance, activity, tag);
        }
        instance.setTarget(fragment, requestCode);
        return instance;
    }

    public static HomeBroadcastListResource attachTo(Fragment fragment) {
        return attachTo(fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    /**
     * @deprecated Use {@code attachTo()} instead.
     */
    @SuppressWarnings("deprecation")
    public HomeBroadcastListResource() {}

    protected HomeBroadcastListResource setArguments() {
        super.setArguments(null, null);
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();

        mStopped = false;
    }

    @Override
    public void onStop() {
        super.onStop();

        mStopped = true;

        if (!isEmpty()) {
            saveToCache(get());
        }
    }

    @Override
    protected boolean shouldIgnoreStartRequest() {
        return mLoadingFromCache;
    }

    @Override
    public boolean isLoading() {
        return super.isLoading() || mLoadingFromCache;
    }

    @Override
    protected void onLoadOnStart() {
        loadFromCache();
    }

    private void loadFromCache() {

        mLoadingFromCache = true;

        mAccount = AccountUtils.getActiveAccount();
        HomeBroadcastListCache.get(mAccount, mHandler, this::onLoadFromCacheFinished,
                getActivity());

        onLoadStarted();
    }

    @Override
    protected ApiRequest<TimelineList> onCreateRequest(boolean more, int count) {
        mAccount = AccountUtils.getActiveAccount();
        return super.onCreateRequest(more, count);
    }

    private void onLoadFromCacheFinished(List<Broadcast> broadcastList) {

        mLoadingFromCache = false;

        if (mStopped) {
            return;
        }

        boolean hasCache = broadcastList != null && !broadcastList.isEmpty();
        if (hasCache) {
            setAndNotifyListener(broadcastList, true);
        }

        if (!hasCache || Settings.AUTO_REFRESH_HOME.getValue()) {
            mHandler.post(() -> {
                if (mStopped) {
                    return;
                }
                HomeBroadcastListResource.super.onLoadOnStart();
            });
        }
    }

    private void saveToCache(List<Broadcast> broadcastList) {
        HomeBroadcastListCache.put(mAccount, broadcastList, getActivity());
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onBroadcastSent(BroadcastSentEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        prependBroadcast(event.broadcast);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onBroadcastRebroadcasted(BroadcastRebroadcastedEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        prependBroadcast(event.rebroadcastBroadcast);
    }

    private void prependBroadcast(Broadcast broadcast) {
        List<Broadcast> broadcastList = get();
        broadcastList.add(0, broadcast);
        getListener().onBroadcastInserted(getRequestCode(), 0, broadcast);
    }

    private Listener getListener() {
        return (Listener) getTarget();
    }

    public interface Listener extends TimelineBroadcastListResource.Listener {
        void onBroadcastInserted(int requestCode, int position, Broadcast insertedBroadcast);
    }
}
