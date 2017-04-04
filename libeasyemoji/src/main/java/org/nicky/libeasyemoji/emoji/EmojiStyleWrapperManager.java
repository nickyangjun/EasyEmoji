package org.nicky.libeasyemoji.emoji;

import android.app.Activity;
import android.os.Parcelable;
import android.text.TextUtils;

import org.nicky.libeasyemoji.emoji.interfaces.EmojiStyle;
import org.nicky.libeasyemoji.emoji.interfaces.EmojiStyleChangeListener;
import org.nicky.libeasyemoji.emojicon.utils.IndexLinkedHashMap;

/**
 * Created by nickyang on 2017/4/1.
 */

public class EmojiStyleWrapperManager<T extends Parcelable> {
    IndexLinkedHashMap<String,EmojiStyleWrapper> wrapperMap = new IndexLinkedHashMap<>();
    EmojiStyleChangeListener styleChangeListener;
    Activity activity;


    public EmojiStyleWrapperManager(Activity activity){
        this.activity = activity;
    }

    public int getEmojiPagerCounts(){
        int count = 0;
        for(int i = 0;i<wrapperMap.size();i++){
            count += wrapperMap.get(i).getPagerCounts();
        }
        return count;
    }

    public int getStyleWrapperCounts(){
        return wrapperMap.size();
    }

    //根据viewpager中的位置，计算是哪个类目的表情
    public EmojiStyleWrapper getEmojiStyleWrapperByPosition(int position){
        int counts = 0;
        EmojiStyleWrapper wrapper = wrapperMap.get(0);
        for(int i=0;i<wrapperMap.size();i++){
            counts += wrapperMap.get(i).getPagerCounts();
            if(counts > position){
                wrapper = wrapperMap.get(i);
                break;
            }
        }
        return wrapper;
    }

    //根据表情类目名得到它在viewpager中他的页面的开始index索引
    public int getViewPageIndexByEmojiStyleName(String styleName){
        int index = 0;
        for(int i=0;i<wrapperMap.size();i++){
            if(wrapperMap.get(i).getStyleName().equals(styleName)){
                break;
            }
            index += wrapperMap.get(i).getPagerCounts();
        }
        return index;
    }


    //将当前类目的表情设置成选中
    public void setSelectedStyleWrapper(EmojiStyleWrapper selectedWrapper){
        for(int i=0;i<wrapperMap.size();i++){
            EmojiStyleWrapper wrapper = wrapperMap.get(i);
            if(wrapper == selectedWrapper){
                wrapper.setSelected(true);
            }else {
                wrapper.setSelected(false);
            }
        }
    }

    //根据viewPager的position得到此pager在EmojiStyleWrapper中的位置
    public int getPagerIndexAtStyleWrapperByVPPosition(int position){
        EmojiStyleWrapper wrapper = getEmojiStyleWrapperByPosition(position);
        int index = getViewPageIndexByEmojiStyleName(wrapper.getStyleName());
        return position - index;
    }

    public EmojiFragment getFragment(int position){
        EmojiStyleWrapper wrapper = getEmojiStyleWrapperByPosition(position);
        int index = getPagerIndexAtStyleWrapperByVPPosition(position);
        return wrapper.getFragment(index);
    }

    public  void addEmojiStyle(EmojiStyle style) {
        addEmojiStyle(wrapperMap.size(),style);
    }

    public  void addEmojiStyle(int position, EmojiStyle style) {
        if(TextUtils.isEmpty(style.getStyleName())){
            throw new RuntimeException("the EmojiStyle's styleName can not null");
        }
        if(wrapperMap.containsKey(style.getStyleName())){
            return;
        }
        EmojiStyleWrapper wrapper = new EmojiStyleWrapper(style);
        wrapperMap.add(position,style.getStyleName(),wrapper);
        if(styleChangeListener != null){
            styleChangeListener.update(EmojiStyleChangeListener.TYPE.ADD,style);
        }
    }


    public void deleteEmojiStyle(String categoryName) {
        EmojiStyleWrapper wrapper = (EmojiStyleWrapper) wrapperMap.get(categoryName);
        if(wrapper != null){
            wrapperMap.remove(categoryName);
            if(styleChangeListener != null){
                styleChangeListener.update(EmojiStyleChangeListener.TYPE.DELETE,wrapper.getEmojiStyle());
            }
        }
    }

    public void setEmojiStyleChangeListener(EmojiStyleChangeListener listener){
        styleChangeListener = listener;
    }

    public void updateStyle(EmojiStyle style) {
        if(wrapperMap.containsKey(style.getStyleName())){
            if(style.getEmojiData().size() == 0){
                deleteEmojiStyle(style.getStyleName());
                return;
            }
            int index = wrapperMap.indexOf(style.getStyleName());
            deleteEmojiStyle(style.getStyleName());
            addEmojiStyle(index,style);
            if(styleChangeListener != null){
                styleChangeListener.update(EmojiStyleChangeListener.TYPE.UPDATE,style);
            }
        }else {
            addEmojiStyle(style);
        }
    }

}
