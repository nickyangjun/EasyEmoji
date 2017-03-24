package org.nicky.libeasyemoji.EasyInput;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import org.nicky.libeasyemoji.EasyInput.interfaces.IKeyboardManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IPanelLayout;
import org.nicky.libeasyemoji.EasyInput.interfaces.OnKeyboardListener;
import org.nicky.libeasyemoji.EasyInput.utils.ConvertUtil;
import org.nicky.libeasyemoji.R;


/**
 * Created by yangjun1 on 2016/9/13.
 */
class IMERootLayout extends RelativeLayout{
    private static final String TAG = IMERootLayout.class.getSimpleName();
    private Context mContext;
    private OnKeyboardListener mKeyboardListener;
    private boolean mAutoHideIME;
    private Window mWindow;
    private IPanelLayout mPanelManager;
    private IKeyboardManager mKeyboardManager;
    private int mAutoHideOffsetPixel = 0;  //距输入法顶端以上此高度，则点击隐藏输入法
    private RootLayoutChangeHandler mRootLayoutChangeHandler;
    private  ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;

    public IMERootLayout(Context context,IKeyboardManager keyboardManager,IPanelLayout panelManager){
        super(context);
        mContext = context;
        mKeyboardManager = keyboardManager;
        mWindow =  ((Activity)mContext).getWindow();
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mRootLayoutChangeHandler = new RootLayoutChangeHandler(this,panelManager,keyboardManager);
        setId(R.id.ime_root_container);
        setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        mPanelManager = panelManager;
        addPanelLayout(mPanelManager.getPanel());
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mGlobalLayoutListener = mKeyboardManager.attach((Activity) mContext,mPanelManager,mKeyboardListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mKeyboardManager.detach((Activity) mContext,mGlobalLayoutListener);
    }

    public void addPanelLayout(View panel){
        ViewGroup contentLayout = (ViewGroup) ((Activity)mContext).findViewById(android.R.id.content);
        View customRootView = contentLayout.getChildAt(0);
        contentLayout.removeView(customRootView);
        contentLayout.addView(this);

        panel.setId(R.id.panel_container);
        LayoutParams customRootParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        customRootParams.addRule(RelativeLayout.ABOVE,panel.getId());
        customRootView.setLayoutParams(customRootParams);

        LayoutParams panelParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        panelParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        panel.setLayoutParams(panelParams);
        addView(panel);
        addView(customRootView);
        mPanelManager.closePanel();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mRootLayoutChangeHandler.handleBeforeMeasure(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP
                && (mKeyboardManager.isKeyboardShowing() || mPanelManager.isVisible())){
            cancelIME();
            return true;
        }
        return super.dispatchKeyEventPreIme(event);
    }

    public void setTouchBlankAutoHideIME(boolean autoHideIME,int offsetPixel){
        mAutoHideIME = autoHideIME;
        mAutoHideOffsetPixel = offsetPixel;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(visibility != VISIBLE){
            cancelIME();
        }
    }

    public void cancelIME(){
        mKeyboardManager.closeKeyboard((Activity) mContext);
        mPanelManager.closePanel();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!mAutoHideIME) {
            return super.dispatchTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if((ev.getRawY() < ConvertUtil.getWindowHeightPX(mContext) - KeyboardManagerImpl.getKeyboardHeight(mContext) - mAutoHideOffsetPixel)
                    &&(mPanelManager.isVisible() || mKeyboardManager.isKeyboardShowing())){
                cancelIME();
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void addOnKeyboardIMEListener(OnKeyboardListener listener){
        mKeyboardListener = listener;
    }

    private boolean isKeyboardShown() {
        View mRootContentView = mWindow.getDecorView().findViewById(android.R.id.content);
        if(mRootContentView == null){
            return false;
        }
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        mRootContentView.getWindowVisibleDisplayFrame(r);
        int heightDiff = mRootContentView.getBottom() - r.bottom;
        DisplayMetrics mDisplayMetrics = getResources().getDisplayMetrics();
        boolean isOpen = heightDiff > softKeyboardHeight * mDisplayMetrics.density;
        return isOpen;
    }

}
