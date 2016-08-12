package com.android.tkengine.elccommerce.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.GoodsBean;
import com.android.tkengine.elccommerce.presenter.PayPresenter;
import com.android.tkengine.elccommerce.utils.DividerItemDecoration;

import java.util.List;

/**
 * Created by 陈嘉shuo on 2016/8/10.
 */
public class PayActivity extends AppCompatActivity {

    private TextView receiverName;
    private TextView receiverTel;
    private TextView receiverAddress;
    private RecyclerView receiverGoodsRecyclerView;
    private Button placeOrder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Intent intent = getIntent();
       /* payPresenter.receiverGoodsList = (List<GoodsBean>)this.getIntent().getSerializableExtra("receiver_goods_data");*/
       /* initPayView();*/
    }

    private void initPayView(){
        receiverName = (TextView)findViewById(R.id.tv_receiver_name);
        receiverTel = (TextView)findViewById(R.id.tv_receiver_tel);
        receiverAddress = (TextView)findViewById(R.id.tv_receiver_address);
        receiverGoodsRecyclerView = (RecyclerView)findViewById(R.id.rv_receiver_goods);
        placeOrder = (Button)findViewById(R.id.btn_place_order);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        receiverGoodsRecyclerView.setLayoutManager(linearLayoutManager);
        receiverGoodsRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
       /*receiverGoodsRecyclerView.setAdapter(payPresenter.receiverGoodsRecyclerViewAdapter);*/


    }




}
