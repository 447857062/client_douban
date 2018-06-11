/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.network.api.info.frodo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * {@code StatusFeedList} in Frodo.
 */
public class TimelineList implements Parcelable {

    public int count;

    public ArrayList<BaseTimelineItem> items = new ArrayList<>();

    @SerializedName("new_status_count_str")
    public String newBroadcastCountString;

    public ArrayList<Broadcast> toBroadcastList() {
        ArrayList<Broadcast> broadcastList = new ArrayList<>();
        for (BaseTimelineItem item : items) {
            if (item instanceof BroadcastTimelineItem) {
                broadcastList.add(((BroadcastTimelineItem) item).broadcast);
            }
        }
        return broadcastList;
    }


    public static final Creator<TimelineList> CREATOR =
            new Creator<TimelineList>() {
                @Override
                public TimelineList createFromParcel(Parcel source) {
                    return new TimelineList(source);
                }
                @Override
                public TimelineList[] newArray(int size) {
                    return new TimelineList[size];
                }
            };

    public TimelineList() {}

    protected TimelineList(Parcel in) {
        count = in.readInt();
        items = new ArrayList<>();
        in.readList(items, BaseTimelineItem.class.getClassLoader());
        newBroadcastCountString = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeList(items);
        dest.writeString(newBroadcastCountString);
    }
}
