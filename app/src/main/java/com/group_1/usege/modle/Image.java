package com.group_1.usege.modle;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Image implements Parcelable {
    private List<String> tags;
    private String description;
    private String date;
    private long size;
    private String location;
    private Uri uri;

    private List<Album> belongToAlbum;

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getLocation() {
        return location;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getDate() { return date; }

    public long getSize() {
        return size;
    }

    public List<String> getTags() { return tags; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image() {
        // Nothing
    }

    public Image(Uri uri, List<String> tags, String description, String date, long size, String location) {
        this.uri = uri;
        this.tags = tags;
        this.description = description;

        if (date == null) this.date = "";
        else {
            String day = date.split(" ")[0];
            List<String> dayComponents = Arrays.asList(day.split(":"));
            String reversedDay = dayComponents.get(2).concat("/").concat(dayComponents.get(1)).concat("/").concat(dayComponents.get(0));
            this.date = reversedDay;
        }

        this.size = size;

        // Thực hiện chuyển đổi ở đây trước khi gán giá trị
        this.location = location;

        this.belongToAlbum = new ArrayList<Album>();
        //////////////////
    }

    protected Image(Parcel in) {
        date = in.readString();
        size = in.readLong();
        description = in.readString();
        location = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeFloat(size);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeParcelable(uri, flags);
    }

    public List<Album> getBelongToAlbum() {
        return belongToAlbum;
    }

    public void setBelongToAlbum(List<Album> belongToAlbum) {
        this.belongToAlbum = belongToAlbum;
    }
}