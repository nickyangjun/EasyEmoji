package org.nicky.libeasyemoji.emojicon.interfaces;

import android.os.Parcelable;

/**
 * Created by nickyang on 2017/3/28.
 */

public interface CategoryDataManger<T extends Parcelable>{
    void addCategory(BaseCategory<T> category);
    void deleteCategory(String categoryName);
}
