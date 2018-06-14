/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.profile.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.douya.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import douya.broadcast.ui.BroadcastActivity;
import douya.broadcast.ui.BroadcastListActivity;
import douya.network.api.info.apiv2.User;
import douya.network.api.info.frodo.Broadcast;
import douya.ui.FriendlyCardView;
import douya.ui.SizedImageItem;
import douya.ui.TimeTextView;
import douya.util.ImageUtils;
import douya.util.ViewUtils;

public class ProfileBroadcastsLayout extends FriendlyCardView {

    private static final int BROADCAST_COUNT_MAX = 3;

    @BindView(R.id.title)
    TextView mTitleText;
    @BindView(R.id.broadcast_list)
    LinearLayout mBroadcastList;
    @BindView(R.id.empty)
    View mEmptyView;
    @BindView(R.id.view_more)
    TextView mViewMoreText;

    public ProfileBroadcastsLayout(Context context) {
        super(context);

        init();
    }

    public ProfileBroadcastsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ProfileBroadcastsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        ViewUtils.inflateInto(R.layout.profile_broadcasts_layout, this);
        ButterKnife.bind(this);
    }

    public void bind(User user, List<Broadcast> broadcastList) {

        Context context = getContext();
        View.OnClickListener viewMoreListener = view -> context.startActivity(
                BroadcastListActivity.makeIntent(user, context));
        mTitleText.setOnClickListener(viewMoreListener);
        mViewMoreText.setOnClickListener(viewMoreListener);

        int i = 0;
        for (Broadcast broadcast : broadcastList) {

            if (i >= BROADCAST_COUNT_MAX) {
                break;
            }

            if (broadcast.rebroadcastedBroadcast != null) {
                continue;
            }

            if (i >= mBroadcastList.getChildCount()) {
                ViewUtils.inflateInto(R.layout.profile_broadcast_item, mBroadcastList);
            }
            View broadcastLayout = mBroadcastList.getChildAt(i);
            broadcastLayout.setVisibility(VISIBLE);
            BroadcastLayoutHolder holder = (BroadcastLayoutHolder) broadcastLayout.getTag();
            if (holder == null) {
                holder = new BroadcastLayoutHolder(broadcastLayout);
                broadcastLayout.setTag(holder);
                ViewUtils.setTextViewLinkClickable(holder.textText);
            }

            // HACK: Should not change on rebind.
            if (holder.boundBroadcastId != broadcast.id) {
                SizedImageItem image = null;
                if (broadcast.attachment != null) {
                    image = broadcast.attachment.image;
                }
                if (image == null) {
                    List<? extends SizedImageItem> images = broadcast.attachment != null
                            && broadcast.attachment.imageBlock != null ?
                            broadcast.attachment.imageBlock.images : broadcast.images;
                    if (images.size() > 0){
                        image = images.get(0);
                    }
                }
                if (image != null) {
                    holder.image.setVisibility(VISIBLE);
                    ImageUtils.loadImage(holder.image, image);
                } else {
                    holder.image.setVisibility(GONE);
                }
                CharSequence text = broadcast.getTextWithEntities(context);
                if (TextUtils.isEmpty(text) && broadcast.attachment != null) {
                    text = broadcast.attachment.title;
                }
                holder.textText.setText(text);
                holder.timeText.setDoubanTime(broadcast.createdAt);
                holder.actionText.setText(broadcast.action);
                broadcastLayout.setOnClickListener(view -> context.startActivity(
                        BroadcastActivity.makeIntent(broadcast, context)));
                holder.boundBroadcastId = broadcast.id;
            }

            ++i;
        }

        ViewUtils.setVisibleOrGone(mBroadcastList, i != 0);
        ViewUtils.setVisibleOrGone(mEmptyView, i == 0);

        if (user.broadcastCount > i) {
            mViewMoreText.setText(context.getString(R.string.view_more_with_count_format,
                    user.broadcastCount));
        } else {
            mViewMoreText.setVisibility(GONE);
        }

        for (int count = mBroadcastList.getChildCount(); i < count; ++i) {
            mBroadcastList.getChildAt(i).setVisibility(GONE);
        }
    }

    static class BroadcastLayoutHolder {

        @BindView(R.id.image)
        public ImageView image;
        @BindView(R.id.text)
        public TextView textText;
        @BindView(R.id.time)
        public TimeTextView timeText;
        @BindView(R.id.action)
        public TextView actionText;

        public long boundBroadcastId;

        public BroadcastLayoutHolder(View broadcastLayout) {
            ButterKnife.bind(this, broadcastLayout);
        }
    }
}
