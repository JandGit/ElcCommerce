package com.android.tkengine.elccommerce.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.RvItemBean;
import com.android.tkengine.elccommerce.model.ElcModel;

import java.util.List;


public class HomeFrgPresenter {

    CallbackOfHomefrg mView;
    CallbackOfModel mModel;
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
        void setRvAdapter(RecyclerView.Adapter adapter);
        void addViewInRv(List<RvItemBean> data);
        void showLoadingHomePage();
        void showLoadingfailed();
        void showLoadingMore();
        void showLoadingHomeCompleted();
        void showLoadingMoreCompleted();
    }

    public interface CallbackOfModel{
        //加载首页数据，位置为from-to的数据项目
        List<RvItemBean> getHomePageData(int from, int to);
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
                Log.i("presenter", "开始加载首页数据");
                final List<RvItemBean> data = mModel.getHomePageData(0, 60);
                Log.i("presenter", "加载成功，现在添加Adapter");
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
                Log.i("presenter", "开始加载首页更多数据");
                List<RvItemBean> data = mModel.getHomePageData(from, from + 40);
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
                    holder = new MyViewHolder(mInflater.inflate(R.layout.homefrg_ad, parent, false));
                    break;
                case RvItemBean.TYPE_CATEGORY:
                    holder = new MyViewHolder(mInflater.inflate(R.layout.homefrg_category, parent, false));
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
                case RvItemBean.TYPE_AD:
                    ViewPager vp = (ViewPager) holder.getView(R.id.vp_homefrg_ad);
                    final Bitmap[] imgs = (Bitmap[]) data.data.get("advertisement");
                    vp.setAdapter(new PagerAdapter() {

                        Bitmap[] data = imgs;

                        @Override
                        public Object instantiateItem(ViewGroup container, int position) {
                            ImageView iv = new ImageView(mContext);
                            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
                            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            iv.setImageBitmap(data[position]);
                            container.addView(iv);
                            return iv;
                        }

                        @Override
                        public void destroyItem(ViewGroup container, int position, Object object) {
                            container.removeView((View)object);
                        }

                        @Override
                        public int getCount() {
                            return data.length;
                        }

                        @Override
                        public boolean isViewFromObject(View view, Object object) {
                            return view == object;
                        }
                    });
                    break;
                case RvItemBean.TYPE_CATEGORY:
                    View item1, item2, item3, item4, item5, item6, item7, item8;
                    item1 = holder.getView(R.id.tv_item1);
                    item2 = holder.getView(R.id.tv_item2);
                    item3 = holder.getView(R.id.tv_item3);
                    item4 = holder.getView(R.id.tv_item4);
                    item5 = holder.getView(R.id.tv_item5);
                    item6 = holder.getView(R.id.tv_item6);
                    item7 = holder.getView(R.id.tv_item7);
                    item8 = holder.getView(R.id.tv_item8);
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(mContext, "选中item", Toast.LENGTH_SHORT).show();
                        }
                    };
                    item1.setOnClickListener(listener);
                    item2.setOnClickListener(listener);
                    item3.setOnClickListener(listener);
                    item4.setOnClickListener(listener);
                    item5.setOnClickListener(listener);
                    item6.setOnClickListener(listener);
                    item7.setOnClickListener(listener);
                    item8.setOnClickListener(listener);
                    break;
                case RvItemBean.TYPE_GROUPTITLE:
                    TextView tv = (TextView) holder.getView(R.id.tv_goodGroupName);
                    tv.setText((String) data.data.get("groupName"));
                    break;
                case RvItemBean.TYPE_ITEM1:
                    ImageView iv_goodsIcon = (ImageView) holder.getView(R.id.iv_goodsIcon);
                    TextView tv_goodsName = (TextView) holder.getView(R.id.tv_goodsName);
                    TextView tv_shopOfGoods = (TextView) holder.getView(R.id.tv_shopOfGoods);
                    RatingBar rb_goodsRate = (RatingBar) holder.getView(R.id.rb_goodsRate);
                    iv_goodsIcon.setImageBitmap((Bitmap) data.data.get("goodsIcon"));
                    tv_goodsName.setText((String) data.data.get("goodsName"));
                    tv_shopOfGoods.setText((String)data.data.get("shopName"));
                    rb_goodsRate.setRating((Float) data.data.get("rating"));
                    break;
                case RvItemBean.TYPE_ITEM2:
                    ImageView iv_goodsIcon1 = (ImageView) holder.getView(R.id.iv_goodIcon1);
                    TextView tv_goodsName1 = (TextView) holder.getView(R.id.tv_goodname1);
                    RatingBar rb1 = (RatingBar) holder.getView(R.id.rb_goodRate1);
                    TextView tv_sale1 = (TextView) holder.getView(R.id.tv_goodSales1);

                    ImageView iv_goodsIcon2 = (ImageView) holder.getView(R.id.iv_goodIcon2);
                    TextView tv_goodsName2 = (TextView) holder.getView(R.id.tv_goodname2);
                    RatingBar rb2 = (RatingBar) holder.getView(R.id.rb_goodRate2);
                    TextView tv_sale2 = (TextView) holder.getView(R.id.tv_goodSales2);

                    iv_goodsIcon1.setImageBitmap((Bitmap) data.data.get("goodsIcon1"));
                    tv_goodsName1.setText((String)data.data.get("goodsName1"));
                    rb1.setRating((Float) data.data.get("rating1"));
                    tv_sale1.setText((String)data.data.get("sale1"));

                    iv_goodsIcon2.setImageBitmap((Bitmap) data.data.get("goodsIcon2"));
                    tv_goodsName2.setText((String)data.data.get("goodsName2"));
                    rb2.setRating((Float) data.data.get("rating2"));
                    tv_sale2.setText((String)data.data.get("sale2"));
                    break;
                case RvItemBean.TYPE_ITEM3:
                    break;
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
