package deplink.com.douya.network.api.info.frodo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import deplink.com.douya.R;
import deplink.com.douya.ui.IconSpan;
import deplink.com.douya.ui.SpaceSpan;
import deplink.com.douya.ui.UriSpan;
import deplink.com.douya.util.DoubanUtils;
import deplink.com.douya.util.GsonHelper;
import deplink.com.douya.util.ViewUtils;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class Broadcast implements Parcelable {
    public static final int MAX_TEXT_LENGTH = 140;

    public static final int MAX_IMAGES_SIZE = 9;
    @SerializedName("activity")
    public String action;

    @SerializedName("ad_info")
    public BroadcastAdInfo adInfo;

    public SimpleUser author;

    @SerializedName("card")
    public BroadcastAttachment attachment;

    @SerializedName("comments_count")
    public int commentCount;

    @SerializedName("create_time")
    public String createdAt;

    @SerializedName("deleted")
    public boolean isDeleted;

    public ArrayList<TextEntity> entities = new ArrayList<>();

    @SerializedName("forbid_reshare_and_comment")
    public boolean isRebroadcastAndCommentForbidden;

    public long id;

    public ArrayList<SizedImage> images = new ArrayList<>();

    @SerializedName("is_status_ad")
    public boolean isStatusAd;

    @SerializedName("is_subscription")
    public boolean isSubscription;

    @SerializedName("like_count")
    public int likeCount;

    @SerializedName("liked")
    public boolean isLiked;

    /**
     * @deprecated Use {@link #getParentBroadcastId()} instead.
     */
    @SerializedName("parent_id")
    public Long parentBroadcastId;

    @SerializedName("parent_status")
    public Broadcast parentBroadcast;

    @SerializedName("reshare_id")
    public String rebroadcastId;

    @SerializedName("reshared_status")
    public Broadcast rebroadcastedBroadcast;

    @SerializedName("reshares_count")
    public int rebroadcastCount;

    @SerializedName("sharing_url")
    public String shareUrl;

    @SerializedName("subscription_text")
    public String subscriptionText;

    public String text;

    public String uri;

    public long getEffectiveBroadcastId() {
        return getEffectiveBroadcast().id;
    }
    public Long getParentBroadcastId() {
        //noinspection deprecation
        return parentBroadcast != null ? (Long) parentBroadcast.id : parentBroadcastId;
    }
    // The broadcast for user actions.
    public Broadcast getEffectiveBroadcast() {
        if (isSimpleRebroadcast()) {
            if (parentBroadcast != null) {
                // parentBroadcast can't be a simple rebroadcast.
                return parentBroadcast;
            } else {
                return rebroadcastedBroadcast;
            }
        } else {
            return this;
        }
    }
    public boolean isSimpleRebroadcastByOneself() {
        return isSimpleRebroadcast() && isAuthorOneself();
    }
    public String getRebroadcastedBy(Context context) {
        return context.getString(
                R.string.broadcast_rebroadcasted_by_format, author.name);
    }
    public static String makeTransitionName(long id) {
        return "broadcast-" + id;
    }

    public String makeTransitionName() {
        return makeTransitionName(id);
    }
    public boolean isAuthorOneself() {
        return author != null && author.isOneself();
    }
    public String getLikeCountString() {
        return likeCount == 0 ? null : String.valueOf(likeCount);
    }

    public String getRebroadcastCountString() {
        return rebroadcastCount == 0 ? null : String.valueOf(rebroadcastCount);
    }
    public String getCommentCountString() {
        return commentCount == 0 ? null : String.valueOf(commentCount);
    }
    public boolean isSimpleRebroadcast() {
        return rebroadcastedBroadcast != null && TextUtils.isEmpty(text);
    }
    public static class Deserializer implements JsonDeserializer<Broadcast> {

        @Override
        public Broadcast deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException {
            Broadcast broadcast = GsonHelper.GSON.fromJson(json, typeOfT);
            broadcast.fix();
            return broadcast;
        }
    }
    private void fixSelf() {
        fixAction();
    }

    public void fix() {
        fixSelf();
        if (parentBroadcast != null) {
            parentBroadcast.fixSelf();
            //noinspection deprecation
            if (parentBroadcast.parentBroadcastId != null
                    && rebroadcastedBroadcast != null
                    && parentBroadcast.parentBroadcastId
                    == rebroadcastedBroadcast.id) {
                // Important for rebroadcast text.
                //noinspection deprecation
                parentBroadcast.parentBroadcastId = null;
            }
        }
        if (rebroadcastedBroadcast != null) {
            rebroadcastedBroadcast.fixSelf();
        }
    }
    private void fixAction() {
        if (TextUtils.isEmpty(action)) {
            action = "说";
        } else {
            action = action
                    .replaceFirst("^分享", "推荐")
                    .replaceFirst("^推荐链接$", "推荐网页")
                    .replaceFirst("^(想.|在.|.过)这.+", "$1")
                    .replaceFirst("^写了日记$", "写了新日记");
        }
    }
    public CharSequence getTextWithEntities(Context context) {
        return getTextWithEntities(true, context);
    }

    public CharSequence getTextWithEntitiesAsParent(Context context) {
        return appendParentText(new SpannableStringBuilder(), this, null, context);
    }
    public CharSequence getTextWithEntities(boolean appendParent, Context context) {
        CharSequence textWithEntities = TextEntity.applyEntities(text, entities);
        if (appendParent) {
            if (textWithEntities == null) {
                textWithEntities = "";
            }
            //noinspection deprecation
            textWithEntities = appendParentText(textWithEntities, parentBroadcast,
                    parentBroadcastId, context);
        }
        return textWithEntities;
    }
    private static CharSequence appendParentText(CharSequence text, Broadcast parentBroadcast,
                                                 Long parentBroadcastId, Context context) {

        if (parentBroadcast == null && parentBroadcastId == null) {
            return text;
        }

        SpannableStringBuilder builder = SpannableStringBuilder.valueOf(text);

        if (parentBroadcast != null) {

            int parentSpaceStartIndex = builder.length();
            builder.append(" ");
            int parentSpaceEndIndex = builder.length();
            // HACK: For the case when rebroadcasting a broadcast.
            float spaceWidthEm = parentSpaceStartIndex > 0 ? 0.5f : -1f / 12;
            builder.setSpan(new SpaceSpan(spaceWidthEm), parentSpaceStartIndex, parentSpaceEndIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            int parentIconStartIndex = builder.length();
            builder.append(" ");
            int parentIconEndIndex = builder.length();
            Drawable icon = AppCompatResources.getDrawable(context,
                    R.drawable.rebroadcast_icon_white_18dp);
            icon = DrawableCompat.wrap(icon);
            DrawableCompat.setTint(icon, ViewUtils.getColorFromAttrRes(parentBroadcast.isDeleted ?
                            android.R.attr.textColorSecondary : android.R.attr.textColorLink, 0,
                    context));
            builder.setSpan(new IconSpan(icon), parentIconStartIndex, parentIconEndIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (parentBroadcast.isDeleted) {

                builder.append(context.getString(
                        R.string.broadcast_rebroadcasted_broadcast_text_rebroadcast_deleted));
                int parentDeletedTextEndIndex = builder.length();
                builder.setSpan(new ForegroundColorSpan(ViewUtils.getColorFromAttrRes(
                        android.R.attr.textColorSecondary, 0, context)), parentSpaceStartIndex,
                        parentDeletedTextEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                parentBroadcastId = null;

            } else {

                builder.append(context.getString(
                        R.string.broadcast_rebroadcasted_broadcast_text_rebroadcaster_format,
                        parentBroadcast.author.name));
                int parentNameEndIndex = builder.length();
                builder.setSpan(new UriSpan(parentBroadcast.uri), parentSpaceStartIndex,
                        parentNameEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                builder.append(parentBroadcast.getTextWithEntities(false, context));

                parentBroadcastId = parentBroadcast.getParentBroadcastId();
            }
        }

        if (parentBroadcastId != null) {

            int parentSpaceStartIndex = builder.length();
            if (parentSpaceStartIndex > 0) {
                builder.append(" ");
                int parentSpaceEndIndex = builder.length();
                builder.setSpan(new SpaceSpan(0.5f), parentSpaceStartIndex, parentSpaceEndIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            builder.append(context.getString(
                    R.string.broadcast_rebroadcasted_broadcast_text_more_rebroadcast));
            int parentMoreEndIndex = builder.length();
            builder.setSpan(new UriSpan(DoubanUtils.makeBroadcastUri(parentBroadcastId)),
                    parentSpaceStartIndex, parentMoreEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }
    public static final Creator<Broadcast> CREATOR = new Creator<Broadcast>() {
        @Override
        public Broadcast createFromParcel(Parcel source) {
            return new Broadcast(source);
        }
        @Override
        public Broadcast[] newArray(int size) {
            return new Broadcast[size];
        }
    };

    public Broadcast() {}

    protected Broadcast(Parcel in) {
        action = in.readString();
        adInfo = in.readParcelable(BroadcastAdInfo.class.getClassLoader());
        author = in.readParcelable(SimpleUser.class.getClassLoader());
        attachment = in.readParcelable(BroadcastAttachment.class.getClassLoader());
        commentCount = in.readInt();
        createdAt = in.readString();
        isDeleted = in.readByte() != 0;
        entities = in.createTypedArrayList(TextEntity.CREATOR);
        isRebroadcastAndCommentForbidden = in.readByte() != 0;
        id = in.readLong();
        images = in.createTypedArrayList(SizedImage.CREATOR);
        isStatusAd = in.readByte() != 0;
        isSubscription = in.readByte() != 0;
        likeCount = in.readInt();
        isLiked = in.readByte() != 0;
        //noinspection deprecation
        parentBroadcastId = (Long) in.readSerializable();
        parentBroadcast = in.readParcelable(Broadcast.class.getClassLoader());
        rebroadcastId = in.readString();
        rebroadcastedBroadcast = in.readParcelable(Broadcast.class.getClassLoader());
        rebroadcastCount = in.readInt();
        shareUrl = in.readString();
        subscriptionText = in.readString();
        text = in.readString();
        uri = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(action);
        dest.writeParcelable(adInfo, flags);
        dest.writeParcelable(author, flags);
        dest.writeParcelable(attachment, flags);
        dest.writeInt(commentCount);
        dest.writeString(createdAt);
        dest.writeByte(isDeleted ? (byte) 1 : (byte) 0);
        dest.writeTypedList(entities);
        dest.writeByte(isRebroadcastAndCommentForbidden ? (byte) 1 : (byte) 0);
        dest.writeLong(id);
        dest.writeTypedList(images);
        dest.writeByte(isStatusAd ? (byte) 1 : (byte) 0);
        dest.writeByte(isSubscription ? (byte) 1 : (byte) 0);
        dest.writeInt(likeCount);
        dest.writeByte(isLiked ? (byte) 1 : (byte) 0);
        //noinspection deprecation
        dest.writeSerializable(parentBroadcastId);
        dest.writeParcelable(parentBroadcast, flags);
        dest.writeString(rebroadcastId);
        dest.writeParcelable(rebroadcastedBroadcast, flags);
        dest.writeInt(rebroadcastCount);
        dest.writeString(shareUrl);
        dest.writeString(subscriptionText);
        dest.writeString(text);
        dest.writeString(uri);
    }
}
