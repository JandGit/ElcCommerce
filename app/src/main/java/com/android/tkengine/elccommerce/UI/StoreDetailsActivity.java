package com.android.tkengine.elccommerce.UI;import android.content.Intent;import android.os.Build;import android.os.Bundle;import android.support.annotation.Nullable;import android.support.v7.app.AppCompatActivity;import android.support.v7.widget.LinearLayoutManager;import android.support.v7.widget.RecyclerView;import android.view.WindowManager;import android.widget.ImageView;import com.android.tkengine.elccommerce.R;import com.android.tkengine.elccommerce.presenter.StoreDetailsPresenter;/** * Created by FangYu on 2016/8/14. */public class StoreDetailsActivity extends AppCompatActivity {    RecyclerView recyclerView;    StoreDetailsPresenter presenter;    StoreDetailsPresenter.StoreDetailsAdapter adapter;    ImageView titleBack,titleMessage;    String id = "";    @Override    protected void onCreate(@Nullable Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_store);        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);        }        Intent intent = this.getIntent();        id = intent.getStringExtra("storeID");        initView();        initPresenter();    }    private void initPresenter() {        presenter = new StoreDetailsPresenter();        presenter.clickBack(StoreDetailsActivity.this, titleBack);        presenter.clickBack(StoreDetailsActivity.this, titleMessage);        adapter = presenter.new StoreDetailsAdapter( id, StoreDetailsActivity.this);        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);        recyclerView.setAdapter(adapter);        recyclerView.setLayoutManager(linearLayoutManager);    }    private void initView() {        titleBack = (ImageView) findViewById(R.id.title_back);        titleMessage = (ImageView) findViewById(R.id.title_message);        recyclerView = (RecyclerView) findViewById(R.id.store_detail_rv);    }}