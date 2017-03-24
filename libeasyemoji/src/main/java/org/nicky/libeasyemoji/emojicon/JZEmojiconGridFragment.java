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

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import org.nicky.libeasyemoji.R;
import org.nicky.libeasyemoji.emojicon.emoji.Emojicon;
import org.nicky.libeasyemoji.emojicon.emoji.People;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 */
public class JZEmojiconGridFragment extends EmojiconGridFragment implements AdapterView.OnItemClickListener {

    private EmojiconRecents mRecents;
    private Emojicon[] mData;
    private boolean mUseSystemDefault = false;
    /** 表情页界面集合 */
    protected ArrayList<View> pageViews;
    protected ViewPager mViewPager;
    protected LinearLayout mPointContainer; // 装点的容器
    protected View mSelectedPoint; // 选中的点
    protected int mSpace; // 点与点间的距
    protected int faceNumPager = 27; // 每页的表情数
    private int faceColumnsNum = 7; // 每页的列数，注意最后一个为删除键，所以三行总数就是20，四行，则总数就是27

    private static final String USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults";
    private static final String EMOJICONS_KEY = "emojicons";

    protected static JZEmojiconGridFragment newInstance(Emojicon[] emojicons, EmojiconRecents recents) {
        return newInstance(emojicons, recents, false);
    }

    protected static JZEmojiconGridFragment newInstance(Emojicon[] emojicons, EmojiconRecents recents, boolean useSystemDefault) {
        JZEmojiconGridFragment emojiGridFragment = new JZEmojiconGridFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(EMOJICONS_KEY, emojicons);
        args.putBoolean(USE_SYSTEM_DEFAULT_KEY, useSystemDefault);
        emojiGridFragment.setArguments(args);
        return emojiGridFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jzemojicon_viewpager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            mData = People.DATA;
            mUseSystemDefault = false;
        } else {
            Parcelable[] parcels = bundle.getParcelableArray(EMOJICONS_KEY);
            mData = new Emojicon[parcels.length];
            for (int i = 0; i < parcels.length; i++) {
                mData[i] = (Emojicon) parcels[i];
            }
            mUseSystemDefault = bundle.getBoolean(USE_SYSTEM_DEFAULT_KEY);
        }
        mViewPager = (ViewPager) view.findViewById(R.id.emojis_pager_viewpager);
        pageViews = new ArrayList<View>();
        ArrayList<Emojicon> tmpDataList = new ArrayList<>();
        for (int i = 0; i < mData.length; i = i + faceNumPager) {
            GridView gridView = (GridView) getActivity().getLayoutInflater().inflate(R.layout.jzemojicon_grid, null);
            tmpDataList.clear();
            for (int j = 0; j < faceNumPager && (i + j) < mData.length; j++) {
                tmpDataList.add(mData[i + j]);
            }
            JzEmojiAdapter adapter = new JzEmojiAdapter(view.getContext(), tmpDataList.toArray(new Emojicon[tmpDataList.size()]));
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(this);
            gridView.setNumColumns(faceColumnsNum);
            pageViews.add(gridView);
        }
        mViewPager.setAdapter(new FacePagerAdapter(pageViews));

