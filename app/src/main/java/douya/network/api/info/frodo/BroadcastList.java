package douya.network.api.info.frodo;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class BroadcastList extends BaseList<Broadcast> {
    @SerializedName("statuses")
    public ArrayList<Broadcast> broadcasts = new ArrayList<>();

    @Override
    public ArrayList<Broadcast> getList() {
        return broadcasts;
    }
    public static final Creator<BroadcastList> CREATOR = new Creator<BroadcastList>() {
        @Override
        public BroadcastList createFromParcel(Parcel source) {
            return new BroadcastList(source);
        }
        @Override
        public BroadcastList[] newArray(int size) {
            return new BroadcastList[size];
        }
    };

    public BroadcastList() {}

    protected BroadcastList(Parcel in) {
        super(in);

        broadcasts = in.createTypedArrayList(Broadcast.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeTypedList(broadcasts);
    }
}
