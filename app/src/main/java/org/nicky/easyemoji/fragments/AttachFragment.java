package org.nicky.easyemoji.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.nicky.easyemoji.R;

import java.util.ArrayList;


/**
 * Created by nickyang on 2017/3/25.
 */

public class AttachFragment extends Fragment {

    RecyclerView mLocalImgList;
    LinearLayout mLayoutDots; //功能面板小圆点容器
    ViewPager mPanelVP;//功能面板显示icon的容器
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
        View root = inflater.inflate(R.layout.fragment_attach, container, false);
        mLocalImgList = root.findViewById(R.id.local_img_list);
        mLayoutDots = root.findViewById(R.id.ll_dots);
        mPanelVP = root.findViewById(R.id.panel_viewpager);
        initView();
        return root;
    }

    private void initView() {
        mLocalImgList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mLocalImgList.setItemAnimator(new DefaultItemAnimator());
        ImageModel.LocalImgAdapter adapter = new ImageModel.LocalImgAdapter(getActivity());
        mLocalImgList.setAdapter(adapter);
        //加载本地图库
        ImageModel.AttachPhotoLoaderCallback callback = new ImageModel.AttachPhotoLoaderCallback(getActivity(), adapter);
        getLoaderManager().initLoader(0, null, callback);

        //一页显示4个 算出要展示几页
        int panelPageNum = ChatPanelUtils.getPageNum(ChatPanelUtils.iconId.length, 4);
        //初始化小圆点
        ChatPanelUtils.initDots(panelPageNum, mLayoutDots, getActivity());
        //有几页加载几个view
        for (int j = 0; j < panelPageNum; j++) {
            if (panelViews == null) {
                panelViews = new ArrayList<View>();
            }
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            panelViews.add(inflater.inflate(R.layout.item_viewpager_panel, null));
        }
        ChatPanelUtils.instantiatedView(panelPageNum, ChatPanelUtils.iconId.length, 4, panelViews, getActivity());
        mPanelVP.setOnPageChangeListener(new ChatPanelUtils.pageChangeListener(panelPageNum, getActivity()));
        mPanelVP.setAdapter(new ChatPanelUtils.PanelViewAdapter(panelViews));
    }


}
