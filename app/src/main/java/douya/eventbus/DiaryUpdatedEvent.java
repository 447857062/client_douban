/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.eventbus;


import douya.network.api.info.frodo.Diary;

public class DiaryUpdatedEvent extends Event {

    public Diary diary;

    public DiaryUpdatedEvent(Diary diary, Object source) {
        super(source);

        this.diary = diary;
    }
}
