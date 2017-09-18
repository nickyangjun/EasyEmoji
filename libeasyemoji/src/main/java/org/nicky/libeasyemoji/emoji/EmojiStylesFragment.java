package org.nicky.libeasyemoji.emoji;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.nicky.libeasyemoji.R;
import org.nicky.libeasyemoji.emoji.interfaces.EmojiStyleChangeListener;
import org.nicky.libeasyemoji.emojicon.EmojiconEditText;
import org.nicky.libeasyemoji.emojicon.EmojiconFragment;
import org.nicky.libeasyemoji.emojicon.interfaces.OnItemClickListener;
import org.nicky.libeasyemoji.emojicon.utils.EmojiUtil;

/**
 * Created by nickyang on 2017/3/28.
 */

public class EmojiStylesFragment extends Fragment implements EmojiconFragment.OnEmojiconClickedListener {

    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;
    private RecyclerView mStylesItemRecyclerView;
    private EmojiStyleWrapperManager mEmojiStyleWrapperManager;
    private ViewPagerAdapter mViewPagerAdapter;
    private StylesItemAdapter mStylesItemAdapter;
    private EmojiconEditText mEmojiconEditText;
    protected LinearLayout mPointContainer; // 装点的容器
    protected View mSelectedPoint; // 选中的点
    protected int mSpace; // 点与点间的距
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    private EmojiStyleWrapper mCurSelectedEmojiStyleWrapper;
    private int curPointsCounts; //当前的点数

    public static EmojiStylesFragment newInstance() {
        EmojiStylesFragment fragment = new EmojiStylesFragment();
        return fragment;
    }

