package org.nicky.libeasyemoji.emojiPanel;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;

import org.nicky.libeasyemoji.EasyInput.interfaces.OnPanelListener;
import org.nicky.libeasyemoji.EasyInput.utils.ConvertUtil;
import org.nicky.libeasyemoji.EasyInput.utils.Utils;
import org.nicky.libeasyemoji.emojicon.EmojiconEditText;
import org.nicky.libeasyemoji.emojicon.EmojiconsFragment;
import org.nicky.libeasyemoji.emojicon.JZEmojiconsFragment;
import org.nicky.libeasyemoji.emojicon.emoji.Emojicon;


/**
 * Created by yangjun1 on 2016/9/14.
 */
public class EmojiFrameLayout extends FrameLayout implements IEmojiLayout {
    private Context mContext;
    private EmojiconEditText mEmojiconEditText;
    private JZEmojiconsFragment mJZEmojiconsFragment;
    private OnPanelListener mListener;
    private int mKeyboardHeight;
    private boolean isOpen;

    public EmojiFrameLayout(Context context,EmojiconEditText emojiconEditText){
        super(context);
        mContext = context;
        mEmojiconEditText = emojiconEditText;
        initView();
    }

    private void initView(){
        setVisibility(GONE);
//        setId(R.id.emoji_container);
        setEmojiconFragment(false);
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        if(mContext instanceof FragmentActivity){
            mJZEmojiconsFragment = JZEmojiconsFragment.newInstance(useSystemDefault);
            mJZEmojiconsFragment.setOnEmojiconBackspaceClickedListener(this);
            mJZEmojiconsFragment.setOnEmojiconClickedListener(this);
            ((FragmentActivity)mContext).getSupportFragmentManager()
                    .beginTransaction()
//                    .replace(R.id.emoji_container,mJZEmojiconsFragment)
                    .commit();
        }
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mEmojiconEditText);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mEmojiconEditText, emojicon);
    }

    @Override
    public void displayShadowEmoji() {
        if(mEmojiconEditText == null){
            return;
        }
        mKeyboardHeight = Utils.getKeyboardHeight(mContext, ConvertUtil.dp2px(mContext,260));
        if (getHeight() != mKeyboardHeight) {
            getLayoutParams().height = mKeyboardHeight;
            requestLayout();
        }
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }
        isOpen = false;
    }

    @Override
    public void displayEmoji(){
        displayShadowEmoji();
        isOpen = true;
        if(mListener != null){
//            mListener.onPanelDisplay();
        }
    }

    @Override
    public void hideEmojiDelay(int delayMillis){
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideEmoji();
            }
        },delayMillis);
    }

    @Override
    public void hideEmoji(){
        if(mEmojiconEditText == null){
            return;
        }
        isOpen = false;
        this.setVisibility(GONE);
        if(mListener != null){
//            mListener.onEmojiHide();
        }
    }

    //输入法在顶层覆盖表情页
    public void fakeHideEmoji(){
        if(mEmojiconEditText == null){
            return;
        }
        isOpen = false;
        if(mListener != null){
//            mListener.onEmojiHide();
        }
    }

    public boolean isOpen(){
        return isOpen;
    }

    @Override
    public void setOnEmojiListener(OnPanelListener listener){
        mListener = listener;
    }
}
