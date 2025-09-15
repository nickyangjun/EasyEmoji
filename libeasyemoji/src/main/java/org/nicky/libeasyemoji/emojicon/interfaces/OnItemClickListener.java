package org.nicky.libeasyemoji.emojicon.interfaces;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by nickyang on 2017/3/28.
 */

public abstract class OnItemClickListener implements View.OnClickListener {
    RecyclerView.ViewHolder holder;
    int position;

    public OnItemClickListener(RecyclerView.ViewHolder holder,int position){
        this.holder = holder;
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        onItemClick(holder,position);
    }

    public abstract void onItemClick(RecyclerView.ViewHolder holder,int position);
}
