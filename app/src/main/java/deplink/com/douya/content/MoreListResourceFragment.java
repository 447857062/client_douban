package deplink.com.douya.content;

import deplink.com.douya.network.api.ApiError;
import deplink.com.douya.network.api.ApiRequest;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public abstract class MoreListResourceFragment<ResponseType, ResourceListType>
        extends ListResourceFragment<ResponseType, ResourceListType> {
    private static final int DEFAULT_LOAD_COUNT = 20;

    private boolean mLoadingMore;
    private boolean mCanLoadMore = true;

    private int mLoadCount;
    public void load(boolean loadMore) {
        load(loadMore, getDefaultLoadCount());
    }
    protected int getDefaultLoadCount() {
        return DEFAULT_LOAD_COUNT;
    }
    public void load(boolean more, int count) {

        if (isLoading() || (more && !mCanLoadMore)) {
            return;
        }

        mLoadingMore = more;
        mLoadCount = count;
        super.load();
    }
    protected void append(ResourceListType more) {
        super.set(addAll(get(), more));

        mCanLoadMore = getSize(more) == mLoadCount;
    }
    @Override
    protected final ApiRequest<ResponseType> onCreateRequest() {
        return onCreateRequest(mLoadingMore, mLoadCount);
    }

    protected ApiRequest<ResponseType> onCreateRequest(boolean more, int count) {
        return onCreateRequest(more && has() ? getSize(get()) : null, count);
    }

    protected abstract ApiRequest<ResponseType> onCreateRequest(Integer start, Integer count);

    protected void setCanLoadMore(boolean canLoadMore) {
        mCanLoadMore = canLoadMore;
    }
    public boolean isLoadingMore() {
        return mLoadingMore;
    }
    protected abstract ResourceListType addAll(ResourceListType resource, ResourceListType more);

    @Override
    protected final void onLoadFinished(boolean successful, ResponseType response,
                                        ApiError error) {
        onLoadFinished(mLoadingMore, mLoadCount, successful, response, error);
        mLoadingMore = false;
    }

    protected abstract void onLoadFinished(boolean more, int count, boolean successful,
                                           ResponseType response, ApiError error);
}
