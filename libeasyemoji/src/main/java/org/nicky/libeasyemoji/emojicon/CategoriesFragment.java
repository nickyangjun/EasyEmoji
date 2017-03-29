package org.nicky.libeasyemoji.emojicon;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
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
import org.nicky.libeasyemoji.emojicon.emoji.Emojicon;
import org.nicky.libeasyemoji.emojicon.emoji.NatureCategory;
import org.nicky.libeasyemoji.emojicon.emoji.PeopleCategory;
import org.nicky.libeasyemoji.emojicon.interfaces.BaseCategory;
import org.nicky.libeasyemoji.emojicon.interfaces.CategoryDataChangeListener;
import org.nicky.libeasyemoji.emojicon.interfaces.CategoryDataManger;
import org.nicky.libeasyemoji.emojicon.interfaces.ICategoryDataWrapper;
import org.nicky.libeasyemoji.emojicon.interfaces.OnItemClickListener;
import org.nicky.libeasyemoji.emojicon.utils.EmojiUtil;

/**
 * Created by nickyang on 2017/3/28.
 */

public class CategoriesFragment extends Fragment implements EmojiCategoryFragment.OnEmojiconClickedListener {

    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;
    private RecyclerView mCategoryItem;
    private CategoryDataManagerImpl mCategoryDateManger;
    private ViewPagerAdapter mViewPagerAdapter;
    private CategoryItemAdapter mCategoryItemAdapter;
    private EmojiconEditText mEmojiconEditText;
    protected LinearLayout mPointContainer; // 装点的容器
    protected View mSelectedPoint; // 选中的点
    protected int mSpace; // 点与点间的距
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    private ICategoryDataWrapper mCurSelectedCategoryWrapper;

    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
        return fragment;
    }

    public void setEmojiconEditText(EmojiconEditText emojiconEditText){
        mEmojiconEditText = emojiconEditText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emojicons, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.emoji_view_pager);
        mCategoryItem = (RecyclerView) view.findViewById(R.id.emoji_category_item);
        mPointContainer = (LinearLayout) view.findViewById(R.id.guide_point_container);
        mSelectedPoint = view.findViewById(R.id.guide_point_selected);
        initData();
        return view;
    }

    public void setCategoryDataManager(CategoryDataManger manger){
        mCategoryDateManger = (CategoryDataManagerImpl) manger;
    }

    private void initData(){
        mFragmentManager = getChildFragmentManager();
        mViewPagerAdapter = new ViewPagerAdapter(mFragmentManager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mCategoryItemAdapter = new CategoryItemAdapter();
        mCategoryItem.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mCategoryItem.setAdapter(mCategoryItemAdapter);
        mCategoryDateManger.setCategoryDataChangeListener(new CategoryChangeListener());
        //默认显示第一个表情类目
        mCurSelectedCategoryWrapper = mCategoryDateManger.getCategoryWrapperByPosition(0);
        initialPoints(mCurSelectedCategoryWrapper);
        setCategoryItemSelected(mCurSelectedCategoryWrapper);
    }

    private void updateCategoryEmoji(CategoryDataChangeListener.TYPE type, BaseCategory category){
        if(mCategoryItemAdapter != null) {
            mCategoryItemAdapter.notifyDataSetChanged();
        }
        if(mViewPagerAdapter!= null) {
            mViewPagerAdapter.notifyDataSetChanged();
        }
    }

    class CategoryChangeListener implements CategoryDataChangeListener {

        @Override
        public void update(final TYPE type, final BaseCategory category) {
            if(EmojiUtil.isInMainThread()){
                updateCategoryEmoji(type,category);
            }else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        update(type,category);
                    }
                });
            }
        }
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
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
            PagerDataCategory category = mCategoryDateManger.getPagerDataCategory(position);
            ICategoryDataWrapper categoryDataWrapper = mCategoryDateManger.getCategoryWrapperByPosition(position);
            BaseCategoryFragment fragment = categoryDataWrapper.getFragment(category);
            if(fragment instanceof EmojiCategoryFragment && mEmojiconEditText != null){
                ((EmojiCategoryFragment)fragment).setOnEmojiconClickedListener(CategoriesFragment.this);
            }
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mCategoryDateManger.getPagerCategoryCounts();
        }
    }

    private class CategoryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageButton imageView = new ImageButton(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(EmojiUtil.dip2px(getActivity(),40),
                    ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setBackgroundColor(Color.WHITE);
            return new Holder(imageView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageButton image = (ImageButton) holder.itemView;
            ICategoryDataWrapper wrapper = (ICategoryDataWrapper) mCategoryDateManger.categoryWrapperMap.get(position);
            int resourceId = wrapper.getCategoryItemIcon();
            if( resourceId > 0){
                image.setImageResource(resourceId);
            }
            if(wrapper.isSelected()){
                image.setSelected(true);
            }else {
                image.setSelected(false);
            }
            holder.itemView.setOnClickListener(new ItemClick(holder,position));
        }

        @Override
        public int getItemCount() {
            return mCategoryDateManger.getCategoryDataCounts();
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
            ICategoryDataWrapper wrapper = (ICategoryDataWrapper) mCategoryDateManger.categoryWrapperMap.get(position);
            String categoryName =  wrapper.getCategoryName();
            mViewPager.setCurrentItem(mCategoryDateManger.getPageCategoryIndex(categoryName),false);
            updatePointsCounts((ICategoryDataWrapper) mCategoryDateManger.categoryWrapperMap.get(position));
            setCategoryItemSelected(wrapper);
        }
    }

    private void setCategoryItemSelected(ICategoryDataWrapper wrapper){
        mCurSelectedCategoryWrapper = wrapper;
        mCategoryDateManger.setSelectedCategoryWrapper(wrapper);
        mCategoryItemAdapter.notifyDataSetChanged();
    }


    private int updatePointsCounts(ICategoryDataWrapper wrapper){
        final int pointsCounts = wrapper.getPagerCounts();
        int curPoints = mPointContainer.getChildCount();
        if(curPoints == pointsCounts){
            return pointsCounts;
        }
        if(curPoints > pointsCounts){
            mPointContainer.removeViews(pointsCounts-1,curPoints-pointsCounts);
        }else {
            for(int i=0;i<pointsCounts-curPoints;i++){
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
        mCurSelectedCategoryWrapper = wrapper;
        if (pointsCounts <= 1) {
            mSelectedPoint.setVisibility(View.GONE);
            mPointContainer.setVisibility(View.GONE);
        } else {
            mSelectedPoint.setVisibility(View.VISIBLE);
            mPointContainer.setVisibility(View.VISIBLE);
        }
        return pointsCounts;
    }
    /**
     * 初始化和viewpager关联的点
     */
    protected void initialPoints(ICategoryDataWrapper wrapper) {
        mSpace = 0;
        final int pointsCounts = updatePointsCounts(wrapper);
        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 当 UI的树布局改变时调用
                if (pointsCounts <= 1) {
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
                ICategoryDataWrapper wrapper = mCategoryDateManger.getCategoryWrapperByPosition(position);
                if(wrapper != mCurSelectedCategoryWrapper){
                    setCategoryItemSelected(wrapper);
                }
                updatePointsCounts(wrapper);
                int index = mCategoryDateManger.getCategoryDataWrapperIndexByPosition(position);
                params.leftMargin = (int) (mSpace * index + mSpace *
                        positionOffset + 0.5f);// 四舍五入

                mSelectedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

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
