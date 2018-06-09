/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package deplink.com.douya.eventbus;


import deplink.com.douya.network.api.info.apiv2.User;

public class UserUpdatedEvent extends Event {

    public User mUser;

    public UserUpdatedEvent(User user, Object source) {
        super(source);

        this.mUser = user;
    }
}
