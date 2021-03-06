/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.ui;

import com.douya.R;

import java.util.List;

import butterknife.BindDimen;
import douya.item.content.BaseItemFragmentResource;
import douya.item.content.BookFragmentResource;
import douya.item.content.ConfirmUncollectItemDialogFragment;
import douya.item.content.UncollectItemManager;
import douya.network.api.info.frodo.Book;
import douya.network.api.info.frodo.CollectableItem;
import douya.network.api.info.frodo.Doulist;
import douya.network.api.info.frodo.Rating;
import douya.network.api.info.frodo.SimpleBook;
import douya.network.api.info.frodo.SimpleItemCollection;
import douya.network.api.info.frodo.SimpleItemForumTopic;
import douya.network.api.info.frodo.SimpleReview;
import douya.ui.BarrierAdapter;
import douya.ui.CopyTextDialogFragment;
import douya.util.DoubanUtils;
import douya.util.ViewUtils;

public class BookFragment extends BaseItemFragment<SimpleBook, Book>
        implements BookFragmentResource.Listener, BookDataAdapter.Listener,
        ConfirmUncollectItemDialogFragment.Listener {

    @BindDimen(R.dimen.toolbar_height)
    int mContentPaddingTopExtra;

    private BookAdapter mAdapter;

    private boolean mBackdropBound;

    public static BookFragment newInstance(long bookId, SimpleBook simpleBook, Book book) {
        //noinspection deprecation
        BookFragment fragment = new BookFragment();
        fragment.setArguments(bookId, simpleBook, book);
        return fragment;
    }

    /**
     * @deprecated Use {@link #newInstance(long, SimpleBook, Book)} instead.
     */
    public BookFragment() {}

    @Override
    protected BaseItemFragmentResource<SimpleBook, Book> onAttachResource(long itemId,
                                                                          SimpleBook simpleItem,
                                                                          Book item) {
        return BookFragmentResource.attachTo(itemId, simpleItem, item, this);
    }

    @Override
    protected float getBackdropRatio() {
        return ViewUtils.isInPortait(getContext()) ? 0 : 2;
    }

    @Override
    protected BarrierAdapter onCreateAdapter() {
        mAdapter = new BookAdapter(this);
        return mAdapter;
    }

    @Override
    protected int getContentListPaddingTopExtra() {
        return mContentPaddingTopExtra;
    }

    @Override
    protected int getContentStateViewsPaddingTopExtra() {
        return mContentPaddingTopExtra;
    }

    @Override
    public void onChanged(int requestCode, Book newBook, Rating newRating,
                          List<SimpleItemCollection> newItemCollectionList,
                          List<SimpleReview> newReviewList,
                          List<SimpleItemForumTopic> newForumTopicList,
                          List<CollectableItem> newRecommendationList,
                          List<Doulist> newRelatedDoulistList) {
        update(newBook, newRating, newItemCollectionList, newReviewList, newForumTopicList,
                newRecommendationList, newRelatedDoulistList);
    }

    private void update(Book book, Rating rating, List<SimpleItemCollection> itemCollectionList,
                        List<SimpleReview> reviewList, List<SimpleItemForumTopic> forumTopicList,
                        List<CollectableItem> recommendationList,
                        List<Doulist> relatedDoulistList) {

        if (book != null) {
            super.updateWithSimpleItem(book);
        }

        if (book == null) {
            return;
        }

        if (!mBackdropBound) {
            if (!ViewUtils.isInPortait(getActivity())) {
                mBackdropImage.setBackgroundColor(book.getThemeColor());
                ViewUtils.fadeIn(mBackdropImage);
            }
            mBackdropBound = true;
        }

        mAdapter.setData(new BookDataAdapter.Data(book, rating, itemCollectionList, reviewList,
                forumTopicList, recommendationList, relatedDoulistList));
        if (mAdapter.getItemCount() > 0) {
            mContentStateLayout.setLoaded(true);
        }
    }

    @Override
    protected String makeItemUrl(long itemId) {
        return DoubanUtils.makeBookUrl(itemId);
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
    public void onUncollectItem(Book book) {
        ConfirmUncollectItemDialogFragment.show(this);
    }

    @Override
    public void uncollect() {
        if (!mResource.hasItem()) {
            return;
        }
        Book book = mResource.getItem();
        UncollectItemManager.getInstance().write(book.getType(), book.id, getActivity());
    }

    @Override
    public void copyText(String text) {
        CopyTextDialogFragment.show(text, this);
    }
}
