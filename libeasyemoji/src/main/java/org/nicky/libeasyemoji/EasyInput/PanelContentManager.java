package org.nicky.libeasyemoji.EasyInput;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;


import org.nicky.libeasyemoji.EasyInput.interfaces.IPanelContentManager;
import org.nicky.libeasyemoji.EasyInput.interfaces.IPanelLayout;
import org.nicky.libeasyemoji.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nickyang on 2017/3/23.
 */

public class PanelContentManager implements IPanelContentManager {

    IPanelLayout mIPanelLayout;
    private Map<String, View> viewMap = new HashMap<>();
    private Map<String, Fragment> fragmentMap = new HashMap<>();
    private Object lastDisplayContent;
    private Object currentDisplayContent;
    private String currentContentTag;
    private boolean hasPanelContent; //当前是否已经有panel显示
    private Context mContext;
    private FragmentManager fragmentManager;

    private PanelContentManager(Context context, IPanelLayout iPanelLayout) {
        mIPanelLayout = iPanelLayout;
        mContext = context;
    }

    public static PanelContentManager newInstance(Context context, IPanelLayout iPanelLayout) {
        return new PanelContentManager(context, iPanelLayout);
    }

    @Override
    public void addContent(String tag, Object content) {
        if (content instanceof View) {
            viewMap.put(tag, (View) content);
        } else if (content instanceof Fragment) {
            fragmentMap.put(tag, (Fragment) content);
        } else {
            throw new RuntimeException("panel不支持显示此的类型");
        }
        currentContentTag = tag;
        currentDisplayContent = content;
    }


    private void setPanelCurrentViewFromTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        Object content;
        if (viewMap.containsKey(tag)) {
            content = viewMap.get(tag);
        } else if (fragmentMap.containsKey(tag)) {
            content = fragmentMap.get(tag);
        } else {
            throw new IllegalArgumentException("no view or fragment add to panel, please invoke add first!");
        }

        if (content != currentDisplayContent) {
            currentContentTag = tag;
            currentDisplayContent = content;
            hasPanelContent = false;
        }
    }

    @Override
    public void openPanel(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            setPanelCurrentViewFromTag(tag);
        }
        if (addContentToPanel()) {
            mIPanelLayout.openPanel();
        }
    }

    @Override
    public void closePanel() {
        mIPanelLayout.closePanel();
    }

    @Override
    public String getCurrentPanelDisplayTag() {
        return currentContentTag;
    }

    @Override
    public void removeContent(String tag) {
        if(TextUtils.isEmpty(tag)){
            return;
        }

        if(currentContentTag.equals(tag)){
            mIPanelLayout.closePanel();
            currentContentTag = "";
            currentDisplayContent = null;
            hasPanelContent = false;
        }
        if(viewMap.containsKey(tag)){
            viewMap.remove(tag);
        }else if(fragmentMap.containsKey(tag)){
            fragmentMap.remove(tag);
            if (fragmentManager == null) {
                fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
            }
            Fragment snapFragment = fragmentManager.findFragmentByTag(tag);
            if(snapFragment != null){
                fragmentManager.beginTransaction().remove(snapFragment).commitAllowingStateLoss();
            }
        }
    }

    private boolean addContentToPanel() {
        if (hasPanelContent) { //panel already has content
            return true;
        }
        if (mIPanelLayout.getPanel() == null || currentDisplayContent == null) {
            return false; //will not display panel
        }
        if (lastDisplayContent == currentDisplayContent) {
            return true;
        }
        if (fragmentManager == null) {
            fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        }
        if (lastDisplayContent instanceof Fragment) { //first hide last Fragment
            fragmentManager.beginTransaction().hide((Fragment) lastDisplayContent).commitAllowingStateLoss();
        }

        if (currentDisplayContent instanceof View) {
            mIPanelLayout.getPanel().removeAllViews();
            View content = (View) currentDisplayContent;
            if (content.getParent() != null) {
                ViewGroup parent = (ViewGroup) content.getParent();
                parent.removeView(content);
            }
            mIPanelLayout.getPanel().addView((View) currentDisplayContent);
        } else if (currentDisplayContent instanceof Fragment) {
            Fragment saved = fragmentManager.findFragmentByTag(currentContentTag);
            if (saved != null) {
                fragmentManager.beginTransaction().show(saved).commitAllowingStateLoss();
            } else {
                Fragment content = (Fragment) currentDisplayContent;
                fragmentManager
                        .beginTransaction()
                        .add(R.id.panel_container, content, currentContentTag)
                        .commitAllowingStateLoss();
            }
        }
        hasPanelContent = true;
        lastDisplayContent = currentDisplayContent;
        return true;
    }
}
