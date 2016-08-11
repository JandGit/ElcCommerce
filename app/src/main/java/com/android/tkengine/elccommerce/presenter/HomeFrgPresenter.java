package com.android.tkengine.elccommerce.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.HomePageItemBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.utils.MultiItemAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HomeFrgPresenter {

    CallbackOfHomefrg mView;
    ElcModel mModel;
    Context mContext;
    private HomeFrgHandler mHandler;

    //消息处理Handler
    public static class HomeFrgHandler extends Handler {
        public final int MSG_RVREFRESH_COMPELETE = 0;
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
                    mView.addViewInRv(data);
                    mView.showLoadingMoreCompleted();
                    break;
            }

            super.handleMessage(msg);
        }
    }

    public interface CallbackOfHomefrg {
        //设置HomeFragment的RecyclerView的Adapter
        void setRvAdapter(RecyclerView.Adapter adapter);

        //将data内的数据加入到RecyclerView的末尾
        void addViewInRv(List<HomePageItemBean> data);

        //提示首页正在加载
        void showLoadingHomePage();

        //提示首页加载失败
        void showLoadingfailed();

        //提示正在加载更多数据
        void showLoadingMore();

        //提示首页加载成功
        void showLoadingHomeCompleted();

        //提示更多数据加载成功
        void showLoadingMoreCompleted();
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
        new Thread() {
            @Override
            public void run() {
                final List<HomePageItemBean> data = mModel.getHomePageData(0, 10);
                if (null == data) {
                    mHandler.sendEmptyMessage(mHandler.MSG_SHOW_LOADING_FAILED);
                } else {
                    Message msg = mHandler.obtainMessage(mHandler.MSG_SETADAPTER);
                    msg.obj = new HomeAdapter(data, mContext);
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    //加载首页更多数据
    public void loadMoreOnHomePage(final int from) {
        mView.showLoadingMore();
        new Thread() {
            @Override
            public void run() {
                List<HomePageItemBean> data = mModel.getHomePageData(from, from + 10);
                if (null != data) {
                    Message msg = mHandler.obtainMessage(mHandler.MSG_ADD_MORE_DATA);
                    msg.obj = data;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    public static class HomeAdapter extends MultiItemAdapter<HomePageItemBean> {

        Context mContext;

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
        }

        @Override
        public void convert(ViewHolder holder, HomePageItemBean itemData) {
            switch (holder.getItemViewType()) {
                case HomePageItemBean.TYPE_HEAD:
                    ViewPager vp = holder.getView(R.id.vp_homeAD);
                    //设置首页广告
                    break;
                case HomePageItemBean.TYPE_GROUP:
                    ImageView iv = holder.getView(R.id.iv_groupBackground);
                    TextView tv = holder.getView(R.id.tv_groupName);
                    Picasso.with(mContext).load((String) itemData.data.get("groupIcon")).fit().into(iv);
                    tv.setText((CharSequence) itemData.data.get("groupName"));
                    break;
                case HomePageItemBean.TYPE_GOODS:
                    ImageView iv_goodsIcon = holder.getView(R.id.iv_goodIcon1);
                    TextView tv_goodsName = holder.getView(R.id.tv_goodname1);
                    RatingBar rb = holder.getView(R.id.rb_goodRate1);
                    TextView tv_sales = holder.getView(R.id.tv_goodSales1);
                    Picasso.with(mContext).load((String) itemData.data.get("icon1")).fit().into(iv_goodsIcon);
                    tv_goodsName.setText((Integer) itemData.data.get("name1"));
                    rb.setRating((Float) itemData.data.get("rate1"));
                    tv_sales.setText((Integer) itemData.data.get("sales1"));

                    iv_goodsIcon = holder.getView(R.id.iv_goodIcon2);
                    tv_goodsName = holder.getView(R.id.tv_goodname2);
                    rb = holder.getView(R.id.rb_goodRate2);
                    tv_sales = holder.getView(R.id.tv_goodSales2);
                    Picasso.with(mContext).load((String) itemData.data.get("icon2")).fit().into(iv_goodsIcon);
                    tv_goodsName.setText((Integer) itemData.data.get("name2"));
                    rb.setRating((Float) itemData.data.get("rate2"));
                    tv_sales.setText((Integer) itemData.data.get("sales2"));

                    break;

                default:
                    Log.e("HomePresenter:", "在viewHolder的convertView方法出现错误！");
            }
        }
    }

}
