/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.eventbus;

public class ItemCollectionWriteStartedEvent extends Event {

    public long itemCollectionId;

    public ItemCollectionWriteStartedEvent(long itemCollectionId, Object source) {
        super(source);

        this.itemCollectionId = itemCollectionId;
    }
}
