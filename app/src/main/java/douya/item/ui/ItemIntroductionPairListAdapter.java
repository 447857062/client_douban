/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.ui;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.douya.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import douya.ui.SimpleAdapter;
import douya.util.ViewUtils;

public class ItemIntroductionPairListAdapter
        extends SimpleAdapter<Pair<String, String>, ItemIntroductionPairListAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(R.layout.item_introduction_pair_item, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<String, String> roleNames = getItem(position);
        holder.titleText.setText(roleNames.first);
        holder.textText.setText(roleNames.second);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        public TextView titleText;
        @BindView(R.id.text)
        public TextView textText;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
