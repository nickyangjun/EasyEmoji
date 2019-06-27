package org.nicky.libeasyemoji.emoji.interfaces;

import android.support.annotation.Nullable;

import org.nicky.libeasyemoji.emoji.EmojiStyleWrapper;

/**
 * Created by nickyang on 2017/4/1.
 */

public interface EmojiStyleChangeListener {
    enum TYPE{
        ADD,
        DELETE,
        UPDATE
    }
    void update(TYPE type, @Nullable EmojiStyleWrapper styleWrapper, int selectedPage);
}
