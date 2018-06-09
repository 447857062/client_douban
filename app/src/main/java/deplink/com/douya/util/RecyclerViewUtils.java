/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package deplink.com.douya.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewUtils {

    private RecyclerViewUtils() {}

    public static Context getContext(RecyclerView.ViewHolder holder) {
        return holder.itemView.getContext();
    }

    public static boolean hasFirstChildReachedTop(RecyclerView recyclerView, int top) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        View firstChild = layoutManager.findViewByPosition(0);
        if (firstChild != null) {
            return firstChild.getTop() <= top;
        } else {
            return layoutManager.getChildCount() > 0;
        }
    }

    public static boolean hasFirstChildReachedTop(RecyclerView recyclerView) {
        return hasFirstChildReachedTop(recyclerView, 0);
    }
}
