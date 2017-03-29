package org.nicky.libeasyemoji.emojicon.interfaces;

import org.nicky.libeasyemoji.emojicon.emoji.Emojicon;

import java.util.List;

/**
 * Created by nickyang on 2017/3/27.
 */

public abstract class EmojiCategory implements BaseCategory<Emojicon>{
    @Override
    public abstract String getCategoryName();

    /**
     * 返回表情种类的icon
     * @return  资源id
     */
    @Override
    public abstract int getCategoryItemIcon();

    @Override
    public abstract List<Emojicon> getEmojiData();

    @Override
    public int getColumn(){
        return 7;
    }

    @Override
    public int getRow(){
        return 3;
    }

    /**
     * 是否支持回退按钮
     * @return
     */
    @Override
    public boolean hasBackspace(){
        return true;
    }
}
