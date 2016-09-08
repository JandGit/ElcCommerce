package com.android.tkengine.elccommerce.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.GoodsBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.utils.HttpCallbackListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈嘉shuo on 2016/8/11.
 */
public class PayPresenter {


    private Context context;
    public List<GoodsBean> receiverGoodsList = new ArrayList<GoodsBean>();
    //商品价格精确度
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
   public ReceiverGoodsRecyclerViewAdapter receiverGoodsRecyclerViewAdapter = new ReceiverGoodsRecyclerViewAdapter();



    public PayPresenter(Context context) {
        this.context = context;
    }





    public class ReceiverGoodsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.cartfrg_goods_item, parent, false);
            return new ReceiverGoodsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            GoodsBean cartGoodsItem = receiverGoodsList.get(position);
            ((ReceiverGoodsViewHolder) holder).receiverGoodsName.setText(cartGoodsItem.getGoodsName());
            Picasso.with(context).load(cartGoodsItem.getGoodsIcon()).fit().into(((ReceiverGoodsViewHolder) holder).receiverGoodsIcon);
            ((ReceiverGoodsViewHolder) holder).receiverGoodsPrice.setText(String.valueOf(cartGoodsItem.getGoodsPrice()));
            ((ReceiverGoodsViewHolder) holder).receiverGoodsNumber.setFocusable(false);
            ((ReceiverGoodsViewHolder) holder).receiverGoodsNumber.setText("× " + String.valueOf(cartGoodsItem.getGoodsNum()));
            ((ReceiverGoodsViewHolder) holder).receiverGoodsNumber.setBackgroundResource(R.color.colorWhite);
            ((ReceiverGoodsViewHolder) holder).receiverGoodsSelected.setVisibility(View.GONE);
            ((ReceiverGoodsViewHolder) holder).receiverGoodsNumReduce.setVisibility(View.INVISIBLE);
            ((ReceiverGoodsViewHolder) holder).receiverGoodsNumAdd.setVisibility(View.INVISIBLE);
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return receiverGoodsList.size();
        }
    }
    class ReceiverGoodsViewHolder extends RecyclerView.ViewHolder{
        public TextView receiverGoodsName;
        public   TextView receiverGoodsPrice;
        public ImageView receiverGoodsIcon;
        public EditText receiverGoodsNumber;
        public TextView receiverGoodsNumAdd;
        public   TextView receiverGoodsNumReduce;
        public CheckBox receiverGoodsSelected;

        public ReceiverGoodsViewHolder(View view) {
            super(view);
            receiverGoodsName = (TextView)view.findViewById(R.id.tv_cart_goodsName);
            receiverGoodsPrice = (TextView)view.findViewById(R.id.tv_cart_goodsPrice);
            receiverGoodsIcon = (ImageView)view.findViewById(R.id.iv_cart_goodsIcon);
            receiverGoodsNumber = (EditText) view.findViewById(R.id.et_cart_goodsNum);
            receiverGoodsNumAdd = (TextView)view.findViewById(R.id.tv_cart_goodsAdd);
            receiverGoodsNumReduce = (TextView)view.findViewById(R.id.tv_cart_goodsReduce);
            receiverGoodsSelected = (CheckBox)view.findViewById(R.id.chk_cart_select);
        }
    }


    //提交订单,并将结果回调给PayActivity
    public void postOrder(final List<GoodsBean> receiverGoodsList, final String addressId, final String moneyAmount,
                            final HttpCallbackListener callBacklistener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    boolean result = new ElcModel(context).postOrderInfo(receiverGoodsList,addressId,moneyAmount);

                    Log.d("result",String.valueOf(result));
                    if(result){
                        if(callBacklistener != null){
                            callBacklistener.onFinish(String.valueOf(result));
                        }
                    }
                }catch (Exception e){
                    if(callBacklistener == null){
                        callBacklistener.onError(e);
                    }
                }

            }
        }).start();

    }

    //获取订单总额
    public String getOrderCost(){
        double totalCost = 0;
        for(GoodsBean receiverGoodsItem:receiverGoodsList){
            totalCost = totalCost + receiverGoodsItem.getGoodsPrice() * receiverGoodsItem.getGoodsNum();
        }
        return String.valueOf(decimalFormat.format(totalCost));
    }



}
