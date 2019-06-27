package org.nicky.libeasyemoji.emoji;

import android.content.Context;
import android.view.ViewGroup;

import org.nicky.libeasyemoji.emoji.interfaces.ViewTab;

/**
 * Created by nickyang on 2017/9/18.
 * <p>
 * 底部的 tab item 管理，包括表情和自定义添加的按钮, 注意 自定义添加的 item 按钮只能添加到最后
 */

public class BottomStyleManager {

    private EmojiStyleWrapperManager styleWrapperManager;
    private Context context;

    public BottomStyleManager(Context context, EmojiStyleWrapperManager styleWrapperManager) {
        this.context = context;
        this.styleWrapperManager = styleWrapperManager;
    }

    ViewTab onCreateViewHolder(ViewGroup parent, int viewType) {
        return styleWrapperManager.getTabItemView(context, parent, viewType);
    }

    public int getItemCount() {
        return styleWrapperManager.getTabItemCounts();
    }

    public int getItemViewType(int position) {
        return styleWrapperManager.getTabItemViewType(position);
    }
}
