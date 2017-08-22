package org.nicky.libeasyemoji.emoji;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Spannable;

import org.nicky.libeasyemoji.emoji.interfaces.EmojiInterceptor;
import org.nicky.libeasyemoji.emoji.interfaces.Target;
import org.nicky.libeasyemoji.emojicon.EmojiconSpan;
import org.nicky.libeasyemoji.emojicon.Emojicons;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nickyang on 2017/4/5.
 */

public class EmojiHandler {
    HashMap<String, EmojiInterceptor> interceptors = new LinkedHashMap<>();

    static class Holder {
        static final EmojiHandler INSTANCE = new EmojiHandler();
    }

    private EmojiHandler() {
    }

    public static EmojiHandler getInstance() {
        return Holder.INSTANCE;
    }

    public void addInterceptor(EmojiInterceptor interceptor) {
        if(!this.interceptors.containsKey(interceptor.getClass().getSimpleName())) {
            this.interceptors.put(interceptor.getClass().getSimpleName(),interceptor);
        }
    }

    public void handleEmojis(Context context, Spannable text, int emojiSize, int emojiAlignment,
                             int textSize, int index, int length, boolean useSystemDefault){
        if (useSystemDefault || text == null) {
            return;
        }

        int textLength = text.length();
        int textLengthToProcessMax = textLength - index;
        int textLengthToProcess = (length < 0 || length > textLengthToProcessMax) ? textLength : length;

        if(length>1) {
            EmojiconSpan[] oldSpans = text.getSpans(index, index + length, EmojiconSpan.class);
            for (int i = 0; i < oldSpans.length; i++) {
                text.removeSpan(oldSpans[i]);
            }
        }

        int skip;
        for (int i = index; i < index+textLengthToProcess; i += skip) {
            skip = 0;
            int icon = 0;
            char c = text.charAt(i);

            for(EmojiInterceptor interceptor: interceptors.values()){
                Target target = interceptor.intercept(text,i);
                if(target!= null){
                    icon = target.getIcon();
                    skip = target.getEmoji().length();
                    break;
                }
            }


            if (icon == 0 && Emojicons.isSoftBankEmoji(c)) {
                icon = Emojicons.getSoftbankEmojiResource(c);
                skip = icon == 0 ? 0 : 1;
            }

            if (icon == 0) {
                int unicode = Character.codePointAt(text, i);
                skip = Character.charCount(unicode);

                if (unicode > 0xff) {
                    icon = Emojicons.getEmojiResource(context, unicode);
                }

                if (i + skip < textLengthToProcess) {
                    int followUnicode = Character.codePointAt(text, i + skip);
                    //Non-spacing mark (Combining mark)
                    if (followUnicode == 0xfe0f) {
                        int followSkip = Character.charCount(followUnicode);
                        if (i + skip + followSkip < textLengthToProcess) {

                            int nextFollowUnicode = Character.codePointAt(text, i + skip + followSkip);
                            if (nextFollowUnicode == 0x20e3) {
                                int nextFollowSkip = Character.charCount(nextFollowUnicode);
                                int tempIcon = Emojicons.getKeyCapEmoji(unicode);

                                if (tempIcon == 0) {
                                    followSkip = 0;
                                    nextFollowSkip = 0;
                                } else {
                                    icon = tempIcon;
                                }
                                skip += (followSkip + nextFollowSkip);
                            }
                        }
                    } else if (followUnicode == 0x20e3) {
                        //some older versions of iOS don't use a combining character, instead it just goes straight to the second part
                        int followSkip = Character.charCount(followUnicode);

                        int tempIcon = Emojicons.getKeyCapEmoji(unicode);
                        if (tempIcon == 0) {
                            followSkip = 0;
                        } else {
                            icon = tempIcon;
                        }
                        skip += followSkip;

                    } else if (Emojicons.sEmojiModifiersMap.get(followUnicode, 0) > 0) {
                        //handle other emoji modifiers
                        int followSkip = Character.charCount(followUnicode);


                        //TODO seems like we could do this for every emoji type rather than having that giant static map, maybe this is too slow?
                        String hexUnicode = Integer.toHexString(unicode);
                        String hexFollowUnicode = Integer.toHexString(followUnicode);

                        String resourceName = "emoji_" + hexUnicode + "_" + hexFollowUnicode;

                        int resourceId = 0;
                        if (Emojicons.sEmojisModifiedMap.containsKey(resourceName)) {
                            resourceId = Emojicons.sEmojisModifiedMap.get(resourceName);
                        } else {
                            resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getApplicationContext().getPackageName());
                            if (resourceId != 0) {
                                Emojicons.sEmojisModifiedMap.put(resourceName, resourceId);
                            }
                        }

                        if (resourceId == 0) {
                            followSkip = 0;
                        } else {
                            icon = resourceId;
                        }
                        skip += followSkip;
                    }
                }
            }

            if (icon > 0) {
                text.setSpan(new EmojiconSpan(context, icon, emojiSize, emojiAlignment, textSize), i, i + skip, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
