package org.nicky.libeasyemoji.emoji;

import android.content.Context;
import android.view.View;

import org.nicky.libeasyemoji.emoji.interfaces.ViewTab;

/**
 * 底部 tab 的自定义按钮， 用于 recyclerView 中
 */
public class ButtonViewTab implements ViewTab {
    public View itemView;

    public ButtonViewTab(View itemView) {
        this.itemView = itemView;
    }

    @Override
    public int type() {
        return ViewTab.T_VIEW;
    }

    @Override
    public View getTabView(Context context) {
        return itemView;
    }

    @Override
    public void setTabSelected(boolean selected) {

    }

    @Override
    public String getViewType() {
        return itemView.hashCode() + "";
    }
}
