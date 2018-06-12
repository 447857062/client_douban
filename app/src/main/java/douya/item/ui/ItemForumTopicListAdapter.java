/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.douya.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import douya.link.UriHandler;
import douya.network.api.info.frodo.SimpleItemForumTopic;
import douya.ui.SimpleAdapter;
import douya.ui.TimeTextView;
import douya.util.ImageUtils;
import douya.util.ViewUtils;

public class ItemForumTopicListAdapter
        extends SimpleAdapter<SimpleItemForumTopic, ItemForumTopicListAdapter.ViewHolder> {

    public ItemForumTopicListAdapter() {
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(R.layout.item_forum_topic_item, parent));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleItemForumTopic itemForumTopic = getItem(position);
        ImageUtils.loadAvatar(holder.avatarImage, itemForumTopic.author.avatar);
        holder.titleText.setText(itemForumTopic.title);
        holder.nameText.setText(itemForumTopic.author.name);
        holder.likeCountText.setText(holder.likeCountText.getContext().getString(
                R.string.item_forum_topic_like_count_format, itemForumTopic.likeCount));
        holder.commentCountText.setText(holder.commentCountText.getContext().getString(
                R.string.item_forum_topic_comment_count_format, itemForumTopic.commentCount));
        holder.updatedAtText.setDoubanTime(itemForumTopic.updatedAt);
        holder.itemView.setOnClickListener(view -> {
            // TODO
            UriHandler.open(itemForumTopic.url, view.getContext());
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        public ImageView avatarImage;
        @BindView(R.id.title)
        public TextView titleText;
        @BindView(R.id.name)
        public TextView nameText;
        @BindView(R.id.like_count)
        public TextView likeCountText;
        @BindView(R.id.comment_count)
        public TextView commentCountText;
        @BindView(R.id.updated_at)
        public TimeTextView updatedAtText;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
