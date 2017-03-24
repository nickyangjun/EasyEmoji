package org.nicky.libeasyemoji.EasyInput;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import org.nicky.libeasyemoji.EasyInput.interfaces.IKeyboardManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IPanelLayout;
import org.nicky.libeasyemoji.EasyInput.interfaces.OnKeyboardListener;
import org.nicky.libeasyemoji.EasyInput.utils.StatusBarHeightUtil;
import org.nicky.libeasyemoji.EasyInput.utils.Utils;
import org.nicky.libeasyemoji.EasyInput.utils.ViewUtil;
import org.nicky.libeasyemoji.R;


/**
 * Created by yangjun1 on 2016/9/14.
 */
public class KeyboardManagerImpl implements IKeyboardManager {
    private Context mContext;
    private InputMethodManager mInputManager;
    private boolean mIsKeyboardShowing;

    public KeyboardManagerImpl(Context context){
        mContext = context;
        mInputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public boolean isKeyboardShowing() {
        return mIsKeyboardShowing;
    }

    @Override
    public void setKeyboardShowing(boolean isShowing) {
        mIsKeyboardShowing = isShowing;
    }

    @Override
    public void openKeyboard(View view) {
        if(mInputManager != null) {
            view.setFocusableInTouchMode(true);
            view.requestFocus();
            mInputManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    @Override
    public void closeKeyboard(View view) {
        if(mInputManager != null) {
            mInputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void closeKeyboard(Activity activity) {
        final View focusView = activity.getCurrentFocus();
        if (focusView != null) {
            closeKeyboard(activity.getCurrentFocus());
            focusView.clearFocus();
        }
    }



    private static int LAST_SAVE_KEYBOARD_HEIGHT = 0;

    //获取最后一次弹出输入框的高度
    private static boolean saveKeyboardHeight(final Context context, int keyboardHeight) {
        if (LAST_SAVE_KEYBOARD_HEIGHT == keyboardHeight) {
            return false;
        }

        if (keyboardHeight < 0) {
            return false;
        }

        LAST_SAVE_KEYBOARD_HEIGHT = keyboardHeight;

        return Utils.saveKeyboardHeight(context, keyboardHeight);
    }


    //获取到输入框的高度
    public static int getKeyboardHeight(final Context context) {
        if (LAST_SAVE_KEYBOARD_HEIGHT == 0) {
            LAST_SAVE_KEYBOARD_HEIGHT = Utils.getKeyboardHeight(context, getMinPanelHeight(context.getResources()));
        }

        return LAST_SAVE_KEYBOARD_HEIGHT;
    }

    public static int getValidPanelHeight(final Context context) {
        final int maxPanelHeight = getMaxPanelHeight(context.getResources());
        final int minPanelHeight = getMinPanelHeight(context.getResources());

        int validPanelHeight = getKeyboardHeight(context);

        validPanelHeight = Math.max(minPanelHeight, validPanelHeight);
        validPanelHeight = Math.min(maxPanelHeight, validPanelHeight);
        return validPanelHeight;
    }

    private static int MAX_PANEL_HEIGHT = 0;
    private static int MIN_PANEL_HEIGHT = 0;
    private static int MIN_KEYBOARD_HEIGHT = 0;

    public static int getMaxPanelHeight(final Resources res) {
        if (MAX_PANEL_HEIGHT == 0) {
            MAX_PANEL_HEIGHT = res.getDimensionPixelSize(R.dimen.max_panel_height);
        }

        return MAX_PANEL_HEIGHT;
    }

    public static int getMinPanelHeight(final Resources res) {
        if (MIN_PANEL_HEIGHT == 0) {
            MIN_PANEL_HEIGHT = res.getDimensionPixelSize(R.dimen.min_panel_height);
        }

        return MIN_PANEL_HEIGHT;
    }

    public static int getMinKeyboardHeight(Context context) {
        if (MIN_KEYBOARD_HEIGHT == 0) {
            MIN_KEYBOARD_HEIGHT = context.getResources().getDimensionPixelSize(R.dimen.min_keyboard_height);
        }
        return MIN_KEYBOARD_HEIGHT;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public ViewTreeObserver.OnGlobalLayoutListener attach(final Activity activity, IPanelLayout target,
                                                                OnKeyboardListener listener) {
        final ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        final boolean isFullScreen = ViewUtil.isFullScreen(activity);
        final boolean isTranslucentStatus = ViewUtil.isTranslucentStatus(activity);
        final boolean isFitSystemWindows = ViewUtil.isFitsSystemWindows(activity);

        final Display display = activity.getWindowManager().getDefaultDisplay();
        final int screenHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            final Point screenSize = new Point();
            display.getSize(screenSize);
            screenHeight = screenSize.y;
        } else {
            screenHeight = display.getHeight();
        }

        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new KeyboardStatusListener(
                isFullScreen,
                isTranslucentStatus,
                isFitSystemWindows,
                contentView,
                target,
                listener,
                screenHeight);

        contentView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        return globalLayoutListener;
    }

    public  ViewTreeObserver.OnGlobalLayoutListener attach(final Activity activity, IPanelLayout target) {
        return attach(activity, target, null);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void detach(Activity activity, ViewTreeObserver.OnGlobalLayoutListener l) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            contentView.getViewTreeObserver().removeOnGlobalLayoutListener(l);
        } else {
            contentView.getViewTreeObserver().removeGlobalOnLayoutListener(l);
        }
    }


    private  class KeyboardStatusListener implements ViewTreeObserver.OnGlobalLayoutListener {

        private int previousDisplayHeight = 0;
        private final ViewGroup contentView;
        private final IPanelLayout panelHeightTarget;
        private final boolean isFullScreen;
        private final boolean isTranslucentStatus;
        private final boolean isFitSystemWindows;
        private final int statusBarHeight;
        private boolean lastKeyboardShowing;
        private final OnKeyboardListener keyboardShowingListener;
        private final int screenHeight;

        private boolean isOverlayLayoutDisplayHContainStatusBar = false;

        KeyboardStatusListener(boolean isFullScreen, boolean isTranslucentStatus,
                               boolean isFitSystemWindows,
                               ViewGroup contentView, IPanelLayout panelHeightTarget,
                               OnKeyboardListener listener, int screenHeight) {
            this.contentView = contentView;
            this.panelHeightTarget = panelHeightTarget;
            this.isFullScreen = isFullScreen;
            this.isTranslucentStatus = isTranslucentStatus;
            this.isFitSystemWindows = isFitSystemWindows;
            this.statusBarHeight = StatusBarHeightUtil.getStatusBarHeight(contentView.getContext());
            this.keyboardShowingListener = listener;
            this.screenHeight = screenHeight;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        @Override
        public void onGlobalLayout() {
            final View userRootView = contentView.getChildAt(0);
            final View actionBarOverlayLayout = (View) contentView.getParent();

            Rect r = new Rect();

            final int displayHeight;
            if (isTranslucentStatus) {
                actionBarOverlayLayout.getWindowVisibleDisplayFrame(r);

                final int overlayLayoutDisplayHeight = (r.bottom - r.top);

                if (!isOverlayLayoutDisplayHContainStatusBar) {
                    isOverlayLayoutDisplayHContainStatusBar = overlayLayoutDisplayHeight == screenHeight;
                }

                if (!isOverlayLayoutDisplayHContainStatusBar) {
                    displayHeight = overlayLayoutDisplayHeight + statusBarHeight;
                } else {
                    displayHeight = overlayLayoutDisplayHeight;
                }

            } else {
                userRootView.getWindowVisibleDisplayFrame(r);
                displayHeight = (r.bottom - r.top);
            }

            calculateKeyboardHeight(displayHeight);
            calculateKeyboardShowing(displayHeight);

            previousDisplayHeight = displayHeight;
        }

        private void calculateKeyboardHeight(final int displayHeight) {
            if (previousDisplayHeight == 0) {
                previousDisplayHeight = displayHeight;

                // init the panel height for target.
                panelHeightTarget.changeHeight(getValidPanelHeight(getContext()));
                return;
            }

            int keyboardHeight;
            if (Utils.isHandleByPlaceholder(isFullScreen, isTranslucentStatus,
                    isFitSystemWindows)) {
                // the height of content parent = contentView.height + actionBar.height
                final View actionBarOverlayLayout = (View) contentView.getParent();

                keyboardHeight = actionBarOverlayLayout.getHeight() - displayHeight;

            } else {
                keyboardHeight = Math.abs(displayHeight - previousDisplayHeight);
            }
            if (keyboardHeight <= getMinKeyboardHeight(getContext())) {
                return;
            }

            if (keyboardHeight == this.statusBarHeight) {
                return;
            }

            boolean changed = saveKeyboardHeight(getContext(), keyboardHeight);
            if (changed) {
                final int validPanelHeight = getValidPanelHeight(getContext());
                if (this.panelHeightTarget.getHeight() != validPanelHeight) {
                    this.panelHeightTarget.changeHeight(validPanelHeight);
                }
            }
        }

        private int maxOverlayLayoutHeight;
        private void calculateKeyboardShowing(final int displayHeight) {

            boolean isKeyboardShowing;

            final View actionBarOverlayLayout = (View) contentView.getParent();
            final int actionBarOverlayLayoutHeight = actionBarOverlayLayout.getHeight() -
                    actionBarOverlayLayout.getPaddingTop();

            if (Utils.isHandleByPlaceholder(isFullScreen, isTranslucentStatus,
                    isFitSystemWindows)) {
                if (!isTranslucentStatus &&
                        actionBarOverlayLayoutHeight - displayHeight == this.statusBarHeight) {
                    // handle the case of status bar layout, not keyboard active.
                    isKeyboardShowing = lastKeyboardShowing;
                } else {
                    isKeyboardShowing = actionBarOverlayLayoutHeight > displayHeight;
                }

            } else {

                final int phoneDisplayHeight = contentView.getResources().getDisplayMetrics().heightPixels;
                if (!isTranslucentStatus &&
                        phoneDisplayHeight == actionBarOverlayLayoutHeight) {
                    return;

                }

                if (maxOverlayLayoutHeight == 0) {
                    // non-used.
                    isKeyboardShowing = lastKeyboardShowing;
                } else {
                    isKeyboardShowing = displayHeight < maxOverlayLayoutHeight - getMinKeyboardHeight(getContext());
                }

                maxOverlayLayoutHeight = Math.max(maxOverlayLayoutHeight, actionBarOverlayLayoutHeight);
            }

            if (lastKeyboardShowing != isKeyboardShowing) {
                setKeyboardShowing(isKeyboardShowing);
                if (keyboardShowingListener != null) {
                    keyboardShowingListener.onKeyboardDisplay(isKeyboardShowing);
                }
            }

            lastKeyboardShowing = isKeyboardShowing;

        }

        private Context getContext() {
            return contentView.getContext();
        }
    }
}
