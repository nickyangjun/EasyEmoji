package org.nicky.libeasyemoji.emojicon;

import android.os.Parcel;
import android.os.Parcelable;

import org.nicky.libeasyemoji.emojicon.interfaces.BaseCategory;

import java.util.List;

/**
 * Created by nickyang on 2017/3/28.
 */

public class PagerDataCategory<T extends Parcelable> implements Parcelable {
    public int icon;
    public String categoryName;
    public List<T> dataList;
    public boolean hasBackspace;
    public int column;
    public int row;

    public PagerDataCategory(BaseCategory<T> category,List<T> dataList){
        this.icon = category.getCategoryItemIcon();
        this.categoryName = category.getCategoryName();
        this.dataList = dataList;
        this.hasBackspace = category.hasBackspace();
        this.column = category.getColumn();
        this.row = category.getRow();
    }

    public PagerDataCategory(Parcel in) {
        icon = in.readInt();
        categoryName = in.readString();
        column = in.readInt();
        row = in.readInt();
        dataList = in.readArrayList(getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeString(categoryName);
        dest.writeInt(column);
        dest.writeInt(row);
        dest.writeParcelableArray((Parcelable[]) dataList.toArray(),flags);
    }

    public String getCategoryName() {
        return categoryName;
    }


    public int getCategoryItemIcon() {
        return icon;
    }

    public List<T> getEmojiData() {
        return dataList;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public boolean hasBackspace() {
        return hasBackspace;
    }

    public static final Creator<PagerDataCategory> CREATOR = new Creator<PagerDataCategory>() {
        @Override
        public PagerDataCategory createFromParcel(Parcel in) {
            return new PagerDataCategory(in);
        }

        @Override
        public PagerDataCategory[] newArray(int size) {
            return new PagerDataCategory[size];
        }
    };

}
