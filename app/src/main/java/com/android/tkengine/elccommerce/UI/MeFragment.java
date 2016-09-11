package com.android.tkengine.elccommerce.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.tkengine.elccommerce.model.MeFrgModel;
import com.squareup.picasso.Picasso;

public class MeFragment extends Fragment {

    MeFrgModel mModel;

    View mView;
    TextView tv_userName;
    ImageView iv_userIcon;
    Toast mToast;

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
                if (!isUserLogined()) {
                    showToast("请先登录");
                }

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
                if (isUserLogined()) {
                    startActivity(new Intent(getActivity(), AddressActivity.class));
                }
                else{
                    showToast("请先登录");
                }
            }
        });

        //我的钱包
        mView.findViewById(R.id.rl_myWallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLogined()) {
                    startActivity(new Intent(getActivity(), WalletActivity.class));
                }
                else{
                    showToast("请先登录");
                }
            }
        });

        mView.findViewById(R.id.rl_myComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserLogined()) {
                    startActivity(new Intent(getActivity(), MyCommentsActivity.class));
                }
                else{
                    showToast("请先登录");
                }
            }
        });

        mView.findViewById(R.id.rl_myInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserLogined()) {
                    startActivity(new Intent(getActivity(), PersonalinfoActivity.class));
                }
                else{
                    showToast("请先登录");
                }
            }
        });

        mModel = new MeFrgModel(getContext());

        return mView;
    }

    //检查用户是否已经登录
    private boolean isUserLogined() {
        SharedPreferences sp = getActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        return sp.getBoolean("isLogin", false);
    }

    @Override
    public void onResume() {
        final SharedPreferences sp = getActivity().getSharedPreferences(Constants.SP_LOGIN_USERINFO, Context.MODE_PRIVATE);
        if (isUserLogined()) {
            //更新用户信息
            mModel.loadUserInfo(sp.getString("UserId", null), new MeFrgModel.ResponseListener(){
                @Override
                public void onResponse() {
                    tv_userName.setText(sp.getString("UserName", "null"));
                    Picasso.with(getContext()).load(sp.getString("UserIcon", null)).fit()
                            .placeholder(R.drawable.frgme_userunlogin)
                            .into(iv_userIcon);
                    TextView tv_money = (TextView) mView.findViewById(R.id.tv_money);
                    tv_money.setText("余额" + sp.getFloat("UserMoney", 0));
                }
            });
        }
        else {
            tv_userName.setText("点击登录");
            iv_userIcon.setImageResource(R.drawable.frgme_userunlogin);
        }
        super.onResume();
    }

    private void showToast(String str){
        if(null == mToast){
            mToast = Toast.makeText(getContext(), str, Toast.LENGTH_SHORT);
        }else{
            mToast.setText(str);
        }
        mToast.show();
    }
}
