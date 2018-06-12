/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.eventbus;


import douya.network.api.info.frodo.SimpleItemCollection;

public class ItemCollectionUpdatedEvent extends Event {

    public SimpleItemCollection itemCollection;

    public ItemCollectionUpdatedEvent(SimpleItemCollection itemCollection, Object source) {
        super(source);

        this.itemCollection = itemCollection;
    }
}
