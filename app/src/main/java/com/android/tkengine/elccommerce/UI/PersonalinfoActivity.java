package com.android.tkengine.elccommerce.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.UserInfoBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.presenter.PersonalActPresenter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class PersonalinfoActivity extends AppCompatActivity implements PersonalActPresenter.CallbackOfView{

    SharedPreferences sp;
    ImageView iv_userIcon;
    TextView tv_userPhone, tv_userName, tv_userSex;
    View changeIcon, changeUserName, changeUserSex, changePassword;

    PersonalActPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);

        mPresenter = new PersonalActPresenter(this, this);

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
        updateUserInfo();

        //实现沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //点击设置头像
        changeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.changeIcon();
            }
        });

        //点击设置用户名
        changeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.changeUserName();
            }
        });

        //点击设置性别
        changeUserSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //点击设置登录密码
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.changePassword();
            }
        });

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


    //用于设置头像
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(RESULT_OK == resultCode){
            File file = new File(Environment.getExternalStorageDirectory() + "/ElcCommerce/UserIconTemp.jpg");
            if(1 == requestCode || 2 == requestCode){
                Intent intent = new Intent("com.android.camera.action.CROP");
                if (1 != requestCode) {
                    intent.setDataAndType(data.getData(), "image/*");
                }
                else {
                    intent.setDataAndType(Uri.fromFile(file), "image/*");
                }
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 200);
                intent.putExtra("aspectY", 200);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, 3);
            }
            else if(3 == requestCode){
                //将文件发送到服务器
                mPresenter.sendIconToServer(file);
            }
        }
    }

    @Override
    public void startCropActivity(Intent intent, int request) {
        startActivityForResult(intent, request);
    }

    @Override
    public void updateUserInfo() {
        Picasso.with(this).load(sp.getString("UserIcon", null)).fit().error(R.drawable.frgme_userunlogin).into(iv_userIcon);
        tv_userPhone.setText(sp.getString("UserPhone", " "));
        tv_userName.setText(sp.getString("UserName", " "));
        tv_userSex.setText(sp.getString("UserSex", " "));
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
