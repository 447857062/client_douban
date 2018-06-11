/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.eventbus;


import douya.network.api.info.frodo.Notification;

public class NotificationUpdatedEvent extends Event {

    public Notification notification;

    public NotificationUpdatedEvent(Notification notification, Object source) {
        super(source);

        this.notification = notification;
    }
}
