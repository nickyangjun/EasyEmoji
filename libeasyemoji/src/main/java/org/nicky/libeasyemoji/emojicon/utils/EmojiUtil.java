package org.nicky.libeasyemoji.emojicon.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by nickyang on 2017/3/27.
 */

public class EmojiUtil {
    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void input(EditText editText, String emoji) {
        if (editText == null || TextUtils.isEmpty(emoji)) {
            return;
        }
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        if (start < 0) {
            editText.append(emoji);
        } else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                int length = getEditTextMaxLength(editText);
                if(start+emoji.length() > length){
                    return;
                }
            }
            editText.getText().replace(Math.min(start, end), Math.max(start, end), emoji, 0, emoji.length());
            if(editText.getText().length() < start+emoji.length()){
                editText.getText().delete(Math.min(start, end), editText.getText().length());
            }
        }
    }

    public static void backspace(EditText editText) {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }

    public static int dip2px(Context context, int dp) {
        //dp和px的转换关系
        float density = context.getResources().getDisplayMetrics().density;
        //2*1.5+0.5  2*0.75 = 1.5+0.5
        return (int)(dp*density+0.5);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getEditTextMaxLength(EditText editText){
        int length=0;
        InputFilter[] inputFilters = editText.getFilters();
        for(InputFilter filter:inputFilters){
            if(filter instanceof InputFilter.LengthFilter){
                length = ((InputFilter.LengthFilter) filter).getMax();
            }
        }
        return length;
    }
}
