package org.nicky.libeasyemoji.emojicon.interfaces;

/**
 * Created by nickyang on 2017/3/27.
 */

public interface CategoryDataChangeListener {
    enum TYPE{
        ADD,
        DELETE
    }
    void update(TYPE type, BaseCategory category);
}
