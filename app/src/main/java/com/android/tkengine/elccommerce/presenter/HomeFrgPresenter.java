package com.android.tkengine.elccommerce.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.UI.DisplayActivity;
import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.RvItemBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HomeFrgPresenter {

    CallbackOfHomefrg mView;
    ElcModel mModel;
    Context mContext;
    private HomeFrgHandler mHandler;

    //消息处理Handler
    public static class HomeFrgHandler extends Handler{
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
            switch (msg.what){
                case MSG_SETADAPTER:
                    HomepageAdapter adapter = (HomepageAdapter) msg.obj;
                    mView.setRvAdapter(adapter);
                    mView.showLoadingHomeCompleted();
                    break;
                case MSG_SHOW_LOADING_FAILED:
                    mView.showLoadingfailed();
                    break;
                case MSG_ADD_MORE_DATA:
                    List<RvItemBean> data = (List<RvItemBean>) msg.obj;
                    mView.addViewInRv(data);
                    mView.showLoadingMoreCompleted();
                    break;
            }

            super.handleMessage(msg);
        }
    }

    public interface CallbackOfHomefrg{
        //设置HomeFragment的RecyclerView的Adapter
        void setRvAdapter(RecyclerView.Adapter adapter);
        //将data内的数据加入到RecyclerView的末尾
        void addViewInRv(List<RvItemBean> data);
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
    public void initHomePage(){
        mView.showLoadingHomePage();
        new Thread(){
            @Override
            public void run() {
                final List<RvItemBean> data = mModel.getHomePageData(0, 10);
                if(null == data){
                    mHandler.sendEmptyMessage(mHandler.MSG_SHOW_LOADING_FAILED);
                }
                else{
                    Message msg = mHandler.obtainMessage(mHandler.MSG_SETADAPTER);
                    msg.obj = new HomepageAdapter(data, mContext);
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    //加载首页更多数据
    public void loadMoreOnHomePage(final int from){
        mView.showLoadingMore();
        new Thread(){
            @Override
            public void run() {
                List<RvItemBean> data = mModel.getHomePageData(from, from + 10);
                if(null != data){
                    Message msg = mHandler.obtainMessage(mHandler.MSG_ADD_MORE_DATA);
                    msg.obj = data;
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    public static class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.MyViewHolder>{

        List<RvItemBean> mData;
        Context mContext;
        LayoutInflater mInflater;

        public HomepageAdapter(List<RvItemBean> mData, Context context) {
            this.mData = mData;
            this.mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        //在末尾添加data指定的数据项
        public void addItems(List<RvItemBean> data){
            for(RvItemBean temp : data){
                mData.add(temp);
                notifyItemInserted(mData.size() - 1);
            }
        }

        //移除末尾Item
        public void removeLast(){
            mData.remove(mData.size() - 1);
            notifyItemRemoved(mData.size());
        }

        @Override
        public int getItemViewType(int position) {
            return mData.get(position).type;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = null;
            switch (viewType){
                case RvItemBean.TYPE_AD:
                    break;
                case RvItemBean.TYPE_CATEGORY:
                    break;
                case RvItemBean.TYPE_GROUPTITLE:
                    holder = new MyViewHolder(mInflater.inflate(R.layout.homefrg_goods_group, parent, false));
                    break;
                case RvItemBean.TYPE_ITEM1:
                    holder = new MyViewHolder(mInflater.inflate(R.layout.homefrg_goods_item, parent, false));
                    break;
                case RvItemBean.TYPE_ITEM2:
                    holder = new MyViewHolder(mInflater.inflate(R.layout.homefrg_bigitem, parent, false));
                    break;
                case RvItemBean.TYPE_ITEM3:
                    break;
                default:
                    Log.e("MyViewHodler", "未知的RvItemBean类型，错误！");
                    holder = null;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            RvItemBean data = mData.get(position);
            switch (holder.getItemViewType()){
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            SparseArray<View> allViews = new SparseArray<>();
            View convertView;

            public MyViewHolder(View itemView) {
                super(itemView);
                convertView = itemView;
            }

            //获得id对应的view
            public View getView(int id){
                View view = allViews.get(id);
                if(null == view){
                    view = convertView.findViewById(id);
                    allViews.put(id, view);
                }
                return view;
            }
        }
    }
}
