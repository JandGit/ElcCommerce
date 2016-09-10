package com.android.tkengine.elccommerce.UI;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.model.WalletModel;

public class MywalletActivity extends AppCompatActivity {

    WalletModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences(Constants.SP_LOGIN_USERINFO, MODE_PRIVATE);
        setMoney(sp.getFloat("UserMoney", 0));
    }

    public void onBackClick(View view){
        finish();
    }

    //充值按钮事件
    public void chargeBtnClick(View view){
        if(null == mModel){
            mModel = new WalletModel(this);
        }
        TextView tv_money = (TextView) findViewById(R.id.et_chargeMoney);
        final double chargeMoney = Double.valueOf(tv_money.getText().toString());

        final SharedPreferences sp = getSharedPreferences(Constants.SP_LOGIN_USERINFO, MODE_PRIVATE);
        String userId = sp.getString("UserId", null);
        String password = sp.getString("password", null);
        if(!sp.getBoolean("isLogin", false) || null == userId || password == null){
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
        }
        else{
            mModel.chargeMoney(userId, password, chargeMoney, new WalletModel.ResponseListener(){
                @Override
                public void onResponse(boolean result) {
                    if(result){
                        Toast.makeText(MywalletActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                        double money = sp.getFloat("UserMoney", 0) + chargeMoney;
                        sp.edit().putFloat("UserMoney", (float) money).apply();
                        setMoney(money);
                    }
                    else{
                        Toast.makeText(MywalletActivity.this, "充值失败，密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onError() {
                    Toast.makeText(MywalletActivity.this, "充值失败，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 设置显示余额
     */
    public void setMoney(double money){
        TextView tv = (TextView) findViewById(R.id.tv_money);
        tv.setText("余额 ￥" + money);
    }
}
