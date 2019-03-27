package org.nicky.libeasyemoji.EasyInput;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import org.nicky.libeasyemoji.EasyInput.interfaces.EasyInputManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IKeyboardManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IPanelContentManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IPanelLayout;
import org.nicky.libeasyemoji.EasyInput.interfaces.OnKeyboardListener;
import org.nicky.libeasyemoji.EasyInput.interfaces.OnPanelListener;
import org.nicky.libeasyemoji.emoji.interfaces.EmojiStyle;
import org.nicky.libeasyemoji.emoji.EmojiStyleWrapperManager;
import org.nicky.libeasyemoji.emoji.EmojiStylesFragment;
import org.nicky.libeasyemoji.emojicon.EmojiconEditText;
import org.nicky.libeasyemoji.emojicon.emoji.NatureStyle;
import org.nicky.libeasyemoji.emojicon.emoji.PeopleStyle;


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
        this(activity,false);
    }

    /**
     *  注意manifest里面设置的属性会失效，默认被设置成：
     *  WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
     *          | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_（HIDDEN or VISIBLE)
     * @param activity
     * @param isKeyboardShow 进入activity时，输入法默认是否显示
     */
    private EasyInputManagerImpl(Activity activity,boolean isKeyboardShow){
        mContext = activity;
        mKeyboardManager = new KeyboardManagerImpl(activity);
        mIPanelLayout = new IMEPanelLayout(activity,mKeyboardManager);
        mPanelContentManager = PanelContentManager.newInstance(mContext,mIPanelLayout);
        mImeRootLayout = new IMERootLayout(activity,mKeyboardManager, mIPanelLayout.getPanel(),isKeyboardShow);
    }

    /**
     * 调用此方法默认会关闭输入法, 注意manifest里面设置的属性会失效，默认被设置成
     *      WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
     *          | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
     * @param activity
     * @return
     */
    public static EasyInputManager newInstance(Activity activity){
        return new EasyInputManagerImpl(activity);
    }

    /**
     * @param isKeyboardShow  true 默认输入打开
     */
    public static EasyInputManager newInstance(Activity activity,boolean isKeyboardShow){
        return new EasyInputManagerImpl(activity,isKeyboardShow);
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
    public void removeFragmentToPanel(String tag) {
        mPanelContentManager.removeContent(tag);
    }

    @Override
    public void addViewToPanel(String tag, View panelView) {
        mPanelContentManager.addContent(tag,panelView);
    }

    @Override
    public void removeViewToPanel(String tag) {
        mPanelContentManager.removeContent(tag);
    }

    @Override
    public void addDefaultEmoji(String tag,EmojiconEditText emojiconEditText) {
        getEmojiBuilder()
                .setTag(tag)
                .setEmojiconEditText(emojiconEditText)
                .addEmojiStyle(new PeopleStyle())
                .addEmojiStyle(new NatureStyle())
                .build();
    }

    public Builder getEmojiBuilder(){
        if(mBuilder == null){
            synchronized (this) {
                if(mBuilder == null) {
                    mBuilder = new Builder((Activity) mContext,mPanelContentManager);
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
        Activity activity;
        EmojiStylesFragment fragment;
        IPanelContentManager panelContentManager;
        EmojiStyleWrapperManager manger;
        EmojiconEditText text;
        String tag;

        public Builder(Activity activity,IPanelContentManager panelContentManager){
            this.activity = activity;
            this.panelContentManager = panelContentManager;
            manger = new EmojiStyleWrapperManager(activity);
        }

        public Builder setTag(String tag){
            this.tag = tag;
            return this;
        }

        public Builder setEmojiconEditText(EmojiconEditText emojiconEditText){
            text = emojiconEditText;
            return this;
        }

        public Builder addEmojiStyle(EmojiStyle style){
            manger.addEmojiStyle(style);
            return this;
        }

        /**
         *增加底部按钮view,比如搜索
         * @param front  true,  放在普通表情最前面，
         *               false 放在普通表情最后面
         */
        public Builder addBottomTypeView(View view, boolean front, View.OnClickListener listener){
            manger.addBottomTypeView(view, front, listener);
            return this;
        }

        /**
         *增加底部按钮view,比如搜索, 默认放在普通表情最后面
         */
        public Builder addBottomTypeView(View view, View.OnClickListener listener){
            manger.addBottomTypeView(view, false, listener);
            return this;
        }

        public Builder addEmojiStyle(int position, EmojiStyle style){
            manger.addEmojiStyle(position,style);
            return this;
        }

        public Builder deleteEmojiStyle(String tag){
            manger.deleteEmojiStyle(tag);
            return this;
        }

        /**
         * 设置底部tab栏背景颜色
         */
        public Builder setTabViewBackgroundColor(int color){
            manger.setTabViewBackgroundColor(color);
            return this;
        }

        /**
         * 设置底部tab栏分割线颜色， -1，去掉分割线
         */
        public Builder setTabViewDividerColor(int color){
            manger.setTabViewDividerColor(color);
            return this;
        }

        /**
         * 设置表情指示器默认图片
         * @return
         */
        public Builder setIndicatorDefaultImageResource(@DrawableRes int resId){
            manger.setIndicatorDefaultImageResource(resId);
            return this;
        }

        /**
         * 设置表情指示器选中图片
         * @return
         */
        public Builder setIndicatorSelectedImageResource(@DrawableRes int resId){
            manger.setIndicatorSelectedImageResource(resId);
            return this;
        }

        public void updateEmojiStyle(EmojiStyle style){
            manger.updateStyle(style);
        }

        public void build(){
            if(TextUtils.isEmpty(tag)){
                throw new RuntimeException("please invoke setTag() method first to set panel tag !!!");
            }
            if(fragment != null){
                throw new RuntimeException("don't need invoke build() method twice to the same Builder instance !!!");
            }
            EmojiStylesFragment fragment = (EmojiStylesFragment) ((FragmentActivity)activity)
                    .getSupportFragmentManager()
                    .findFragmentByTag(tag);
            if(fragment == null) {
                fragment = EmojiStylesFragment.newInstance();
            }
            if(text != null) {
                fragment.setEmojiconEditText(text);
            }
            fragment.setEmojiStyleWrapperManager(manger);
            panelContentManager.addContent(tag,fragment);
        }
    }
}
