package org.nicky.libeasyemoji.EasyInput;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
    private Object defaultContent;
    private boolean hasPanelContent;
    private Context mContext;

    private PanelContentManager(Context context,IPanelLayout iPanelLayout){
        mIPanelLayout = iPanelLayout;
        mContext = context;
    }

    public static PanelContentManager newInstance(Context context,IPanelLayout iPanelLayout){
        return new PanelContentManager(context,iPanelLayout);
    }

    @Override
    public void addContent(String tag, Object content) {
        if(content instanceof View){
            viewMap.put(tag, (View) content);
        }else if(content instanceof Fragment){
            fragmentMap.put(tag, (Fragment) content);
        }else {
            throw new RuntimeException("panel不支持显示此的类型");
        }
        defaultContent = content;
    }

    @Override
    public void openPanel() {
        if(addContentToPanel()){
            mIPanelLayout.openPanel();
        }
    }

    @Override
    public void closePanel() {
        mIPanelLayout.closePanel();
    }

    private boolean addContentToPanel(){
        if(hasPanelContent){ //panel already has content
            return true;
        }
        if(mIPanelLayout.getPanel() == null){
            return false;
        }
        mIPanelLayout.getPanel().removeAllViews();
        if(defaultContent !=null){
            if(defaultContent instanceof View){
                View content = (View) defaultContent;
                if(content.getParent() != null){
                    ViewGroup parent = (ViewGroup) content.getParent();
                    parent.removeView(content);
                }
                mIPanelLayout.getPanel().addView((View) defaultContent);
                hasPanelContent = true;
            }else if(defaultContent instanceof Fragment){
                Fragment content = (Fragment) defaultContent;
                ((FragmentActivity)mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.panel_container,content)
                        .commit();
                hasPanelContent = true;
            }
        }
        return hasPanelContent;
    }
}
