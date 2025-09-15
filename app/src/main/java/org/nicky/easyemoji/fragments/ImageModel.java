package org.nicky.easyemoji.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.nicky.easyemoji.R;
import org.nicky.easyemoji.image.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天内容页业务处理类
 * Created by nicky on 2017/3/18.
 */

public class ImageModel {


    //本地图库加载器
    public static class AttachPhotoLoaderCallback implements androidx.loader.app.LoaderManager.LoaderCallbacks<Cursor> {

        private Context mContext;
        private LocalImgAdapter adapter;
        private final String[] IMAGE_PROJECTION = new String[]{"_data", "_display_name", "date_added", "mime_type", "_size", "_id"};

        public AttachPhotoLoaderCallback(Context context, LocalImgAdapter localImgAdapter) {
            mContext = context;
            adapter = localImgAdapter;
        }

        @Override
        public androidx.loader.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new androidx.loader.content.CursorLoader( mContext,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION,
                    "",
                    null,
                    IMAGE_PROJECTION[2] + " DESC");
        }

        private boolean fileExist(String path) {
            return !TextUtils.isEmpty(path) && (new File(path)).exists();
        }

        @Override
        public void onLoadFinished(androidx.loader.content.Loader<Cursor> loader, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                ArrayList<String> images = new ArrayList<>();
                cursor.moveToFirst();
                do {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));

                    if (fileExist(path)) {
                        images.add(path);
                    }
                } while (cursor.moveToNext());
                adapter.setData(images);
            }
        }

        @Override
        public void onLoaderReset(androidx.loader.content.Loader<Cursor> loader) {

        }
    }


    /**
     * 本地图库 功能面板的画廊适配器
     */
    public static class LocalImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> imageList = new ArrayList<>();
        private Activity mContext;

        public LocalImgAdapter(Activity context) {
            mContext = context;
        }

        public void setData(List<String> images) {
            if (images != null && images.size() > 0) {
                imageList = images;
            } else {
                imageList.clear();
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 1;
            } else {
                return 2;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (1 == viewType) {
                view = mContext.getLayoutInflater().inflate(R.layout.item_localimg, parent, false);
                //第一项固定显示相机
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // mContext.startActivityForResult(new Intent(mContext, VideoRecordActivity.class), SGChatActivity.CODE_TAKE_VIDEO);
                    }
                });
                return new FisrtImageViewHolder(view);
            } else {
                view = mContext.getLayoutInflater().inflate(R.layout.item_localimg_firsht, parent, false);
                return new AttachPhotoHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (2 == holder.getItemViewType()) {
                ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.item_local_img);
                if (imageList.size() > 1) {
                    final String path = imageList.get(position - 1);
                    ImageLoader.getInstance().displayImage(imageView.getContext(), path, imageView, R.drawable.icon_default, 3);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(mContext, path, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return imageList.size() + 1;
        }

        private class FisrtImageViewHolder extends RecyclerView.ViewHolder {

            private FisrtImageViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class AttachPhotoHolder extends RecyclerView.ViewHolder {
            private AttachPhotoHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
