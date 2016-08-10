package com.android.tkengine.elccommerce.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.utils.BadgeView;


public class MeFragment extends Fragment{

    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_me, container, false);
        mView.findViewById(R.id.rv_frgme_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogined = false;
                //若用户未登录，则进入登录页面
                if(!isLogined){
                    startActivityForResult(new Intent(getContext(), UserLoginActivity.class), 1);
                }
            }
        });

        return mView;
    }
}
