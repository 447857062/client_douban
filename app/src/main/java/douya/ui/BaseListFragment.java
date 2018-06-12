package douya.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.FriendlySwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.douya.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import douya.content.MoreListResourceFragment;
import douya.network.api.ApiError;
import douya.util.LogUtils;
import douya.util.ToastUtils;
import douya.util.ViewUtils;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public abstract class BaseListFragment<T> extends Fragment {
    @BindView(R.id.swipe_refresh)
    protected FriendlySwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.list)
    protected RecyclerView mList;
    @BindView(R.id.progress)
    protected ProgressBar mProgress;

    protected SimpleAdapter<T, ?> mItemAdapter;
    protected LoadMoreAdapter mAdapter;

    protected MoreListResourceFragment<?, List<T>> mResource;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mResource = onAttachResource();
        mSwipeRefreshLayout.setOnRefreshListener(this::onSwipeRefresh);

        mList.setItemAnimator(new NoChangeAnimationItemAnimator());
        mList.setLayoutManager(onCreateLayoutManager());
        mItemAdapter = onCreateAdapter();
        if (mResource.has()) {
            mItemAdapter.replace(mResource.get());
        }
        mAdapter = new LoadMoreAdapter(mItemAdapter);
        mList.setAdapter(mAdapter);
        onAttachScrollListener();

        updateRefreshing();
    }
    protected RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    abstract protected SimpleAdapter<T, ?> onCreateAdapter();


    protected void onAttachScrollListener() {
        mList.addOnScrollListener(new OnVerticalScrollListener() {
            @Override
            public void onScrolledToBottom() {
                mResource.load(true);
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        mResource.detach();
    }
    protected abstract MoreListResourceFragment<?, List<T>> onAttachResource();

    protected void onLoadListStarted() {
        updateRefreshing();
    }

    protected void onLoadListFinished() {
        updateRefreshing();
    }

    protected void onLoadListError(ApiError error) {
        LogUtils.e(error.toString());
        Activity activity = getActivity();
        ToastUtils.show(ApiError.getErrorString(error, activity), activity);
    }

    protected void onListChanged(List<T> newList) {
        mItemAdapter.replace(newList);
        //noinspection unchecked
        onListUpdated(mResource.get());
    }
    protected void onListAppended(List<T> appendedList) {
        mItemAdapter.addAll(appendedList);
        //noinspection unchecked
        onListUpdated(mResource.get());
    }

    protected void onItemChanged(int position, T newItem) {
        mItemAdapter.set(position, newItem);
        //noinspection unchecked
        onListUpdated(mResource.get());
    }
    protected void onItemInserted(int position, T insertedItem) {
        mItemAdapter.add(position, insertedItem);
        //noinspection unchecked
        onListUpdated(mResource.get());
    }

    protected void onItemRemoved(int position) {
        mItemAdapter.remove(position);
        //noinspection unchecked
        onListUpdated(mResource.get());
    }

    protected void onListUpdated(List<T> list) {}

    protected void onItemWriteFinished(int position) {
        mItemAdapter.notifyItemChanged(position);
    }

    protected void onItemWriteStarted(int position) {
        mItemAdapter.notifyItemChanged(position);
    }

    private void updateRefreshing() {
        boolean loading = mResource.isLoading();
        boolean empty = mResource.isEmpty();
        boolean loadingMore = mResource.isLoadingMore();
        mSwipeRefreshLayout.setRefreshing(loading && (mSwipeRefreshLayout.isRefreshing() || !empty)
                && !loadingMore);
        ViewUtils.setVisibleOrGone(mProgress, loading && empty);
        mAdapter.setLoading(loading && !empty && loadingMore);
    }

    protected void onSwipeRefresh() {
        mResource.load(false);
    }
}
