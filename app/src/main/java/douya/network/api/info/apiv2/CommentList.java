/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.network.api.info.apiv2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CommentList extends BaseList implements Parcelable {

    public ArrayList<Comment> comments = new ArrayList<>();


    public static final Creator<CommentList> CREATOR =
            new Creator<CommentList>() {
                public CommentList createFromParcel(Parcel source) {
                    return new CommentList(source);
                }
                public CommentList[] newArray(int size) {
                    return new CommentList[size];
                }
            };

    public CommentList() {}

    protected CommentList(Parcel in) {
        super(in);

        comments = in.createTypedArrayList(Comment.CREATOR);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeTypedList(comments);
    }
}
