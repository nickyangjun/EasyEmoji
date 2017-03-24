package org.nicky.libeasyemoji.EasyInput;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import org.nicky.libeasyemoji.EasyInput.interfaces.EasyInputManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IKeyboardManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IPanelContentManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IPanelLayout;
import org.nicky.libeasyemoji.EasyInput.interfaces.OnKeyboardListener;
import org.nicky.libeasyemoji.EasyInput.interfaces.OnPanelListener;
import org.nicky.libeasyemoji.emojicon.EmojiconEditText;
import org.nicky.libeasyemoji.emojicon.JZEmojiconsFragment;


/**
 * Created by nickyang on 2017/3/22.
 */

public class EasyInputManagerImpl implements EasyInputManager {
    private Context mContext;
    private IMERootLayout mImeRootLayout;
    private IKeyboardManager mKeyboardManager;
    private IPanelLayout mIPanelLayout;
    private IPanelContentManager mPanelContentManager;

    private EasyInputManagerImpl(Activity activity){
        mContext = activity;
        mKeyboardManager = new KeyboardManagerImpl(activity);
        mIPanelLayout = new IMEPanelLayout(activity,mKeyboardManager);
        mPanelContentManager = PanelContentManager.newInstance(mContext,mIPanelLayout);
        mImeRootLayout = new IMERootLayout(activity,mKeyboardManager, mIPanelLayout.getPanel());
    }

    public static EasyInputManager newInstance(Activity activity){
        return new EasyInputManagerImpl(activity);
    }

    @Override
    public void openKeyboard(View view) {
        mKeyboardManager.openKeyboard(view);
    }

    @Override
    public void closeKeyboard(View view) {
        mKeyboardManager.closeKeyboard(view);
    }

    @Override
    public void openPanel() {
        mPanelContentManager.openPanel();
        if(mKeyboardManager.isKeyboardShowing()){
            mKeyboardManager.closeKeyboard((Activity) mContext);
        }
    }

    @Override
    public void closePanel() {
        mPanelContentManager.closePanel();
    }

    @Override
    public void setTouchBlankAutoHideIME(boolean autoHideIME, int offsetPixel) {
        mImeRootLayout.setTouchBlankAutoHideIME(autoHideIME,offsetPixel);
    }

    @Override
    public void addFragmentToPanel(String tag, Fragment panelFragment) {
        mPanelContentManager.addContent(tag,panelFragment);
    }

    @Override
    public void addViewToPanel(String tag, View panelView) {
        mPanelContentManager.addContent(tag,panelView);
    }

    @Override
    public void setPanelCurrentViewFromTag(String tag) {

    }

    @Override
    public void addDefaultEmoji(EmojiconEditText emojiconEditText) {
        Fragment fragment = JZEmojiconsFragment.newInstance(false);
        mPanelContentManager.addContent("emojicon",fragment);
    }

    @Override
    public void addOnKeyboardIMEListener(OnKeyboardListener listener) {
        mImeRootLayout.addOnKeyboardIMEListener(listener);
    }

    @Override
    public void addOnPanelListener(OnPanelListener listener) {
        mIPanelLayout.addOnPanelListener(listener);
    }
}