/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.ui;

import android.content.Context;
import android.support.annotation.NonNull;

import com.douya.R;

import java.util.Arrays;
import java.util.List;

import douya.functional.Functional;
import douya.network.api.info.frodo.CollectableItem;
import douya.network.api.info.frodo.ItemCollectionState;
import douya.ui.ArrayAdapterCompat;

public class ItemCollectionStateSpinnerAdapter extends ArrayAdapterCompat<String> {

    private List<ItemCollectionState> mStates;

    public ItemCollectionStateSpinnerAdapter(CollectableItem.Type type, @NonNull Context context) {
        super(context, R.layout.simple_spinner_item, getStateNames(type, context));

        mStates = getStates(type);

        setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
    }

    private static List<ItemCollectionState> getStates(CollectableItem.Type type) {
        List<ItemCollectionState> states = Arrays.asList(ItemCollectionState.values());
        if (!type.hasDoingState()) {
            states = Functional.filter(states, state -> state != ItemCollectionState.DOING);
        }
        return states;
    }

    private static List<String> getStateNames(CollectableItem.Type type, Context context) {
        return Functional.map(getStates(type), state -> state.getString(type, context));
    }

    public int getPositionForState(ItemCollectionState state) {
        return mStates.indexOf(state);
    }

    public ItemCollectionState getStateAtPosition(int position) {
        return mStates.get(position);
    }
}
