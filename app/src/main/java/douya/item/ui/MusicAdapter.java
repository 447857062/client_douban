/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.ui;

import android.support.v7.widget.RecyclerView;

import douya.network.api.info.frodo.SimpleItemCollection;
import douya.ui.BarrierAdapter;

public class MusicAdapter extends BarrierAdapter {

    private MusicDataAdapter mDataAdapter;

    public MusicAdapter(MusicDataAdapter.Listener listener) {
        super(new MusicDataAdapter(listener));

        RecyclerView.Adapter<?>[] adapters = getAdapters();
        mDataAdapter = (MusicDataAdapter) adapters[0];
    }

    public void setData(MusicDataAdapter.Data data) {
        mDataAdapter.setData(data);
    }

    public void notifyItemCollectionChanged() {
        mDataAdapter.notifyItemCollectionChanged();
    }

    public void notifyTrackListChanged() {
        mDataAdapter.notifyTrackListChanged();
    }

    public void setItemCollectionListItem(int position, SimpleItemCollection newItemCollection) {
        mDataAdapter.notifyItemCollectionListItemChanged(position, newItemCollection);
    }

    public void notifyItemCollectionListItemChanged(int position) {
        mDataAdapter.notifyItemCollectionListItemChanged(position, null);
    }
}
