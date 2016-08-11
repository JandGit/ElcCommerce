package com.android.tkengine.elccommerce.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/*
 * 封装RecyclerView Adapter
 * 用法
 *
 * MultiItemAdapter.MultiItemSupport callback = new MultiItemAdapter.MultiItemSupport() {
        @Override
        public int getViewItemType(int position) {
            if(0 == position){
                return 0;
            }
            else if(1 == position || 6 == position){
                return 1;
            }
            else {
                return 2;
            }
        }
        @Override
        public int getViewItemLayoutId(int viewType) {
            if(0 == viewType){
                return R.layout.rv_headview;
            }
            else if(1 == viewType){
                return R.layout.rv_group;
            }
            else {
                return R.layout.rv_item;
            }
        }
    };
    rv.setAdapter(new MultiItemAdapter<Bean>(data, this, callback) {
    @Override
    public void convert(ViewHolder holder, Bean itemData) {
        if(itemData.TYPE_GROUP == itemData.type){
        holder.setText(R.id.tv_groupName, itemData.groupName);
        }
        else if(itemData.TYPE_HEADVIEW == itemData.type){
        holder.setImageResource(R.id.iv_head, R.mipmap.head);
        }
        else{
        holder.setImageResource(R.id.iv_itemIcon, itemData.itemIconRes);
        holder.setText(R.id.tv_itemName, itemData.itemName);
        }
        }
        });

 */

public abstract class MultiItemAdapter<T> extends RecyclerView.Adapter<MultiItemAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View convertView;
        private SparseArray<View> mViewChildren;

        public ViewHolder(View itemView) {
            super(itemView);
            convertView = itemView;
            mViewChildren = new SparseArray<>();
        }

        public static ViewHolder createViewHolder(int layoutId, Context context, ViewGroup parent){
            return new ViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
        }

        public <T extends View> T getView(int viewId){
            View view = mViewChildren.get(viewId);
            if(null == view){
                view = convertView.findViewById(viewId);
                mViewChildren.put(viewId, view);
            }
            return (T)view;
        }

        public void setText(int id, String text){
            TextView tv = getView(id);
            tv.setText(text);
        }

        public void setImageResource(int id, int res){
            ImageView iv = getView(id);
            iv.setImageResource(res);
        }

        public void setImageBitmap(int id, Bitmap bitmap){
            ImageView iv = getView(id);
            iv.setImageBitmap(bitmap);
        }
    }

    public interface MultiItemSupport{
        int getViewItemType(int position);
        int getViewItemLayoutId(int viewType);
    }

    Context mContext;
    List<T> mData;
    MultiItemSupport mMultiItemSupport;

    @Override
    public int getItemViewType(int position) {
        return mMultiItemSupport.getViewItemType(position);
    }

    //多种类型Item支持
    public MultiItemAdapter(final List<T> mData, final Context mContext, MultiItemSupport mMultiItemSupport) {
        this.mData = mData;
        this.mContext = mContext;
        this.mMultiItemSupport = mMultiItemSupport;
    }

    //单一类型Item支持
    public MultiItemAdapter(final List<T> mData, final Context mContext, final int layoutId) {
        this(mData, mContext, new MultiItemSupport() {
            @Override
            public int getViewItemType(int position) {
                return 0;
            }
            @Override
            public int getViewItemLayoutId(int viewType) {
                return layoutId;
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.createViewHolder(mMultiItemSupport.getViewItemLayoutId(viewType), mContext,
                parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mData.get(position));
    }

    public abstract void convert(ViewHolder holder, T itemData);

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
