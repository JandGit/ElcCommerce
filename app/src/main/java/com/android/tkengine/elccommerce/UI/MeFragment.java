package com.android.tkengine.elccommerce.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.UserInfoBean;
import com.android.tkengine.elccommerce.utils.BadgeView;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;


public class MeFragment extends Fragment {

    View mView;
    TextView tv_userName;
    ImageView iv_userIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_me, container, false);

        tv_userName = (TextView) mView.findViewById(R.id.tv_frgMe_userName);
        iv_userIcon = (ImageView) mView.findViewById(R.id.iv_frgMeUserIcon);

        mView.findViewById(R.id.rv_frgme_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //若用户未登录，则进入登录页面
                    startActivityForResult(new Intent(getContext(), UserLoginActivity.class), 1);
            }
        });

        //若用户已登录,展示信息
        if (isUserLogined()) {
            showUserInfo();
        }

        return mView;
    }

    //检查用户是否已经登录
    private boolean isUserLogined() {
        SharedPreferences sp = getActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        return sp.getBoolean("isLogin", false);
    }

    //设置上方的用户头像及用户名
    private void showUserInfo() {
        SharedPreferences sp = getActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        tv_userName.setText(sp.getString("UserName", "null"));
        Log.i("presenter", "设置头像" + (Constants.SERVER_ADDRESS  + sp.getString("UserIcon", null)));
        Picasso.with(getContext()).load(Constants.SERVER_ADDRESS  + sp.getString("UserIcon", null)).fit().into(iv_userIcon);
    }

    //用户登录后返回，若用户登录成功，则把登录信息记录到本地
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode && 1 == requestCode) {
            UserInfoBean info = (UserInfoBean) data.getSerializableExtra("info");
            //更改当前已登录用户信息
            SharedPreferences sp = getActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isLogin", true)
                    .putString("UserPhone", info.getUser_phone())
                    .putString("UserName", info.getUser_name())
                    .putString("UserId", info.getUserId())
                    .putString("UserIcon", info.getUser_picture_url())
                    .putString("UserSex", info.getUser_sex())
                    .putFloat("UserMoney", (float) info.getUser_money())
                    .apply();
            //记住用户
            sp = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            editor = sp.edit();
            editor.putString(info.getUserId() + ":UserPassword", data.getStringExtra("password"))
                    .putString(info.getUserId() + ":UserPhone", info.getUser_phone())
                    .putString(info.getUserId() + ":UserName", info.getUser_name())
                    .putString(info.getUserId() + ":UserIcon", info.getUser_picture_url())
                    .apply();

            //显示用户信息
            showUserInfo();
        }
    }
}
