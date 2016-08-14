package com.android.tkengine.elccommerce.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.telecom.Call;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.UserInfoBean;
import com.android.tkengine.elccommerce.model.ElcModel;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class PersonalActPresenter {

    public interface CallbackOfView{
        void startCropActivity(Intent intent, int request);
        //更新用户个人信息
        void updateUserInfo();

        void showToast(String text);
    }
    private static class MyHandler extends Handler{
        final int MSG_UPDATE_INFO = 0;
        final int MSG_SHOWERROR = 1;
        final int MSG_CHANGEPASSWORD_SUCCESS = 2;

        CallbackOfView mView;

        public MyHandler(CallbackOfView mView) {
            this.mView = mView;
        }

        @Override

        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_UPDATE_INFO:{//用户信息更改，界面内容需要更新
                    mView.updateUserInfo();
                }
                case MSG_SHOWERROR:{
                    mView.showToast("设置失败，请检查网络或重新登录后尝试");
                }
                case MSG_CHANGEPASSWORD_SUCCESS:{
                    mView.showToast("修改成功");
                }
            }
            super.handleMessage(msg);
        }
    }

    private CallbackOfView mView;
    private MyHandler mHandler;
    private ElcModel mModel;
    private Context mContext;

    public PersonalActPresenter(CallbackOfView mView, Context context) {
        this.mView = mView;
        this.mHandler = new MyHandler(mView);
        this.mContext = context;
        this.mModel = new ElcModel(mContext);
    }

    /**
     * 修改用户头像
     */
    public void changeIcon(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(mContext)
                .setItems(new String[]{"拍照", "从照片中获取"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File file = new File(Environment.getExternalStorageDirectory() + "/ElcCommerce/UserIconTemp.jpg");
                        if(!file.getParentFile().exists()){
                            file.getParentFile().mkdirs();
                        }
                        if (file.getParentFile().exists()) {
                            if(0 == i){
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                mView.startCropActivity(intent, 1);
                            }
                            else {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                intent.putExtra("crop",true);
                                intent.putExtra("scale",true);
                                mView.startCropActivity(intent, 2);
                            }
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * 不应当直接调用该接口
     */
    public void sendIconToServer(final File file){
        new Thread(){
            @Override
            public void run() {
                SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_LOGIN_USERINFO,
                        Context.MODE_PRIVATE);
                try {
                    if (mModel.setUserInfo(sp.getString("UserId", "2"), file, sp.getString("UserSex", "man"),
                            sp.getString("UserName", "noName"))) {

                        UserInfoBean info = mModel.getUserInfo(sp.getString("UserId", null));
                        if (info != null) {
                            sp.edit().putString("UserIcon", info.getUser_picture_url()).apply();
                            mHandler.sendEmptyMessage(mHandler.MSG_UPDATE_INFO);
                        }
                    }
                    else{
                        //参数错误
                        mHandler.sendEmptyMessage(mHandler.MSG_SHOWERROR);
                    }
                } catch (JSONException e) {
                    Log.i("Activity:", "Json数据写入错误");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("Activity:", "图片文件读取错误");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 修改用户姓名
     */
    public void changeUserName(){
        EditText et = new EditText(mContext);
        et.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        et.setEms(8);
        et.setMaxEms(15);
        et.setMaxLines(1);
        AlertDialog.Builder builder =  new AlertDialog.Builder(mContext)
                .setTitle("请输入要修改的用户名")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(){
                            @Override
                            public void run() {
                                SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_LOGIN_USERINFO,
                                        Context.MODE_PRIVATE);
                                try {
                                    if (mModel.setUserInfo(sp.getString("UserId", "2"), null, sp.getString("UserSex", "man"),
                                            sp.getString("UserName", "noName"))) {

                                        UserInfoBean info = mModel.getUserInfo(sp.getString("UserId", null));
                                        if (info != null) {
                                            sp.edit().putString("UserIcon", info.getUser_picture_url()).apply();
                                            mHandler.sendEmptyMessage(mHandler.MSG_UPDATE_INFO);
                                        }
                                    }
                                    else{
                                        //参数错误
                                        mHandler.sendEmptyMessage(mHandler.MSG_SHOWERROR);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 修改密码
     */
    public void changePassword(){
        final EditText et = new EditText(mContext);
        et.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        et.setEms(8);
        et.setMaxEms(15);
        et.setMaxLines(1);
        AlertDialog.Builder builder =  new AlertDialog.Builder(mContext)
                .setTitle("请输入要修改的密码")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final SharedPreferences sp  = mContext.getSharedPreferences(Constants.SP_LOGIN_USERINFO, Context.MODE_PRIVATE);
                        final String s = ((TextView)et).getText().toString();
                        if(!s.isEmpty()){
                            new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        if (mModel.setPassword(sp.getString("UserId", null), sp.getString("password", null), s)) {

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        mHandler.sendEmptyMessage(mHandler.MSG_SHOWERROR);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        mHandler.sendEmptyMessage(mHandler.MSG_SHOWERROR);
                                    }
                                }
                            }.start();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}
