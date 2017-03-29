package org.nicky.libeasyemoji.emojicon.emoji;

import android.os.Bundle;

import org.nicky.libeasyemoji.R;
import org.nicky.libeasyemoji.emojicon.BaseCategoryFragment;
import org.nicky.libeasyemoji.emojicon.EmojiCategoryFragment;
import org.nicky.libeasyemoji.emojicon.PagerDataCategory;
import org.nicky.libeasyemoji.emojicon.interfaces.EmojiCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nickyang on 2017/3/27.
 */

public class NatureCategory extends EmojiCategory {
    List<Emojicon> emojiconList;
    public NatureCategory(){
        emojiconList = new ArrayList<>(Arrays.asList(Emojicon.getEmojicons(Emojicon.TYPE_NATURE)));
    }

    @Override
    public String getCategoryName() {
        return "nature";
    }

    @Override
    public int getCategoryItemIcon() {
        return R.drawable.ic_emoji_flower;
    }

    @Override
    public List<Emojicon> getEmojiData() {
        return emojiconList;
    }

    @Override
    public BaseCategoryFragment<Emojicon> getFragment(PagerDataCategory category) {
        EmojiCategoryFragment fragment = new EmojiCategoryFragment();
        fragment.setArguments(category);
        return fragment;
    }


}
