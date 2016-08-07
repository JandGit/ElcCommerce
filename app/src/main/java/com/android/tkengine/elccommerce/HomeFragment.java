package com.android.tkengine.elccommerce;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

    //


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frgment_home, container, false);

        //沉浸式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            final Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

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
                if(RecyclerView.SCROLL_STATE_IDLE == newState && mRvAdapter.getItemCount() - 1 == lastVisibleItem){
                    mPresenter.loadMoreOnHomePage(lastVisibleItem + 1);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        mAppBarLayout = (AppBarLayout) mView.findViewById(R.id.app_bar);

        iv_loginUserIcon = (ImageView) mView.findViewById(R.id.iv_loginUserIcon);
        iv_loginUserIcon.setImageBitmap(ImageTools.toRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.touxiang)));

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipRefresh_main);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                    mToolbar.setVisibility(View.INVISIBLE);
                } else{
                    mSwipeRefreshLayout.setEnabled(false);
                    mToolbar.setVisibility(View.VISIBLE);
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });

        mToast = Toast.makeText(getContext(), null, Toast.LENGTH_SHORT);

        mPresenter = new HomeFrgPresenter(this, getContext());
        mPresenter.initHomePage();
    }

    @Override
    public void showToast(String text){
        mToast.setText(text);
        mToast.show();
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
        mToast.setText("正在加载首页");
        mToast.show();
    }

    @Override
    public void showLoadingfailed() {
        mToast.setText("加载首页失败");
        mToast.show();
    }

    @Override
    public void showLoadingMore() {
        mToast.setText("正在加载更多数据");
        mToast.show();
    }

    @Override
    public void stopShowingLoadmore() {
        mToast.cancel();
    }

    @Override
    public void showNomoreData() {
        mToast.setText("没有更多数据可以加载");
        mToast.show();
    }
}
