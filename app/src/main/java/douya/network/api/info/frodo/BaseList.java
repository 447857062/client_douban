package douya.network.api.info.frodo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public abstract class BaseList<T> implements Parcelable {
    public int count;

    public int start;

    public int total;

    public abstract ArrayList<T> getList();


    public BaseList() {}

    protected BaseList(Parcel in) {
        count = in.readInt();
        start = in.readInt();
        total = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeInt(start);
        dest.writeInt(total);
    }
}
