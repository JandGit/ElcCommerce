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

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.utils.MultiItemAdapter;

public class OrderActivity extends AppCompatActivity {

    ViewPager mViewPager;
    TabLayout mTab;
    Fragment[] allFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView();
    }

    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        mTab = (TabLayout) findViewById(R.id.tl_tags);
        mTab.setupWithViewPager(mViewPager);

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
                RecyclerView rv = (RecyclerView) mViewPager.getChildAt(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
