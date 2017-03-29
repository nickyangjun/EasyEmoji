package org.nicky.libeasyemoji.emojicon.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by nickyang on 2017/3/27.
 */

public class IndexLinkedHashMap<K,V> extends LinkedHashMap {
    private ArrayList<K> arrayList;
    int curIndex = 0;

    public V add(K key,V val){
        V v = (V) super.put(key,val);
        if(arrayList == null){
            arrayList = new ArrayList<>();
        }
        arrayList.add(curIndex++,key);
        return v;
    }

    public V add(int position, K key,V val){
        V v;
        if(position>=curIndex){
            v = add(key,val);
        }else {
            arrayList.add(position,key);
            curIndex++;
            v = (V) super.put(key,val);
        }
        return v;
    }

    public V get(int index){
        if(arrayList == null){
            return null;
        }
        return (V) super.get(arrayList.get(index));
    }

    @Override
    public Object remove(Object key) {
        if(arrayList.contains(key)) {
            arrayList.remove(key);
            curIndex--;
            return super.remove(key);
        }
        return null;
    }

    public int indexOf(K key){
        int index = arrayList.indexOf(key);
        if(index>=0 && super.containsKey(key)){
            return index;
        }
        return -1;
    }
}