        initialPoints(view, mData);
    }

    public void setOnEmojiconClickedListener(OnEmojiconClickedListener listener){
        super.setOnEmojiconClickedListener(listener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(EMOJICONS_KEY, mData);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position == faceNumPager) {
            // 增加一个删除操作
            if (mOnEmojiconClickedListener != null) {
                Emojicon del = new Emojicon(0, 'd', "delete");
                mOnEmojiconClickedListener.onEmojiconClicked(del);
            }
        } else if (parent.getAdapter().getItemViewType(position) == 0) { // 填充的表情

        } else {
            if (mOnEmojiconClickedListener != null) {
                mOnEmojiconClickedListener.onEmojiconClicked((Emojicon) parent.getItemAtPosition(position));
            }
        }
    }

    /**
     * 初始化和viewpager关联的点
     */
    protected void initialPoints(View view, final Emojicon[] mData) {
        mPointContainer = (LinearLayout) view.findViewById(R.id.guide_point_container);
        mSelectedPoint = view.findViewById(R.id.guide_point_selected);
        mPointContainer.removeAllViews();
        for (int i = 0; i < mData.length; i = i + faceNumPager) {
            ImageView imageView = new ImageView(view.getContext());
            imageView.setImageResource(R.drawable.shape_dot_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                    );
            params.setMargins(0, 0, dip2px(view.getContext(), 5), 0);
            imageView.setLayoutParams(params);
            mPointContainer.addView(imageView);
        }
        // 计算点与点之间的距离
        mSelectedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        // 当 UI的树布局改变时调用
                        if (mData.length < faceNumPager) {
                            return;
                        }
                        if (mPointContainer.getChildAt(1) != null) {
                            mSpace = mPointContainer.getChildAt(1).getLeft()
                                    - mPointContainer.getChildAt(0).getLeft();
                        }

                    }
                });
        // 让viewPager和点关联起来
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 1. 去对滑动的点做操作
                // 2. 设置marginLeft
                RelativeLayout.LayoutParams params =
                        (RelativeLayout.LayoutParams) mSelectedPoint
                                .getLayoutParams();
                params.leftMargin = (int) (mSpace * position + mSpace *
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
        if (mData.length < faceNumPager) {
            mSelectedPoint.setVisibility(View.GONE);
            mPointContainer.setVisibility(View.GONE);
        } else {
            mSelectedPoint.setVisibility(View.VISIBLE);
            mPointContainer.setVisibility(View.VISIBLE);
        }
    }

    // dip To px
    public static int dip2px(Context context, int dp) {
        // dp和px的转换关系
        float density = context.getResources().getDisplayMetrics().density;
        // 2*1.5+0.5 2*0.75 = 1.5+0.5
        return (int) (dp * density + 0.5);
    }

    class JzEmojiAdapter extends BaseAdapter {

        private Emojicon[] Data;
        private Context mContext;

        public JzEmojiAdapter(Context context, Emojicon[] data) {
            Data = data;
            mContext = context;
        }

        @Override
        public int getCount() {
            return faceNumPager + 1;
        }

        @Override
        public Object getItem(int position) {

            if (position >= 0 && position < Data.length) {
                return Data[position];
            } else {
                return null;
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == faceNumPager) {
                return 2;
            } else if (position < Data.length) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (position != 0) { // 在视图大小确定前，0位置会多次调用。
                if (position >= Data.length && position < faceNumPager) { // 填充的表情
                    v = View.inflate(mContext, R.layout.emojicon_item, null);
                    v.setClickable(false);
                    v.setFocusable(false);
                    v.setVisibility(View.INVISIBLE);
                    return v;
                }
                if (position == faceNumPager) { // 删除键
                    ImageView img = new ImageView(mContext);
                    img.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(mContext, 36)));
                    img.setImageResource(R.drawable.sym_keyboard_delete_holo_dark);
                    img.setScaleType(ImageView.ScaleType.CENTER);
                    return img;
                }
                if (v == null) { // 正常的表情
                    v = View.inflate(mContext, R.layout.emojicon_item, null);
                    ViewHolder holder = new ViewHolder();
                    holder.icon = (EmojiconTextView) v.findViewById(R.id.emojicon_icon);
                    holder.icon.setUseSystemDefault(mUseSystemDefault);
                    v.setTag(holder);
                }
                Emojicon emoji = (Emojicon) getItem(position);
                ViewHolder holder = (ViewHolder) v.getTag();
                if (emoji != null) {
                    holder.icon.setText(emoji.getEmoji());
                }
                if (position == 1) { // 加载此时真正0位置的图片
                    Emojicon emoji0 = (Emojicon) getItem(0);
                    ViewHolder holder0 = (ViewHolder) parent.getChildAt(0).getTag();
                    if (emoji0 != null) {
                        holder0.icon.setText(emoji0.getEmoji());
                    }
                }
            } else { // 0位置加载布局，但是不加载图片
                if (v == null) { // 正常的表情
                    v = View.inflate(mContext, R.layout.emojicon_item, null);
                    ViewHolder holder = new ViewHolder();
                    holder.icon = (EmojiconTextView) v.findViewById(R.id.emojicon_icon);
                    holder.icon.setUseSystemDefault(mUseSystemDefault);
                    v.setTag(holder);
                }
            }
            return v;
        }

        class ViewHolder {
            EmojiconTextView icon;
        }
    }

    public class FacePagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public FacePagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
