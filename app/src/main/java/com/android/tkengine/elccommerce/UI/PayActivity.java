package com.android.tkengine.elccommerce.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.GoodsBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.presenter.AddressPresenter;
import com.android.tkengine.elccommerce.presenter.PayPresenter;
import com.android.tkengine.elccommerce.utils.DividerItemDecoration;
import com.android.tkengine.elccommerce.utils.HttpCallbackListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈嘉shuo on 2016/8/10.
 */
public class PayActivity extends AppCompatActivity {

    private View payView;
    private TextView receiverName;    //收货人姓名
    private TextView receiverTel;     //收货人电话
    private TextView receiverAddress;    //收货人具体地址
    private RecyclerView receiverGoodsRecyclerView;    //下单所购买的商品
    private Button placeOrder;      //下单点击按钮
    private TextView moneyAmount;    //订单总额
    private PayPresenter payPresenter = new PayPresenter(this);
    private PopupWindow payPopupWindow;
    private RelativeLayout address;   //地址，用于点击跳转到地址管理页面
    private String addressId;  //地址ID，下订单时发送给服务器


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        // 1 表示由购物车结算跳转到支付页面， 2 表示由商品详情立即下单跳转到支付页面
        int type = getIntent().getIntExtra("from",0);
        if(type == 1){
            //获取从购物车传送过来的商品信息
            payPresenter.receiverGoodsList = (List<GoodsBean>) this.getIntent().getSerializableExtra("receiver_goods_data");
        }else if(type == 2){
            //获取从商店详情传送过来的商品信息
            GoodsBean goodsBean = (GoodsBean)this.getIntent().getSerializableExtra("product");
            Log.d("goodsBean",goodsBean.getGoodsName());
            Log.d("goodsBean",String.valueOf(goodsBean.getGoodsPrice()));
            List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
            goodsList.add(goodsBean);
            payPresenter.receiverGoodsList = goodsList;
        }


        initPayView();
    }

    private void initPayView() {

        payView = getLayoutInflater().from(this).inflate(R.layout.activity_pay, null);
        receiverName = (TextView) findViewById(R.id.tv_receiver_name);
        receiverAddress = (TextView) findViewById(R.id.tv_receiver_address);
        receiverTel = (TextView) findViewById(R.id.tv_receiver_tel);

        try{
            SharedPreferences sharedPreferences = getSharedPreferences("default_address", MODE_PRIVATE);
            addressId = sharedPreferences.getString("address_id", "");
            receiverName.setText(sharedPreferences.getString("receiver_name", ""));
            receiverAddress.setText(sharedPreferences.getString("receiver_address", ""));
            receiverTel.setText(sharedPreferences.getString("receiver_tel", ""));
        }catch (Exception e){
            receiverTel.setText("请选择 >");
        }



        receiverGoodsRecyclerView = (RecyclerView) findViewById(R.id.rv_receiver_goods);
        moneyAmount = (TextView) findViewById(R.id.tv_money_amount);
        moneyAmount.setText(payPresenter.getOrderCost());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        receiverGoodsRecyclerView.setLayoutManager(linearLayoutManager);
        receiverGoodsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        receiverGoodsRecyclerView.setAdapter(payPresenter.receiverGoodsRecyclerViewAdapter);

        placeOrder = (Button) findViewById(R.id.btn_place_order);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(receiverAddress.getText().toString())) {
                    showPayWindow();
                } else {
                    Toast.makeText(PayActivity.this, "请选择收货信息，再下订单！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        address = (RelativeLayout) findViewById(R.id.rl_receiver_address);
        address.setOnClickListener(new View.OnClickListener() {    //点击跳转到地址管理页面
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayActivity.this, AddressActivity.class);
                intent.putExtra("from", 1);
                startActivityForResult(intent, 1);
            }
        });


    }

    //支付界面设置
    private void showPayWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_pay, null);
        payPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        payPopupWindow.setFocusable(true);
        payPopupWindow.setOutsideTouchable(false);
        payPopupWindow.setTouchable(true);
        payPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        Button payEnsure = (Button) view.findViewById(R.id.pay_ensure);
        Button payCancel = (Button) view.findViewById(R.id.pay_cancel);
        final EditText payPassword = (EditText) view.findViewById(R.id.edtTxt_password);
        payEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(payPassword.toString().equals("123")){
                    payPresenter.postOrder(payPresenter.receiverGoodsList, "1", moneyAmount.getText().toString(),
                            "http://192.168.1.108:8080/TKBaas//order/pc/submitOrder", new HttpCallbackListener() {
                                @Override
                                public void onFinish(String result) {
                                    if(result.equalsIgnoreCase("true")){
                                        Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onError(Exception e) {
                                    Toast.makeText(PayActivity.this,"支付失败，请稍后再试",Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(PayActivity.this,"密码错误，请重新输入！",Toast.LENGTH_SHORT).show();
                }*/
                SharedPreferences sp = getSharedPreferences(Constants.SP_LOGIN_USERINFO, Context.MODE_PRIVATE);
                String password = sp.getString("password", null);
                Log.d("password",password);
                if(payPassword.getText().toString().equals(password)){
                    payPresenter.postOrder(payPresenter.receiverGoodsList, addressId, moneyAmount.getText().toString(),
                            new HttpCallbackListener() {
                                @Override
                                public void onFinish(String result) {
                                    if (result.equals("true")) {
                                        Log.d("true", "true");
                                        Intent intent = new Intent(PayActivity.this, OrderActivity.class);
                                        intent.putExtra("flag",2);
                                        startActivity(intent);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        finish();
                                    }
                                    else{
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(PayActivity.this, "账户余额不足", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(PayActivity.this, "支付失败，请稍后再试", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                    payPopupWindow.dismiss();
                }else{
                    Toast.makeText(PayActivity.this,"密码错误，请重新输入！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        payCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                payPopupWindow.dismiss();

            }
        });
        payPopupWindow.showAtLocation(payView, Gravity.CENTER, 0, 0);
    }


    //地址选择后返回的结果
    @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    receiverName.setText(data.getStringExtra("receiver_name"));
                    receiverAddress.setText(data.getStringExtra("receiver_address"));
                    receiverTel.setText(data.getStringExtra("receiver_tel"));

                }
        }
    }


}
