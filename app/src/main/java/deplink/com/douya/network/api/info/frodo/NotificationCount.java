package deplink.com.douya.network.api.info.frodo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class NotificationCount implements Parcelable {
    protected NotificationCount(Parcel in) {
    }

    public static final Creator<NotificationCount> CREATOR = new Creator<NotificationCount>() {
        @Override
        public NotificationCount createFromParcel(Parcel in) {
            return new NotificationCount(in);
        }

        @Override
        public NotificationCount[] newArray(int size) {
            return new NotificationCount[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
