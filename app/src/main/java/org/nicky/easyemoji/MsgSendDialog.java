package org.nicky.easyemoji;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.nicky.libeasyemoji.EasyInput.EasyInputManagerImpl;
import org.nicky.libeasyemoji.EasyInput.interfaces.EasyInputManager;
import org.nicky.libeasyemoji.emojicon.EmojiconEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class MsgSendDialog extends DialogFragment {

    @BindView(R.id.send_edt)
    EmojiconEditText emojiconEditText;
    @BindView(R.id.inputbox_emoji)
    CheckBox emojiSwitch;
    @BindView(R.id.lv_msg_send)
    LinearLayout lvSend;

    private Context context;
    private CallBack callBack;

    protected EasyInputManager mEasyInputManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_msg_send, container, false);
        ButterKnife.bind(this, view);
        initEmoji();
        return view;
    }


    private void initEmoji(){
        mEasyInputManager = EasyInputManagerImpl.newInstance(getActivity());
        mEasyInputManager.addDefaultEmoji("emoji",emojiconEditText);
    }

    @OnClick(R.id.inputbox_emoji)
    void switchEmoji() {
        if (emojiSwitch.isChecked()) {
            mEasyInputManager.openPanel("emoji");
        } else {
            mEasyInputManager.openKeyboard(emojiconEditText);
        }
    }

    //    @OnClick(R.id.view)
//    public void clickWhiteView() {
//        dismiss();
//    }

    @OnTextChanged(value = R.id.send_edt, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onTextChanged(CharSequence c, int i1, int i2, int i3) {

    }

    @OnClick(R.id.publish)
    public void sendMoment() {
        String strMoment = emojiconEditText.getText().toString().trim();
        if (TextUtils.isEmpty(strMoment)) {
            Toast.makeText(getContext(), "请输入评论", Toast.LENGTH_SHORT).show();
            return;
        }

        emojiconEditText.setText("");
        dismiss();
        if (null != callBack) {
            callBack.callBack(strMoment);
        }
    }

    public void setHint(String hint) {
        emojiconEditText.setHint(hint);
    }

    public LinearLayout getLvSend() {
        return lvSend;
    }

    public interface CallBack {
        void callBack(String str);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
