package org.nicky.libeasyemoji.EasyInput.interfaces;

import android.support.v4.app.Fragment;
import android.view.View;

import org.nicky.libeasyemoji.emojicon.EmojiconEditText;


/**
 * Created by nickyang on 2017/3/22.
 */

public interface EasyInputManager {
    void openKeyboard(View view);
    void closeKeyboard(View view);
    void openPanel();
    void closePanel();
    void addOnPanelListener(OnPanelListener listener);
    void addOnKeyboardIMEListener(OnKeyboardListener listener);
    void setTouchBlankAutoHideIME(boolean autoHideIME, int offsetPixel);
    void addFragmentToPanel(String tag, Fragment panelFragment);
    void addViewToPanel(String tag, View panelView);
    void setPanelCurrentViewFromTag(String tag);
    void addDefaultEmoji(EmojiconEditText emojiconEditText);
}
