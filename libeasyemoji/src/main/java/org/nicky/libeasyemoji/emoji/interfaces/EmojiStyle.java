package org.nicky.libeasyemoji.emoji.interfaces;

import android.os.Parcelable;

import org.nicky.libeasyemoji.emoji.EmojiFragment;

import java.util.List;

/**
 * Created by nickyang on 2017/4/1.
 */

public interface EmojiStyle<T extends Parcelable> {
    String getStyleName();
    int getStyleIcon();
    List<T> getEmojiData();
    EmojiFragment getCustomFragment(int index);
    PageEmojiStyle getPagerData(int index);
}
