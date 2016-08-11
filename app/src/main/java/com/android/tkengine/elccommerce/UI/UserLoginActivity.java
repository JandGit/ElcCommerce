package com.android.tkengine.elccommerce.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.beans.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.UserInfoBean;
import com.android.tkengine.elccommerce.presenter.UserLoginActPresenter;
import com.squareup.picasso.Picasso;

public class UserLoginActivity extends AppCompatActivity implements UserLoginActPresenter.CallbackOfView{

    Toast mToast;
    //登录界面的两个输入框
    TextView et_userName, et_password;
    //用户头像
    ImageView iv_userIcon;
    //用户名
    TextView tv_userName;

    private UserLoginActPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        //实现沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mPresenter = new UserLoginActPresenter(this, this);
        mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
        et_userName = (TextView) findViewById(R.id.et_userName);
        et_password = (TextView) findViewById(R.id.et_password);
        iv_userIcon = (ImageView) findViewById(R.id.iv_userIcon);
        tv_userName = (TextView) findViewById(R.id.tv_localUserName);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //检查用户是否已经登录
    private boolean isUserLogined() {
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        return sp.getBoolean("isLogin", false);
    }

    //设置上方的用户头像及用户名
    private void showUserInfo() {
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        tv_userName.setText(sp.getString("UserName", "null"));
        Log.i("presenter", "设置头像" + (Constants.SERVER_ADDRESS  + sp.getString("UserIcon", null)));
        Picasso.with(this).load(Constants.SERVER_ADDRESS  + sp.getString("UserIcon", null)).fit().into(iv_userIcon);
    }

    //登录按钮按下回调事件
    public void onLoginBtnClick(View view) {
        String userName = et_userName.getText().toString();
        String password = et_password.getText().toString();
        mPresenter.login(userName, password);
    }

    //回调接口,用户输入密码时根据用户名加载用户头像
    public void onPasswordInput(View view){
        String userName = et_userName.getText().toString();
        mPresenter.loadUserIcon(userName);
    }

    @Override
    public void onLoginSuccess(UserInfoBean info) {
        Intent intent = new Intent();
        intent.putExtra("info", info);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showToast(String text) {
        mToast.setText(text);
        mToast.show();
    }

    public void setUserIcon(Bitmap icon){
        iv_userIcon.setImageBitmap(icon);
    }

    @Override
    public void showLogining(){
        findViewById(R.id.tv_nowLogining).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_signUp).setEnabled(false);
    }

    @Override
    public void onLoginFailed(){
        findViewById(R.id.tv_nowLogining).setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_signUp).setEnabled(true);
    }

}
