package org.nicky.libeasyemoji.emoji;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;

import org.nicky.libeasyemoji.R;
import org.nicky.libeasyemoji.emoji.interfaces.EmojiStyle;
import org.nicky.libeasyemoji.emoji.interfaces.EmojiStyleChangeListener;
import org.nicky.libeasyemoji.emoji.interfaces.ViewTab;
import org.nicky.libeasyemoji.emojicon.utils.IndexLinkedHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nickyang on 2017/4/1.
 */

public class EmojiStyleWrapperManager<T extends Parcelable> {
    private IndexLinkedHashMap<String,EmojiStyleWrapper> wrapperMap = new IndexLinkedHashMap<>();
    private List<ButtonViewTab> bottomTypeFrontViews = new ArrayList<>(2);  //放在普通表情前面的自定义按钮
    private List<ButtonViewTab> bottomTypeViews = new ArrayList<>(2);//放在普通表情后面的自定义按钮
    private IndexLinkedHashMap<String,ViewTab> bottomViewTabMap = new IndexLinkedHashMap<>();
    private EmojiStyleChangeListener styleChangeListener;
    private Activity activity;
    private EmojiStyleWrapper curSelectedEmojiStyleWrapper;
    private int tabViewBackgroundColor = -1;  //底部tabView背景颜色
    private int tabViewDividerColor = 0xffcccccc;  //底部tabView分割线颜色
    private int indicatorDefaultImageResource;
    private int indicatorSelectedImageResource;


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

    //获取有数据的style counts, 不显示没有数据的style
    public int getValidStyleWrapperCounts(){
        int counts = 0;
        for(int i = 0;i<wrapperMap.size();i++){
            if(wrapperMap.get(i).getPagerCounts()>0){
                counts ++;
            }
        }
        return counts;
    }

    //纠正表情的position, 因为表情前面有自定义view
    private int calcPosition(int position){
        return position - bottomTypeFrontViews.size();
    }

