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

public class ObjectsCategory extends EmojiCategory {

    @Override
    public String getCategoryName() {
        return "objects";
    }

    @Override
    public int getCategoryItemIcon() {
        return R.drawable.ic_emoji_bell;
    }

    @Override
    public List<Emojicon> getEmojiData() {
        return Arrays.asList(Emojicon.getEmojicons(Emojicon.TYPE_OBJECTS));
    }

    @Override
    public BaseCategoryFragment<Emojicon> getFragment(PagerDataCategory category) {
        EmojiCategoryFragment fragment = new EmojiCategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable("data",category);
        fragment.setArguments(args);
        return fragment;
    }


}
