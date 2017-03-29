package org.nicky.libeasyemoji.emojicon.interfaces;

import android.os.Parcelable;

import org.nicky.libeasyemoji.emojicon.BaseCategoryFragment;
import org.nicky.libeasyemoji.emojicon.PagerDataCategory;

import java.util.List;

/**
 * Created by nickyang on 2017/3/28.
 */

public interface BaseCategory<T extends Parcelable> {

    String getCategoryName();

    int getCategoryItemIcon();

    List<T> getEmojiData();

    int getColumn();

    int getRow();

    boolean hasBackspace();
    /**
     *  用于显示表情的Fragment, 目前以这种方式来支持扩展自定义显示界面
     */
    BaseCategoryFragment<T> getFragment(PagerDataCategory category);

}
