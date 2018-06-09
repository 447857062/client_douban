package deplink.com.douya.ui;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public abstract class SimpleAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    private List<T> mList = new ArrayList<>();
    public SimpleAdapter() {
        this(null);
    }
    public SimpleAdapter(List<T> list) {
        if (list != null) {
            mList.addAll(list);
        }
    }
    public void clear() {
        int oldSize = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, oldSize);
    }
    public T remove(int position) {
        T item = mList.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void replace(Collection<? extends T> collection) {
        mList.clear();
        mList.addAll(collection);
        notifyDataSetChanged();
    }
    public List<T> getList() {
        return mList;
    }

    public void addAll(Collection<? extends T> collection) {
        int oldSize = mList.size();
        mList.addAll(collection);
        notifyItemRangeInserted(oldSize, collection.size());
    }
    public void add(int position, T item) {
        mList.add(position, item);
        notifyItemInserted(position);
    }

    public void add(T item) {
        add(mList.size(), item);
    }

    public void set(int position, T item) {
        mList.set(position, item);
        notifyItemChanged(position);
    }
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
