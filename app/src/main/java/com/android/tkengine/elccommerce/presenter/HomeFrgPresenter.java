package com.android.tkengine.elccommerce.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.UI.DisplayActivity;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.HomePageItemBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.utils.MultiItemAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HomeFrgPresenter {

    CallbackOfHomefrg mView;
    ElcModel mModel;
    Context mContext;

    //首页RecyclerView的数据源
    List<HomePageItemBean> homePageData;
    //首页Adapter
    HomeAdapter homepageAdapter;
    //首页标记
    int nowType;

    private HomeFrgHandler mHandler;

    //消息处理Handler
    public static class HomeFrgHandler extends Handler {
        public final int MSG_NETWORK_ERROR = 0;
        public final int MSG_SETADAPTER = 1;
        public final int MSG_SHOW_LOADING_FAILED = 2;
        public final int MSG_SHOW_NOMORE_DATA = 3;
        public final int MSG_ADD_MORE_DATA = 4;

        private CallbackOfHomefrg mView;

        public HomeFrgHandler(CallbackOfHomefrg mView) {
            this.mView = mView;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SETADAPTER:
                    HomeAdapter adapter = (HomeAdapter) msg.obj;
                    mView.setRvAdapter(adapter);
                    mView.showLoadingHomeCompleted();
                    break;
                case MSG_SHOW_LOADING_FAILED:
                    mView.showLoadingfailed();
                    break;
                case MSG_ADD_MORE_DATA:
                    List<HomePageItemBean> data = (List<HomePageItemBean>) msg.obj;
                    mView.addMoreItem(data);
                    mView.showLoadingMoreCompleted();
                    break;
                case MSG_NETWORK_ERROR:
                    mView.showNetworkError();
                    break;
            }

            super.handleMessage(msg);
        }
    }

    public interface CallbackOfHomefrg {
        //设置HomeFragment的RecyclerView的Adapter
        void setRvAdapter(RecyclerView.Adapter adapter);

        //提示首页正在加载
        void showLoadingHomePage();

        //提示首页加载失败
        void showLoadingfailed();

        //提示正在加载更多数据
        void showLoadingMore();

        //添加数据到首页末尾
        void addMoreItem(List<HomePageItemBean> data);

        //提示首页加载成功
        void showLoadingHomeCompleted();

        //提示更多数据加载成功
        void showLoadingMoreCompleted();

        //提示网络连接错误
        void showNetworkError();
    }

    public HomeFrgPresenter(CallbackOfHomefrg mView, Context context) {
        this.mView = mView;
        this.mContext = context;
        this.mModel = new ElcModel(context);
        mHandler = new HomeFrgHandler(mView);
    }

    //加载首页
    public void initHomePage() {
        mView.showLoadingHomePage();
        nowType = 1;
        new Thread() {
            @Override
            public void run() {
                try {
                    homePageData = mModel.getHomePageData();
                    if (null == homePageData) {
                        mHandler.sendEmptyMessage(mHandler.MSG_SHOW_LOADING_FAILED);
                    } else {
                        Message msg = mHandler.obtainMessage(mHandler.MSG_SETADAPTER);
                        homepageAdapter = new HomeAdapter(homePageData, mContext);
                        msg.obj = homepageAdapter;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(mHandler.MSG_SHOW_LOADING_FAILED);
                }
            }
        }.start();
    }

    //加载首页更多数据
    public void loadMoreOnHomePage() {
        if(nowType > 4){
            return;
        }
        mView.showLoadingMore();

        new Thread() {
            @Override
            public void run() {
                try {
                    List<HomePageItemBean> data = mModel.getGoods(nowType++);
                    if (null != data) {
                        Message msg = mHandler.obtainMessage(mHandler.MSG_ADD_MORE_DATA);
                        msg.obj = data;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(mHandler.MSG_NETWORK_ERROR);
                }
            }
        }.start();

    }

    public static class HomeAdapter extends MultiItemAdapter<HomePageItemBean> {

        Context mContext;
        List<HomePageItemBean> mData;


        public void addItem(List<HomePageItemBean> data){
            for(HomePageItemBean temp : data){
                mData.add(temp);
                notifyItemInserted(mData.size() - 1);
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
                            return R.layout.homefrg_headview;
                        case HomePageItemBean.TYPE_GROUP:
                            return R.layout.homefrg_goodgroup;
                        case HomePageItemBean.TYPE_GOODS:
                            return R.layout.homefrg_gooditem;
                        default:
                            Log.e("homepresenter:", "在getViewItemLayoutId处发生错误，未知viewType!");
                    }
                    return 0;
                }
            });

            this.mContext = mContext;
            this.mData = mData;
        }

        @Override
        public void convert(ViewHolder holder, HomePageItemBean itemData) {
            switch (holder.getItemViewType()) {
                case HomePageItemBean.TYPE_HEAD:
                    ViewPager vp = holder.getView(R.id.vp_homeAD);
                    //设置首页广告
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
                    Picasso.with(mContext).load(Constants.SERVER_ADDRESS + (String) itemData.data.get("icon1"))
                            .fit().error(R.mipmap.ic_launcher).into(iv_goodsIcon);
                    tv_goodsName.setText((String) itemData.data.get("name1"));
                    tv_city.setText(((String) itemData.data.get("city1")));
                    tv_sales.setText(((Integer) itemData.data.get("sales1")).toString());
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
                        Picasso.with(mContext).load(Constants.SERVER_ADDRESS + (String) itemData.data.get("icon2"))
                                .fit().error(R.mipmap.ic_launcher).into(iv_goodsIcon);
                        tv_goodsName.setText((String) itemData.data.get("name2"));
                        tv_city.setText(((String) itemData.data.get("city2")));
                        tv_sales.setText(((Integer) itemData.data.get("sales2")).toString());
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

                default:
                    Log.e("HomePresenter:", "在viewHolder的convertView方法出现错误！");
            }
        }
    }

}
