package com.android.tkengine.elccommerce.UI;

import android.support.design.widget.TabLayout;
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
import com.android.tkengine.elccommerce.presenter.OrderActPresenter;

public class OrderActivity extends AppCompatActivity implements OrderActPresenter.CallbackOfView {

    //订单切换VP
    ViewPager mViewPager;
    TabLayout mTab;
    //显示当前网络访问状态的tv
    TextView tv_tips;
    //presenter
    OrderActPresenter mPresenter;
    //ViewPager所有页面
    RecyclerView[] mAllPages;
    //RootView
    View mRootView;

    boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        mPresenter = new OrderActPresenter(this, this);
        initView();
    }

    private void initView() {
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        mTab = (TabLayout) findViewById(R.id.tl_tags);
        mRootView = findViewById(R.id.order_rootView);

        //设置返回箭头
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //初始化ViewPager页面
        mAllPages = new RecyclerView[5];
        for (int i = 0; i < mAllPages.length; i++) {
            mAllPages[i] = new RecyclerView(this);
            mAllPages[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mAllPages[i].setLayoutManager(new LinearLayoutManager(this));
        }
        //设置ViewPager
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mAllPages.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
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
                    default:
                        return null;
                }
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mAllPages[position]);
                return mAllPages[position];
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                RecyclerView view = mAllPages[position];
                if (null == view.getAdapter()) {
                    mPresenter.setPage(view, position);
                } else {
                    onLoadingSuccess();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        int i = getIntent().getIntExtra("flag", 0);
        if (0 > i || i > 4) {
            i = 0;
        }
        mViewPager.setCurrentItem(i);
        mTab.setupWithViewPager(mViewPager);
        //设置第一页
        mPresenter.setPage(mAllPages[0], 0);

        //设置网络错误时点击重新加载
        tv_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updata();
            }
        });
    }

    @Override
    public void showNowLoading() {
        tv_tips.setText("正在努力加载...");
        tv_tips.setVisibility(View.VISIBLE);
        tv_tips.setClickable(false);
        isLoading = true;
    }

    @Override
    public void showLoadingFailed() {
        tv_tips.setText("网络连接错误，点击重试");
        tv_tips.setVisibility(View.VISIBLE);
        tv_tips.setClickable(true);
        isLoading = false;
    }

    @Override
    public void showNodata() {
        tv_tips.setText("空空如也...");
        tv_tips.setVisibility(View.VISIBLE);
        tv_tips.setClickable(false);
        isLoading = false;
    }

    @Override
    public void onLoadingSuccess() {
        tv_tips.setVisibility(View.INVISIBLE);
        isLoading = false;
    }

    @Override
    public void updata() {
        int position = mViewPager.getCurrentItem();
        RecyclerView rview = mAllPages[position];
        mPresenter.setPage(rview, position);
    }
}
