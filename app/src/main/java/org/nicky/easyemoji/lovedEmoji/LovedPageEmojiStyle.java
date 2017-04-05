package org.nicky.easyemoji.lovedEmoji;

import android.os.Parcel;

import org.nicky.libeasyemoji.emoji.interfaces.PageEmojiStyle;
import org.nicky.libeasyemoji.emojicon.emoji.Emojicon;

import java.util.List;

/**
 * Created by nickyang on 2017/4/1.
 */

public class LovedPageEmojiStyle<T extends Emoji> implements PageEmojiStyle<T> {
    private List<T> list;
    private boolean hasBackspace = true;

    public LovedPageEmojiStyle(){}

    public LovedPageEmojiStyle(Parcel in) {
        hasBackspace = in.readByte()!=0;
        in.readTypedList(list, (Creator<T>) Emoji.CREATOR);
    }

    public int getColumn(){
        return 7;
    }

    public int getRow(){
        return 4;
    }

    public boolean hasBackspace(){
        return hasBackspace;
    }

    @Override
    public int getExceptItemCount() {
        return hasBackspace?(getColumn()*getRow()-1):(getColumn()*getRow());
    }

    @Override
    public void setData(List<T> data) {
        list = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<T> getData(){
        return list;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (hasBackspace?1:0));
        dest.writeTypedList(list);
    }

    public static final Creator<LovedPageEmojiStyle> CREATOR = new Creator<LovedPageEmojiStyle>() {
        @Override
        public LovedPageEmojiStyle createFromParcel(Parcel in) {
            return new LovedPageEmojiStyle(in);
        }

        @Override
        public LovedPageEmojiStyle[] newArray(int size) {
            return new LovedPageEmojiStyle[size];
        }
    };

}
