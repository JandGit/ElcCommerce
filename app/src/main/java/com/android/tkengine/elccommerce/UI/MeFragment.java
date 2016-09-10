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
import android.widget.Toast;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.UserInfoBean;
import com.android.tkengine.elccommerce.model.ElcModel;
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

        //上方用户头像点击事件
        mView.findViewById(R.id.rv_frgme_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //若用户未登录，则进入登录页面
                if (!isUserLogined()) {
                    startActivityForResult(new Intent(getContext(), UserLoginActivity.class), 1);
                } else {
                    startActivity(new Intent(getContext(), PersonalinfoActivity.class));
                }
            }
        });
        //我的订单点击事件
        View.OnClickListener listener = new View.OnClickListener() {
            int i;
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.showMyOrders:
                        i = 0;
                        break;
                    case R.id.unpaidOrder:
                        i = 1;
                        break;
                    case R.id.unsentOrder:
                        i = 2;
                        break;
                    case R.id.unrecievedOrder:
                        i = 3;
                        break;
                    default:
                        i = 0;
                }
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra("flag", i);
                startActivity(intent);
            }
        };
        mView.findViewById(R.id.showMyOrders).setOnClickListener(listener);
        mView.findViewById(R.id.unpaidOrder).setOnClickListener(listener);
        mView.findViewById(R.id.unsentOrder).setOnClickListener(listener);
        mView.findViewById(R.id.unrecievedOrder).setOnClickListener(listener);

        //收货地址管理点击事件
        mView.findViewById(R.id.rl_myAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddressActivity.class));
            }
        });

        //我的钱包
        mView.findViewById(R.id.rl_myWallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WalletActivity.class));
            }
        });

        mView.findViewById(R.id.rl_myComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyCommentsActivity.class));
            }
        });

        mView.findViewById(R.id.rl_myInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PersonalinfoActivity.class));
            }
        });

        return mView;
    }

    //检查用户是否已经登录
    private boolean isUserLogined() {
        SharedPreferences sp = getActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        return sp.getBoolean("isLogin", false);
    }

    @Override
    public void onResume() {
        //若用户已登录,展示信息
        if (isUserLogined()) {
            SharedPreferences sp = getActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
            tv_userName.setText(sp.getString("UserName", "null"));
            Picasso.with(getContext()).load(sp.getString("UserIcon", null)).fit()
                    .error(R.drawable.frgme_userunlogin)
                    .into(iv_userIcon);
            TextView tv_money = (TextView) mView.findViewById(R.id.tv_money);
            tv_money.setText("余额" + sp.getFloat("UserMoney", 0));
        }
        else {
            tv_userName.setText("点击登录");
            iv_userIcon.setImageResource(R.drawable.frgme_userunlogin);
        }
        super.onResume();
    }
}
