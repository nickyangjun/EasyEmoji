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
import org.nicky.libeasyemoji.EasyInput.utils.Utils;
import org.nicky.libeasyemoji.R;


/**
 * Created by yangjun1 on 2016/9/13.
 *
 * 最底层的布局，继承RelativeLayout， 上面放用户自定义的布局，下面放panel layout
 *
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

    public IMERootLayout(Context context,IKeyboardManager keyboardManager,IPanelLayout panelManager,boolean isKeyboardShow){
        super(context);
        mContext = context;
        mKeyboardManager = keyboardManager;
        mWindow =  ((Activity)mContext).getWindow();
        //设置activity输入法模式
        // 根据应用的 targetSdkVersion 决定使用哪种模式
        // 只有当系统版本 >= 35 且应用 targetSdkVersion >= 35 时才使用新的 ADJUST_NOTHING 模式
        int adjustMode;
        boolean shouldUseApi35Mode = Utils.shouldUseApi35Mode(context);
        if (shouldUseApi35Mode) {
            adjustMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
        } else {
            adjustMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        }

        if(isKeyboardShow) {
            mWindow.setSoftInputMode(adjustMode | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }else {
            mWindow.setSoftInputMode(adjustMode | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

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
        //获取到activity默认的根content View
        ViewGroup contentLayout = (ViewGroup) ((Activity)mContext).findViewById(android.R.id.content);
        //先移除根content View上用户自定义的布局
        View customRootView = contentLayout.getChildAt(0);
        contentLayout.removeView(customRootView);
        contentLayout.addView(this);

        //设置用户自定义的布局在新的IMERootLayout中的布局参数
        panel.setId(R.id.panel_container);
        LayoutParams customRootParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        customRootParams.addRule(RelativeLayout.ABOVE,panel.getId());
        customRootView.setLayoutParams(customRootParams);

        //设置panel layout在IMERootLayout中的布局参数, 先默认panel的高度是0
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
        try {
            if(!mAutoHideIME) {
                return super.dispatchTouchEvent(ev);
            }
            if (ev.getAction() == MotionEvent.ACTION_DOWN && !super.dispatchTouchEvent(ev)) {
                if((ev.getRawY() < ConvertUtil.getWindowHeightPX(mContext) - KeyboardManagerImpl.getKeyboardHeight(mContext) - mAutoHideOffsetPixel)
                        &&(mPanelManager.isVisible() || mKeyboardManager.isKeyboardShowing())){
                    cancelIME();
                    return true;
                }
            }
            return super.dispatchTouchEvent(ev);
        }catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }
        return false;
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
