package deplink.com.douya.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import deplink.com.douya.R;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class ContentStateAdapter extends RecyclerView.Adapter<ContentStateAdapter.ViewHolder> {
    private boolean mHasItem;
    private int mState = ContentStateLayout.STATE_EMPTY;
    private ViewHolder mViewHolder;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mHasItem ? 1 : 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        mViewHolder = null;
    }
    public void setHasItem(boolean hasItem) {

        if (mHasItem == hasItem) {
            return;
        }

        mHasItem = hasItem;
        if (mHasItem) {
            notifyItemInserted(0);
        } else {
            notifyItemRemoved(0);
        }
    }

    public void setState(int state) {
        if (mState == state) {
            return;
        }
        mState = state;
        if (mHasItem) {
            if (mViewHolder != null) {
                onBindViewHolder(mViewHolder, 0);
            } else {
                notifyItemChanged(0);
            }
        }
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content_state)
        public ContentStateLayout contentStateLayout;
        @BindView(R.id.error)
        public TextView errorText;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
