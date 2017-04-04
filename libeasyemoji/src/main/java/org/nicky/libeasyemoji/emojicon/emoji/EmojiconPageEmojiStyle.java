package org.nicky.libeasyemoji.emojicon.emoji;

import android.os.Parcel;

import org.nicky.libeasyemoji.emoji.interfaces.PageEmojiStyle;

import java.util.List;

/**
 * Created by nickyang on 2017/4/1.
 */

public class EmojiconPageEmojiStyle<T extends Emojicon> implements PageEmojiStyle<T> {
    private List<T> list;
    private boolean hasBackspace = true;

    public EmojiconPageEmojiStyle(){}

    public EmojiconPageEmojiStyle(Parcel in) {
        hasBackspace = in.readByte()!=0;
        in.readTypedList(list, (Creator<T>) Emojicon.CREATOR);
    }

    public int getColumn(){
        return 7;
    }

    public int getRow(){
        return 3;
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

    public static final Creator<EmojiconPageEmojiStyle> CREATOR = new Creator<EmojiconPageEmojiStyle>() {
        @Override
        public EmojiconPageEmojiStyle createFromParcel(Parcel in) {
            return new EmojiconPageEmojiStyle(in);
        }

        @Override
        public EmojiconPageEmojiStyle[] newArray(int size) {
            return new EmojiconPageEmojiStyle[size];
        }
    };

}
