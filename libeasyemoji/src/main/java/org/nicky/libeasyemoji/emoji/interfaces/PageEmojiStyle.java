package org.nicky.libeasyemoji.emoji.interfaces;

import android.os.Parcelable;

import java.util.List;

/**
 * Created by nickyang on 2017/4/1.
 */

public interface PageEmojiStyle<T extends Parcelable> extends Parcelable{
    int getExceptItemCount();
    void setData(List<T> data);
}
