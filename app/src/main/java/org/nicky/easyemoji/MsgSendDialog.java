package org.nicky.easyemoji;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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


public class MsgSendDialog extends DialogFragment {

    EmojiconEditText emojiconEditText;
    CheckBox emojiSwitch;
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
        initEmoji();
        return view;
    }


    private void initEmoji(){
        mEasyInputManager = EasyInputManagerImpl.newInstance(getActivity());
        mEasyInputManager.addDefaultEmoji("emoji",emojiconEditText);
    }

    void switchEmoji() {
        if (emojiSwitch.isChecked()) {
            mEasyInputManager.openPanel("emoji");
        } else {
            mEasyInputManager.openKeyboard(emojiconEditText);
        }
    }

//    public void clickWhiteView() {
//        dismiss();
//    }

    public void onTextChanged(CharSequence c, int i1, int i2, int i3) {

    }

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
