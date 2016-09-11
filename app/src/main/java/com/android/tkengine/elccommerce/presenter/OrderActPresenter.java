package com.android.tkengine.elccommerce.presenter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.UI.DisplayActivity;
import com.android.tkengine.elccommerce.UI.OrderDetailActivity;
import com.android.tkengine.elccommerce.UI.PostEvaluationActivity;
import com.android.tkengine.elccommerce.UI.StoreDetailsActivity;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.OrderBean;
import com.android.tkengine.elccommerce.model.OrderActModel;
import com.android.tkengine.elccommerce.utils.MultiItemAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActPresenter {

    private static final String TAG = "OrderActPresenter";

    public interface CallbackOfView{
        //当前页面显示正在读取数据
        void showNowLoading();
        //网络连接错误
        void showLoadingFailed();
        //当前页面无数据
        void showNodata();
        //页面加载成功
        void onLoadingSuccess();
        //更新当前页面
        void updata();
    }

    CallbackOfView mView;
    OrderActModel mModel;
    Context mContext;

    public OrderActPresenter(CallbackOfView mView, Context context) {
        this.mView = mView;
        mContext = context;
        this.mModel = new OrderActModel(mContext);
    }

    /**
     * 读取指定订单页面的内容，读取完成后设置RecyclerView
     * @param page 0为全部，1为待付款，2为待发货，3为待收货，4为待评价
     */
    public void setPage(final RecyclerView view, int page){
        mView.showNowLoading();
        SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_LOGIN_USERINFO, Context.MODE_PRIVATE);
        final String userId = sp.getString("UserId", null);
        mModel.getOrder(userId, page, 1, new OrderActModel.ResponseListener() {
            @Override
            public void onResponse(OrderBean[] orders) {
                if(0 == orders.length){
                    mView.showNodata();
                }
                else{
                    List<OrderBean> data = Arrays.asList(orders);
                    MultiItemAdapter<OrderBean> adapter = new OrderAdapter(data, mContext);
                    view.setAdapter(adapter);
                    mView.onLoadingSuccess();
                }
            }

            @Override
            public void onError() {
                mView.showLoadingFailed();
            }
        });
    }

    public class OrderAdapter extends MultiItemAdapter<OrderBean>{

        public OrderAdapter(final List<OrderBean> mData, final Context mContext) {
            super(mData, mContext, new MultiItemSupport() {
                @Override
                public int getViewItemType(int position) {
                    String status = mData.get(position).state;
                    if(null == status){
                        Log.e(TAG, "未预料的订单状态！status = " + status);
                        return 0;
                    }
                    switch (status){
                        case "unpaid":
                            return 1;
                        case "unsent":
                            return 2;
                        case "unreceived":
                            return 3;
                        case "uncomment":
                            return 4;
                        default:
                            Log.e(TAG, "未预料的订单状态！status = " + status);
                            return 0;
                    }
                }

                @Override
                public int getViewItemLayoutId(int viewType) {
                    switch (viewType){
                        case 1://未付款
                            return R.layout.activity_order_item_unpaid;
                        case 2://未发货
                            return R.layout.activity_order_item_unsent;
                        case 3://待收货
                            return R.layout.activity_order_item_unreceived;
                        case 4://待评价
                            return R.layout.activity_order_item_uncomment;
                        default:
                            throw new RuntimeException("未预料的订单状态");
                    }
                }
            });
        }

        @Override
        public void convert(ViewHolder holder, final OrderBean itemData) {
            TextView tv_shopName = holder.getView(R.id.tv_shopName);
            TextView tv_state = holder.getView(R.id.tv_state);
            ListView lv = holder.getView(R.id.lv_orderProitems);
            TextView tv_sum = holder.getView(R.id.tv_sum);

            View btn_delete = holder.getView(R.id.btn_delete);
            View btn_detail = holder.getView(R.id.btn_detail);
            View btn_comment = holder.getView(R.id.btn_comment);
            View btn_cancel = holder.getView(R.id.btn_cancel);
            View btn_pay = holder.getView(R.id.btn_pay);
            View btn_checkSending = holder.getView(R.id.btn_checkSending);
            View btn_receive = holder.getView(R.id.btn_receive);
            View btn_urge = holder.getView(R.id.btn_urge);

            if(itemData.shopName != null){
                tv_shopName.setText(itemData.shopName);
            }
            //设置点击跳转到商铺
            final String id = itemData.sellerId;
            holder.getView(R.id.itemTitle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, StoreDetailsActivity.class);
                    intent.putExtra("storeID", id);
                    mContext.startActivity(intent);
                }
            });

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
                //id
                temp.put("id", itemData.proItems.get(i).product.id);

                data.add(temp);
            }
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
                public View getView(final int i, View view, ViewGroup viewGroup) {
                    if(null == view){
                        view = LayoutInflater.from(mContext).inflate(R.layout.activity_order_item_goods, viewGroup, false);
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
                    //点击商品跳转到详情
                    final String id = (String) mData.get(i).get("id");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, DisplayActivity.class);
                            intent.putExtra("productID", id);
                            mContext.startActivity(intent);
                        }
                    });

                    return view;
                }
            });

            tv_sum.setText("共" + countSum + "件商品 合计￥" + priceSum + "元");

            btn_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("data", itemData);
                    mContext.startActivity(intent);
                }
            });
            String status = itemData.state;
            if(status.equals("unpaid")){
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "取消订单", Toast.LENGTH_SHORT).show();
                    }
                });
                btn_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "付款", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if(status.equals("unsent")){
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "取消订单", Toast.LENGTH_SHORT).show();
                    }
                });
                btn_urge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mModel.updateOrder(itemData.id, "unreceived", new OrderActModel.ResponseListener() {
                            @Override
                            public void onResponse(OrderBean[] orders) {
                                mView.updata();
                            }
                            @Override
                            public void onError() {
                                Toast.makeText(mContext, "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
            else if(status.equals("unreceived")){
                btn_checkSending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "查看物流", Toast.LENGTH_SHORT).show();
                    }
                });
                btn_receive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mModel.updateOrder(itemData.id, "uncomment", new OrderActModel.ResponseListener() {
                            @Override
                            public void onResponse(OrderBean[] orders) {
                                mView.updata();
                            }
                            @Override
                            public void onError() {
                                Toast.makeText(mContext, "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
            else if(status.equals("uncomment")){
                btn_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, PostEvaluationActivity.class);
                        //intent.putExtra("data", itemData);
                        mContext.startActivity(intent);
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
}
