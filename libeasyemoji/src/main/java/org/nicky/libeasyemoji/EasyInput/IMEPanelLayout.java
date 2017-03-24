package org.nicky.libeasyemoji.EasyInput;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import org.nicky.libeasyemoji.EasyInput.interfaces.IKeyboardManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IPanelLayout;
import org.nicky.libeasyemoji.EasyInput.interfaces.OnPanelListener;
import org.nicky.libeasyemoji.EasyInput.utils.ViewUtil;


/**
 * Created by nickyang on 2017/3/22.
 */

public class IMEPanelLayout extends FrameLayout implements IPanelLayout {
    private static final String TAG = IMEPanelLayout.class.getSimpleName();
    private boolean mIsHide = false;
    IKeyboardManager mKeyboardManager;

    public IMEPanelLayout(@NonNull Context context, IKeyboardManager keyboardManager) {
        super(context);
        mKeyboardManager = keyboardManager;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIsHide) {
            setVisibility(View.GONE);

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean isVisible() {
        return !mIsHide;
    }

    @Override
    public void setHide() {
        mIsHide = true;
    }

    @Override
    public void handleShow() {
        super.setVisibility(VISIBLE);
    }

    private boolean filterSetVisibility(final int visibility) {
        if (visibility == View.VISIBLE) {
            this.mIsHide = false;
        }else {
            this.mIsHide = true;
        }

        if (visibility == this.getVisibility()) {
            return true;
        }

        if (mKeyboardManager.isKeyboardShowing() && visibility == View.VISIBLE) {
            return true;
        }

        return false;
    }

    @Override
    public void setVisibility(int visibility) {
        if (filterSetVisibility(visibility)) {
            return;
        }
        super.setVisibility(visibility);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(mOnPanelListener == null){
            return;
        }
        if(visibility == VISIBLE && isVisible()){
            mOnPanelListener.onPanelDisplay(true);
        }else {
            mOnPanelListener.onPanelDisplay(false);
        }
    }

    @Override
    public IMEPanelLayout getPanel() {
        return this;
    }

    @Override
    public void changeHeight(int panelHeight) {
        ViewUtil.refreshHeight(this,panelHeight);
    }

    @Override
    public void openPanel() {
        setVisibility(VISIBLE);
    }

    @Override
    public void closePanel() {
        setVisibility(GONE);
    }
    private OnPanelListener mOnPanelListener;
    @Override
    public void addOnPanelListener(OnPanelListener listener) {
        mOnPanelListener = listener;
    }
}
