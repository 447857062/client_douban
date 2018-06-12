/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.ui;

import android.support.v7.widget.RecyclerView;

import douya.network.api.info.frodo.SimpleItemCollection;
import douya.ui.BarrierAdapter;


public class BookAdapter extends BarrierAdapter {

    private BookDataAdapter mDataAdapter;

    public BookAdapter(BookDataAdapter.Listener listener) {
        super(new BookDataAdapter(listener));

        RecyclerView.Adapter<?>[] adapters = getAdapters();
        mDataAdapter = (BookDataAdapter) adapters[0];
    }

    public void setData(BookDataAdapter.Data data) {
        mDataAdapter.setData(data);
    }

    public void notifyItemCollectionChanged() {
        mDataAdapter.notifyItemCollectionChanged();
    }

    public void setItemCollectionListItem(int position, SimpleItemCollection newItemCollection) {
        mDataAdapter.notifyItemCollectionListItemChanged(position, newItemCollection);
    }

    public void notifyItemCollectionListItemChanged(int position) {
        mDataAdapter.notifyItemCollectionListItemChanged(position, null);
    }
}
