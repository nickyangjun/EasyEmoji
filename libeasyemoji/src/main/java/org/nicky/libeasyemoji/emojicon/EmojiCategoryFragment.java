package org.nicky.libeasyemoji.emojicon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.nicky.libeasyemoji.R;
import org.nicky.libeasyemoji.emojicon.emoji.Emojicon;

/**
 * Created by nickyang on 2017/3/28.
 */

public class EmojiCategoryFragment extends BaseCategoryFragment<Emojicon> {
    private RecyclerView mRecyclerView;
    private EmojiAdapter mEmojiAdapter;
    private OnEmojiconClickedListener mOnEmojiconClickedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        initView();
        return view;
    }

    private void initView(){
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),mPagerDataCategory.getColumn()));
        mEmojiAdapter = new EmojiAdapter();
        mRecyclerView.setAdapter(mEmojiAdapter);
    }



    private class EmojiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int EMOJI = 0;
        public static final int DELETE = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = View.inflate(getContext(), R.layout.emojicon_item, null);
            EmojiAdapter.Holder holder = new EmojiAdapter.Holder(v);
            holder.icon = (EmojiconTextView) v.findViewById(R.id.emojicon_icon);
            holder.image = (ImageView) v.findViewById(R.id.emoji_image);
            if(viewType == EMOJI) {
                holder.icon.setVisibility(View.VISIBLE);
                holder.image.setVisibility(View.GONE);
            }else {
                holder.icon.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            EmojiAdapter.Holder emojiHolder = (EmojiAdapter.Holder) holder;
            if(position == getItemCount() -1){
                emojiHolder.image.setImageResource(R.drawable.ic_smiles_backspace);
            }else {
                Emojicon emojicon = mPagerDataCategory.getEmojiData().get(position);
                emojiHolder.icon.setText(emojicon.getEmoji());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnEmojiconClickedListener != null){
                        if(position == getItemCount() -1){
                            mOnEmojiconClickedListener.onBackspaceClicked();
                        }else {
                            mOnEmojiconClickedListener.onEmojiconClicked(mPagerDataCategory.getEmojiData().get(position));
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mPagerDataCategory.hasBackspace()){
                return mPagerDataCategory.getEmojiData().size()+1;
            }
            return mPagerDataCategory.getEmojiData().size();
        }

        @Override
        public int getItemViewType(int position) {
            if(mPagerDataCategory.hasBackspace()){
                if(position == getItemCount()-1){
                    return DELETE;  //delete button
                }
            }
            return EMOJI;
        }

        class Holder extends RecyclerView.ViewHolder {
            EmojiconTextView icon;
            ImageView image;
            public Holder(View itemView) {
                super(itemView);
            }
        }
    }


    public void setOnEmojiconClickedListener(OnEmojiconClickedListener listener){
        mOnEmojiconClickedListener = listener;
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
        void onBackspaceClicked();
    }
}
