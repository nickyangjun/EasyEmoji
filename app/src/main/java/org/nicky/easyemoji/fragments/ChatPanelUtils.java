package org.nicky.easyemoji.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.nicky.easyemoji.R;
import org.nicky.easyemoji.utils.CommonUtil;

import java.util.ArrayList;


/**
 * 功能面板的icon区域 辅助工具类
 * Created by Seaky on 2017/3/18.
 */

public class ChatPanelUtils {

    public static int[] iconId = {R.drawable.icon_link_photo,
            R.drawable.icon_link_picture,
            R.drawable.icon_link_phone,
            R.drawable.icon_link_contacts,
            R.drawable.icon_link_location,
            R.drawable.icon_link_red,
            R.drawable.icon_link_game,
            R.drawable.icon_link_bill,
            R.drawable.icon_link_file
    };

    private static String[] textId = {
            "Camera",
            "Photo",
            "Call",
            "ContactCard",
            "Location",
            "RedPacket",
            "GameHall",
            "GroupBill",
            "File",
    };

    private static int[] viewId = {
            R.id.vp_btn_1,
            R.id.vp_btn_2,
            R.id.vp_btn_3,
            R.id.vp_btn_4
    };

    //当前是第几项标识
    public static int positionNow;


    //初始化小圆点
    public static void initDots(int pageNum,LinearLayout mLayoutDots,Activity context){
        //有几页显示几个小圆点
        for (int i = 0; i < pageNum; i++) {
            if (pageNum > 1) {
                ImageView dots = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(CommonUtil.dip2px(context,5),0,0,0);
                params.width = CommonUtil.dip2px(context,5);
                params.height = CommonUtil.dip2px(context,5);
                dots.setLayoutParams(params);
                if (i == 0) {
                    //第一个默认显示选中状态
                    dots.setImageResource(R.drawable.dot_selected);
                } else {
                    dots.setImageResource(R.drawable.dot_normal);
                }
                dots.setId(i);
                mLayoutDots.addView(dots);
            }
        }
    }

    //根据数组的长度、每页item数量，确定viewpager的页数
    public static int getPageNum(int length, int numberOfEveryPage) {//数组长度、每页item数量
        if (length > numberOfEveryPage) {
            int n = 1;
            while (length - numberOfEveryPage >= 0) {
                n++;
                length = length - numberOfEveryPage;
            }
            return n;
        } else {
            return 1;
        }
    }

    //实例化功能面板中TextView 方法,输入数组长度、每页item数
    public static void instantiatedView(int pageNum, int listSize, int numberOfEveryPage, ArrayList<View> views,Activity context) {
        for(int i = 0 ; i < pageNum -1; i++){
            //第一页加载
            for(int j = 0; j < numberOfEveryPage; j++) {
                TextView icon = (TextView)views.get(i).findViewById(viewId[j]);
                icon.setText(textId[i*numberOfEveryPage+j]);
                icon.setTag(textId[i*numberOfEveryPage+j]);
                Drawable top = context.getResources().getDrawable(iconId[i*numberOfEveryPage+j]);
                icon.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                icon.setOnClickListener(new PanelBtnClick(context));
            }
            //第一页加载完如果还有
            if(listSize%numberOfEveryPage != 0) {
                for(int k=0; k < listSize % numberOfEveryPage; k++) {
                    TextView icon = (TextView)views.get(pageNum - 1).findViewById(viewId[k]);
                    icon.setText(textId[(pageNum-1)*numberOfEveryPage+k]);
                    icon.setTag(textId[(pageNum-1)*numberOfEveryPage+k]);
                    Drawable top = context.getResources().getDrawable(iconId[(pageNum - 1) * 4 + k]);
                    icon.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
                    icon.setOnClickListener(new PanelBtnClick(context));
                }
            }
        }
    }

    //功能面板适配器
    public static class PanelViewAdapter extends PagerAdapter {
        private ArrayList<View> listViews;
        public int size;

        public PanelViewAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
        }
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);
            } catch (Exception e) {
               e.printStackTrace();
            }
            return listViews.get(arg1 % size);
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }


    //功能面板滑动监听
    public static class pageChangeListener implements ViewPager.OnPageChangeListener {

        private int pageNums;
        private int oldPosition = 0;
        private Activity context;

        public pageChangeListener(int pageNum,Activity activity){
            pageNums = pageNum;
            context = activity;
        }

        @Override
        public void onPageSelected(int position) {
            positionNow = position;
            if(pageNums > 1){
                ImageView dotSelected = (ImageView)context.findViewById(context.getResources().getIdentifier("" + position, "id", context.getPackageName()));
                dotSelected.setImageResource(R.drawable.dot_selected);

                ImageView dotNormal = (ImageView)context.findViewById(context.getResources().getIdentifier("" + oldPosition, "id", context.getPackageName()));
                dotNormal.setImageResource(R.drawable.dot_normal);

                oldPosition = position;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
    }


    private static class PanelBtnClick implements View.OnClickListener {

        private Activity context;

        private PanelBtnClick(Activity activity) {
            context = activity;
        }
        @Override
        public void onClick(View view) {
            Toast.makeText(context, view.getTag().toString(), Toast.LENGTH_SHORT).show();
            String tag = view.getTag().toString();
        }
    }
}
