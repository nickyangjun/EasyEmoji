package org.nicky.easyemoji.lovedEmoji;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nickyang on 2017/4/5.
 */

public class Emoji implements Parcelable{
    public String emoji;
    public int icon;

    public Emoji(String code,int icon){
        this.emoji = code;
        this.icon = icon;
    }

    public Emoji(Parcel in) {
        emoji = in.readString();
        icon = in.readInt();
    }

    public String getEmoji(){
        return emoji;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(emoji);
        dest.writeInt(icon);
    }

    public static final Creator<Emoji> CREATOR = new Creator<Emoji>() {
        @Override
        public Emoji createFromParcel(Parcel in) {
            return new Emoji(in);
        }

        @Override
        public Emoji[] newArray(int size) {
            return new Emoji[size];
        }
    };
}
