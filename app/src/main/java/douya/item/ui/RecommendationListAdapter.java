/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.douya.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import douya.link.UriHandler;
import douya.network.api.info.frodo.CollectableItem;
import douya.ui.RatioImageView;
import douya.ui.SimpleAdapter;
import douya.util.ImageUtils;
import douya.util.ViewUtils;

public class RecommendationListAdapter
        extends SimpleAdapter<CollectableItem, RecommendationListAdapter.ViewHolder> {

    public RecommendationListAdapter() {
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(R.layout.item_recommendation_item,
                parent));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollectableItem item = getItem(position);
        float ratio = 1;
        switch (item.getType()) {
            case BOOK:
            case EVENT:
            case GAME:
            case MOVIE:
            case TV:
                ratio = 2f / 3f;
                break;
        }
        holder.coverImage.setRatio(ratio);
        ImageUtils.loadImage(holder.coverImage, item.cover);
        holder.titleText.setText(item.title);
        boolean hasRating = item.rating.hasRating();
        if (hasRating) {
            holder.ratingText.setText(item.rating.getRatingString(holder.ratingText.getContext()));
        } else {
            holder.ratingText.setText(item.getRatingUnavailableReason(
                    holder.ratingText.getContext()));
        }
        ViewUtils.setVisibleOrGone(holder.ratingStarText, hasRating);
        holder.itemView.setOnClickListener(view -> {
            // TODO
            UriHandler.open(item.url, view.getContext());
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cover)
        public RatioImageView coverImage;
        @BindView(R.id.title)
        public TextView titleText;
        @BindView(R.id.rating)
        public TextView ratingText;
        @BindView(R.id.rating_star)
        public TextView ratingStarText;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
