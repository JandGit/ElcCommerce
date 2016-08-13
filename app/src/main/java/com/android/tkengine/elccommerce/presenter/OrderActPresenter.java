package com.android.tkengine.elccommerce.presenter;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.OrderBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.utils.MultiItemAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    ElcModel mModel;
    Context mContext;
    MyHandler mHandler;


    private static class MyHandler extends Handler{
        final int MSG_SETADAPTER = 0;

        CallbackOfView mView;

        public MyHandler(CallbackOfView view) {
            this.mView = view;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_SETADAPTER:{
                    OrderAdapter adapter = (OrderAdapter) msg.obj;
                    mView.setAdapter(adapter);
                    break;
                }
            }
            super.handleMessage(msg);
        }
    }

    public OrderActPresenter(CallbackOfView mView, Context context) {
        this.mView = mView;
        mContext = context;
        this.mModel = new ElcModel(mContext);
        this.mHandler = new MyHandler(mView);
    }

    /**
     * 读取指定订单页面的内容，读取完成后设置RecyclerView
     * @param page 0为全部，1为待付款，2为待发货，3为待收货，4为待评价
     */
    public void loadPage(final int page){
        mView.showNowLoading();
        SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_LOGIN_USERINFO, Context.MODE_PRIVATE);
        final String userId = sp.getString("UserId", null);
        if(null == userId){
            //用户尚未登录时的操作
            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(){
            @Override
            public void run() {
                try {
                    OrderBean[] orders = mModel.getOrder(userId, page, 0);
                    if(orders != null){
                        List<OrderBean> data = Arrays.asList(orders);
                        MultiItemAdapter<OrderBean> adapter = new OrderAdapter(data, mContext, R.layout.activity_order_item);
                        Message msg = mHandler.obtainMessage(mHandler.MSG_SETADAPTER);
                        msg.obj = adapter;
                        mHandler.sendMessage(msg);
                    }
                    else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static class OrderAdapter extends MultiItemAdapter<OrderBean>{

        public OrderAdapter(List<OrderBean> mData, Context mContext, int layoutId) {
            super(mData, mContext, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, OrderBean itemData) {

        }
    }
}
