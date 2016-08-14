package com.android.tkengine.elccommerce.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.squareup.picasso.Picasso;

public class PersonalinfoActivity extends AppCompatActivity {

    SharedPreferences sp;
    ImageView iv_userIcon;
    TextView tv_userPhone, tv_userName, tv_userSex;
    View changeIcon, changeUserName, changeUserSex, changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);

        iv_userIcon = (ImageView) findViewById(R.id.iv_userIcon);
        tv_userPhone = (TextView) findViewById(R.id.tv_userPhone);
        tv_userName = (TextView) findViewById(R.id.tv_userName);
        tv_userSex = (TextView) findViewById(R.id.tv_userSex);
        changeIcon = findViewById(R.id.rl_userIcon);
        changeUserName = findViewById(R.id.rl_userName);
        changeUserSex = findViewById(R.id.rl_userSex);
        changePassword = findViewById(R.id.rl_modifyPassword);

        sp = getSharedPreferences(Constants.SP_LOGIN_USERINFO, MODE_PRIVATE);

        //显示用户信息
        Picasso.with(this).load(sp.getString("UserIcon", null)).fit().into(iv_userIcon);
        tv_userPhone.setText(sp.getString("UserPhone", " "));
        tv_userName.setText(sp.getString("UserName", " "));
        tv_userSex.setText(sp.getString("UserSex", " "));
    }

    //退出账户按钮事件
    public void onLogoutClick(View view){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", false)
                .remove("password")
                .apply();
        finish();
    }
    //切换账户按钮事件
    public void onChangeAccountClick(View view){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", false)
                .remove("UserPhone")
                .remove("password")
                .apply();
        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
        finish();
    }

    //后退按钮点击事件
    public void onBackClick(View view){
        finish();
    }

}
