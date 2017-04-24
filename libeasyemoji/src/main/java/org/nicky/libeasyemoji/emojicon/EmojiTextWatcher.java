package org.nicky.libeasyemoji.emojicon;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import org.nicky.libeasyemoji.emoji.EmojiHandler;

/**
 * Created by nickyang on 2017/4/24.
 */

public class EmojiTextWatcher implements TextWatcher {
    private int mEmojiconSize;
    private int mEmojiconAlignment;
    private int mEmojiconTextSize;
    private boolean mUseSystemDefault = false;
    private Context context;
    private int start;
    private int count;


    public EmojiTextWatcher(Context context,int mEmojiconSize, int mEmojiconAlignment,int mEmojiconTextSize,boolean mUseSystemDefault){
        this.context = context;
        this.mEmojiconSize = mEmojiconSize;
        this.mEmojiconAlignment = mEmojiconAlignment;
        this.mEmojiconTextSize = mEmojiconTextSize;
        this.mUseSystemDefault = mUseSystemDefault;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.start = start;
        this.count = count;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length()>0) {
            EmojiHandler.getInstance().handleEmojis(context, s, mEmojiconSize,
                    mEmojiconAlignment, mEmojiconTextSize, start, count, mUseSystemDefault);
        }
    }
}
