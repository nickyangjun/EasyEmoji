package org.nicky.libeasyemoji.emoji.interfaces;

import android.text.Spannable;

/**
 * Created by nickyang on 2017/4/5.
 */

public interface EmojiInterceptor {
    Target intercept( Spannable text,int startIndex);
}
