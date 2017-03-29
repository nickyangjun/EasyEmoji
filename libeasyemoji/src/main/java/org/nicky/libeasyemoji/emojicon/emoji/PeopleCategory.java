package org.nicky.libeasyemoji.emojicon.emoji;

import android.os.Bundle;
import android.support.v4.app.Fragment;

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

public class PeopleCategory extends EmojiCategory {

    List<Emojicon> emojiconList;
    public PeopleCategory(){
        emojiconList = new ArrayList<>(Arrays.asList(Emojicon.getEmojicons(Emojicon.TYPE_PEOPLE)));
    }

    @Override
    public String getCategoryName() {
        return "people";
    }

    @Override
    public int getCategoryItemIcon() {
        return R.drawable.ic_emoji_smile;
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
