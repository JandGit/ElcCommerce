package com.android.tkengine.elccommerce.presenter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.UserInfoBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class UserLoginActPresenter {

    CallbackOfView mView;
    CallbackOfModel mModel;
    Context mContext;

    private MyHandler mHandler;

    private static class MyHandler extends Handler {
        final int MSG_LOGINOK = 0;
        final int MSG_LOGINFAILED = 1;
        final int MSG_NETWORKERROR = 2;

        private CallbackOfView mView;

        public MyHandler(CallbackOfView mView) {
            this.mView = mView;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOGINOK:
                    mView.showToast("登录成功");
                    UserInfoBean info = (UserInfoBean) msg.obj;
                    mView.setUserIcon(Constants.SERVER_ADDRESS + info.getUser_picture_url());
                    Log.i("presenter:loginOk:", "USER:" + info.toString());
                    break;
                case MSG_LOGINFAILED:
                    mView.showToast("登录失败");
                    break;
                case MSG_NETWORKERROR:
                    mView.showToast("网络连接错误");
                    break;
            }
            super.handleMessage(msg);
        }
    }

    //由UserLoginAcitivity实现的接口
    public interface CallbackOfView {
        //登录成功后的操作
        void onLoginSuccess();

        //登录失败后的操作
        void onLoginFailed();

        //在Acitivity展示消息
        void showToast(String text);

        //在页面显示用户头像
        void setUserIcon(Bitmap icon);

        void setUserIcon(String url);
    }

    //ElcModel接口,接口操作不开启新线程访问网络,注意调用时在子线程调用
    public interface CallbackOfModel {
        //用户登录，登录成功返回true
        UserInfoBean login(String userName, String password) throws Exception;
    }

    public UserLoginActPresenter(CallbackOfView mView, Context context) {
        this.mView = mView;
        this.mContext = context;
        this.mModel = new ElcModel(mContext);
        this.mHandler = new MyHandler(mView);
    }

    public void login(final String userName, final String password) {
        if (null == userName || null == password || userName.isEmpty() || password.isEmpty()) {
            mView.showToast("用户名或密码不能为空");
            return;
        }
        if (!userName.matches("^[1][358][0-9]{9}$")) {
            mView.showToast("手机号码格式不正确");
            return;
        }
        new Thread() {
            @Override
            public void run() {
                UserInfoBean info = null;
                try {
                    info = mModel.login(userName, password);
                    if (info != null) {
                        Message msg = mHandler.obtainMessage(mHandler.MSG_LOGINOK);
                        msg.obj = info;
                        mHandler.sendMessage(msg);
                    } else {
                        mHandler.sendEmptyMessage(mHandler.MSG_LOGINFAILED);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(mHandler.MSG_NETWORKERROR);
                }
            }
        }.start();
    }

    public void loadUserIcon(String userName) {
        if (userName != null && userName.matches("^[1][358][0-9]{9}$")) {

        }
    }
}
