package com.android.tkengine.elccommerce.UI;import android.content.SharedPreferences;import android.os.Bundle;import android.support.annotation.Nullable;import android.support.v7.app.AppCompatActivity;import android.view.View;import android.widget.Button;import android.widget.ImageView;import android.widget.RelativeLayout;import android.widget.TextView;import android.widget.Toast;import com.android.tkengine.elccommerce.R;import com.android.tkengine.elccommerce.beans.Constants;import com.android.tkengine.elccommerce.model.WalletModel;/** * Created by FangYu on 2016/9/10. */public class AddWalletActivity extends AppCompatActivity {    TextView money, name;    ImageView back;    Button sure;    WalletModel mModel;    @Override    protected void onCreate(@Nullable Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_add_wallet);        mModel = new WalletModel(this);        initView();    }    private void initView() {        money = (TextView) findViewById(R.id.add_wallet_money);        back = (ImageView) findViewById(R.id.title_back);        name = (TextView) findViewById(R.id.title_name);        sure = (Button) findViewById(R.id.add_wallet_sure);        sure.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                if(null == mModel){                    mModel = new WalletModel(AddWalletActivity.this);                }                TextView tv_money = (TextView) findViewById(R.id.et_chargeMoney);                final double chargeMoney = Double.valueOf(tv_money.getText().toString());                final SharedPreferences sp = getSharedPreferences(Constants.SP_LOGIN_USERINFO, MODE_PRIVATE);                String userId = sp.getString("UserId", null);                String password = sp.getString("password", null);                if(!sp.getBoolean("isLogin", false) || null == userId || password == null){                    Toast.makeText(AddWalletActivity.this, "请先登录", Toast.LENGTH_SHORT).show();                }                else{                    mModel.chargeMoney(userId, password, chargeMoney, new WalletModel.ResponseListener(){                        @Override                        public void onResponse(boolean result) {                            if(result){                                Toast.makeText(AddWalletActivity.this, "充值成功", Toast.LENGTH_SHORT).show();                                double money = sp.getFloat("UserMoney", 0) + chargeMoney;                                sp.edit().putFloat("UserMoney", (float) money).apply();                            }                            else{                                Toast.makeText(AddWalletActivity.this, "充值失败，密码错误", Toast.LENGTH_SHORT).show();                            }                        }                        @Override                        public void onError() {                            Toast.makeText(AddWalletActivity.this, "充值失败，请稍后再试", Toast.LENGTH_SHORT).show();                        }                    });                }            }        });        name.setText("充值");        back.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                finish();            }        });    }}