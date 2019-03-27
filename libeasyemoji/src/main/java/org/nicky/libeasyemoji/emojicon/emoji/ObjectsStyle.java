package org.nicky.libeasyemoji.emojicon.emoji;

import android.content.Context;
import android.view.View;

import org.nicky.libeasyemoji.R;
import org.nicky.libeasyemoji.emoji.EmojiFragment;
import org.nicky.libeasyemoji.emoji.interfaces.EmojiInterceptor;
import org.nicky.libeasyemoji.emoji.interfaces.EmojiStyle;
import org.nicky.libeasyemoji.emoji.interfaces.PageEmojiStyle;
import org.nicky.libeasyemoji.emojicon.EmojiconFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nickyang on 2017/3/27.
 */

public class ObjectsStyle implements EmojiStyle {

    List<Emojicon> emojiconList;
    public ObjectsStyle(){
        emojiconList = new ArrayList<>(Arrays.asList(Emojicon.getEmojicons(Emojicon.TYPE_OBJECTS)));
    }

    @Override
    public String getStyleName() {
        return "object";
    }

    @Override
    public int getStyleIcon() {
        return R.drawable.ic_emoji_bell;
    }

    @Override
    public View getStyleIconView(Context context) {
        return null;
    }

    @Override
    public List<Emojicon> getEmojiData() {
        return emojiconList;
    }

    @Override
    public EmojiFragment getCustomFragment(int index) {
        return new EmojiconFragment();
    }

    @Override
    public PageEmojiStyle getPagerData(int index) {
        return new EmojiconPageEmojiStyle();
    }

    @Override
    public EmojiInterceptor getEmojiInterceptor() {
        return null;
    }
}
