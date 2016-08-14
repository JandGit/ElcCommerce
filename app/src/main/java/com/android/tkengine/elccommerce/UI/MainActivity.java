package com.android.tkengine.elccommerce.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.UserInfoBean;
import com.android.tkengine.elccommerce.model.ElcModel;


public class MainActivity extends AppCompatActivity {
    boolean isFirstOpen = true;

    FragmentManager mFM;
    Fragment frgHome, frgShop, frgCart, frgMe;

    TextView tv_tagHome, tv_tagShop, tv_tagCart, tv_tagMy;
    ImageView iv_tagHome, iv_tagShop, iv_tagCart, iv_tagMy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        //启动时载入用户信息，自动登录
        final SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        final String phone = sp.getString("UserPhone", null);
        final String password = sp.getString("password", null);
        if(phone != null && !phone.isEmpty() && password != null && !password.isEmpty()){
            new Thread(){
                @Override
                public void run() {
                    try {
                        UserInfoBean info = new ElcModel(MainActivity.this).login(phone, password);
                        if(info != null){
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isLogin", true)
                                    .putString("UserPhone", info.getUser_phone())
                                    .putString("password", password)
                                    .putString("UserName", info.getUser_name())
                                    .putString("UserId", info.getUserId())
                                    .putString("UserIcon", info.getUser_picture_url())
                                    .putString("UserSex", info.getUser_sex())
                                    .putFloat("UserMoney", (float) info.getUser_money())
                                    .apply();
                            Log.i("main", "自动登录");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            //
            if(savedInstanceState != null){
                isFirstOpen = savedInstanceState.getBoolean("flag");
                switch (savedInstanceState.getInt("selected")){
                    case 0:
                        findViewById(R.id.tag_home).performClick();
                        break;
                    case 1:
                        findViewById(R.id.tag_shop).performClick();
                        break;
                    case 2:
                        findViewById(R.id.tag_cart).performClick();
                        break;
                    case 3:
                        findViewById(R.id.tag_my).performClick();
                        break;
                }
            }
        }
    }

    private void initView(){
        tv_tagHome = (TextView) findViewById(R.id.tv_tagHome);
        tv_tagShop = (TextView) findViewById(R.id.tv_tagShop);
        tv_tagCart = (TextView) findViewById(R.id.tv_tagCart);
        tv_tagMy = (TextView) findViewById(R.id.tv_tagMy);
        iv_tagHome = (ImageView) findViewById(R.id.iv_tagHome);
        iv_tagShop = (ImageView) findViewById(R.id.iv_tagShop);
        iv_tagCart = (ImageView) findViewById(R.id.iv_tagCart);
        iv_tagMy = (ImageView) findViewById(R.id.iv_tagMy);

        mFM = getSupportFragmentManager();

        if (null == (frgHome = mFM.findFragmentByTag("Home"))) {
            frgHome = new HomeFragment();
        }
        if (null == (frgShop = mFM.findFragmentByTag("Shop"))) {
            frgShop = new ShopFragment();
        }
        if (null == (frgCart = mFM.findFragmentByTag("Cart"))) {
            frgCart = new CartFragment();
        }
        if (null == (frgMe = mFM.findFragmentByTag("My"))) {
            frgMe = new MeFragment();
        }
        findViewById(R.id.tag_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagClick("Home");
                if(null == mFM.findFragmentByTag("Home")){
                    mFM.beginTransaction().add(R.id.frm_whatPage, frgHome, "Home")
                            .commit();
                }
                mFM.beginTransaction().hide(frgShop).hide(frgCart).hide(frgMe).show(frgHome).commit();
            }
        });
        findViewById(R.id.tag_shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagClick("Shop");
                if(null == mFM.findFragmentByTag("Shop")){
                    mFM.beginTransaction().add(R.id.frm_whatPage, frgShop, "Shop")
                            .commit();
                }
                mFM.beginTransaction().hide(frgHome).hide(frgCart).hide(frgMe).show(frgShop).commit();
            }
        });
        findViewById(R.id.tag_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagClick("Cart");
                if(null == mFM.findFragmentByTag("Cart")){
                    mFM.beginTransaction().add(R.id.frm_whatPage, frgCart, "Cart")
                            .commit();
                }
                mFM.beginTransaction().hide(frgHome).hide(frgShop).hide(frgMe).show(frgCart).commit();
            }
        });
        findViewById(R.id.tag_my).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagClick("My");
                if(null == mFM.findFragmentByTag("My")){
                    mFM.beginTransaction().add(R.id.frm_whatPage, frgMe, "My")
                            .commit();
                }
                mFM.beginTransaction().hide(frgHome).hide(frgShop).hide(frgCart).show(frgMe).commit();
            }
        });
    }

    public void tagClick(String tag){
        switch (tag){
            case "Home":
                tv_tagHome.setTextColor(Color.rgb(0xEA,0x4f, 0x38));
                iv_tagHome.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.home_fill));
                tv_tagShop.setTextColor(Color.BLACK);
                iv_tagShop.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shop));
                tv_tagCart.setTextColor(Color.BLACK);
                iv_tagCart.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cart));
                tv_tagMy.setTextColor(Color.BLACK);
                iv_tagMy.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.my));
                break;
            case "Shop":
                tv_tagHome.setTextColor(Color.BLACK);
                iv_tagHome.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.home));
                tv_tagShop.setTextColor(Color.rgb(0xEA,0x4f, 0x38));
                iv_tagShop.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shop_fill));
                tv_tagCart.setTextColor(Color.BLACK);
                iv_tagCart.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cart));
                tv_tagMy.setTextColor(Color.BLACK);
                iv_tagMy.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.my));
                break;
            case "Cart":
                tv_tagHome.setTextColor(Color.BLACK);
                iv_tagHome.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.home));
                tv_tagShop.setTextColor(Color.BLACK);
                iv_tagShop.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shop));
                tv_tagCart.setTextColor(Color.rgb(0xEA,0x4f, 0x38));
                iv_tagCart.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cart_fill));
                tv_tagMy.setTextColor(Color.BLACK);
                iv_tagMy.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.my));
                break;
            case "My":
                tv_tagHome.setTextColor(Color.BLACK);
                iv_tagHome.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.home));
                tv_tagShop.setTextColor(Color.BLACK);
                iv_tagShop.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shop));
                tv_tagCart.setTextColor(Color.BLACK);
                iv_tagCart.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cart));
                tv_tagMy.setTextColor(Color.rgb(0xEA,0x4f, 0x38));
                iv_tagMy.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.my_fill));
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (isFirstOpen && hasFocus) {
            findViewById(R.id.tag_home).performClick();
            isFirstOpen = false;
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("flag", isFirstOpen);
        int i = 5;
        if(!frgHome.isHidden()){
           i = 0;
        }
        else if(!frgShop.isHidden()){
            i = 1;
        }
        else if(!frgCart.isHidden()){
            i = 2;
        }
        else if(!frgMe.isHidden()){
            i = 3;
        }
        outState.putInt("selected", i);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        //退出程序时，清空用户登录信息
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("isLogin", false).apply();

        super.onDestroy();
    }
}