    public void setEmojiconEditText(EmojiconEditText emojiconEditText){
        mEmojiconEditText = emojiconEditText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emojicons, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.emoji_view_pager);
        mStylesItemRecyclerView = (RecyclerView) view.findViewById(R.id.emoji_category_item);
        mPointContainer = (LinearLayout) view.findViewById(R.id.guide_point_container);
        mSelectedPoint = view.findViewById(R.id.guide_point_selected);
        initData();
        return view;
    }

    public void setEmojiStyleWrapperManager(EmojiStyleWrapperManager manger){
        mEmojiStyleWrapperManager =  manger;
    }

    private void initData(){
        mFragmentManager = getChildFragmentManager();
        mViewPagerAdapter = new ViewPagerAdapter(mFragmentManager);
        mViewPager.setAdapter(mViewPagerAdapter);
        BottomStyleManager bottomStyleManager = new BottomStyleManager(getContext(),mEmojiStyleWrapperManager);
        mStylesItemAdapter = new StylesItemAdapter(bottomStyleManager);
        mStylesItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mStylesItemRecyclerView.setAdapter(mStylesItemAdapter);
        mEmojiStyleWrapperManager.setEmojiStyleChangeListener(new StyleChangeListener());
        //默认显示第一个表情类目
        mCurSelectedEmojiStyleWrapper = mEmojiStyleWrapperManager.getEmojiStyleWrapperByPosition(0);
        initialPoints(mCurSelectedEmojiStyleWrapper);
        setStyleItemSelected(mCurSelectedEmojiStyleWrapper,0);
    }

    private void updateEmojiStyle(EmojiStyleChangeListener.TYPE type,  EmojiStyleWrapper styleWrapper,int selectedPage){
        if(mStylesItemAdapter != null) {
            mStylesItemAdapter.notifyDataSetChanged();
        }
        if(mViewPagerAdapter!= null) {
            mViewPagerAdapter.notifyDataSetChanged();
        }
        if(selectedPage != -1) {
            mViewPager.setCurrentItem(selectedPage, false);
        }
    }

    class StyleChangeListener implements EmojiStyleChangeListener {

        @Override
        public void update(final TYPE type, final EmojiStyleWrapper styleWrapper, final int selectedPage) {
            if(EmojiUtil.isInMainThread()){
                updateEmojiStyle(type,styleWrapper,selectedPage);
            }else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateEmojiStyle(type,styleWrapper,selectedPage);
                    }
                });
            }
        }
    }

    @Override
    public void onEmojiconClicked(String emojicon) {
        EmojiUtil.input(mEmojiconEditText,emojicon);
    }

    @Override
    public void onBackspaceClicked() {
        EmojiUtil.backspace(mEmojiconEditText);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        FragmentManager fragmentManager;
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {
            EmojiFragment fragment = mEmojiStyleWrapperManager.getFragment(position);
            if(mEmojiconEditText != null){
                fragment.setOnEmojiconClickedListener(EmojiStylesFragment.this);
            }
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mEmojiStyleWrapperManager.getEmojiPagerCounts();
        }
    }

    //底部item类型
    private class StylesItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private BottomStyleManager styleManager;

        StylesItemAdapter(BottomStyleManager styleManager){
            this.styleManager = styleManager;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = styleManager.onCreateViewHolder(parent,viewType);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(styleManager.getItemViewType(position) == BottomStyleManager.ITEM_EMOJ) {
                ImageButton image = (ImageButton) holder.itemView;
                EmojiStyleWrapper wrapper = mEmojiStyleWrapperManager.getStyleWrapperByStyleItem(position);
                int resourceId = wrapper.getStyleIcon();
                if (resourceId > 0) {
                    image.setImageResource(resourceId);
                }
                if (wrapper.isSelected()) {
                    image.setSelected(true);
                } else {
                    image.setSelected(false);
                }
                holder.itemView.setOnClickListener(new ItemClick(holder, position));
            }else {
                View.OnClickListener clickListener = (View.OnClickListener) holder.itemView.getTag(R.id.bottom_item_click);
                if(clickListener != null) {
                    holder.itemView.setOnClickListener(clickListener);
                }
            }
        }

        @Override
        public int getItemCount() {
            return styleManager.getItemCount();
        }

        @Override
        public int getItemViewType(int position) {
            return styleManager.getItemViewType(position);
        }

        private class Holder extends RecyclerView.ViewHolder{
            public Holder(View itemView) {
                super(itemView);
            }
        }
    }

    private class ItemClick extends OnItemClickListener {
        public ItemClick(RecyclerView.ViewHolder holder, int position) {
            super(holder, position);
        }

        @Override
        public void onItemClick(RecyclerView.ViewHolder holder, int position) {
            EmojiStyleWrapper wrapper = mEmojiStyleWrapperManager.getStyleWrapperByStyleItem(position);
            String styleName =  wrapper.getStyleName();
            mViewPager.setCurrentItem(mEmojiStyleWrapperManager.getViewPageIndexByEmojiStyleName(styleName),false);
            updatePointsCounts(wrapper);
            setStyleItemSelected(wrapper,position);
        }
    }

    /**
     * @param wrapper  被选中的EmojiStyle
     * @param stylePosition  选中的EmojiStyle 在所有EmojiStyle中的位置， 0开始
     */
    private void setStyleItemSelected(EmojiStyleWrapper wrapper, int stylePosition){
        mCurSelectedEmojiStyleWrapper = wrapper;
        mEmojiStyleWrapperManager.setSelectedStyleWrapper(wrapper);
        mStylesItemAdapter.notifyDataSetChanged();
        mStylesItemRecyclerView.smoothScrollToPosition(stylePosition);
        mCurSelectedEmojiStyleWrapper.setCurDisplayPageIndex(mEmojiStyleWrapperManager.getPagerIndexAtStyleWrapperByVPPosition(mViewPager.getCurrentItem()));
    }


    private int updatePointsCounts(EmojiStyleWrapper wrapper){
        curPointsCounts = wrapper.getPagerCounts();
        int curPoints = mPointContainer.getChildCount();
        if(curPoints == curPointsCounts){
            return curPointsCounts;
        }
        if(curPoints > curPointsCounts){
            mPointContainer.removeViews(curPointsCounts-1,curPoints-curPointsCounts);
        }else {
            for(int i=0;i<curPointsCounts-curPoints;i++){
                ImageView imageView = new ImageView(getActivity());
                imageView.setImageResource(R.drawable.shape_dot_normal);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, EmojiUtil.dip2px(getActivity(), 5), 0);
                imageView.setLayoutParams(params);
                mPointContainer.addView(imageView);
            }
        }
        mCurSelectedEmojiStyleWrapper = wrapper;
        if (curPointsCounts <= 1) {
            mSelectedPoint.setVisibility(View.GONE);
            mPointContainer.setVisibility(View.GONE);
        } else {
            mSelectedPoint.setVisibility(View.VISIBLE);
            mPointContainer.setVisibility(View.VISIBLE);
        }
        return curPointsCounts;
    }
    /**
     * 初始化和viewpager关联的点
     */
    protected void initialPoints(EmojiStyleWrapper wrapper) {
        mSpace = 0;
        updatePointsCounts(wrapper);
        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 当 UI的树布局改变时调用
                 if (curPointsCounts <= 1) {
                    return;
                }
                if (mPointContainer.getChildAt(1) != null) {
                    mSpace = mPointContainer.getChildAt(1).getLeft()
                            - mPointContainer.getChildAt(0).getLeft();
                }
            }
        };
        // 计算点与点之间的距离
        mSelectedPoint.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        // 让viewPager和点关联起来
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                // 1. 去对滑动的点做操作
                // 2. 设置marginLeft
                RelativeLayout.LayoutParams params =
                        (RelativeLayout.LayoutParams) mSelectedPoint
                                .getLayoutParams();
                EmojiStyleWrapper wrapper = mEmojiStyleWrapperManager.getEmojiStyleWrapperByPosition(position);
                if(wrapper != mCurSelectedEmojiStyleWrapper){
                    setStyleItemSelected(wrapper, mEmojiStyleWrapperManager.getEmojiStyleWrapperIndexByPosition(position));
                }
                updatePointsCounts(wrapper);
                int index = mEmojiStyleWrapperManager.getPagerIndexAtStyleWrapperByVPPosition(position);
                params.leftMargin = (int) (mSpace * index + mSpace *
                        positionOffset + 0.5f);// 四舍五入

                mSelectedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                mCurSelectedEmojiStyleWrapper.setCurDisplayPageIndex(mEmojiStyleWrapperManager.getPagerIndexAtStyleWrapperByVPPosition(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewPager.clearOnPageChangeListeners();
        mSelectedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
    }
}
