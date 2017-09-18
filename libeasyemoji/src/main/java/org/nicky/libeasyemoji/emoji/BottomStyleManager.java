package org.nicky.libeasyemoji.emoji;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.nicky.libeasyemoji.emojicon.utils.EmojiUtil;

/**
 * Created by nickyang on 2017/9/18.
 *
 * 底部的item 管理, 注意item 按钮只能添加到最后
 */

public class BottomStyleManager {
    public static final int ITEM_EMOJ = 0; //正常的表情类型
    public static final int ITEM_VIEW = 1; //其他单独的View item类型

    private EmojiStyleWrapperManager styleWrapperManager;
    private Context context;

    public BottomStyleManager(Context context, EmojiStyleWrapperManager styleWrapperManager){
        this.context = context;
        this.styleWrapperManager = styleWrapperManager;
    }

    View onCreateViewHolder(ViewGroup parent, int viewType){
        if(viewType == ITEM_EMOJ){
            ImageButton imageView = new ImageButton(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(EmojiUtil.dip2px(context,40),
                    ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setBackgroundColor(Color.WHITE);
            return imageView;
        }else {
            View view = (View) styleWrapperManager.bottomTypeViews.get(viewType-ITEM_VIEW);
            view.setLayoutParams(new ViewGroup.LayoutParams(EmojiUtil.dip2px(context,40),
                    ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(Color.WHITE);
            return view;
        }
    }

    public int getItemCount(){
        return styleWrapperManager.getValidStyleWrapperCounts() + styleWrapperManager.getBottomTypeViewCounts();
    }

    public int getItemViewType(int position) {
        if(position < styleWrapperManager.getValidStyleWrapperCounts()){
            return ITEM_EMOJ;
        }else {
            return ITEM_VIEW + (position - styleWrapperManager.getValidStyleWrapperCounts());
        }
    }
}
