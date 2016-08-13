package com.android.tkengine.elccommerce.UI;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.presenter.OrderActPresenter;
import com.android.tkengine.elccommerce.utils.MultiItemAdapter;

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
        mTab.setupWithViewPager(mViewPager);

        //设置网络错误时点击重新加载
        tv_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadPage(mViewPager.getCurrentItem());
            }
        });

        mViewPager.setAdapter(new PagerAdapter() {
            RecyclerView[] allItems = new RecyclerView[5];
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public int getItemPosition(Object object) {
                for(int i = 0; i < 5; i++){
                    if(object == allItems[i]){
                        return i;
                    }
                }
                return 0;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
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

                container.addView(view);
                allItems[position] = view;
                return view;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentRv = (RecyclerView) mViewPager.getChildAt(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void showNowLoading() {
        tv_tips.setText("正在努力加载...");
        tv_tips.setVisibility(View.VISIBLE);
        tv_tips.setClickable(false);
    }

    @Override
    public void showLoadingFailed() {
        tv_tips.setText("网络连接错误，点击重试");
        tv_tips.setVisibility(View.VISIBLE);
        tv_tips.setClickable(true);
    }

    @Override
    public void setAdapter(MultiItemAdapter adapter) {

    }
}
