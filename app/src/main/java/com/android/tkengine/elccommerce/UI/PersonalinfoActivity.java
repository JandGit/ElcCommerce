package com.android.tkengine.elccommerce.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;

public class PersonalinfoActivity extends AppCompatActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);

        sp = getSharedPreferences(Constants.SP_LOGIN_USERINFO, MODE_PRIVATE);
    }

    //退出账户按钮事件
    public void onLogoutClick(View view){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", false).apply();
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

}
