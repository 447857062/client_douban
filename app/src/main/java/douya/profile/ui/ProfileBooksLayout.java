/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.profile.ui;

import android.content.Context;
import android.util.AttributeSet;

import douya.link.UriHandler;
import douya.network.api.info.frodo.CollectableItem;


public class ProfileBooksLayout extends ProfileItemsLayout {

    public ProfileBooksLayout(Context context) {
        super(context);
    }

    public ProfileBooksLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfileBooksLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected CollectableItem.Type getItemType() {
        return CollectableItem.Type.BOOK;
    }

    @Override
    protected void onViewPrimaryItems() {
        // FIXME
        UriHandler.open(String.format("https://book.douban.com/people/%s/collect",
                getUserIdOrUid()), getContext());
    }

    @Override
    protected void onViewSecondaryItems() {
        // FIXME
        UriHandler.open(String.format("https://book.douban.com/people/%s/do", getUserIdOrUid()),
                getContext());
    }

    @Override
    protected void onViewTertiaryItems() {
        // FIXME
        UriHandler.open(String.format("https://book.douban.com/people/%s/wish", getUserIdOrUid()),
                getContext());
    }
}
