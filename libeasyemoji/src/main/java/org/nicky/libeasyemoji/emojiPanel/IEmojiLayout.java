package org.nicky.libeasyemoji.emojiPanel;


import org.nicky.libeasyemoji.EasyInput.interfaces.OnPanelListener;
import org.nicky.libeasyemoji.emojicon.EmojiconGridFragment;
import org.nicky.libeasyemoji.emojicon.EmojiconsFragment;

/**
 * Created by yangjun1 on 2016/9/14.
 */
public interface IEmojiLayout extends  EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    void displayShadowEmoji();
    void displayEmoji();
    void hideEmoji();
    void hideEmojiDelay(int delayMillis);
    void setOnEmojiListener(OnPanelListener listener);
}
