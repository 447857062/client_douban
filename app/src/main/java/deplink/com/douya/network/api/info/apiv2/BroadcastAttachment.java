/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package deplink.com.douya.network.api.info.apiv2;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import deplink.com.douya.network.api.info.frodo.SizedImage;
import deplink.com.douya.util.DoubanUtils;

public class BroadcastAttachment implements Parcelable {

    @SerializedName("desc")
    public String description;

    public String href;

    public String image;

    public String title;

    public String type;


    @SuppressWarnings("deprecation")
    public deplink.com.douya.network.api.info.frodo.BroadcastAttachment toFrodo(
            ArrayList<Photo> photos) {
        deplink.com.douya.network.api.info.frodo.BroadcastAttachment attachment =
                new deplink.com.douya.network.api.info.frodo.BroadcastAttachment();
        attachment.type = photos.size() > 0 ?
                deplink.com.douya.network.api.info.frodo.BroadcastAttachment.Type.IMAGES
                        .getApiString()
                : deplink.com.douya.network.api.info.frodo.BroadcastAttachment.Type.NORMAL
                        .getApiString();
        attachment.image = new SizedImage();
        attachment.image.medium = new SizedImage.Item();
        attachment.image.medium.url = image;
        attachment.image.medium.width = 1;
        attachment.image.medium.height = 1;
        attachment.imageBlock =
                new deplink.com.douya.network.api.info.frodo.BroadcastAttachment
                        .ImageBlock();
        for (Photo photo : photos) {
            deplink.com.douya.network.api.info.frodo.BroadcastAttachment.ImageBlock.Image
                    image = new deplink.com.douya.network.api.info.frodo.BroadcastAttachment
                    .ImageBlock.Image();
            image.image = photo.toFrodoSizedImage();
            image.uri = DoubanUtils.makePhotoAlbumUri(photo.albumId);
            attachment.imageBlock.images.add(image);
        }
        attachment.imageBlock.countString = attachment.imageBlock.images.size() + "张";
        attachment.text = description;
        attachment.title = title;
        attachment.uri = href;
        attachment.url = href;
        return attachment;
    }


    public static final Creator<BroadcastAttachment> CREATOR =
            new Creator<BroadcastAttachment>() {
                public BroadcastAttachment createFromParcel(Parcel source) {
                    return new BroadcastAttachment(source);
                }
                public BroadcastAttachment[] newArray(int size) {
                    return new BroadcastAttachment[size];
                }
            };

    public BroadcastAttachment() {}

    protected BroadcastAttachment(Parcel in) {
        description = in.readString();
        href = in.readString();
        image = in.readString();
        title = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(href);
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(type);
    }
}
