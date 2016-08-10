package com.android.tkengine.elccommerce;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class UserLoginActivity extends AppCompatActivity {

    Toast mToast;
    //登录界面的两个输入框
    TextView et_userName, et_password;

    private MyHandler myHandler;

    private static class MyHandler extends Handler {
        final int MSG_LOGINOK = 0;
        final int MSG_LOGINFAILED = 1;

        UserLoginActivity mAtivity;

        public MyHandler(UserLoginActivity mAtivity) {
            this.mAtivity = mAtivity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOGINOK:
                    mAtivity.showToast("登录成功");
                    break;
                case MSG_LOGINFAILED:
                    mAtivity.showToast("登录失败");
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public void showToast(String text) {
        mToast.setText(text);
        mToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        myHandler = new MyHandler(this);
        mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
        et_userName = (TextView) findViewById(R.id.et_userName);
        et_password = (TextView) findViewById(R.id.et_password);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //登录按钮按下回调事件
    public void onLoginBtnClick(View view) {
        if (null == et_userName || null == et_password) {
            mToast.setText("用户名或密码不能为空");
            mToast.show();
        }
        final String userName = et_userName.getText().toString();
        final String password = et_password.getText().toString();

        JSONObject user_login = new JSONObject();
        JSONObject js = new JSONObject();
        try {
            user_login.put("user_phone", userName);
            user_login.put("user_password", password);
            js.put("user_login", user_login);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String json = js.toString();
        Log.i("Login", "json串为:" + json);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean result = false;

                URL url;
                HttpURLConnection conn;
                OutputStream os = null;
                InputStream is = null;
                try {
                    Log.i("Login", "开始连接服务器");
                    url = new URL(Constants.SERVER_ADDRESS_LOGIN);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(8000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setUseCaches(false);
                    conn.connect();
                    os = conn.getOutputStream();
                    Log.i("Login", "服务器连接成功");
                    if (os != null) {
                        os.write(json.getBytes("utf-8"));
                        os.flush();
                    }
                    is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String json = reader.readLine();
                    try {
                        Log.i("Login", "开始解析Json:" + json);
                        JSONObject jsonObject = new JSONObject(json);
                        result = (boolean) jsonObject.get("result");
                        Log.i("Login", "Json解析成功,值为" + result);
                    } catch (JSONException e) {
                        Log.i("Login", "服务器连接失败");
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if(is != null){
                            is.close();
                        }
                        if(os != null){
                            os.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return result;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    myHandler.sendEmptyMessage(myHandler.MSG_LOGINOK);
                } else {
                    myHandler.sendEmptyMessage(myHandler.MSG_LOGINFAILED);
                }
            }
        }.execute();
    }
}
