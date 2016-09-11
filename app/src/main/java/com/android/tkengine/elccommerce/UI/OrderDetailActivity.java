package com.android.tkengine.elccommerce.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.OrderBean;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);

        OrderBean data = (OrderBean) getIntent().getSerializableExtra("data");
        if(data != null){
            TextView tv;
            tv = (TextView) findViewById(R.id.tv_receiver);
            tv.setText("收货人：" + data.userId);
            tv = (TextView) findViewById(R.id.tv_address);
            tv.setText("收货地址：" + "");
            tv = (TextView) findViewById(R.id.tv_orderTime);
            tv.setText("下单时间：" + data.boughtDate.year + "年" + data.boughtDate.month + "月" +
                data.boughtDate.day + "日" + data.boughtDate.hours + "时" + data.boughtDate.minutes + "分");
            tv = (TextView) findViewById(R.id.tv_status);
            switch (data.state){
                case "unpaid":
                    tv.setText("订单状态：未付款");
                    break;
                case "unsent":
                    tv.setText("订单状态：未发货");
                    break;
                case "unreceived":
                    tv.setText("订单状态：待收货");
                    break;
                case "uncomment":
                    tv.setText("订单状态：待评价");
                    break;
            }
            tv = (TextView) findViewById(R.id.tv_orderId);
            tv.setText("订单编号：" + data.id);
        }
        else{
            finish();
        }
    }

    //后退箭头单击
    public void onBackClick(View view){
        finish();
    }
}
