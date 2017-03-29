package org.nicky.libeasyemoji.emojicon;

import android.os.Parcelable;

import org.nicky.libeasyemoji.emojicon.interfaces.BaseCategory;
import org.nicky.libeasyemoji.emojicon.interfaces.ICategoryDataWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickyang on 2017/3/28.
 */

public class CategoryDataWrapper<T extends Parcelable> implements BaseCategory<T>,ICategoryDataWrapper<T> {

    private BaseCategory<T> baseCategory;
    private int icon;
    private String categoryName;
    //不包括删除键
    private int countsPerPage;
    private boolean isSelected;
    private List<T> dataList;
    private List<PagerDataCategory<T>> pagerDataCategoryList;

    public CategoryDataWrapper(BaseCategory<T> category){
        updatePagerCategoryList(category);
    }

    @Override
    public BaseCategory<T> getBaseCategory() {
        return baseCategory;
    }

    @Override
    public void updatePagerCategoryList(BaseCategory<T> category){
        this.baseCategory = category;
        this.icon = category.getCategoryItemIcon();
        this.countsPerPage = category.getColumn()*category.getRow();
        if(category.hasBackspace()){
            this.countsPerPage -=1;
        }
        this.categoryName = category.getCategoryName();
        this.dataList = category.getEmojiData();

        if(pagerDataCategoryList == null){
            pagerDataCategoryList = new ArrayList<>(10);
        }else {
            pagerDataCategoryList.clear();
        }
        for(int i=0;i<getPagerCounts();i++){
            int start = i*countsPerPage;
            int end = start+countsPerPage;
            if(end > dataList.size()){
                end = dataList.size();
            }
            pagerDataCategoryList.add(new PagerDataCategory<T>(baseCategory,dataList.subList(start,end)));
        }
    }

    @Override
    public String getCategoryName() {
        return baseCategory.getCategoryName();
    }

    @Override
    public List<PagerDataCategory<T>> getPagerDataCategory() {
        return pagerDataCategoryList;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public void add(T t) {

    }

    @Override
    public void delete(T t) {

    }

    @Override
    public int getPagerCounts() {
        int count = dataList.size()/ countsPerPage;
        int mod = dataList.size()% countsPerPage;
        return mod>0?count+1:count;
    }

    @Override
    public int getCategoryItemIcon() {
        return icon;
    }

    @Override
    public List<T> getEmojiData() {
        return dataList;
    }

    @Override
    public int getColumn() {
        return baseCategory.getColumn();
    }

    @Override
    public int getRow() {
        return baseCategory.getRow();
    }

    @Override
    public boolean hasBackspace() {
        return baseCategory.hasBackspace();
    }

    @Override
    public BaseCategoryFragment<T> getFragment(PagerDataCategory category) {
        return baseCategory.getFragment(category);
    }

}
