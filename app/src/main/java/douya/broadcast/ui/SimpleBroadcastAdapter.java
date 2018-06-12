/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.broadcast.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.douya.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import douya.network.api.info.frodo.Broadcast;
import douya.profile.ui.ProfileActivity;
import douya.ui.SimpleAdapter;
import douya.ui.TimeTextView;
import douya.util.ImageUtils;
import douya.util.RecyclerViewUtils;
import douya.util.ViewUtils;

public class SimpleBroadcastAdapter
        extends SimpleAdapter<Broadcast, SimpleBroadcastAdapter.ViewHolder> {

    public SimpleBroadcastAdapter() {
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        //noinspection deprecation
        return getItem(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(ViewUtils.inflate(R.layout.simple_broadcast_item,
                parent));
        ViewUtils.setTextViewLinkClickable(holder.textText);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Context context = RecyclerViewUtils.getContext(holder);
        Broadcast broadcast = getItem(position);
        holder.itemView.setOnClickListener(view -> {
            // rebroadcastedBroadcast can always be empty.
            //if (TextUtils.isEmpty(broadcast.isSimpleRebroadcast()) {
            if (TextUtils.isEmpty(broadcast.text)) {
                return;
            }
            // TODO: Can we pass the broadcast? But rebroadcastedBroadcast and parentBroadcast will
            // be missing.
            context.startActivity(BroadcastActivity.makeIntent(broadcast.id, context));
        });
        ImageUtils.loadAvatar(holder.avatarImage, broadcast.author.avatar);
        holder.avatarImage.setOnClickListener(view -> context.startActivity(
                ProfileActivity.makeIntent(broadcast.author, context)));
        holder.nameText.setText(broadcast.author.name);
        holder.timeText.setDoubanTime(broadcast.createdAt);
        holder.textText.setText(broadcast.getRebroadcastText(context));
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
