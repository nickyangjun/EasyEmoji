package org.nicky.libeasyemoji.EasyInput.interfaces;

import android.app.Activity;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by nickyang on 2017/3/23.
 */

public interface IKeyboardManager {
    boolean isKeyboardShowing();
    void setKeyboardShowing(boolean isShowing);
    void openKeyboard(View view);
    void closeKeyboard(View view);
    void closeKeyboard(Activity activity);
    ViewTreeObserver.OnGlobalLayoutListener attach(final Activity activity, IPanelLayout target, OnKeyboardListener listener);
    void detach(Activity activity, ViewTreeObserver.OnGlobalLayoutListener l);
}
