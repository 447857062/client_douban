/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.broadcast.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.douya.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import douya.network.api.info.frodo.Comment;
import douya.profile.ui.ProfileActivity;
import douya.ui.ClickableSimpleAdapter;
import douya.ui.TimeTextView;
import douya.util.ImageUtils;
import douya.util.RecyclerViewUtils;
import douya.util.ViewUtils;

public class CommentAdapter extends ClickableSimpleAdapter<Comment, CommentAdapter.ViewHolder> {

    public CommentAdapter(List<Comment> commentList,
                          OnItemClickListener<Comment> onItemClickListener) {
        super(commentList, onItemClickListener);

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(ViewUtils.inflate(R.layout.broadcast_comment_item,
                parent));
        ViewUtils.setTextViewLinkClickable(holder.textText);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = getItem(position);
        ImageUtils.loadAvatar(holder.avatarImage, comment.author.avatar);
        Context context = RecyclerViewUtils.getContext(holder);
        holder.avatarImage.setOnClickListener(view -> context.startActivity(
                ProfileActivity.makeIntent(comment.author, context)));
        holder.nameText.setText(comment.author.name);
        holder.timeText.setDoubanTime(comment.createdAt);
        holder.textText.setText(comment.getTextWithEntities());
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.avatarImage.setImageDrawable(null);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        public ImageView avatarImage;
        @BindView(R.id.name)
        public TextView nameText;
        @BindView(R.id.time)
        public TimeTextView timeText;
        @BindView(R.id.text)
        public TextView textText;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
