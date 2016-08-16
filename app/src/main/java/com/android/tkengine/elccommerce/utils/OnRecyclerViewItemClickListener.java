package com.android.tkengine.elccommerce.utils;

import com.android.tkengine.elccommerce.presenter.CartFrgPresenter;

/**
 * Created by 陈嘉shuo on 2016/8/9.
 */
public interface OnRecyclerViewItemClickListener {
    void onItemAddClick(CartFrgPresenter.GoodsViewHolder viewHolder);
    void onItemReduceClick(CartFrgPresenter.GoodsViewHolder viewHolder);
    void onItemCheckboxClick(CartFrgPresenter.GoodsViewHolder viewHolder);
    void onItemViewClick(CartFrgPresenter.GoodsViewHolder viewHolder);
    void onGroupCheckboxClick(CartFrgPresenter. StoreViewHolder viewHolder);
    void onGroupClick(CartFrgPresenter. StoreViewHolder viewHolder);
}
