package com.android.tkengine.elccommerce.presenter;

/**
 * Created by 陈嘉shuo on 2016/8/13.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.UI.NewAddressActivity;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.GoodsAddressBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.utils.CallbackActivityListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈嘉shuo on 2016/8/12.
 */
public class AddressPresenter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final  static int GET_ADDRESSINFO_SUCCESS = 1;
    private final static int DELETE_ADDRESSINFO_SUCCESS = 2;
    private final static int EDIT_ADDRESSINFO_SUCCESS = 3;
    private Context context;
    public List<GoodsAddressBean.ResultBean> goodsAddressList = new ArrayList<GoodsAddressBean.ResultBean>();
    protected static CallbackActivityListener callbackActivityListener;



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_ADDRESSINFO_SUCCESS:
                    for(GoodsAddressBean.ResultBean goodsAddressItem: goodsAddressList){   //保存默认地址
                        if(goodsAddressItem.isDefaultAddress()){
                            SharedPreferences.Editor editor = context.getSharedPreferences("default_address",context.MODE_PRIVATE).edit();
                            editor.putString("address_id",goodsAddressItem.getId());
                            editor.putString("receiver_name",goodsAddressItem.getReceiver());
                            Log.d("receiver",goodsAddressItem.getReceiver());
                            editor.putString("receiver_tel",goodsAddressItem.getPhone());
                            String receiverAddress = goodsAddressItem.getProvince() + "|" + goodsAddressItem.getCity() + "|" + goodsAddressItem.getCountyTown()
                                    + "|" + goodsAddressItem.getDetailsAddress();
                            editor.putString("receiver_address",receiverAddress);
                            editor.commit();
                        }
                    }
                    notifyDataSetChanged();
                    break;
                case DELETE_ADDRESSINFO_SUCCESS:
                    notifyDataSetChanged();
                    break;
                case EDIT_ADDRESSINFO_SUCCESS:
                    notifyDataSetChanged();
                default:
                    break;
            }
        }
    };


    public AddressPresenter(Context context) {
        this.context = context;
        initGoodsAddressList();
    }


    // //从服务器获取收货地址信息
    public void initGoodsAddressList(){
        SharedPreferences sp = context.getSharedPreferences(Constants.SP_LOGIN_USERINFO, Context.MODE_PRIVATE);
        final String userId = sp.getString("UserId", null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    goodsAddressList = new ElcModel(context).getGoodsAddressList(userId);
                    Message message = new Message();
                    message.what = GET_ADDRESSINFO_SUCCESS;
                    handler.sendMessage(message);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_item, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Log.d("position",String.valueOf(position));
        GoodsAddressBean.ResultBean goodsAddressItem = goodsAddressList.get(position);
        ((AddressViewHolder) holder).receiverName.setText(goodsAddressItem.getReceiver());
        final String address = goodsAddressItem.getProvince() + "|" + goodsAddressItem.getCity() + "|" + goodsAddressItem.getCountyTown()
                + "|" + goodsAddressItem.getDetailsAddress();
        ((AddressViewHolder) holder).receiverAddress.setText(address);
        ((AddressViewHolder) holder).receiverTel.setText(goodsAddressItem.getPhone());
        ((AddressViewHolder) holder).defaultAddress.setChecked(goodsAddressItem.isDefaultAddress());
        ((AddressViewHolder) holder).defaultAddress.setClickable(false);
        ((AddressViewHolder) holder).addressId.setText(goodsAddressItem.getId());
        ((AddressViewHolder) holder).addressView.setOnClickListener(new View.OnClickListener() {   //点击返回确定订单页面或者无效
            @Override
            public void onClick(View view) {
                callbackActivityListener.CallbackActivityResult((AddressViewHolder) holder);
            }
        });

        //编辑地址
        ((AddressViewHolder) holder).editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoodsAddressBean.ResultBean addressBean = new GoodsAddressBean.ResultBean();
                addressBean.setId(((AddressViewHolder) holder).addressId.getText().toString());
                Log.d("id",((AddressViewHolder) holder).addressId.getText().toString());
                addressBean.setReceiver(((AddressViewHolder) holder).receiverName.getText().toString());
                addressBean.setPhone(((AddressViewHolder) holder).receiverTel.getText().toString());
                String address = ((AddressViewHolder) holder).receiverAddress.getText().toString();
                String[] array = address.split("\\|");
                addressBean.setProvince(array[0]);
                addressBean.setCity(array[1]);
                addressBean.setCountyTown(array[2]);
                addressBean.setDetailsAddress(array[3]);
                addressBean.setDefaultAddress(((AddressViewHolder) holder).defaultAddress.isChecked());
                callbackActivityListener.CallbackActivity(addressBean);
            }
        });

        //删除地址
        ((AddressViewHolder) holder).deleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressId = goodsAddressList.get(holder.getPosition()).getId();
                goodsAddressList.remove(holder.getPosition());
                SharedPreferences sp = context.getSharedPreferences(Constants.SP_LOGIN_USERINFO, Context.MODE_PRIVATE);
                String userId = sp.getString("UserId", null);
                deleteAddress(userId,addressId);
            }
        });


    }

    @Override
    public int getItemCount() {
        return goodsAddressList.size();

    }



    public class AddressViewHolder extends RecyclerView.ViewHolder{

        public View addressView;
        public TextView addressId;
        public TextView receiverName;
         public TextView receiverTel;
        public TextView receiverAddress;
         public CheckBox defaultAddress;   //默认地址选择框
        public TextView defaultAddressLabel;
        public LinearLayout deleteAddress;   //删除地址
        public LinearLayout editAddress;   //编辑地址

        public AddressViewHolder(View view) {
            super(view);
            addressView = view;
            addressId = (TextView)view.findViewById(R.id.tv_address_id);
            receiverName = (TextView)view.findViewById(R.id.tv_receiverName);
            receiverTel = (TextView)view.findViewById(R.id.tv_receiverTel);
            receiverAddress = (TextView) view.findViewById(R.id.tv_receiverAddress);
            defaultAddress = (CheckBox) view.findViewById(R.id.chk_defaultAddress);
            defaultAddressLabel = (TextView)view.findViewById(R.id.tv_defaultAddress);
            editAddress = (LinearLayout) view.findViewById(R.id.ll_editAddress);
            deleteAddress = (LinearLayout) view.findViewById(R.id.ll_deleteAddress);
    }


    }

    public void deleteAddress(final String userId, final String addressId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean result = new ElcModel(context).postDeletedAddressInfo(userId, addressId);
                    if(result){
                        Message message = new Message();
                        message.what = DELETE_ADDRESSINFO_SUCCESS;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    public void setCallbackListener(CallbackActivityListener callbackActivityListener){
        this.callbackActivityListener = callbackActivityListener;
    }




}

