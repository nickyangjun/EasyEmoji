package org.nicky.libeasyemoji.emoji.interfaces;

import android.content.Context;
import android.view.View;

/**
 * 表情栏底部的tab
 */
public interface ViewTab {
    int T_EMOJI = 0;  //有表情页的Type
    int T_VIEW = 1;     //没有表情页的，自定义可点击view

    int type();

    /**
     * 底部tab的图标View，tab 被选中时会调用view的 selected 事件
     */
    View getTabView(Context context);

    void setTabSelected(boolean selected);

    /**
     * 用于 recyclerView 的 viewType, 必须唯一,
     * 这里返回的String类型必须可以转为int类型， 这里用String类型是用于与position区分， 因为ViewTab会存入LinkHaspMap集合里
     *
     * @return
     */
    String getViewType();

}
