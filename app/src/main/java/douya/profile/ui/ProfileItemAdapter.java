/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.profile.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.douya.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import douya.item.ui.ItemActivities;
import douya.link.UriHandler;
import douya.network.api.info.frodo.CollectableItem;
import douya.ui.RatioFrameLayout;
import douya.ui.SimpleAdapter;
import douya.util.DrawableUtils;
import douya.util.ImageUtils;
import douya.util.RecyclerViewUtils;
import douya.util.ViewUtils;

public class ProfileItemAdapter
        extends SimpleAdapter<CollectableItem, ProfileItemAdapter.ViewHolder> {

    public ProfileItemAdapter() {
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(ViewUtils.inflate(R.layout.profile_item_item, parent));
        ViewCompat.setBackground(holder.scrimView, DrawableUtils.makeScrimDrawable());
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CollectableItem item = getItem(position);
        float ratio = 1;
        switch (item.getType()) {
            case BOOK:
            case EVENT:
            case MOVIE:
            case TV:
                ratio = 2f / 3f;
                break;
        }
        holder.itemLayout.setRatio(ratio);
        final Context context = RecyclerViewUtils.getContext(holder);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                Intent intent = ItemActivities.makeIntent(item, context);
                if (intent != null) {
                    context.startActivity(intent);
                } else {
                    UriHandler.open(item.url, context);
                }
            }
        });
        ImageUtils.loadImage(holder.coverImage, item.cover.getLargeUrl());
        holder.titleText.setText(item.title);
        // FIXME: This won't work properly if items are changed.
        ViewUtils.setVisibleOrGone(holder.dividerSpace, position != getItemCount() - 1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item)
        public RatioFrameLayout itemLayout;
        @BindView(R.id.cover)
        public ImageView coverImage;
        @BindView(R.id.scrim)
        public View scrimView;
        @BindView(R.id.title)
        public TextView titleText;
        @BindView(R.id.divider)
        public Space dividerSpace;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
