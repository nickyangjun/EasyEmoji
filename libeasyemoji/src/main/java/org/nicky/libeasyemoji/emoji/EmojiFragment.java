package org.nicky.libeasyemoji.emoji;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.nicky.libeasyemoji.emoji.interfaces.PageEmojiStyle;

/**
 * Created by nickyang on 2017/4/1.
 */

public abstract class EmojiFragment<T extends Parcelable> extends Fragment {

    protected OnEmojiconClickedListener mOnEmojiconClickedListener;

    protected Bundle saveState = new Bundle();

    final public void setEmojiData(PageEmojiStyle<T> emojiData){
        saveState.putParcelable("emojiData",emojiData);
        setData(emojiData);
    }

    public abstract void setData(PageEmojiStyle<T> emojiData);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreData(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle("saveState",saveState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        restoreData(savedInstanceState);
    }

    private void restoreData(Bundle savedInstanceState){
        if(savedInstanceState!=null && saveState.getParcelable("emojiData") == null){
            Bundle data = (Bundle) savedInstanceState.get("saveState");
            if(data != null&& data.getParcelable("emojiData")!=null){
                setEmojiData((PageEmojiStyle) data.getParcelable("emojiData"));
            }
        }
    }

    public void setOnEmojiconClickedListener(OnEmojiconClickedListener listener){
        mOnEmojiconClickedListener = listener;
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(String emojiCode);
        void onBackspaceClicked();
    }
}
