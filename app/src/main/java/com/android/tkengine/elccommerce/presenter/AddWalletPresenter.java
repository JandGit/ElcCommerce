package com.android.tkengine.elccommerce.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.model.ElcModel;

/**
 * Created by 陈嘉shuo on 2016/9/10.
 */
public class AddWalletPresenter {

    public Context context;

    public AddWalletPresenter(Context context){
        this.context = context;
    }

    public void cahrgeMoney(final String money){
            SharedPreferences sp = context.getSharedPreferences(Constants.SP_LOGIN_USERINFO, Context.MODE_PRIVATE);
            final String userId = sp.getString("UserId", null);
            final String password = sp.getString("password",null);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new ElcModel(context).addMoneyAmount(userId,password,money);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }

}
