package org.nicky.libeasyemoji.emojicon.interfaces;

import android.os.Parcelable;

import org.nicky.libeasyemoji.emojicon.BaseCategoryFragment;
import org.nicky.libeasyemoji.emojicon.PagerDataCategory;

import java.util.List;

/**
 * Created by nickyang on 2017/3/28.
 */

public interface ICategoryDataWrapper<T extends Parcelable> {
    void add(T t);
    void delete(T t);
    int getPagerCounts();
    int getCategoryItemIcon();
    String getCategoryName();
    List<PagerDataCategory<T>> getPagerDataCategory();
    boolean isSelected();
    void setSelected(boolean selected);
    BaseCategory<T> getBaseCategory();
    void updatePagerCategoryList(BaseCategory<T> t);
    BaseCategoryFragment<T> getFragment(PagerDataCategory category);
}
