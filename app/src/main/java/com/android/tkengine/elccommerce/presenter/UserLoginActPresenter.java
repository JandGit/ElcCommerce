package com.android.tkengine.elccommerce.presenter;


import android.content.Context;
import android.graphics.Bitmap;

public class UserLoginActPresenter {

    CallbackOfView mView;
    CallbackOfModel mModel;
    Context mContext;

    //由UserLoginAcitivity实现的接口
    public interface CallbackOfView{
        //登录成功后的操作
        void onLoginSuccess();

        //登录失败后的操作
        void onLoginFailed();

        //在Acitivity展示消息
        void showToast(String text);

        //在页面显示用户头像
        void setUserIcon(Bitmap icon);
    }
    public interface CallbackOfModel{

    }

    public UserLoginActPresenter(CallbackOfView mView, Context context) {
        this.mView = mView;
        this.mContext = context;
    }

    public void login(String userName, String password){
        if(null == userName || null == password || userName.isEmpty() || password.isEmpty()){
            mView.showToast("用户名或密码不能为空");
            return;
        }
        if(!userName.matches("^[1][358][0-9]{9}$")){
            mView.showToast("手机号码格式不正确");
            return;
        }

    }

    public void loadUserIcon(String userName){
        if(userName != null && userName.matches("^[1][358][0-9]{9}$")){

        }
    }
}
