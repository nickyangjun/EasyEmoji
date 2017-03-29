package org.nicky.libeasyemoji.emojicon;

import android.os.Parcelable;
import android.text.TextUtils;

import org.nicky.libeasyemoji.emojicon.interfaces.BaseCategory;
import org.nicky.libeasyemoji.emojicon.interfaces.CategoryDataChangeListener;
import org.nicky.libeasyemoji.emojicon.interfaces.CategoryDataManger;
import org.nicky.libeasyemoji.emojicon.interfaces.ICategoryDataWrapper;
import org.nicky.libeasyemoji.emojicon.utils.IndexLinkedHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickyang on 2017/3/28.
 */

public class CategoryDataManagerImpl<T extends Parcelable> implements CategoryDataManger {
    //表情种类
    IndexLinkedHashMap<String,ICategoryDataWrapper> categoryWrapperMap = new IndexLinkedHashMap<>();
    //表情显示的pager
    List<PagerDataCategory<T>> categoryPagerList = new ArrayList<>();
    CategoryDataChangeListener categoryChangeListener; //表情类目变化

    private CategoryDataManagerImpl(){}

    public static CategoryDataManagerImpl newInstance(){
        return new CategoryDataManagerImpl();
    }

    public int getCategoryDataCounts(){
        return  categoryWrapperMap.size();
    }

    //根据viewpager中的位置，计算是哪个类目的表情
    public ICategoryDataWrapper getCategoryWrapperByPosition(int position){
        int counts = 0;
        ICategoryDataWrapper wrapper = categoryWrapperMap.get(0);
        for(int i=0;i<categoryWrapperMap.size();i++){
            counts += categoryWrapperMap.get(i).getPagerCounts();
            if(counts > position){
                wrapper = categoryWrapperMap.get(i);
                break;
            }
        }
        return wrapper;
    }

    //根据viewPager的position得到当前CategoryDataWrapper中的位置
    public int getCategoryDataWrapperIndexByPosition(int position){
        ICategoryDataWrapper wrapper = getCategoryWrapperByPosition(position);
        PagerDataCategory category = getPagerDataCategory(position);
        return wrapper.getPagerDataCategory().indexOf(category);
    }

    public int getPagerCategoryCounts(){
        return categoryPagerList.size();
    }

    public PagerDataCategory getPagerDataCategory(int position){
        return categoryPagerList.get(position);
    }

    //根据表情类目名得到所在viewpager的index
    public int getPageCategoryIndex(String categoryName){
        int index = 0;
        for(PagerDataCategory pager : categoryPagerList){
            if(pager.getCategoryName().equals(categoryName)){
                break;
            }
            index++;
        }
        return index;
    }

    //将当前类目的表情设置成选中
    public void setSelectedCategoryWrapper(ICategoryDataWrapper selectedWrapper){
        for(int i=0;i<categoryWrapperMap.size();i++){
            ICategoryDataWrapper wrapper = categoryWrapperMap.get(i);
            if(wrapper == selectedWrapper){
                wrapper.setSelected(true);
            }else {
                wrapper.setSelected(false);
            }
        }
    }

    @Override
    public  void addCategory(BaseCategory category) {
        if(TextUtils.isEmpty(category.getCategoryName())){
            throw new RuntimeException("the category's categoryName can not null");
        }
        if(categoryWrapperMap.containsKey(category.getCategoryName())){
           return;
        }
        ICategoryDataWrapper<T> wrapper = new CategoryDataWrapper<>(category);
        categoryWrapperMap.add(category.getCategoryName(),wrapper);
        categoryPagerList.addAll(wrapper.getPagerDataCategory());
        if(categoryChangeListener != null){
            categoryChangeListener.update(CategoryDataChangeListener.TYPE.ADD,category);
        }
    }

    public  void addCategory(int position, BaseCategory category) {
        if(TextUtils.isEmpty(category.getCategoryName())){
            throw new RuntimeException("the category's categoryName can not null");
        }
        if(categoryWrapperMap.containsKey(category.getCategoryName())){
            return;
        }
        ICategoryDataWrapper<T> wrapper = new CategoryDataWrapper<>(category);
        categoryWrapperMap.add(position,category.getCategoryName(),wrapper);
        int pagerCounts = 0;
        for(int i=0;i<position;i++){
            pagerCounts += categoryWrapperMap.get(i).getPagerCounts();
        }
        categoryPagerList.addAll(pagerCounts,wrapper.getPagerDataCategory());
        if(categoryChangeListener != null){
            categoryChangeListener.update(CategoryDataChangeListener.TYPE.ADD,category);
        }
    }

    @Override
    public void deleteCategory(String categoryName) {
        ICategoryDataWrapper<T> wrapper = (ICategoryDataWrapper<T>) categoryWrapperMap.get(categoryName);
        if(wrapper != null){
            categoryPagerList.removeAll(wrapper.getPagerDataCategory());
            categoryWrapperMap.remove(categoryName);
            if(categoryChangeListener != null){
                categoryChangeListener.update(CategoryDataChangeListener.TYPE.DELETE,wrapper.getBaseCategory());
            }
        }
    }

    @Override
    public void updateCategory(BaseCategory category) {
        if(categoryWrapperMap.containsKey(category.getCategoryName())){
            if(category.getEmojiData().size() == 0){
                deleteCategory(category.getCategoryName());
                return;
            }
            int index = categoryWrapperMap.indexOf(category.getCategoryName());
            deleteCategory(category.getCategoryName());
            addCategory(index,category);
        }else {
            addCategory(category);
        }
    }

    public void setCategoryDataChangeListener(CategoryDataChangeListener listener){
        categoryChangeListener = listener;
    }
}
