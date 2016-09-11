package com.android.tkengine.elccommerce.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.UI.DisplayActivity;
import com.android.tkengine.elccommerce.UI.SearchActivity;
import com.android.tkengine.elccommerce.beans.HomePageItemBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.model.HomeFrgModel;
import com.android.tkengine.elccommerce.utils.MultiItemAdapter;
import com.android.tkengine.elccommerce.utils.MyIndicator;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;


public class HomeFrgPresenter {

    static final String TAG = "HomeFrgPresenter";

    CallbackOfHomefrg mView;
    //ElcModel mModel;
    HomeFrgModel mModel;
    Context mContext;

    //与RecyclerView绑定的adapter
    HomeAdapter mHomepageAdapter;
    //首页的RecyclerView
    RecyclerView mRv;
    //首页标记,用于上拉加载
    int nowType;
    //RecyclerView是否正在加载
    boolean isLoading;

    public interface CallbackOfHomefrg {

        //提示正在加载刷新圈
        void showLoading();
        //停止加载，停止显示刷新圈
        void stopLoading();

        //提示首页正在加载
        void showLoadingHome();
        //提示首页加载失败,点击重新加载
        void showLoadingHomeFailed();
        //隐藏首页加载提示
        void hideHomeLoading();

        void showToast(String str);
    }

    public HomeFrgPresenter(CallbackOfHomefrg mView, Context context) {
        this.mView = mView;
        this.mContext = context;
        this.mModel = new HomeFrgModel(mContext);
    }

