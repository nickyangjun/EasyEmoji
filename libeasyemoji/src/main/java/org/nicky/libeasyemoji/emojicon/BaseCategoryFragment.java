package org.nicky.libeasyemoji.emojicon;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by nickyang on 2017/3/28.
 */

public class BaseCategoryFragment<T extends Parcelable> extends Fragment{

    protected PagerDataCategory<T> mPagerDataCategory;

    public BaseCategoryFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPagerDataCategory = getArguments().getParcelable("data");
    }

    public final void setArguments(PagerDataCategory<T> category){
        Bundle bundle = new Bundle();
        bundle.putParcelable("data",category);
        setArguments(bundle);
    }
}
