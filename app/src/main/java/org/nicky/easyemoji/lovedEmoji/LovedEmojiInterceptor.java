package org.nicky.easyemoji.lovedEmoji;

import android.text.Spannable;

import org.nicky.libeasyemoji.emoji.interfaces.EmojiInterceptor;
import org.nicky.libeasyemoji.emoji.interfaces.Target;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nickyang on 2017/4/5.
 */

public class LovedEmojiInterceptor implements EmojiInterceptor {

    @Override
    public Target intercept(Spannable text, int startIndex) {
        for(int i=startIndex;i<text.length();i++) {
            int unicode = Character.codePointAt(text, i);
            int skip = Character.charCount(unicode);
            String pattern = "^(\\[(\\w|[\\u4e00-\\u9fa5]){1,4}\\])";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(text.subSequence(startIndex,text.length()));
            if(m.find()){
                String match = m.group();
                if(LovedEmojicons.emojiconMap.containsKey(match)) {
                    Target target = new LovedTarget(LovedEmojicons.emojiconMap.get(match),match);
                    return target;
                }
            }
        }

        return null;
    }


    public static class LovedTarget implements Target{
        int icon;
        String emoji;

        LovedTarget(int icon,String emoji){
            this.icon = icon;
            this.emoji = emoji;
        }

        @Override
        public int getIcon() {
            return icon;
        }

        @Override
        public String getEmoji() {
            return emoji;
        }
    }

}
