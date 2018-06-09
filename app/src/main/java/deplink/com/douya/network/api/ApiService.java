package deplink.com.douya.network.api;

import android.media.Rating;
import android.text.TextUtils;

import java.util.List;

import deplink.com.douya.network.api.info.apiv2.User;
import deplink.com.douya.network.api.info.apiv2.UserList;
import deplink.com.douya.network.api.info.frodo.Broadcast;
import deplink.com.douya.network.api.info.frodo.BroadcastLikerList;
import deplink.com.douya.network.api.info.frodo.BroadcastList;
import deplink.com.douya.network.api.info.frodo.CelebrityList;
import deplink.com.douya.network.api.info.frodo.CollectableItem;
import deplink.com.douya.network.api.info.frodo.Comment;
import deplink.com.douya.network.api.info.frodo.CommentList;
import deplink.com.douya.network.api.info.frodo.CompleteCollectableItem;
import deplink.com.douya.network.api.info.frodo.DiaryList;
import deplink.com.douya.network.api.info.frodo.DoulistList;
import deplink.com.douya.network.api.info.frodo.DoumailThread;
import deplink.com.douya.network.api.info.frodo.DoumailThreadList;
import deplink.com.douya.network.api.info.frodo.ItemAwardList;
import deplink.com.douya.network.api.info.frodo.ItemCollection;
import deplink.com.douya.network.api.info.frodo.ItemCollectionList;
import deplink.com.douya.network.api.info.frodo.ItemForumTopicList;
import deplink.com.douya.network.api.info.frodo.NotificationCount;
import deplink.com.douya.network.api.info.frodo.NotificationList;
import deplink.com.douya.network.api.info.frodo.PhotoList;
import deplink.com.douya.network.api.info.frodo.ReviewList;
import deplink.com.douya.network.api.info.frodo.TimelineList;
import deplink.com.douya.network.api.info.frodo.UploadedImage;
import deplink.com.douya.network.api.info.frodo.UserItemList;
import deplink.com.douya.util.StringUtils;
import okhttp3.MultipartBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class ApiService {

    private static final ApiService sInstance = new ApiService();

    private FrodoService mFrodoService;
    private LifeStreamService mLifeStreamService;
    public static ApiService getInstance() {
        return sInstance;
    }
    public ApiRequest<User> getUser(String userIdOrUid) {
        if (TextUtils.isEmpty(userIdOrUid)) {
            userIdOrUid = "~me";
        }
        return mLifeStreamService.getUser(userIdOrUid);
    }
    public ApiRequest<Void> deleteBroadcastComment(long broadcastId, long commentId) {
        return mFrodoService.deleteBroadcastComment(broadcastId, commentId);
    }

    public ApiRequest<Comment> sendBroadcastComment(long broadcastId, String comment) {
        return mFrodoService.sendBroadcastComment(broadcastId, comment);
    }

    public ApiRequest<Void> deleteBroadcast(long broadcastId) {
        return mFrodoService.deleteBroadcast(broadcastId);
    }
    public ApiRequest<Broadcast> rebroadcastBroadcast(long broadcastId, String text) {
        return mFrodoService.rebroadcastBroadcast(broadcastId, text);
    }
    public ApiRequest<TimelineList> getTimelineList(String userIdOrUid, String topic, Long untilId,
                                                    Integer count) {
        return getTimelineList(userIdOrUid, topic, untilId, count, null, false);
    }
    public ApiRequest<Broadcast> likeBroadcast(long broadcastId, boolean like) {
        if (like) {
            return mFrodoService.likeBroadcast(broadcastId);
        } else {
            return mFrodoService.unlikeBroadcast(broadcastId);
        }
    }
    public ApiRequest<TimelineList> getTimelineList(String userIdOrUid, String topic, Long untilId,
                                                    Integer count, Long lastVisitedId,
                                                    boolean guestOnly) {
        String url;
        if (TextUtils.isEmpty(userIdOrUid) && TextUtils.isEmpty(topic)) {
            url = "status/home_timeline";
        } else if (TextUtils.isEmpty(topic)) {
            url = StringUtils.formatUs("status/user_timeline/%s", userIdOrUid);
        } else {
            url = "status/topic/timeline";
        }
       /* return mFrodoService.getTimelineList(url, untilId, count, lastVisitedId, topic, guestOnly ?
                1 : null);*/
       return null;
    }
    public interface LifeStreamService {

        @GET("lifestream/user/{userIdOrUid}")
        ApiRequest<User> getUser(@Path("userIdOrUid") String userIdOrUid);

        @POST("lifestream/user/{userIdOrUid}/follow")
        ApiRequest<User> follow(@Path("userIdOrUid") String userIdOrUid);

        @DELETE("lifestream/user/{userIdOrUid}/follow")
        ApiRequest<User> unfollow(@Path("userIdOrUid") String userIdOrUid);

        @GET("lifestream/user/{userIdOrUid}/followings")
        ApiRequest<UserList> getFollowingList(
                @Path("userIdOrUid") String userIdOrUid, @Query("start") Integer start,
                @Query("count") Integer count, @Query("tag") String tag);

        @GET("lifestream/user/{userIdOrUid}/followers")
        ApiRequest<UserList> getFollowerList(
                @Path("userIdOrUid") String userIdOrUid, @Query("start") Integer start,
                @Query("count") Integer count);

        @POST("lifestream/statuses")
        @FormUrlEncoded
        ApiRequest<deplink.com.douya.network.api.info.apiv2.Broadcast> sendBroadcast(
                @Field("text") String text, @Field("image_urls") String imageUrls,
                @Field("rec_title") String linkTitle, @Field("rec_url") String linkUrl);

        @GET
        ApiRequest<List<deplink.com.douya.network.api.info.apiv2.Broadcast>>
        getBroadcastList(@Url String url, @Query("until_id") Long untilId,
                         @Query("count") Integer count, @Query("q") String topic);

        @GET("lifestream/status/{broadcastId}")
        ApiRequest<deplink.com.douya.network.api.info.apiv2.Broadcast> getBroadcast(
                @Path("broadcastId") long broadcastId);

        @GET("lifestream/status/{broadcastId}/comments")
        ApiRequest<deplink.com.douya.network.api.info.apiv2.CommentList>
        getBroadcastCommentList(@Path("broadcastId") long broadcastId,
                                @Query("start") Integer start,
                                @Query("count") Integer count);

        @POST("lifestream/status/{broadcastId}/like")
        ApiRequest<deplink.com.douya.network.api.info.apiv2.Broadcast> likeBroadcast(
                @Path("broadcastId") long broadcastId);

        @DELETE("lifestream/status/{broadcastId}/like")
        ApiRequest<deplink.com.douya.network.api.info.apiv2.Broadcast> unlikeBroadcast(
                @Path("broadcastId") long broadcastId);

        @POST("lifestream/status/{broadcastId}/reshare")
        ApiRequest<deplink.com.douya.network.api.info.apiv2.Broadcast> rebroadcastBroadcast(
                @Path("broadcastId") long broadcastId);

        @DELETE("lifestream/status/{broadcastId}/reshare")
        ApiRequest<deplink.com.douya.network.api.info.apiv2.Broadcast>
        unrebroadcastBroadcast(@Path("broadcastId") long broadcastId);

        @GET("lifestream/status/{broadcastId}/likers")
        ApiRequest<List<deplink.com.douya.network.api.info.apiv2.SimpleUser>>
        getBroadcastLikerList(@Path("broadcastId") long broadcastId,
                              @Query("start") Integer start, @Query("count") Integer count);

        @GET("lifestream/status/{broadcastId}/resharers")
        ApiRequest<List<deplink.com.douya.network.api.info.apiv2.SimpleUser>>
        getBroadcastRebroadcasterList(@Path("broadcastId") long broadcastId,
                                      @Query("start") Integer start,
                                      @Query("count") Integer count);

        @DELETE("lifestream/status/{broadcastId}/comment/{commentId}")
        ApiRequest<Boolean> deleteBroadcastComment(@Path("broadcastId") long broadcastId,
                                                   @Path("commentId") long commentId);

        @POST("lifestream/status/{broadcastId}/comments")
        @FormUrlEncoded
        ApiRequest<deplink.com.douya.network.api.info.apiv2.Comment> sendBroadcastComment(
                @Path("broadcastId") long broadcastId, @Field("text") String comment);

        @DELETE("lifestream/status/{broadcastId}")
        ApiRequest<deplink.com.douya.network.api.info.apiv2.Broadcast> deleteBroadcast(
                @Path("broadcastId") long broadcastId);
    }

    public interface FrodoService {

        @GET("notification_chart")
        ApiRequest<NotificationCount> getNotificationCount();

        @GET("mine/notifications")
        ApiRequest<NotificationList> getNotificationList(@Query("start") Integer start,
                                                         @Query("count") Integer count);

        @GET("user/{userIdOrUid}/following")
        ApiRequest<UserList> getFollowingList(@Path("userIdOrUid") String userIdOrUid,
                                              @Query("start") Integer start,
                                              @Query("count") Integer count,
                                              @Query("contact_prior") String followersFirst);

        @GET("user/{userIdOrUid}/followers")
        ApiRequest<UserList> getFollowerList(@Path("userIdOrUid") String userIdOrUid,
                                             @Query("start") Integer start,
                                             @Query("count") Integer count);

        @GET
        ApiRequest<TimelineList> getTimelineList(@Url String url,
                                                 @Query("max_id") Long untilId,
                                                 @Query("count") Integer count,
                                                 @Query("last_visit_id") Long lastVisitedId,
                                                 @Query("name") String topic,
                                                 @Query("guest_only") Integer guestOnly);

        @POST("status/upload")
        @Multipart
        ApiRequest<UploadedImage> uploadBroadcastImage(@Part MultipartBody.Part part);

        @POST("status/create_status")
        @FormUrlEncoded
        ApiRequest<Broadcast> sendBroadcast(@Field("text") String text,
                                            @Field("image_urls") String imageUrls,
                                            @Field("rec_title") String linkTitle,
                                            @Field("rec_url") String linkUrl);

        @POST("https://api.douban.com/v2/lifestream/statuses")
        @FormUrlEncoded
        ApiRequest<deplink.com.douya.network.api.info.apiv2.Broadcast>
        sendBroadcastWithLifeStream(@Field("text") String text,
                                    @Field("image_urls") String imageUrls,
                                    @Field("rec_title") String linkTitle,
                                    @Field("rec_url") String linkUrl);

        @GET("status/{broadcastId}")
        ApiRequest<Broadcast> getBroadcast(@Path("broadcastId") long broadcastId);

        @GET("status/{broadcastId}/likers")
        ApiRequest<BroadcastLikerList> getBroadcastLikerList(@Path("broadcastId") long broadcastId,
                                                             @Query("start") Integer start,
                                                             @Query("count") Integer count);

        @GET("status/{broadcastId}/resharers_statuses")
        ApiRequest<BroadcastList> getBroadcastRebroadcastedBroadcastList(
                @Path("broadcastId") long broadcastId, @Query("start") Integer start,
                @Query("count") Integer count);

        @POST("status/{broadcastId}/like")
        ApiRequest<Broadcast> likeBroadcast(@Path("broadcastId") long broadcastId);

        @POST("status/{broadcastId}/unlike")
        ApiRequest<Broadcast> unlikeBroadcast(@Path("broadcastId") long broadcastId);

        @POST("status/{broadcastId}/reshare")
        @FormUrlEncoded
        ApiRequest<Broadcast> rebroadcastBroadcast(@Path("broadcastId") long broadcastId,
                                                   @Field("text") String text);

        @POST("status/{broadcastId}/report")
        @FormUrlEncoded
        ApiRequest<Void> reportBroadcast(@Path("broadcastId") long broadcastId,
                                         @Field("reason") int reason);

        @POST("status/{broadcastId}/delete")
        ApiRequest<Void> deleteBroadcast(@Path("broadcastId") long broadcastId);

        @GET("status/{broadcastId}/comments")
        ApiRequest<CommentList> getBroadcastCommentList(@Path("broadcastId") long broadcastId,
                                                        @Query("start") Integer start,
                                                        @Query("count") Integer count);

        @POST("status/{broadcastId}/create_comment")
        @FormUrlEncoded
        ApiRequest<Comment> sendBroadcastComment(@Path("broadcastId") long broadcastId,
                                                 @Field("text") String text);

        @POST("status/{broadcastId}/report_unfriendly_comment")
        @FormUrlEncoded
        ApiRequest<Comment> reportBroadcastComment(@Path("broadcastId") long broadcastId,
                                                   @Field("comment_id") long commentId);

        @POST("status/{broadcastId}/delete_comment")
        @FormUrlEncoded
        ApiRequest<Void> deleteBroadcastComment(@Path("broadcastId") long broadcastId,
                                                @Field("comment_id") long commentId);

        @GET("user/{userIdOrUid}/notes")
        ApiRequest<DiaryList> getDiaryList(@Path("userIdOrUid") String userIdOrUid,
                                           @Query("start") Integer start,
                                           @Query("count") Integer count);

        @GET("user/{userIdOrUid}/itemlist")
        ApiRequest<UserItemList> getUserItemList(@Path("userIdOrUid") String userIdOrUid);

        @GET("user/{userIdOrUid}/reviews")
        ApiRequest<ReviewList> getUserReviewList(@Path("userIdOrUid") String userIdOrUid,
                                                 @Query("start") Integer start,
                                                 @Query("count") Integer count);

        @GET("{itemType}/{itemId}")
        ApiRequest<CompleteCollectableItem> getItem(@Path("itemType") String itemType,
                                                    @Path("itemId") long itemId);

        @POST("{itemType}/{itemId}/{state}")
        @FormUrlEncoded
        ApiRequest<ItemCollection> collectItem(@Path("itemType") String itemType,
                                               @Path("itemId") long itemId,
                                               @Path("state") String state,
                                               @Field("rating") int rating,
                                               @Field("tags") String tags,
                                               @Field("comment") String comment,
                                               @Field("platform") List<Long> gamePlatformIds,
                                               @Field("sync_douban") Integer shareToBroadcast,
                                               @Field("sync_weibo") Integer shareToWeibo,
                                               @Field("sync_wechat_timeline") Integer shareToWeChatMoments);

        @POST("{itemType}/{itemId}/unmark")
        ApiRequest<ItemCollection> uncollectItem(@Path("itemType") String itemType,
                                                 @Path("itemId") long itemId);

        @GET("{itemType}/{itemId}/rating")
        ApiRequest<Rating> getItemRating(@Path("itemType") String itemType,
                                         @Path("itemId") long itemId);

        @GET("{itemType}/{itemId}/photos")
        ApiRequest<PhotoList> getItemPhotoList(@Path("itemType") String itemType,
                                               @Path("itemId") long itemId,
                                               @Query("start") Integer start,
                                               @Query("count") Integer count);

        @GET("{itemType}/{itemId}/celebrities")
        ApiRequest<CelebrityList> getItemCelebrityList(@Path("itemType") String itemType,
                                                       @Path("itemId") long itemId);

        @GET("{itemType}/{itemId}/awards")
        ApiRequest<ItemAwardList> getItemAwardList(@Path("itemType") String itemType,
                                                   @Path("itemId") long itemId,
                                                   @Query("start") Integer start,
                                                   @Query("count") Integer count);

        @GET("{itemType}/{itemId}/interests")
        ApiRequest<ItemCollectionList> getItemCollectionList(
                @Path("itemType") String itemType, @Path("itemId") long itemId,
                @Query("following") Integer followingsFirst, @Query("start") Integer start,
                @Query("count") Integer count);

        @POST("{itemType}/{itemId}/vote_interest")
        @FormUrlEncoded
        ApiRequest<ItemCollection> voteItemCollection(@Path("itemType") String itemType,
                                                      @Path("itemId") long itemId,
                                                      @Field("interest_id") long itemCollectionId);

        @GET("{itemType}/{itemId}/forum_topics")
        ApiRequest<ItemForumTopicList> getItemForumTopicList(@Path("itemType") String itemType,
                                                             @Path("itemId") long itemId,
                                                             @Query("episode") Integer episode,
                                                             @Query("start") Integer start,
                                                             @Query("count") Integer count);

        @GET("{itemType}/{itemId}/reviews")
        ApiRequest<ReviewList> getItemReviewList(@Path("itemType") String itemType,
                                                 @Path("itemId") long itemId,
                                                 @Query("rtype") String reviewType,
                                                 @Query("start") Integer start,
                                                 @Query("count") Integer count);

        @GET("{itemType}/{itemId}/recommendations")
        ApiRequest<List<CollectableItem>> getItemRecommendationList(
                @Path("itemType") String itemType, @Path("itemId") long itemId,
                @Query("count") Integer count);

        @GET("{itemType}/{itemId}/related_doulists")
        ApiRequest<DoulistList> getItemRelatedDoulistList(@Path("itemType") String itemType,
                                                          @Path("itemId") long itemId,
                                                          @Query("start") Integer start,
                                                          @Query("count") Integer count);

        @GET("chat_list")
        ApiRequest<DoumailThreadList> getDoumailThreadList(@Query("start") Integer start,
                                                           @Query("count") Integer count);

        @GET("user/{userId}/chat")
        ApiRequest<DoumailThread> getDoumailThread(@Path("userId") long userId);
    }
}
