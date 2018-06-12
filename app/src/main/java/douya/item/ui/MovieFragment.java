/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.ui;

import android.content.Context;

import com.douya.R;

import java.util.List;

import butterknife.BindDimen;
import douya.gallery.ui.GalleryActivity;
import douya.item.content.BaseItemFragmentResource;
import douya.item.content.ConfirmUncollectItemDialogFragment;
import douya.item.content.MovieFragmentResource;
import douya.item.content.UncollectItemManager;
import douya.link.UriHandler;
import douya.network.api.info.frodo.CollectableItem;
import douya.network.api.info.frodo.Doulist;
import douya.network.api.info.frodo.ItemAwardItem;
import douya.network.api.info.frodo.Movie;
import douya.network.api.info.frodo.Photo;
import douya.network.api.info.frodo.Rating;
import douya.network.api.info.frodo.SimpleCelebrity;
import douya.network.api.info.frodo.SimpleItemCollection;
import douya.network.api.info.frodo.SimpleItemForumTopic;
import douya.network.api.info.frodo.SimpleMovie;
import douya.network.api.info.frodo.SimpleReview;
import douya.ui.BarrierAdapter;
import douya.ui.CopyTextDialogFragment;
import douya.util.DoubanUtils;
import douya.util.ImageUtils;
import douya.util.ViewUtils;

public class MovieFragment extends BaseItemFragment<SimpleMovie, Movie>
        implements MovieFragmentResource.Listener, MovieDataAdapter.Listener,
        ConfirmUncollectItemDialogFragment.Listener {

    @BindDimen(R.dimen.item_cover_vertical_margin_negative)
    int mContentListPaddingTopExtra;

    private MovieAdapter mAdapter;

    private boolean mBackdropBound;
    private boolean mExcludeFirstPhoto;

    public static MovieFragment newInstance(long movieId, SimpleMovie simpleMovie, Movie movie) {
        //noinspection deprecation
        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(movieId, simpleMovie, movie);
        return fragment;
    }

    /**
     * @deprecated Use {@link #newInstance(long, SimpleMovie, Movie)} instead.
     */
    public MovieFragment() {}

    @Override
    protected BaseItemFragmentResource<SimpleMovie, Movie> onAttachResource(long itemId,
                                                                            SimpleMovie simpleItem,
                                                                            Movie item) {
        return MovieFragmentResource.attachTo(itemId, simpleItem, item, this);
    }

    @Override
    protected BarrierAdapter onCreateAdapter() {
        mAdapter = new MovieAdapter(this);
        return mAdapter;
    }

    @Override
    protected int getContentListPaddingTopExtra() {
        return mContentListPaddingTopExtra;
    }

    @Override
    public void onChanged(int requestCode, Movie newMovie, Rating newRating,
                          List<Photo> newPhotoList, List<SimpleCelebrity> newCelebrityList,
                          List<ItemAwardItem> newAwardList,
                          List<SimpleItemCollection> newItemCollectionList,
                          List<SimpleReview> newReviewList,
                          List<SimpleItemForumTopic> newForumTopicList,
                          List<CollectableItem> newRecommendationList,
                          List<Doulist> newRelatedDoulistList) {
        update(newMovie, newRating, newPhotoList, newCelebrityList, newAwardList,
                newItemCollectionList, newReviewList, newForumTopicList, newRecommendationList,
                newRelatedDoulistList);
    }

    private void update(Movie movie, Rating rating, List<Photo> photoList,
                        List<SimpleCelebrity> celebrityList, List<ItemAwardItem> awardList,
                        List<SimpleItemCollection> itemCollectionList,
                        List<SimpleReview> reviewList, List<SimpleItemForumTopic> forumTopicList,
                        List<CollectableItem> recommendationList,
                        List<Doulist> relatedDoulistList) {

        if (movie != null) {
            super.updateWithSimpleItem(movie);
        }

        if (movie == null || photoList == null) {
            return;
        }

        if (!mBackdropBound) {
            boolean hasTrailer = movie.trailer != null;
            mExcludeFirstPhoto = false;
            String backdropUrl = null;
            if (hasTrailer) {
                backdropUrl = movie.trailer.coverUrl;
                mBackdropLayout.setOnClickListener(view -> {
                    // TODO
                    UriHandler.open(movie.trailer.videoUrl, view.getContext());
                });
            } else if (!photoList.isEmpty()) {
                backdropUrl = photoList.get(0).getLargeUrl();
                mExcludeFirstPhoto = true;
                mBackdropLayout.setOnClickListener(view -> {
                    // TODO
                    Context context = view.getContext();
                    context.startActivity(GalleryActivity.makeImageListIntent(photoList, 0, context));
                });
            } else if (movie.poster != null) {
                backdropUrl = movie.poster.getLargeUrl();
                mBackdropLayout.setOnClickListener(view -> {
                    // TODO
                    Context context = view.getContext();
                    context.startActivity(GalleryActivity.makeIntent(movie.poster, context));
                });
            } else if (movie.cover != null) {
                backdropUrl = movie.cover.getLargeUrl();
                mBackdropLayout.setOnClickListener(view -> {
                    // TODO
                    Context context = view.getContext();
                    context.startActivity(GalleryActivity.makeIntent(movie.cover, context));
                });
            }
            if (backdropUrl != null) {
                ImageUtils.loadItemBackdropAndFadeIn(mBackdropImage, backdropUrl,
                        hasTrailer ? mBackdropPlayImage : null);
            } else {
                mBackdropImage.setBackgroundColor(movie.getThemeColor());
                ViewUtils.fadeIn(mBackdropImage);
            }
            mBackdropBound = true;
        }

        mAdapter.setData(new MovieDataAdapter.Data(movie, rating, photoList, mExcludeFirstPhoto,
                celebrityList, awardList, itemCollectionList, reviewList, forumTopicList,
                recommendationList, relatedDoulistList));
        if (mAdapter.getItemCount() > 0) {
            mContentStateLayout.setLoaded(true);
        }
    }

    @Override
    protected String makeItemUrl(long itemId) {
        return DoubanUtils.makeMovieUrl(itemId);
    }

    @Override
    public void onItemCollectionChanged(int requestCode) {
        mAdapter.notifyItemCollectionChanged();
    }

    @Override
    public void onItemCollectionListItemChanged(int requestCode, int position,
                                                SimpleItemCollection newItemCollection) {
        mAdapter.setItemCollectionListItem(position, newItemCollection);
    }

    @Override
    public void onItemCollectionListItemWriteStarted(int requestCode, int position) {
        mAdapter.notifyItemCollectionListItemChanged(position);
    }

    @Override
    public void onItemCollectionListItemWriteFinished(int requestCode, int position) {
        mAdapter.notifyItemCollectionListItemChanged(position);
    }

    @Override
    public void onUncollectItem(Movie movie) {
        ConfirmUncollectItemDialogFragment.show(this);
    }

    @Override
    public void uncollect() {
        if (!mResource.hasItem()) {
            return;
        }
        Movie movie = mResource.getItem();
        UncollectItemManager.getInstance().write(movie.getType(), movie.id, getActivity());
    }

    @Override
    public void copyText(String text) {
        CopyTextDialogFragment.show(text, this);
    }
}
