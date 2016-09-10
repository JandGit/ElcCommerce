package com.android.tkengine.elccommerce.UI;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.model.ElcModel;

import org.json.JSONException;

import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    EditText et_userPhone, et_userPassword, et_userPassword1;

    MyHandler mHandler;

    private static class MyHandler extends Handler {
        final int MSG_OK = 0;
        final int MSG_ERROR = 1;


        SignupActivity me;

        public MyHandler(SignupActivity me) {
            this.me = me;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OK: {//用户信息更改，界面内容需要更新
                    Toast.makeText(me, "注册成功", Toast.LENGTH_SHORT).show();
                    me.finish();
                }
                case MSG_ERROR: {
                    String s = (String) msg.obj;
                    Toast.makeText(me, s, Toast.LENGTH_SHORT).show();
                }
            }
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mHandler = new MyHandler(this);
        et_userPhone = (EditText) findViewById(R.id.et_userPhone);
        et_userPassword = (EditText) findViewById(R.id.et_userpassword);
        et_userPassword1 = (EditText) findViewById(R.id.et_userpassword1);
    }

    /**
     * 注册按钮点击事件
     */
    public void onSignupClick(View view) {
        final String userPhone = ((TextView) et_userPhone).getText().toString();
        final String password = ((TextView) et_userPassword).getText().toString();
        String password1 = ((TextView) et_userPassword1).getText().toString();
        if (userPhone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "手机号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!userPhone.matches("^[1][358][0-9]{9}$")) {
            Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(password1)) {
            Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    if (new ElcModel(SignupActivity.this).signUp(userPhone, password)) {
                        mHandler.sendEmptyMessage(mHandler.MSG_OK);
                    }
                    else {
                        Message msg = mHandler.obtainMessage(mHandler.MSG_ERROR);
                        msg.obj = "注册失败，用户已存在";
                        mHandler.sendMessage(msg);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    Message msg = mHandler.obtainMessage(mHandler.MSG_ERROR);
                    msg.obj = "网络错误";
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void onBackClick(View view){
        finish();
    }
}
