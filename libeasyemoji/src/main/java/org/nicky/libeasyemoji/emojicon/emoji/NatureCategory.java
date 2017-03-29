package org.nicky.libeasyemoji.emojicon.emoji;

import android.os.Bundle;

import org.nicky.libeasyemoji.R;
import org.nicky.libeasyemoji.emojicon.BaseCategoryFragment;
import org.nicky.libeasyemoji.emojicon.EmojiCategoryFragment;
import org.nicky.libeasyemoji.emojicon.PagerDataCategory;
import org.nicky.libeasyemoji.emojicon.interfaces.EmojiCategory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nickyang on 2017/3/27.
 */

public class NatureCategory extends EmojiCategory {
    @Override
    public String getCategoryName() {
        return "nature";
    }

    @Override
    public int getCategoryItemIcon() {
        return R.drawable.ic_emoji_nature_light;
    }

    @Override
    public List<Emojicon> getEmojiData() {
        return Arrays.asList(Emojicon.getEmojicons(Emojicon.TYPE_NATURE));
    }

    @Override
    public BaseCategoryFragment<Emojicon> getFragment(PagerDataCategory category) {
        EmojiCategoryFragment fragment = new EmojiCategoryFragment();
        fragment.setArguments(category);
        return fragment;
    }


}
