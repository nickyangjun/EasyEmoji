package org.nicky.libeasyemoji.emojicon;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.nicky.libeasyemoji.R;
import org.nicky.libeasyemoji.emoji.EmojiFragment;
import org.nicky.libeasyemoji.emoji.interfaces.PageEmojiStyle;
import org.nicky.libeasyemoji.emojicon.emoji.Emojicon;
import org.nicky.libeasyemoji.emojicon.emoji.EmojiconPageEmojiStyle;

/**
 * Created by nickyang on 2017/4/1.
 */

public class EmojiconFragment<T extends Emojicon> extends EmojiFragment {
    private EmojiconPageEmojiStyle<T> emojiconData;
    private RecyclerView mRecyclerView;
    private EmojiAdapter mEmojiAdapter;

    @Override
    public void setData(PageEmojiStyle emojiData) {
        emojiconData = (EmojiconPageEmojiStyle<T>) emojiData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        initView();
        return view;
    }

    private void initView(){
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), emojiconData.getColumn()));
        mEmojiAdapter = new EmojiAdapter();
        mRecyclerView.setAdapter(mEmojiAdapter);
    }



    private class EmojiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int EMOJI = 0;
        public static final int DELETE = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            EmojiAdapter.Holder holder;
            if(viewType == EMOJI){
                View v = View.inflate(getContext(), R.layout.emojicon_item, null);
                holder = new EmojiAdapter.Holder(v);
                holder.icon = (EmojiconTextView) v.findViewById(R.id.emojicon_icon);
            }else {
                ImageView imageView = new ImageView(getActivity());
                holder = new EmojiAdapter.Holder(imageView);
            }
            holder.type = viewType;
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final EmojiAdapter.Holder emojiHolder = (EmojiAdapter.Holder) holder;
            if(emojiHolder.type==DELETE){
                ((ImageView)emojiHolder.itemView).setImageResource(R.drawable.ic_smiles_backspace);
            }else {
                Emojicon emojicon = emojiconData.getData().get(position);
                (emojiHolder.icon).setText(emojicon.getEmoji());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnEmojiconClickedListener != null){
                        if(position == getItemCount() -1){
                            mOnEmojiconClickedListener.onBackspaceClicked();
                        }else {
                            mOnEmojiconClickedListener.onEmojiconClicked(emojiconData.getData().get(position).getEmoji());
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if(emojiconData.hasBackspace()){
                return emojiconData.getData().size()+1;
            }
            return emojiconData.getData().size();
        }

        @Override
        public int getItemViewType(int position) {
            if(emojiconData.hasBackspace()){
                if(position == getItemCount()-1){
                    return DELETE;  //delete button
                }
            }
            return EMOJI;
        }

        class Holder extends RecyclerView.ViewHolder {
            EmojiconTextView icon;
            int type;
            public Holder(View itemView) {
                super(itemView);
            }
        }
    }
}
