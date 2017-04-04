package org.nicky.libeasyemoji.emoji.interfaces;

/**
 * Created by nickyang on 2017/4/1.
 */

public interface EmojiStyleChangeListener {
    enum TYPE{
        ADD,
        DELETE,
        UPDATE
    }
    void update(TYPE type, EmojiStyle style);
}
