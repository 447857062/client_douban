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
import douya.item.content.GameFragmentResource;
import douya.item.content.UncollectItemManager;
import douya.network.api.info.frodo.CollectableItem;
import douya.network.api.info.frodo.Doulist;
import douya.network.api.info.frodo.Game;
import douya.network.api.info.frodo.Photo;
import douya.network.api.info.frodo.Rating;
import douya.network.api.info.frodo.SimpleGame;
import douya.network.api.info.frodo.SimpleItemCollection;
import douya.network.api.info.frodo.SimpleItemForumTopic;
import douya.network.api.info.frodo.SimpleReview;
import douya.ui.BarrierAdapter;
import douya.ui.CopyTextDialogFragment;
import douya.util.DoubanUtils;
import douya.util.ImageUtils;
import douya.util.ViewUtils;

public class GameFragment extends BaseItemFragment<SimpleGame, Game>
        implements GameFragmentResource.Listener, GameDataAdapter.Listener,
        ConfirmUncollectItemDialogFragment.Listener {

    @BindDimen(R.dimen.item_cover_vertical_margin_negative)
    int mContentListPaddingTopExtra;

    private GameAdapter mAdapter;

    private boolean mBackdropBound;
    private boolean mExcludeFirstPhoto;

    public static GameFragment newInstance(long gameId, SimpleGame simpleGame, Game game) {
        //noinspection deprecation
        GameFragment fragment = new GameFragment();
        fragment.setArguments(gameId, simpleGame, game);
        return fragment;
    }

    /**
     * @deprecated Use {@link #newInstance(long, SimpleGame, Game)} instead.
     */
    public GameFragment() {}

    @Override
    protected BaseItemFragmentResource<SimpleGame, Game> onAttachResource(long itemId,
                                                                          SimpleGame simpleItem,
                                                                          Game item) {
        return GameFragmentResource.attachTo(itemId, simpleItem, item, this);
    }

    @Override
    protected BarrierAdapter onCreateAdapter() {
        mAdapter = new GameAdapter(this);
        return mAdapter;
    }

    @Override
    protected int getContentListPaddingTopExtra() {
        return mContentListPaddingTopExtra;
    }

    @Override
    public void onChanged(int requestCode, Game newGame, Rating newRating,
                          List<Photo> newPhotoList,
                          List<SimpleItemCollection> newItemCollectionList,
                          List<SimpleReview> newGameGuideList,
                          List<SimpleReview> newReviewList,
                          List<SimpleItemForumTopic> newForumTopicList,
                          List<CollectableItem> newRecommendationList,
                          List<Doulist> newRelatedDoulistList) {
        update(newGame, newRating, newPhotoList, newItemCollectionList, newGameGuideList,
                newReviewList, newForumTopicList, newRecommendationList, newRelatedDoulistList);
    }

    private void update(Game game, Rating rating, List<Photo> photoList,
                        List<SimpleItemCollection> itemCollectionList,
                        List<SimpleReview> gameGuideList, List<SimpleReview> reviewList,
                        List<SimpleItemForumTopic> forumTopicList,
                        List<CollectableItem> recommendationList,
                        List<Doulist> relatedDoulistList) {

        if (game != null) {
            super.updateWithSimpleItem(game);
        }

        if (game == null || photoList == null) {
            return;
        }

        if (!mBackdropBound) {
            // TODO: Add game videos like movie trailer.
            mExcludeFirstPhoto = false;
            String backdropUrl = null;
            if (!photoList.isEmpty()) {
                backdropUrl = photoList.get(0).getLargeUrl();
                mExcludeFirstPhoto = true;
                mBackdropLayout.setOnClickListener(view -> {
                    // TODO
                    Context context = view.getContext();
                    context.startActivity(GalleryActivity.makeImageListIntent(photoList, 0, context));
                });
            } else if (game.cover != null) {
                backdropUrl = game.cover.getLargeUrl();
                mBackdropLayout.setOnClickListener(view -> {
                    // TODO
                    Context context = view.getContext();
                    context.startActivity(GalleryActivity.makeIntent(game.cover, context));
                });
            }
            if (backdropUrl != null) {
                ImageUtils.loadItemBackdropAndFadeIn(mBackdropImage, backdropUrl, null);
            } else {
                mBackdropImage.setBackgroundColor(game.getThemeColor());
                ViewUtils.fadeIn(mBackdropImage);
            }
            mBackdropBound = true;
        }

        mAdapter.setData(new GameDataAdapter.Data(game, rating, photoList, mExcludeFirstPhoto,
                itemCollectionList, gameGuideList, reviewList, forumTopicList, recommendationList,
                relatedDoulistList));
        if (mAdapter.getItemCount() > 0) {
            mContentStateLayout.setLoaded(true);
        }
    }

    @Override
    protected String makeItemUrl(long itemId) {
        return DoubanUtils.makeGameUrl(itemId);
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
    public void onUncollectItem(Game game) {
        ConfirmUncollectItemDialogFragment.show(this);
    }

    @Override
    public void uncollect() {
        if (!mResource.hasItem()) {
            return;
        }
        Game game = mResource.getItem();
        UncollectItemManager.getInstance().write(game.getType(), game.id, getActivity());
    }

    @Override
    public void copyText(String text) {
        CopyTextDialogFragment.show(text, this);
    }
}
