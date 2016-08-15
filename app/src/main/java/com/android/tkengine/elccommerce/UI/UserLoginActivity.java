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
    //忘记密码、注册用户、切换用户按钮
    View viewLeft, viewRight;

    private UserLoginActPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);


        mPresenter = new UserLoginActPresenter(this, this);
        mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
        et_userName = (TextView) findViewById(R.id.et_userName);
        et_password = (TextView) findViewById(R.id.et_password);
        iv_userIcon = (ImageView) findViewById(R.id.iv_userIcon);
        tv_userName = (TextView) findViewById(R.id.tv_localUserName);
        viewLeft = findViewById(R.id.tv_forgotPassword);
        viewRight = findViewById(R.id.tv_signIn);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setPage();
    }

    private void setPage(){
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        String userPhone = sp.getString("UserPhone", null);
        if (userPhone != null) {
            tv_userName.setText(sp.getString("UserName", "null"));
            tv_userName.setVisibility(View.VISIBLE);
            et_userName.setText(userPhone);
            et_userName.setVisibility(View.INVISIBLE);
            ((TextView)viewRight).setText("切换登录用户");
            viewLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast("忘记密码");
                }
            });
            viewRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iv_userIcon.setImageResource(R.drawable.frgme_userunlogin);
                    tv_userName.setVisibility(View.INVISIBLE);
                    et_userName.setVisibility(View.VISIBLE);
                    viewLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showToast("忘记密码");
                        }
                    });
                    ((TextView)viewRight).setText("注册新用户");
                    viewRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(UserLoginActivity.this, SignupActivity.class));
                            finish();
                        }
                    });
                }
            });
            Picasso.with(this).load(sp.getString("UserIcon", null)).fit()
                    .error(R.drawable.frgme_userunlogin)
                    .into(iv_userIcon);
        }
        else {
            iv_userIcon.setImageResource(R.drawable.frgme_userunlogin);
            tv_userName.setVisibility(View.INVISIBLE);
            et_userName.setVisibility(View.VISIBLE);
            viewLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast("忘记密码");
                }
            });
            ((TextView)viewRight).setText("注册新用户");
            viewRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(UserLoginActivity.this, SignupActivity.class));
                    finish();
                }
            });
        }
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
        //更改当前已登录用户信息
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", true)
                .putString("UserPhone", info.getUser_phone())
                .putString("password", et_password.getText().toString())
                .putString("UserName", info.getUser_name())
                .putString("UserId", info.getUserId())
                .putString("UserIcon", info.getUser_picture_url())
                .putString("UserSex", info.getUser_sex())
                .putFloat("UserMoney", (float) info.getUser_money())
                .apply();
        //记住用户
        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putString(info.getUserId() + ":UserPassword", et_password.getText().toString())
                .putString(info.getUserId() + ":UserPhone", info.getUser_phone())
                .putString(info.getUserId() + ":UserName", info.getUser_name())
                .putString(info.getUserId() + ":UserIcon", info.getUser_picture_url())
                .apply();
        setResult(RESULT_OK);
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
