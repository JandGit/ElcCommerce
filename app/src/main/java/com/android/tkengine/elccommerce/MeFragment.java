package com.android.tkengine.elccommerce;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.android.tkengine.elccommerce.R;


public class MeFragment extends Fragment{

    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_me, container, false);

        //沉浸式
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            final Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/

        return mView;
    }
}
