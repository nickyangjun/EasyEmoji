package org.nicky.easyemoji;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.nicky.easyemoji.fragments.AttachFragment;
import org.nicky.easyemoji.lovedEmoji.LovedEmojiStyle;
import org.nicky.libeasyemoji.EasyInput.EasyInputManagerImpl;
import org.nicky.libeasyemoji.EasyInput.interfaces.EasyInputManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.OnKeyboardListener;
import org.nicky.libeasyemoji.EasyInput.interfaces.OnPanelListener;
import org.nicky.libeasyemoji.emoji.interfaces.EmojiStyle;
import org.nicky.libeasyemoji.emojicon.EmojiconEditText;
import org.nicky.libeasyemoji.emojicon.emoji.Emojicon;
import org.nicky.libeasyemoji.emojicon.emoji.ObjectsStyle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.emoji_input_txt)
    EmojiconEditText emojiconEditText;
    @BindView(R.id.emojicon_switch)
    CheckBox emojiSwitch;
    @BindView(R.id.close_keyboard)
    Button closeKeyboard;
    @BindView(R.id.open_keyboard)
    Button openKeyboard;
    @BindView(R.id.close_panel)
    Button closePanel;
    @BindView(R.id.open_panel)
    Button openPanel;
    @BindView(R.id.attach)
    ImageView attach;
    @BindView(R.id.add_emoji)
    Button addEmoji;
    @BindView(R.id.delete_emoji)
    Button deleteEmoji;
    @BindView(R.id.add_a_emoji)
    Button addOneEmoji;
    @BindView(R.id.delete_a_emoji)
    Button deleteOneEmoji;
    @BindView(R.id.publish)
    TextView publish;
    @BindView(R.id.emoji_txt)
    TextView txt;

    protected EasyInputManager mEasyInputManager;
    private EmojiStyle category = new ObjectsStyle();
    private EmojiStyle lovedEmoji = new LovedEmojiStyle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mEasyInputManager = EasyInputManagerImpl.newInstance(this);
        mEasyInputManager.addOnPanelListener(new changePanelListener());
        mEasyInputManager.addOnKeyboardIMEListener(new changeKeyboardListener());
        mEasyInputManager.setTouchBlankAutoHideIME(true,dip2px(this, 50));
        mEasyInputManager.addFragmentToPanel("attach", AttachFragment.newInstance());
        mEasyInputManager.addDefaultEmoji("emoji",emojiconEditText);
        mEasyInputManager.getEmojiBuilder().addEmojiStyle(lovedEmoji);

        emojiconEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    publish.setBackground(getResources().getDrawable(R.drawable.shape_bg_blue_round_active));
                }else {
                    publish.setBackground(getResources().getDrawable(R.drawable.shape_bg_grey_round));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.emojicon_switch)
    void switchEmoji() {
        if (emojiSwitch.isChecked()) {
            mEasyInputManager.openPanel("emoji");
        } else {
            mEasyInputManager.openKeyboard(emojiconEditText);
        }
    }

    @OnClick(R.id.attach)
    void attach(){
        mEasyInputManager.openPanel("attach");
        emojiSwitch.setChecked(false);
    }

    @OnClick({R.id.open_panel,R.id.close_panel,R.id.open_keyboard,R.id.close_keyboard,R.id.add_emoji,R.id.delete_emoji,
                R.id.delete_a_emoji,R.id.add_a_emoji,R.id.publish})
    void onClick(View view){
        switch (view.getId()){
            case R.id.open_keyboard:
                mEasyInputManager.openKeyboard(emojiconEditText);
                break;
            case R.id.open_panel:
                mEasyInputManager.openPanel();
                break;
            case R.id.close_keyboard:
                mEasyInputManager.closeKeyboard(emojiconEditText);
                break;
            case R.id.close_panel:
                mEasyInputManager.closePanel();
                break;
            case R.id.add_emoji:
                mEasyInputManager.getEmojiBuilder().addEmojiStyle(category);
                break;
            case R.id.delete_emoji:
                mEasyInputManager.getEmojiBuilder().deleteEmojiStyle(category.getStyleName());
                break;
            case R.id.add_a_emoji:
                category.getEmojiData().add(Emojicon.fromCodePoint(0x1f3e0));
                mEasyInputManager.getEmojiBuilder().updateEmojiStyle(category);
                break;
            case R.id.delete_a_emoji:
                category.getEmojiData().remove(category.getEmojiData().size()-1);
                mEasyInputManager.getEmojiBuilder().updateEmojiStyle(category);
                break;
            case R.id.publish:
                txt.setText(emojiconEditText.getText());
                break;
        }
    }

    private class changePanelListener implements OnPanelListener {
        @Override
        public void onPanelDisplay(boolean isShowing) {
            if(mEasyInputManager.getCurrentPanelDisplayTag().equals("attach")){
                return;
            }
            if(isShowing){
                emojiSwitch.setChecked(true);
            }
            else {
                emojiSwitch.setChecked(false);
            }
        }
    }


    private class changeKeyboardListener implements OnKeyboardListener {

        @Override
        public void onKeyboardDisplay(boolean isShowing) {
            if(isShowing){
                emojiSwitch.setChecked(false);
            }
        }
    }


    //dip To  px
    public static int dip2px(Context context, int dp) {
        //dp和px的转换关系
        float density = context.getResources().getDisplayMetrics().density;
        //2*1.5+0.5  2*0.75 = 1.5+0.5
        return (int)(dp*density+0.5);
    }
}
