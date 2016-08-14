package com.android.tkengine.elccommerce.presenter;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.OrderBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.utils.MultiItemAdapter;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class OrderActPresenter {

    public interface CallbackOfView{
        //设置当前页面RecyclerView的内容
        void setAdapter(MultiItemAdapter adapter);
        //当前页面显示正在读取数据
        void showNowLoading();
        //当前页面显示读取失败
        void showLoadingFailed();
        //当前页面无数据
        void showNodata();
        //添加更多数据到RecyclerView末尾
        void addMoreItem(List<OrderBean> data);
    }

    CallbackOfView mView;
    ElcModel mModel;
    Context mContext;
    MyHandler mHandler;


    private static class MyHandler extends Handler{
        final int MSG_SETADAPTER = 0;
        final int MSG_NODATA = 1;
        final int MSG_NETWORK_ERROR = 2;

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
                case MSG_NODATA:{
                    mView.showNodata();
                    break;
                }
                case MSG_NETWORK_ERROR:{
                    mView.showLoadingFailed();
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
                    OrderBean[] orders = mModel.getOrder(userId, page, 1);
                    if(orders != null && orders.length != 0){
                        List<OrderBean> data = Arrays.asList(orders);
                        MultiItemAdapter<OrderBean> adapter = new OrderAdapter(data, mContext, R.layout.activity_order_item);
                        Message msg = mHandler.obtainMessage(mHandler.MSG_SETADAPTER);
                        msg.obj = adapter;
                        mHandler.sendMessage(msg);
                    }
                    else {
                        mHandler.sendEmptyMessage(mHandler.MSG_NODATA);
                    }
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(mHandler.MSG_NETWORK_ERROR);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static class OrderAdapter extends MultiItemAdapter<OrderBean>{

        Context mContext;

        public OrderAdapter(List<OrderBean> mData, Context mContext, int layoutId) {
            super(mData, mContext, layoutId);
            this.mContext = mContext;
        }

        @Override
        public void convert(ViewHolder holder, OrderBean itemData) {
            TextView tv_shopName = holder.getView(R.id.tv_shopName);
            TextView tv_state = holder.getView(R.id.tv_state);
            ListView lv = holder.getView(R.id.lv_orderProitems);
            TextView tv_sum = holder.getView(R.id.tv_sum);
            View btn_delete = holder.getView(R.id.btn_delete);
            View btn_detail = holder.getView(R.id.btn_detail);
            View btn_comment = holder.getView(R.id.btn_comment);
            if(itemData.shopName != null){
                tv_shopName.setText(itemData.shopName);
            }
            if(itemData.state != null){
                tv_state.setText(itemData.state);
            }

            int countSum = 0;
            float priceSum = 0;
            final ArrayList<Map<String, Object>> data = new ArrayList<>(itemData.proItems.size());
            for(int i = 0; i < itemData.proItems.size(); i++){
                HashMap<String, Object> temp = new HashMap<>();

                countSum += itemData.proItems.get(i).num;
                priceSum += itemData.proItems.get(i).product.price * itemData.proItems.get(i).num;
                //商品数量
                temp.put("count", "×" + itemData.proItems.get(i).num);
                //单价
                temp.put("price", "￥" + itemData.proItems.get(i).product.price);
                //名称
                temp.put("name", itemData.proItems.get(i).product.name);
                //产地
                temp.put("city", itemData.proItems.get(i).product.city);
                //图片
                temp.put("icon", itemData.proItems.get(i).product.picture);

                data.add(temp);
            }
            Log.i("商品数目：", "" + data.size());
            lv.setAdapter(new BaseAdapter() {
                ArrayList<Map<String, Object>> mData = data;
                @Override
                public int getCount() {
                    return mData.size();
                }

                @Override
                public Object getItem(int i) {
                    return mData.get(i);
                }

                @Override
                public long getItemId(int i) {
                    return 0;
                }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {
                    if(null == view){
                        view = LayoutInflater.from(mContext).inflate(R.layout.activity_order_item_item, viewGroup, false);
                    }
                    TextView tv = (TextView) view.findViewById(R.id.tv_goodName);
                    tv.setText((CharSequence) mData.get(i).get("name"));
                    tv = (TextView) view.findViewById(R.id.tv_city);
                    tv.setText((CharSequence) mData.get(i).get("city"));
                    tv = (TextView) view.findViewById(R.id.tv_goodPrice);
                    tv.setText((CharSequence) mData.get(i).get("price"));
                    tv = (TextView) view.findViewById(R.id.tv_count);
                    tv.setText((CharSequence) mData.get(i).get("count"));
                    ImageView iv = (ImageView) view.findViewById(R.id.iv_goodIcon);
                    Picasso.with(mContext).load((String) mData.get(i).get("icon")).fit().into(iv);

                    return view;
                }
            });
           /* lv.setAdapter(new SimpleAdapter(mContext, data, R.layout.activity_order_item_item,
                    new String[]{"icon", "name", "city", "price", "count"}, new int[]{R.id.iv_goodIcon,
                        R.id.tv_goodName, R.id.tv_city, R.id.tv_goodPrice, R.id.tv_count}));*/

            tv_sum.setText("共" + countSum + "件商品 合计￥" + priceSum + "元");

            btn_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "详情", Toast.LENGTH_SHORT).show();
                }
            });
            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "评价", Toast.LENGTH_SHORT).show();
                }
            });
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "删除", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
