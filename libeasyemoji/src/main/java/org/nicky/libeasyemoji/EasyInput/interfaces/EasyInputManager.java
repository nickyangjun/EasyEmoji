package org.nicky.libeasyemoji.EasyInput.interfaces;

import androidx.fragment.app.Fragment;
import android.view.View;

import org.nicky.libeasyemoji.EasyInput.EasyInputManagerImpl;
import org.nicky.libeasyemoji.emojicon.EmojiconEditText;


/**
 * Created by nickyang on 2017/3/22.
 */

public interface EasyInputManager {
    void openKeyboard(View view);
    void closeKeyboard(View view);
    void openPanel();
    void openPanel(String tag);
    void closePanel();
    void addOnPanelListener(OnPanelListener listener);
    void addOnKeyboardIMEListener(OnKeyboardListener listener);
    void setTouchBlankAutoHideIME(boolean autoHideIME, int offsetPixel);
    void addFragmentToPanel(String tag, Fragment panelFragment);
    void removeFragmentToPanel(String tag);
    void addViewToPanel(String tag, View panelView);
    void removeViewToPanel(String tag);
    void addDefaultEmoji(String tag,EmojiconEditText emojiconEditText);
    EasyInputManagerImpl.Builder getEmojiBuilder();
    String getCurrentPanelDisplayTag();
    boolean isKeyboardShowing();
}
