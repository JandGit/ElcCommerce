package com.android.tkengine.elccommerce.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.presenter.HomeFrgPresenter;

public class HomeFragment extends Fragment implements HomeFrgPresenter.CallbackOfHomefrg{

    //本页面的View对象
    View mView;
    //主页面RecyclerView
    RecyclerView mRv;
    //标题栏toolbar
    Toolbar mToolbar;
    //下拉刷新
    SwipeRefreshLayout mSwipeRefreshLayout;
    //Presenter
    HomeFrgPresenter mPresenter;
    //
    Toast mToast;
    //提示首页加载状态的页面
    TextView tv_tips;
    //搜索框
    TextView toolbarSearch, toolbarCity;
    LinearLayout toolbarLocation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();

        return mView;
    }

    private void initView(){
        mToolbar = (Toolbar) mView.findViewById(R.id.tb_homeToolbar);
        toolbarSearch =(TextView)mView.findViewById(R.id.et_toolbarSearch);
        toolbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mView.getContext(),SearchActivity.class);
                intent.putExtra("type","");
                startActivity(intent);
            }
        });
        toolbarCity = (TextView) mView.findViewById(R.id.tv_toolbarUserLocation);
        toolbarLocation = (LinearLayout) mView.findViewById(R.id.tv_toolbarLocation);
        toolbarLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mView.getContext(),ProvinceInfoActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
            }
        });

        mRv = (RecyclerView) mView.findViewById(R.id.rv_goodsList);
        mPresenter = new HomeFrgPresenter(this, getContext());
        //设置下拉刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipRefresh_main);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.controlRecyclerView(mRv);
            }
        });
        //设置网络错误重新加载按钮
        tv_tips = (TextView) mView.findViewById(R.id.tv_frghome_tips);
        tv_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mSwipeRefreshLayout.isRefreshing()){
                    mPresenter.controlRecyclerView(mRv);
                }
            }
        });
        mPresenter.controlRecyclerView(mRv);
    }

    public void showToast(String str) {
        if(null == mToast){
            mToast = Toast.makeText(getContext(), str, Toast.LENGTH_SHORT);
        }
        mToast.setText(str);
        mToast.show();
    }

    @Override
    public void stopLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadingHomeFailed() {
        mSwipeRefreshLayout.setRefreshing(false);
        tv_tips.setText("首页加载失败，点击重试");
    }

    @Override
    public void showLoadingHome() {
        tv_tips.setText("正在加载首页...");
        tv_tips.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideHomeLoading() {
        tv_tips.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume(){     //获取用户新添加的地址（省/市/区）
        super.onResume();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("position",getContext().MODE_PRIVATE);
        String address = sharedPreferences.getString("position","广州");
        toolbarCity.setText(address);
    }
}
