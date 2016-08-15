package com.android.tkengine.elccommerce.utils;

import android.support.v7.widget.RecyclerView;

import com.android.tkengine.elccommerce.beans.GoodsAddressBean;
import com.android.tkengine.elccommerce.presenter.CartFrgPresenter;

/**
 * Created by 陈嘉shuo on 2016/8/13.
 */
public interface CallbackActivityListener {
    void CallbackActivity(GoodsAddressBean.ResultBean goodsAddressBean);
    void CallbackActivityResult(RecyclerView.ViewHolder viewHolder);
}
