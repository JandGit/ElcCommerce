package com.android.tkengine.elccommerce.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public abstract class MultiItemAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

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
