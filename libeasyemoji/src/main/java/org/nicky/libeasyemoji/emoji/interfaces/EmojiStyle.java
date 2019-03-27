package org.nicky.libeasyemoji.emoji.interfaces;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;

import org.nicky.libeasyemoji.emoji.EmojiFragment;

import java.util.List;

/**
 * Created by nickyang on 2017/4/1.
 */

public interface EmojiStyle<T extends Parcelable> {
    /**
     * 名称tag, 必须唯一
     */
    String getStyleName();

    /**
     * 底部tab的图标
     */
    int getStyleIcon();

    /**
     * 底部tab的图标， 如果不为空，优先使用， 如果为空则使用 getStyleIcon() 返回的图标，
     * tab 被选中时会调用view的 selected 事件
     */
    View getStyleIconView(Context context);

    /**
     * 所有emoji的数据源
     */
    List<T> getEmojiData();

    /**
     * 自定义的emoji显示页面， 一个 EmojiFragment 页面对应 一个 PageEmojiStyle 对象
     * EmojiFragment 为展示页面， PageEmojiStyle 为一个页面的数据源
     *
     * @param index
     * @return
     */
    EmojiFragment getCustomFragment(int index);

    /**
     * 一个显示页面的数据源
     *
     * @param index
     * @return
     */
    PageEmojiStyle getPagerData(int index);

    /**
     * 文本拦截事件， 用于处理自定义表情
     *
     * @return
     */
    EmojiInterceptor getEmojiInterceptor();
}
