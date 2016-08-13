package com.android.tkengine.elccommerce.presenter;


import android.content.Context;

import com.android.tkengine.elccommerce.utils.MultiItemAdapter;

public class OrderActPresenter {

    public interface CallbackOfView{
        //设置当前页面RecyclerView的内容
        void setAdapter(MultiItemAdapter adapter);
        //当前页面显示正在读取数据
        void showNowLoading();
        //当前页面显示读取失败
        void showLoadingFailed();
    }

    CallbackOfView mView;

    public OrderActPresenter(CallbackOfView mView) {
        this.mView = mView;
    }

    /**
     * 读取指定订单页面的内容，读取完成后设置RecyclerView
     * @param page 0为全部，1为待付款，2为待发货，3为待收货，4为待评价
     */
    public void loadPage(int page){
        mView.showNowLoading();
        switch (page){
            case 0:

        }
    }
}