    //获取emoji底部style的有效的EmojiStyleWrapper
    public EmojiStyleWrapper getStyleWrapperByStyleItem(int position){
        int newPosition = calcPosition(position);
        int counts = -1;
        for(int i = 0;i<wrapperMap.size();i++){
            if(wrapperMap.get(i).getPagerCounts()>0){  //排除没有表情item的类目
                counts ++;
            }
            if(counts == newPosition){
                return wrapperMap.get(i);
            }
        }
        return wrapperMap.get(0);
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

    //根据viewpager中的位置，得到被选择类目的表情在所有表情中的index
    public int getEmojiStyleWrapperIndexByPosition(int position){
        int counts = 0;
        EmojiStyleWrapper wrapper = wrapperMap.get(0);
        for(int i=0;i<wrapperMap.size();i++){
            counts += wrapperMap.get(i).getPagerCounts();
            if(counts > position){
                wrapper = wrapperMap.get(i);
                break;
            }
        }
        return wrapperMap.indexOf(wrapper.getStyleName());
    }

    //根据viewpager中的位置，得到被选择类目的表情在所有底部导航tab中的index
    public int getEmojiStyleTabIndexByVPPosition(int position){
        return bottomTypeFrontViews.size() + getEmojiStyleWrapperIndexByPosition(position);
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
                curSelectedEmojiStyleWrapper = wrapper;
            }else {
                wrapper.setSelected(false);
            }
        }
    }

    //根据viewPager的position得到此pager在它所属的EmojiStyleWrapper中的位置
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

    /**
     *增加底部按钮view,比如搜索
     * @param front  true,  放在普通表情最前面，
     *               false 放在普通表情最后面
     */
    public void addBottomTypeView(View view, boolean front, View.OnClickListener listener){
        if(bottomTypeViews.contains(view)){
            throw new RuntimeException("Cannot add the same view !!!");
        }
        view.setTag(R.id.bottom_item_click,listener);
        ButtonViewTab tab = new ButtonViewTab(view);
        if(front){
            bottomViewTabMap.add(bottomTypeFrontViews.size(), tab.getViewType(), tab);
            bottomTypeFrontViews.add(tab);
        }else {
            bottomTypeViews.add(tab);
            bottomViewTabMap.add(tab.getViewType(), tab);
        }
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
        if(style.getEmojiInterceptor() != null){
            EmojiHandler.getInstance().addInterceptor(style.getEmojiInterceptor());
        }
        // 有表情的表情tab要加入底部tab集合，用于显示此tab
        if(wrapper.getPagerCounts()>0) {
            if(!bottomViewTabMap.containsKey(wrapper.getViewType())) {
                int tabPositon = bottomTypeFrontViews.size() + position;
                bottomViewTabMap.add(tabPositon, wrapper.getViewType(), wrapper);
            }
        }

        if(styleChangeListener != null){
            int curViewPagerItem = -1;
            if(curSelectedEmojiStyleWrapper != null) {
                curViewPagerItem = getViewPageIndexByEmojiStyleName(curSelectedEmojiStyleWrapper.getStyleName())
                        + curSelectedEmojiStyleWrapper.getCurDisplayPageIndex();
            }
            styleChangeListener.update(EmojiStyleChangeListener.TYPE.ADD,wrapper,curViewPagerItem);
        }
    }

    //获取删除某个Style后，ViewPager要选中的item
    private int getAfterDeleteStyleVPItem(String categoryName) {
        int index = wrapperMap.indexOf(categoryName);
        if (index == -1) {
            return index;
        }
        if (index == wrapperMap.size() - 1) {//删除的是最后一项
            //默认会选中剩下的最后一项
            return -1;
        }else {
            if(curSelectedEmojiStyleWrapper == null){
                return -1;
            }
            int curStyleIndex = wrapperMap.indexOf(curSelectedEmojiStyleWrapper.getStyleName());
            if(curStyleIndex == index){ //删除的是当前选中的项
                //下一项的起始页
                index = getViewPageIndexByEmojiStyleName(categoryName);
            }else if(curStyleIndex < index){
                index = -1;
            }else {
                index = getViewPageIndexByEmojiStyleName(curSelectedEmojiStyleWrapper.getStyleName())
                        +curSelectedEmojiStyleWrapper.getCurDisplayPageIndex()
                        - wrapperMap.get(index).getPagerCounts();
            }

        }
        return index;
    }

    public void deleteEmojiStyle(String categoryName) {
        int selectedItem = getAfterDeleteStyleVPItem(categoryName);
        EmojiStyleWrapper wrapper = (EmojiStyleWrapper) wrapperMap.get(categoryName);
        if(wrapper != null){
            wrapperMap.remove(categoryName);
            bottomViewTabMap.remove(wrapper.getViewType());
            if(styleChangeListener != null){
                styleChangeListener.update(EmojiStyleChangeListener.TYPE.DELETE,wrapper,selectedItem);
            }
        }
    }

    //获得当前显示的页在Viewpager中的index
    private int getCurViewPagerItem(){
        return getViewPageIndexByEmojiStyleName(curSelectedEmojiStyleWrapper.getStyleName())+curSelectedEmojiStyleWrapper.getCurDisplayPageIndex();
    }

    private int getAfterUpdateStyleVPItem(EmojiStyle style){
        int index = wrapperMap.indexOf(style.getStyleName());
        if(index == -1){
            return index;
        }
        if(curSelectedEmojiStyleWrapper == null){
            return -1;
        }
        int curStyleIndex = wrapperMap.indexOf(curSelectedEmojiStyleWrapper.getStyleName());
        if(index < curStyleIndex){ //更新的项在当前显示的项前面
            EmojiStyleWrapper tmpWrapper = new EmojiStyleWrapper(style);
            //计算更新前后的页差距
            index = getCurViewPagerItem()- wrapperMap.get(index).getPagerCounts() + tmpWrapper.getPagerCounts();
        }else if(index > curStyleIndex){ //更新的项在当前显示的项后面
            return -1; //默认还是选中以前的项
        }else { //更新的项就是目前选中的项
            EmojiStyleWrapper tmpWrapper = new EmojiStyleWrapper(style);
            if(curSelectedEmojiStyleWrapper.getCurDisplayPageIndex()<=tmpWrapper.getPagerCounts()){
                //如果当前选中的页小于或等于更新后的项的页总数，则依然选中当前页
                index = getCurViewPagerItem();
            }else {
                //否则，选中更新后当前项的页的最后一页
                index = getViewPageIndexByEmojiStyleName(style.getStyleName())+tmpWrapper.getPagerCounts()-1;
            }
        }
        return index;
    }

    public void updateStyle(EmojiStyle style) {
        int selectedItem = getAfterUpdateStyleVPItem(style);
        if(wrapperMap.containsKey(style.getStyleName())){
            int index = wrapperMap.indexOf(style.getStyleName());
            deleteEmojiStyle(style.getStyleName());
            addEmojiStyle(index,style);
            if(styleChangeListener != null){
                styleChangeListener.update(EmojiStyleChangeListener.TYPE.UPDATE,wrapperMap.get(index),selectedItem);
            }
        }else {
            addEmojiStyle(style);
        }
    }

    public void setEmojiStyleChangeListener(EmojiStyleChangeListener listener){
        styleChangeListener = listener;
    }

    public void setTabViewBackgroundColor(int color){
        tabViewBackgroundColor = color;
    }

    public int getTabViewBackgroundColor() {
        return tabViewBackgroundColor;
    }

    public void setTabViewDividerColor(int tabViewDividerColor) {
        this.tabViewDividerColor = tabViewDividerColor;
    }

    public int getTabViewDividerColor() {
        return tabViewDividerColor;
    }

    public void setIndicatorDefaultImageResource(int indicatorDefaultImageResource) {
        this.indicatorDefaultImageResource = indicatorDefaultImageResource;
    }

    public int getIndicatorDefaultImageResource() {
        return indicatorDefaultImageResource;
    }

    public void setIndicatorSelectedImageResource(int indicatorSelectedImageResource) {
        this.indicatorSelectedImageResource = indicatorSelectedImageResource;
    }

    public int getIndicatorSelectedImageResource() {
        return indicatorSelectedImageResource;
    }

    /**
     *  获取每一个 tab 的 view， 用于显示在表情的tab栏
     */
    public ViewTab getTabItemView(Context context, int viewType) {
        return bottomViewTabMap.get(viewType+"");
    }

    public int getTabItemViewType(int position) {
        return Integer.valueOf(bottomViewTabMap.get(position).getViewType());
    }

    public int getTabItemCounts(){
        return bottomViewTabMap.size();
    }

}
