package org.nicky.easyemoji.lovedEmoji;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.nicky.easyemoji.R;
import org.nicky.libeasyemoji.emoji.EmojiFragment;
import org.nicky.libeasyemoji.emoji.interfaces.EmojiInterceptor;
import org.nicky.libeasyemoji.emoji.interfaces.EmojiStyle;
import org.nicky.libeasyemoji.emoji.interfaces.PageEmojiStyle;

import java.util.List;

/**
 * Created by nickyang on 2017/4/5.
 */

public class LovedEmojiStyle implements EmojiStyle {

    LovedEmojicons emojicons;

    public LovedEmojiStyle() {
        emojicons = new LovedEmojicons();
    }

    @Override
    public String getStyleName() {
        return "loved";
    }

    @Override
    public int getStyleIcon() {
        return R.drawable.sg001;
    }

    @Override
    public View getStyleIconView(Context context) {
        ImageButton image = new ImageButton(context);
        image.setImageResource(R.drawable.sg003);
        image.setBackgroundResource(R.drawable.love_emojicon);
        image.setLayoutParams(new ViewGroup.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT));
        return image;
    }

    @Override
    public List getEmojiData() {
        return emojicons.getEmojis();
    }

    @Override
    public EmojiFragment getCustomFragment(int index) {
        return new LovedEmojiFragment<>();
    }

    @Override
    public PageEmojiStyle getPagerData(int index) {
        return new LovedPageEmojiStyle();
    }

    @Override
    public EmojiInterceptor getEmojiInterceptor() {
        return new LovedEmojiInterceptor();
    }
}
