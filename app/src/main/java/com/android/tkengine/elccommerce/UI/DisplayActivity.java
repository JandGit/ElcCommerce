package com.android.tkengine.elccommerce.UI;import android.content.Intent;import android.os.Build;import android.os.Bundle;import android.support.annotation.Nullable;import android.support.v4.view.ViewPager;import android.support.v7.app.AppCompatActivity;import android.support.v7.widget.LinearLayoutManager;import android.support.v7.widget.RecyclerView;import android.view.Gravity;import android.view.View;import android.view.WindowManager;import android.widget.ImageView;import android.widget.TextView;import com.android.tkengine.elccommerce.R;import com.android.tkengine.elccommerce.presenter.DisplayPresenter;import com.android.tkengine.elccommerce.utils.AddCartPopupWindow;import com.android.tkengine.elccommerce.utils.Indicator;/** * Created by FangYu on 2016/8/9. */public class DisplayActivity extends AppCompatActivity {    ViewPager goodsPager;    DisplayPresenter presenter;    RecyclerView goodsContent;    DisplayPresenter.DisplayAdapter mDisplayAdapter;    ImageView titleBack,titleMessage;    TextView addCart, purchaseIm;    AddCartPopupWindow addCartPopupWindow;    String id = "";    private View.OnClickListener itemsOnclick = new View.OnClickListener() {        @Override        public void onClick(View view) {            addCartPopupWindow.dismiss();        }    };    @Override    protected void onCreate(@Nullable Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_display);        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);        //实现沉浸式状态栏        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        }        Intent intent = this.getIntent();        id = intent.getStringExtra("productID");        initView();        initPresenter();    }    private void initPopupWindow(int type) {        addCartPopupWindow = new AddCartPopupWindow(DisplayActivity.this, itemsOnclick, type);        addCartPopupWindow.showAtLocation(DisplayActivity.this.findViewById(R.id.hey), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);    }    private void initPresenter() {        presenter = new DisplayPresenter();        presenter.clickBack(DisplayActivity.this, titleBack);        presenter.clickBack(DisplayActivity.this, titleMessage);        mDisplayAdapter = presenter.new DisplayAdapter( id, DisplayActivity.this);        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);        goodsContent.setAdapter(mDisplayAdapter);        goodsContent.setLayoutManager(linearLayoutManager);    }    private void initView() {        titleBack = (ImageView) findViewById(R.id.title_back);        goodsPager = (ViewPager) findViewById(R.id.goods_pager);        goodsContent = (RecyclerView) findViewById(R.id.goods_content);        titleMessage = (ImageView) findViewById(R.id.title_message);        addCart = (TextView) findViewById(R.id.add_cart);        purchaseIm = (TextView) findViewById(R.id.purchase_imm);        addCart.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                initPopupWindow(1);            }        });        purchaseIm.setOnClickListener(new View.OnClickListener(){            @Override            public void onClick(View view) {                initPopupWindow(2);            }        });    }    @Override    protected void onDestroy() {        Indicator.onFinish();        super.onDestroy();    }}