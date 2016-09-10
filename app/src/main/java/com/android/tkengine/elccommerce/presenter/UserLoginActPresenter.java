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
import com.android.tkengine.elccommerce.model.UserLoginActModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class UserLoginActPresenter {

    CallbackOfView mView;
    UserLoginActModel mModel;
    Context mContext;

    //由UserLoginAcitivity实现的接口
    public interface CallbackOfView {
        //在Acitivity展示消息
        void showToast(String text);
        //在页面显示用户头像
        void setUserIcon(Bitmap icon);
        //显示正在登录
        void showLogining();
        //登录成功
        void onLoginSuccess();
        //登录失败
        void onLoginFailed();
    }

    public UserLoginActPresenter(CallbackOfView mView, Context context) {
        this.mView = mView;
        this.mContext = context;
        this.mModel = new UserLoginActModel(mContext);
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

        mView.showLogining();
        mModel.login(userName, password, new UserLoginActModel.ResponseListener() {
            @Override
            public void onLoginResponse(boolean result) {
                if(result){
                    mView.onLoginSuccess();
                }
                else{
                    mView.showToast("登录失败，帐号或密码错误");
                    mView.onLoginFailed();
                }
            }
            @Override
            public void onError() {
                mView.showToast("网络错误，请稍后尝试");
                mView.onLoginFailed();
            }
        });
    }

    public void loadUserIcon(String userName) {
        if (userName != null && userName.matches("^[1][358][0-9]{9}$")) {

        }
    }
}
