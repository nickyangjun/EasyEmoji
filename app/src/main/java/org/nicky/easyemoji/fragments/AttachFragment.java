package org.nicky.easyemoji.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.nicky.easyemoji.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nickyang on 2017/3/25.
 */

public class AttachFragment extends Fragment {

    @BindView(R.id.local_img_list)RecyclerView mLocalImgList;
    @BindView(R.id.ll_dots) LinearLayout mLayoutDots; //功能面板小圆点容器
    @BindView(R.id.panel_viewpager) ViewPager mPanelVP;//功能面板显示icon的容器
    //功能面板容器
    private ArrayList<View> panelViews = null;

    public static AttachFragment newInstance() {

        Bundle args = new Bundle();

        AttachFragment fragment = new AttachFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attach,container,false);
        ButterKnife.bind(this,root);
        initView();
        return root;
    }

    private void initView(){
        mLocalImgList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mLocalImgList.setItemAnimator(new DefaultItemAnimator());
        ImageModel.LocalImgAdapter adapter = new ImageModel.LocalImgAdapter(getActivity());
        mLocalImgList.setAdapter(adapter);
        //加载本地图库
        ImageModel.AttachPhotoLoaderCallback callback = new ImageModel.AttachPhotoLoaderCallback(getActivity(), adapter);
        getLoaderManager().initLoader(0, null, callback);

        //一页显示4个 算出要展示几页
        int panelPageNum = ChatPanelUtils.getPageNum(ChatPanelUtils.iconId.length, 4);
        //初始化小圆点
        ChatPanelUtils.initDots(panelPageNum,mLayoutDots,getActivity());
        //有几页加载几个view
        for (int j = 0; j < panelPageNum; j++) {
            if (panelViews == null) {
                panelViews = new ArrayList<View>();
            }
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            panelViews.add(inflater.inflate(R.layout.item_viewpager_panel, null));
        }
        ChatPanelUtils.instantiatedView(panelPageNum,ChatPanelUtils.iconId.length,4,panelViews,getActivity());
        mPanelVP.setOnPageChangeListener(new ChatPanelUtils.pageChangeListener(panelPageNum,getActivity()));
        mPanelVP.setAdapter(new ChatPanelUtils.PanelViewAdapter(panelViews));
    }




}
