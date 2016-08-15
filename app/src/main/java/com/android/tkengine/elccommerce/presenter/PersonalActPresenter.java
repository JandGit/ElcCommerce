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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.UserInfoBean;
import com.android.tkengine.elccommerce.model.ElcModel;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class PersonalActPresenter {

    public interface CallbackOfView {
        void startCropActivity(Intent intent, int request);

        //更新用户个人信息
        void updateUserInfo();

        void showToast(String text);
    }

    private static class MyHandler extends Handler {
        final int MSG_UPDATE_INFO = 0;
        final int MSG_SHOWERROR = 1;
        final int MSG_CHANGEPASSWORD_SUCCESS = 2;

        CallbackOfView mView;

        public MyHandler(CallbackOfView mView) {
            this.mView = mView;
        }

        @Override

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_INFO: {//用户信息更改，界面内容需要更新
                    mView.updateUserInfo();
                    break;
                }
                case MSG_SHOWERROR: {
                    String s = (String) msg.obj;
                    mView.showToast(s);
                    break;
                }
                case MSG_CHANGEPASSWORD_SUCCESS: {
                    mView.showToast("修改成功");
                    break;
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
    public void changeIcon() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setItems(new String[]{"拍照", "从照片中获取"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File file = new File(Environment.getExternalStorageDirectory() + "/ElcCommerce/UserIconTemp.jpg");
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        if (file.getParentFile().exists()) {
                            if (0 == i) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                mView.startCropActivity(intent, 1);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                intent.putExtra("crop", true);
                                intent.putExtra("scale", true);
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
    public void sendIconToServer(final File file) {
        new Thread() {
            @Override
            public void run() {
                SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_LOGIN_USERINFO,
                        Context.MODE_PRIVATE);
                try {
                    if (mModel.setUserInfo(sp.getString("UserId", null), file, sp.getString("UserSex", null),
                            sp.getString("UserName", null))) {

                        UserInfoBean info = mModel.getUserInfo(sp.getString("UserId", null));
                        if (info != null) {
                            sp.edit().putString("UserIcon", info.getUser_picture_url()).apply();
                            mHandler.sendEmptyMessage(mHandler.MSG_UPDATE_INFO);
                            Log.i("presenter", "设置成功，更新头像");
                        }
                    } else {
                        //参数错误
                        Message msg = mHandler.obtainMessage(mHandler.MSG_SHOWERROR);
                        msg.obj = "设置失败，网络连接错误";
                        mHandler.sendMessage(msg);
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
    public void changeUserName() {
        final EditText et = new EditText(mContext);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(10, 10, 10, 10);
        et.setLayoutParams(lp);
        et.setEms(8);
        et.setMaxEms(15);
        et.setMaxLines(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("请输入要修改的用户名")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_LOGIN_USERINFO,
                                Context.MODE_PRIVATE);
                        final String userName = ((TextView) et).getText().toString();
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    if (mModel.setUserInfo(sp.getString("UserId", null), null, sp.getString("UserSex", null),
                                            userName)) {

                                        UserInfoBean info = mModel.getUserInfo(sp.getString("UserId", null));
                                        if (info != null) {
                                            sp.edit().putString("UserName", info.getUser_name()).apply();
                                            mHandler.sendEmptyMessage(mHandler.MSG_UPDATE_INFO);
                                        }
                                    } else {
                                        //参数错误
                                        Message msg = mHandler.obtainMessage(mHandler.MSG_SHOWERROR);
                                        msg.obj = "设置失败，网络连接错误";
                                        mHandler.sendMessage(msg);
                                    }
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                    Message msg = mHandler.obtainMessage(mHandler.MSG_SHOWERROR);
                                    msg.obj = "设置失败，网络连接错误";
                                    mHandler.sendMessage(msg);
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
    public void changePassword() {
        //输入旧密码
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_personal_dialog, null);
        final EditText et_oldpassword = (EditText) view.findViewById(R.id.et_oldpassword);
        final EditText et_newpassword = (EditText) view.findViewById(R.id.et_newpassword);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("修改的密码")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_LOGIN_USERINFO, Context.MODE_PRIVATE);
                        final String s_old = ((TextView) et_oldpassword).getText().toString();
                        final String s_new = ((TextView) et_newpassword).getText().toString();
                        if (!s_new.isEmpty() && !s_old.isEmpty()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        if (mModel.setPassword(sp.getString("UserId", null), s_old, s_new)) {
                                            Message msg = mHandler.obtainMessage(mHandler.MSG_SHOWERROR);
                                            msg.obj = "修改成功";
                                            mHandler.sendMessage(msg);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putBoolean("isLogin", false)
                                                    .remove("password")
                                                    .apply();
                                        }
                                        else {
                                            Message msg = mHandler.obtainMessage(mHandler.MSG_SHOWERROR);
                                            msg.obj = "修改失败，旧密码错误";
                                            mHandler.sendMessage(msg);
                                        }
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                        Message msg = mHandler.obtainMessage(mHandler.MSG_SHOWERROR);
                                        msg.obj = "设置失败，网络连接错误";
                                        mHandler.sendMessage(msg);
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