    /**
     * 控制RecyclerView的显示逻辑
     * Presenter完成RecyclerView的数据显示
     */
    public void controlRecyclerView(RecyclerView view){
        mRv = view;
        if (null == mRv.getLayoutManager()) {
            mRv.setLayoutManager(new LinearLayoutManager(mContext));
            mRv.setItemAnimator(new DefaultItemAnimator());
            //设置RecyclerView滚动到底部自动加载数据
            mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                int lastVisibleItem = 0;
                LinearLayoutManager llManager = (LinearLayoutManager) mRv.getLayoutManager();
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    lastVisibleItem = llManager.findLastVisibleItemPosition();
                }
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if(mHomepageAdapter.getItemCount() - 1 == lastVisibleItem && !isLoading){

                        loadMoreOnHomePage();
                        isLoading = true;
                    }
                }
            });
        }
        //开始加载首页
        nowType = 0;
        mView.showLoadingHome();
        List<HomePageItemBean> data = mModel.getHomePageData();
        if (null == data) {
            mView.showLoadingHomeFailed();
        } else {
            mView.hideHomeLoading();
            mHomepageAdapter = new HomeAdapter(data, mContext);
            mRv.setAdapter(mHomepageAdapter);
            loadMoreOnHomePage();
        }
    }

    //上拉加载的实现，根据RecyclerView最后一个Item获取数据
    public void loadMoreOnHomePage() {
        Log.i(TAG, "加载第" + nowType);
        if(nowType > 4){
            HomePageItemBean item = new HomePageItemBean();
            item.type = HomePageItemBean.TYPE_TIPS;
            item.data = new HashMap<>(1);
            item.data.put("tips", "没有更多数据了...");
            mHomepageAdapter.addItem(item);
            return;
        }
        //显示加载提示
        isLoading = true;
        mView.showLoading();
        HomePageItemBean item = new HomePageItemBean();
        item.type = HomePageItemBean.TYPE_TIPS;
        item.data = new HashMap<>(1);
        item.data.put("tips", "正在加载数据...");
        mHomepageAdapter.addItem(item);
        Log.i(TAG, "添加提示");

        mModel.getGoods(nowType++, new HomeFrgModel.ResponseListener(){
            @Override
            public void onResponse(List<HomePageItemBean> result) {
                Log.i(TAG, "添加数据");
                if (null != result) {
                    mHomepageAdapter.removeLastItem();
                    mHomepageAdapter.addItem(result);
                    mView.stopLoading();
                }
                isLoading = false;
            }
            @Override
            public void onError() {
                mHomepageAdapter.removeLastItem();
                mView.showToast("网络访问失败，请重试");
                isLoading = false;
                nowType--;
            }
        });
    }

    //RecyclerView绑定的Adapter
    private class HomeAdapter extends MultiItemAdapter<HomePageItemBean> {

        public void addItem(List<HomePageItemBean> data){
            for(HomePageItemBean temp : data){
                mData.add(temp);
            }
            notifyDataSetChanged();
        }
        public void addItem(HomePageItemBean item){
            mData.add(item);
            //notifyItemInserted(mData.size() - 1); 不知什么原因这句话会导致异常
            notifyDataSetChanged();
        }
        public void removeLastItem(){
            if (mData.size() > 0) {
                mData.remove(mData.size() - 1);
                notifyItemRemoved(mData.size());
            }
        }

        public HomeAdapter(final List<HomePageItemBean> mData, Context mContext) {
            super(mData, mContext, new MultiItemSupport() {
                @Override
                public int getViewItemType(int position) {
                    return mData.get(position).type;
                }

                @Override
                public int getViewItemLayoutId(int viewType) {
                    switch (viewType) {
                        case HomePageItemBean.TYPE_HEAD:
                            return R.layout.fragment_home_headview;
                        case HomePageItemBean.TYPE_GROUP:
                            return R.layout.fragment_home_goodgroup;
                        case HomePageItemBean.TYPE_GOODS:
                            return R.layout.fragment_home_gooditem;
                        case HomePageItemBean.TYPE_TIPS:
                            return R.layout.fragment_home_tips;
                        default:
                            throw new RuntimeException("在getViewItemLayoutId处发生错误，未知viewType!");
                    }
                }
            });
        }

        @Override
        public void convert(ViewHolder holder, HomePageItemBean itemData) {
            switch (holder.getItemViewType()) {
                case HomePageItemBean.TYPE_HEAD:
                    /*View advise = holder.getView(R.id.ll_advise);*/
                    View north = holder.getView(R.id.ll_north);
                    View south = holder.getView(R.id.ll_south);
                    View west = holder.getView(R.id.ll_west);
                   /* advise.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, SearchActivity.class);
                            intent.putExtra("type","专属推荐");
                            mContext.startActivity(intent);
                        }
                    });*/
                    north.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, SearchActivity.class);
                            intent.putExtra("type","北果风光");
                            mContext.startActivity(intent);
                        }
                    });
                    south.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, SearchActivity.class);
                            intent.putExtra("type","南果缤纷");
                            mContext.startActivity(intent);
                        }
                    });
                    west.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, SearchActivity.class);
                            intent.putExtra("type","西域果情");
                            mContext.startActivity(intent);
                        }
                    });

                    //设置首页广告
                    ViewPager vp = holder.getView(R.id.vp_homeAD);
                    final Bitmap[] imgs = (Bitmap[]) itemData.data.get("AD");
                    vp.setAdapter(new PagerAdapter() {
                        @Override
                        public int getCount() {
                            return imgs.length;
                        }

                        @Override
                        public Object instantiateItem(ViewGroup container, int position) {
                            ImageView iv = new ImageView(mContext);
                            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
                            iv.setImageBitmap(imgs[position]);
                            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            container.addView(iv);
                            return iv;
                        }

                        @Override
                        public void destroyItem(ViewGroup container, int position, Object object) {
                            container.removeView((View) object);
                        }

                        @Override
                        public boolean isViewFromObject(View view, Object object) {
                            return view == object;
                        }
                    });
                    LinearLayout indicator = holder.getView(R.id.bottom_indicator);
                    new MyIndicator().setUpViewPager(vp, indicator,mContext, 3, 1);
                    break;
                case HomePageItemBean.TYPE_GROUP:
                    TextView tv = holder.getView(R.id.tv_groupName);
                    tv.setText((CharSequence) itemData.data.get("groupName"));
                    break;
                case HomePageItemBean.TYPE_GOODS:
                    ImageView iv_goodsIcon = holder.getView(R.id.iv_goodIcon1);
                    TextView tv_goodsName = holder.getView(R.id.tv_goodname1);
                    TextView tv_city = holder.getView(R.id.tv_city1);
                    TextView tv_sales = holder.getView(R.id.tv_goodSales1);
                    TextView tv_goodPrice = holder.getView(R.id.tv_goodPrice1);
                    Picasso.with(mContext).load((String) itemData.data.get("icon1"))
                            .fit().error(R.mipmap.ic_launcher).into(iv_goodsIcon);
                    tv_goodsName.setText((String) itemData.data.get("name1"));
                    tv_city.setText(((String) itemData.data.get("city1")));
                    tv_sales.setText(((Integer) itemData.data.get("sales1")).toString());
                    tv_goodPrice.setText(((Double)itemData.data.get("price1")).toString());
                    //设置首页商品点击事件
                    final String id1 = (String) itemData.data.get("id1");
                    View view1 = holder.getView(R.id.layout_itemleft);
                    view1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, DisplayActivity.class);
                            intent.putExtra("productID", id1);
                            mContext.startActivity(intent);
                        }
                    });

                    if (itemData.data.size() > 9) {
                        iv_goodsIcon = holder.getView(R.id.iv_goodIcon2);
                        tv_goodsName = holder.getView(R.id.tv_goodname2);
                        tv_city = holder.getView(R.id.tv_city2);
                        tv_sales = holder.getView(R.id.tv_goodSales2);
                        tv_goodPrice = holder.getView(R.id.tv_goodPrice2);
                        Picasso.with(mContext).load((String) itemData.data.get("icon2"))
                                .fit().into(iv_goodsIcon);
                        tv_goodsName.setText((String) itemData.data.get("name2"));
                        tv_city.setText(((String) itemData.data.get("city2")));
                        tv_sales.setText(((Integer) itemData.data.get("sales2")).toString());
                        tv_goodPrice.setText(((Double)itemData.data.get("price2")).toString());
                        //设置首页商品点击事件
                        final String id2 = (String) itemData.data.get("id2");
                        View view2 = holder.getView(R.id.layout_itemright);
                        view2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, DisplayActivity.class);
                                intent.putExtra("productID", id2);
                                mContext.startActivity(intent);
                            }
                        });
                    }

                    break;

                case HomePageItemBean.TYPE_TIPS:
                    TextView tv_tips = holder.getView(R.id.tv_tips);
                    tv_tips.setText((CharSequence) itemData.data.get("tips"));
                    break;

                default:
                    Log.e(TAG, "在viewHolder的convertView方法出现错误！");
            }
        }
    }

}
