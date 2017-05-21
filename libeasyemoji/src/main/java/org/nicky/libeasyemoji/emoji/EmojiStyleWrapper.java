package org.nicky.libeasyemoji.emoji;

import android.os.Parcelable;

import org.nicky.libeasyemoji.emoji.interfaces.EmojiStyle;
import org.nicky.libeasyemoji.emoji.interfaces.PageEmojiStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickyang on 2017/4/1.
 */

public class EmojiStyleWrapper<T extends Parcelable> {
    private EmojiStyle style;
    private boolean isSelected;
    private List<PageEmojiStyle> pagerDataList;
    //如果当前style被选中，curDisplayPageIndex >= 0 , 表示当前显示页在style中的索引
    private int curDisplayPageIndex = -1;

    public EmojiStyleWrapper(EmojiStyle style){
        this.style = style;
        setup();
    }

    private void setup(){
        int index = 0;
        int allDataCounts = this.style.getEmojiData().size();
        int itemCounts = 0;
        if(pagerDataList == null){
            pagerDataList = new ArrayList<>(10);
        }else {
            pagerDataList.clear();
        }
        for(int i=0;i< allDataCounts;){
            PageEmojiStyle pager = this.style.getPagerData(index);
            if(pager != null){
                pagerDataList.add(pager);
                int count = pager.getExceptItemCount();
                itemCounts += count;
                if(itemCounts < allDataCounts){
                    index++;
                    pager.setData(new ArrayList(this.style.getEmojiData().subList(i,i+count)));
                }else {
                    pager.setData(new ArrayList(this.style.getEmojiData().subList(i,allDataCounts)));
                    break;
                }
                i += count;
            }
        }
    }

    public EmojiStyle getEmojiStyle(){
        return style;
    }

    public int getPagerCounts(){
        return pagerDataList.size();
    }

    /**
     *
     * @param index 此EmojiStyleWrapper中的pager下标，从0开始
     * @return
     */
    public EmojiFragment getFragment(int index){
        if(index >= getPagerCounts()){
            return null;
        }
        EmojiFragment fragment = this.style.getCustomFragment(index);
        fragment.setEmojiData(pagerDataList.get(index));
        return fragment;
    }

    public String getStyleName(){
        return style.getStyleName();
    }

    public int getStyleIcon(){
        return style.getStyleIcon();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        if(!isSelected){
            curDisplayPageIndex = -1;
        }else {
            curDisplayPageIndex = 0;
        }
    }

    public void setCurDisplayPageIndex(int displayPageIndex){
        curDisplayPageIndex = displayPageIndex;
    }

    public int getCurDisplayPageIndex() {
        if(!isSelected){
            return -1;
        }
        return curDisplayPageIndex;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof EmojiStyleWrapper){
            EmojiStyleWrapper wrapper = (EmojiStyleWrapper) o;
            return wrapper.getStyleName().equals(getStyleName());
        }
        return false;
    }
}
