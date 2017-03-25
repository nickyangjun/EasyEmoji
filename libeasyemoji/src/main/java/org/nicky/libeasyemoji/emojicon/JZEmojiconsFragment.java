/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nicky.libeasyemoji.emojicon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nicky.libeasyemoji.R;
import org.nicky.libeasyemoji.emojicon.emoji.Emojicon;
import org.nicky.libeasyemoji.emojicon.emoji.People;


/**
 * @author Hieu Rocker (rockerhieu@gmail.com).
 */
public class JZEmojiconsFragment extends EmojiconsFragment implements  EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener{
    private FragmentManager mFragmentManager;
    private JZEmojiconGridFragment mJZEmojiconGridFragment;
    private EmojiconGridFragment.OnEmojiconClickedListener mOnEmojiconClickedListener;
    private boolean mUseSystemDefault = false;

    private static final String USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults";
    EmojiconEditText mEmojiconEditText;

    public static JZEmojiconsFragment newInstance(boolean useSystemDefault) {
        JZEmojiconsFragment fragment = new JZEmojiconsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(USE_SYSTEM_DEFAULT_KEY, useSystemDefault);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setEmojiconEditText(EmojiconEditText emojiconEditText){
        mEmojiconEditText = emojiconEditText;
    }

    public void setOnEmojiconBackspaceClickedListener(OnEmojiconBackspaceClickedListener listener) {
        super.setOnEmojiconBackspaceClickedListener(listener);
    }

    public void setOnEmojiconClickedListener(EmojiconGridFragment.OnEmojiconClickedListener listener){
        mOnEmojiconClickedListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jzemojicons, container, false);
        if (mFragmentManager == null) {
            mFragmentManager = getChildFragmentManager();
        }
        Fragment f = mFragmentManager.findFragmentByTag("JZEmojiconGridFragment");
        if (f == null) {
            mJZEmojiconGridFragment = JZEmojiconGridFragment.newInstance(People.DATA, null, mUseSystemDefault);
            mJZEmojiconGridFragment.setOnEmojiconClickedListener(mOnEmojiconClickedListener);
            mFragmentManager.beginTransaction()
                    .replace(R.id.emojis_pager, mJZEmojiconGridFragment, "JZEmojiconGridFragment").commitAllowingStateLoss();
        } else {
            mFragmentManager.beginTransaction().show(f).commitAllowingStateLoss();
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUseSystemDefault = getArguments().getBoolean(USE_SYSTEM_DEFAULT_KEY);
        } else {
            mUseSystemDefault = false;
        }
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mEmojiconEditText,emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mEmojiconEditText);
    }
}
