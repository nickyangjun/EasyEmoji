package org.nicky.easyemoji;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.add_panel)
    Button addPanel;
    @BindView(R.id.delete_panel)
    Button deletePanel;
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasPer = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if(hasPer != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
            }
        }


        mEasyInputManager = EasyInputManagerImpl.newInstance(this);
        mEasyInputManager.addOnPanelListener(new changePanelListener());
        mEasyInputManager.addOnKeyboardIMEListener(new changeKeyboardListener());
        mEasyInputManager.setTouchBlankAutoHideIME(true,dip2px(this, 50));
        mEasyInputManager.addFragmentToPanel("attach", AttachFragment.newInstance());
        mEasyInputManager.addDefaultEmoji("emoji",emojiconEditText);
        mEasyInputManager.getEmojiBuilder().addEmojiStyle(lovedEmoji);


        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = 30;
        params.rightMargin = 30;

        TextView view  = new TextView(this);
        view.setGravity(Gravity.CENTER);
        view.setText("HAHA");
        view.setLayoutParams(params);
        mEasyInputManager.getEmojiBuilder().addBottomTypeView(view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "HAHAHAHAH",Toast.LENGTH_SHORT).show();
            }
        });


        RecyclerView.LayoutParams params2 = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params2.leftMargin = 30;
        params2.rightMargin = 30;

        TextView view2  = new TextView(this);
        view2.setGravity(Gravity.CENTER);
        view2.setText("HAHA2");
        view2.setLayoutParams(params2);
        mEasyInputManager.getEmojiBuilder().addBottomTypeView(view2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "HAHAHAHAH22",Toast.LENGTH_SHORT).show();
            }
        });


        RecyclerView.LayoutParams params3 = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params3.leftMargin = 130;
        params3.rightMargin = 130;

        TextView view3  = new TextView(this);
        view3.setGravity(Gravity.CENTER);
        view3.setText("HAHA3");
        view3.setLayoutParams(params3);
        mEasyInputManager.getEmojiBuilder().addBottomTypeView(view3, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "HAHAHAHAH3",Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView.LayoutParams params4 = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params4.leftMargin = 130;
        params4.rightMargin = 130;

        TextView view4  = new TextView(this);
        view4.setGravity(Gravity.CENTER);
        view4.setText("HAHA4");
        view4.setLayoutParams(params4);
        mEasyInputManager.getEmojiBuilder().addBottomTypeView(view4, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "HAHAHAHAH4",Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView.LayoutParams params5 = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params5.leftMargin = 130;
        params5.rightMargin = 130;

        TextView view5  = new TextView(this);
        view5.setGravity(Gravity.CENTER);
        view5.setText("HAHA5");
        view5.setLayoutParams(params5);
        mEasyInputManager.getEmojiBuilder().addBottomTypeView(view5, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "HAHAHAHAH5",Toast.LENGTH_SHORT).show();
            }
        });

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

    @OnClick({R.id.open_dialog, R.id.open_panel,R.id.close_panel,R.id.open_keyboard,R.id.close_keyboard,R.id.add_emoji,R.id.delete_emoji,
                R.id.delete_a_emoji,R.id.add_a_emoji,R.id.publish,R.id.add_panel,R.id.delete_panel})
    void onClick(View view){
        switch (view.getId()){
            case R.id.open_dialog: // TODO: 2019/3/22  未实现
//                MsgSendDialog dialog = new MsgSendDialog();
//                dialog.show(getSupportFragmentManager(), "dialog");
                break;
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
                if(category.getEmojiData().size()>0) {
                    category.getEmojiData().remove(category.getEmojiData().size() - 1);
                    mEasyInputManager.getEmojiBuilder().updateEmojiStyle(category);
                }
                break;
            case R.id.publish:
                txt.setText(emojiconEditText.getText());
                break;
            case R.id.add_panel:
                mEasyInputManager.addFragmentToPanel("attach", AttachFragment.newInstance());
                break;
            case R.id.delete_panel:
                mEasyInputManager.removeFragmentToPanel("attach");
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
