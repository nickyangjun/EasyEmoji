package org.nicky.libeasyemoji.EasyInput;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import org.nicky.libeasyemoji.EasyInput.interfaces.EasyInputManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IKeyboardManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IPanelContentManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IPanelLayout;
import org.nicky.libeasyemoji.EasyInput.interfaces.OnKeyboardListener;
import org.nicky.libeasyemoji.EasyInput.interfaces.OnPanelListener;
import org.nicky.libeasyemoji.emojicon.CategoriesFragment;
import org.nicky.libeasyemoji.emojicon.CategoryDataManagerImpl;
import org.nicky.libeasyemoji.emojicon.EmojiconEditText;
import org.nicky.libeasyemoji.emojicon.emoji.NatureCategory;
import org.nicky.libeasyemoji.emojicon.emoji.PeopleCategory;
import org.nicky.libeasyemoji.emojicon.interfaces.BaseCategory;
import org.nicky.libeasyemoji.emojicon.interfaces.CategoryDataManger;


/**
 * Created by nickyang on 2017/3/22.
 */

public class EasyInputManagerImpl implements EasyInputManager {
    private Context mContext;
    private IMERootLayout mImeRootLayout;
    private IKeyboardManager mKeyboardManager;
    private IPanelLayout mIPanelLayout;
    private IPanelContentManager mPanelContentManager;
    private volatile Builder mBuilder;

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
        openPanel(null);
    }

    @Override
    public void openPanel(String tag) {
        mPanelContentManager.openPanel(tag);
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
    public void addDefaultEmoji(String tag,EmojiconEditText emojiconEditText) {
        getEmojiBuilder()
                .setTag(tag)
                .setEmojiconEditText(emojiconEditText)
                .addEmojiCategory(new PeopleCategory())
                .addEmojiCategory(new NatureCategory()).build();
    }

    public Builder getEmojiBuilder(){
        if(mBuilder == null){
            synchronized (this) {
                if(mBuilder == null) {
                    mBuilder = new Builder(mPanelContentManager);
                }
            }
        }
        return mBuilder;
    }

    @Override
    public String getCurrentPanelDisplayTag() {
        return mPanelContentManager.getCurrentPanelDisplayTag();
    }

    @Override
    public boolean isKeyboardShowing() {
        return mKeyboardManager.isKeyboardShowing();
    }

    @Override
    public void addOnKeyboardIMEListener(OnKeyboardListener listener) {
        mImeRootLayout.addOnKeyboardIMEListener(listener);
    }

    @Override
    public void addOnPanelListener(OnPanelListener listener) {
        mIPanelLayout.addOnPanelListener(listener);
    }

    public static class Builder<T extends Parcelable>{
        CategoriesFragment fragment;
        IPanelContentManager panelContentManager;
        CategoryDataManger<T> manger;
        EmojiconEditText text;
        String tag;

        public Builder(IPanelContentManager panelContentManager){
            this.panelContentManager = panelContentManager;
            manger = CategoryDataManagerImpl.newInstance();
        }

        public Builder setTag(String tag){
            this.tag = tag;
            return this;
        }

        public Builder setEmojiconEditText(EmojiconEditText emojiconEditText){
            text = emojiconEditText;
            return this;
        }

        public Builder addEmojiCategory(BaseCategory category){
            manger.addCategory(category);
            return this;
        }

        public Builder deleteEmojiCategory(String tag){
            manger.deleteCategory(tag);
            return this;
        }

        public Builder updateEmojiCategory(BaseCategory category){
            manger.updateCategory(category);
            return this;
        }

        public void build(){
            if(TextUtils.isEmpty(tag)){
                throw new RuntimeException("please invoke setTag() method first to set panel tag !!!");
            }
            if(fragment != null){
                throw new RuntimeException("don't need invoke build() method twice to the same Builder instance !!!");
            }
            fragment = CategoriesFragment.newInstance();
            if(text != null) {
                fragment.setEmojiconEditText(text);
            }
            fragment.setCategoryDataManager(manger);
            panelContentManager.addContent(tag,fragment);
        }
    }
}
