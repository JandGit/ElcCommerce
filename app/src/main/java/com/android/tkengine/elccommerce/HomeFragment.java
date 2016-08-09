package com.android.tkengine.elccommerce;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.beans.RvItemBean;
import com.android.tkengine.elccommerce.presenter.HomeFrgPresenter;
import com.android.tkengine.elccommerce.utils.ImageTools;

import java.util.List;

public class HomeFragment extends Fragment implements HomeFrgPresenter.CallbackOfHomefrg{

    //本页面的View对象
    View mView;
    //主页面RecyclerView
    RecyclerView rv_mainView;
    //RecyclerViewAdapter
    HomeFrgPresenter.HomepageAdapter mRvAdapter;
    //标题栏toolbar
    Toolbar mToolbar;
    //下拉登录框头像
    ImageView iv_loginUserIcon;
    //下拉刷新
    SwipeRefreshLayout mSwipeRefreshLayout;
    //
    AppBarLayout mAppBarLayout;
    //Presenter
    HomeFrgPresenter mPresenter;
    //
    Toast mToast;
    //提示首页加载状态的页面
    TextView tv_tips;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        initView();

        return mView;
    }

    private void initView(){
        mToolbar = (Toolbar) mView.findViewById(R.id.tb_homeToolbar);

        //设置首页商品列表
        rv_mainView = (RecyclerView) mView.findViewById(R.id.rv_goodsList);
        rv_mainView.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_mainView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = 0;
            LinearLayoutManager llManager = (LinearLayoutManager) rv_mainView.getLayoutManager();
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                lastVisibleItem = llManager.findLastVisibleItemPosition();
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(RecyclerView.SCROLL_STATE_IDLE == newState && mRvAdapter.getItemCount() - 1 == lastVisibleItem
                        && !mSwipeRefreshLayout.isRefreshing()){
                    mPresenter.loadMoreOnHomePage(lastVisibleItem + 1);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        tv_tips = (TextView) mView.findViewById(R.id.tv_frghome_tips);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipRefresh_main);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.initHomePage();
            }
        });

        mToast = Toast.makeText(getContext(), null, Toast.LENGTH_SHORT);

        mPresenter = new HomeFrgPresenter(this, getContext());
        mPresenter.initHomePage();
    }

    @Override
    public void setRvAdapter(RecyclerView.Adapter adapter) {
        mRvAdapter = (HomeFrgPresenter.HomepageAdapter) adapter;
        rv_mainView.setAdapter(adapter);
    }

    @Override
    public void addViewInRv(List<RvItemBean> data) {
        mRvAdapter.addItems(data);
    }

    @Override
    public void showLoadingHomePage() {
        mSwipeRefreshLayout.setRefreshing(true);
        tv_tips.setText("正在努力加载......");
    }

    @Override
    public void showLoadingfailed() {
        mSwipeRefreshLayout.setRefreshing(false);
        tv_tips.setText("首页加载失败，点击重试");
    }

    @Override
    public void showLoadingMore() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showLoadingHomeCompleted() {
        mSwipeRefreshLayout.setRefreshing(false);
        tv_tips.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingMoreCompleted() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}