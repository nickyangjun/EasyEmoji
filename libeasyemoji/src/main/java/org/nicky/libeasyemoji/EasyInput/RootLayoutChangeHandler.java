package org.nicky.libeasyemoji.EasyInput;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;

import org.nicky.libeasyemoji.EasyInput.interfaces.IKeyboardManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IPanelLayout;
import org.nicky.libeasyemoji.EasyInput.utils.StatusBarHeightUtil;
import org.nicky.libeasyemoji.EasyInput.utils.ViewUtil;


/**
 * Created by nickyang on 2017/3/22.
 */

public class RootLayoutChangeHandler {
    private final View mIMELayoutView;
    private int mOldHeight = -1;
    private final int mStatusBarHeight;
    private final boolean mIsTranslucentStatus;
    private IKeyboardManager mKeyboardManager;
    IPanelLayout mPanelManager;

    public RootLayoutChangeHandler(final View imeLayout, IPanelLayout panelManager, IKeyboardManager keyboardManager){
        mIMELayoutView = imeLayout;
        this.mStatusBarHeight = StatusBarHeightUtil.getStatusBarHeight(mIMELayoutView.getContext());
        this.mIsTranslucentStatus = ViewUtil.isTranslucentStatus((Activity) mIMELayoutView.getContext());
        mKeyboardManager = keyboardManager;
        mPanelManager = panelManager;
    }

    public void handleBeforeMeasure(final int width, int height) {
        // 布局变化
        if (mIsTranslucentStatus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (mIMELayoutView.getFitsSystemWindows()) {
                final Rect rect = new Rect();
                mIMELayoutView.getWindowVisibleDisplayFrame(rect);
                height = rect.bottom - rect.top;
            }
        }

        if (height < 0) {
            return;
        }

        if (mOldHeight < 0) {
            mOldHeight = height;
            return;
        }

        final int offset = mOldHeight - height;

        if (offset == 0) {
            return;
        }

        if (Math.abs(offset) == mStatusBarHeight) {
            // 极有可能是 相对本页面的二级页面的主题是全屏&是透明，但是本页面不是全屏，因此会有status bar的布局变化差异，进行调过
            // 极有可能是 该布局采用了透明的背景(windowIsTranslucent=true)，而背后的布局`full screen`为false，
            // 因此有可能第一次绘制时没有attach上status bar，而第二次status bar attach上去，导致了这个变化。
            return;
        }

        mOldHeight = height;
        if (mPanelManager.getPanel() == null) {
            return;
        }

        // 检测到布局变化非键盘引起
        if (Math.abs(offset) < KeyboardManagerImpl.getMinKeyboardHeight(mIMELayoutView.getContext())) {
            return;
        }

        if (offset > 0) {   //键盘弹起 (offset > 0，高度变小)
            mPanelManager.setHide();
        } else if (mKeyboardManager.isKeyboardShowing() || mPanelManager.isVisible()) {
            // 1. 总得来说，在监听到键盘已经显示的前提下，键盘收回才是有效有意义的。
            // 2. 修复在Android L下使用V7.Theme.AppCompat主题，进入Activity，默认弹起面板bug，
            // 第2点的bug出现原因:在Android L下使用V7.Theme.AppCompat主题，并且不使用系统的ActionBar/ToolBar，V7.Theme.AppCompat主题,还是会先默认绘制一帧默认ActionBar，然后再将他去掉（略无语）
            //键盘收回 (offset < 0，高度变大)
            if (mPanelManager.isVisible()) {
                mPanelManager.handleShow();
            }
        }
    }
}
