package com.android.tkengine.elccommerce.UI;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.OrderBean;
import com.android.tkengine.elccommerce.presenter.OrderActPresenter;
import com.android.tkengine.elccommerce.utils.MultiItemAdapter;

import java.util.List;

public class OrderActivity extends AppCompatActivity implements OrderActPresenter.CallbackOfView{

    //订单切换VP
    ViewPager mViewPager;
    TabLayout mTab;
    //当前显示的页面RecyclerView
    RecyclerView currentRv;
    //显示当前网络访问状态的tv
    TextView tv_tips;
    //presenter
    OrderActPresenter mPresenter;
    //
    RecyclerView[] allItems = new RecyclerView[5];
    //标记是否在加载
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView();
        mPresenter = new OrderActPresenter(this, this);
    }

    private void initView(){
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        mTab = (TabLayout) findViewById(R.id.tl_tags);

        //设置返回箭头
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //设置网络错误时点击重新加载
        tv_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadPage(mViewPager.getCurrentItem());
            }
        });

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "全部";
                    case 1:
                        return "待付款";
                    case 2:
                        return "待发货";
                    case 3:
                        return "待收货";
                    case 4:
                        return "待评价";
                }
                return null;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
                Log.i("recycler", "销毁" + position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                if(allItems[position] != null){
                    container.addView(allItems[position]);
                    return allItems[position];
                }
                RecyclerView view = new RecyclerView(OrderActivity.this);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                view.setLayoutManager(new LinearLayoutManager(OrderActivity.this));
                /*currentRv = view;
                if(null == currentRv.getAdapter()){
                    mPresenter.loadPage(position);
                    Log.i("act", "加载网络");
                }*/

                container.addView(view);
                allItems[position] = view;
                Log.i("recycler", "加载" + position);
                return view;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentRv = allItems[position];
                tv_tips.setVisibility(View.GONE);
                Log.i("act", "选择" + position);
                if(currentRv != null && null == currentRv.getAdapter() && !isLoading){
                    mPresenter.loadPage(position);
                    isLoading = true;
                    Log.i("act", "加载网络");
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mTab.setupWithViewPager(mViewPager);

        int i = getIntent().getIntExtra("flag", -1);
        mViewPager.setCurrentItem(i + 1 % 4);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            //设置ViewPager页面
            int i = getIntent().getIntExtra("flag", -1);
            if (0 <= i && i <= 4) {
                mViewPager.setCurrentItem(i);
                Log.i("act", "输入" + i);
            }
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void showNowLoading() {
        isLoading = true;
        tv_tips.setText("正在努力加载...");
        tv_tips.setVisibility(View.VISIBLE);
        tv_tips.setClickable(false);
    }

    @Override
    public void showLoadingFailed() {
        isLoading = false;
        tv_tips.setText("网络连接错误，点击重试");
        tv_tips.setVisibility(View.VISIBLE);
        tv_tips.setClickable(true);
    }

    @Override
    public void setAdapter(MultiItemAdapter adapter) {
        isLoading = false;
        currentRv.setAdapter(adapter);
        tv_tips.setVisibility(View.GONE);
    }

    @Override
    public void showNodata() {
        isLoading = false;
        tv_tips.setText("空空如也...");
        tv_tips.setVisibility(View.VISIBLE);
        tv_tips.setClickable(false);
    }

    @Override
    public void addMoreItem(List<OrderBean> data) {

    }
}
