package com.android.tkengine.elccommerce;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    boolean isFirstOpen = true;

    FragmentManager mFM;

    TextView tv_tagHome, tv_tagShop, tv_tagCart, tv_tagMy;
    ImageView iv_tagHome, iv_tagShop, iv_tagCart, iv_tagMy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        mFM = getSupportFragmentManager();
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
        findViewById(R.id.tag_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagClick("Home");
                if(null == mFM.findFragmentByTag("Home")){
                    mFM.beginTransaction().replace(R.id.frm_whatPage, new HomeFragment(), "Home")
                            .commit();
                }
            }
        });
        findViewById(R.id.tag_shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagClick("Shop");
            }
        });
        findViewById(R.id.tag_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagClick("Cart");
            }
        });
        findViewById(R.id.tag_my).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagClick("My");
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
}
