/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.douya.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import douya.link.UriHandler;
import douya.network.api.info.frodo.Doulist;
import douya.ui.SimpleAdapter;
import douya.util.ViewUtils;

public class ItemRelatedDoulistListAdapter
        extends SimpleAdapter<Doulist, ItemRelatedDoulistListAdapter.ViewHolder> {

    public ItemRelatedDoulistListAdapter() {
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(R.layout.item_related_doulist_item, parent));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Doulist doulist = getItem(position);
        holder.titleText.setText(doulist.title);
        holder.followerCountText.setText(holder.followerCountText.getContext().getString(
                R.string.item_related_doulist_follower_count_format, doulist.followerCount));
        holder.itemView.setOnClickListener(view -> {
            // TODO
            UriHandler.open(doulist.url, view.getContext());
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        public TextView titleText;
        @BindView(R.id.follower_count)
        public TextView followerCountText;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
