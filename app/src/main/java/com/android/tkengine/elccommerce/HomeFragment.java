package com.android.tkengine.elccommerce;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tkengine.elccommerce.utils.ImageTools;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    //本页面的View对象
    View mView;
    //主页面RecyclerView
    RecyclerView rv_mainView;
    //标题栏toolbar
    Toolbar mToolbar;
    //下拉登录框头像
    ImageView iv_loginUserIcon;
    //下拉刷新
    SwipeRefreshLayout mSwipeRefreshLayout;
    //
    AppBarLayout mAppBarLayout;

    //
    //消息处理Handler
    private static class HomeFrgHandler extends Handler{
        final int MSG_RVREFRESH_COMPELETE = 0;

        RecyclerView rv_mainView;
        SwipeRefreshLayout mSwipeRefreshLayout;

        public HomeFrgHandler(SwipeRefreshLayout mSwipeRefreshLayout, RecyclerView rv_mainView) {
            this.mSwipeRefreshLayout = mSwipeRefreshLayout;
            this.rv_mainView = rv_mainView;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_RVREFRESH_COMPELETE:
                    mSwipeRefreshLayout.setRefreshing(false);
                    ArrayList<ArrayList<GoodsDataToShow>> allItem = (ArrayList<ArrayList<GoodsDataToShow>>) msg.getData().get("allItem");
                    String[] groupNames = (String[]) msg.getData().get("groupNames");
                    MyAdapter adapter = (MyAdapter) rv_mainView.getAdapter();
                    adapter.setData(allItem, groupNames);
                    adapter.notifyDataSetChanged();
                    break;
            }

            super.handleMessage(msg);
        }
    }
    private HomeFrgHandler mHandler;

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
        rv_mainView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_mainView.setAdapter(new MyAdapter());

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
                new Thread(){
                    @Override
                    public void run() {
                        ArrayList<ArrayList<GoodsDataToShow>> allItem = new ArrayList<>();
                        String[] groupNames = new String[6];
                        for(int i = 0; i < groupNames.length; i++){
                            groupNames[i] = "分组" + i;
                            ArrayList<GoodsDataToShow> temp = new ArrayList<>();
                            allItem.add(temp);
                            for(int j = 0; j < 30; j++){
                                GoodsDataToShow g = new GoodsDataToShow();
                                g.name = "商品" + j;
                                BitmapFactory.Options opt = new BitmapFactory.Options();
                                opt.inSampleSize = 10;
                                g.icon = BitmapFactory.decodeResource(getResources(), R.mipmap.background, opt);
                                g.whatShop = "店铺" + j;
                                g.rate = 5;
                                temp.add(g);
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Message msg = mHandler.obtainMessage(mHandler.MSG_RVREFRESH_COMPELETE);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("allItem", allItem);
                        bundle.putSerializable("groupNames", groupNames);
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                }.start();
            }
        });

        mHandler = new HomeFrgHandler( mSwipeRefreshLayout, rv_mainView);
    }


    //存放每一个商品展示数据的类
    private class GoodsDataToShow{
        //本Item的类别，只有TYPE_GROUPVIEW和TYPE_GOODVIEW两种
        int itemType = -1;

        Bitmap icon = null;  //商品图标
        String name = null;  //商品名称
        float rate = 0; //商品评分
        String whatShop = null; //所属店铺

        //分组名称,当itemType == TYPE_GROUPVIEW时有效
        String groupName = null;
    }
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        //分类View,在广告栏下方
        final int TYPE_HEADVIEW = 0;
        //分组的View，在每一组商品的上方
        final int TYPE_GROUPVIEW = 1;
        //具体商品
        final int TYPE_GOODSVIEW = 2;
        //存放要展示在首页的所有商品及分组信息
        ArrayList<GoodsDataToShow> list_allData;


        public boolean setData(ArrayList<ArrayList<GoodsDataToShow>> allItem, String[] groupNames){

            if(null == allItem || null == groupNames || 0 == allItem.size()){
                return false;
            }
            for(String i : groupNames){
                if(null == i) return false;
            }
            if(allItem.size() != groupNames.length){
                return false;
            }

            ArrayList<GoodsDataToShow> temp = new ArrayList<>();
            for(int i = 0; i < groupNames.length; i++){
                GoodsDataToShow group = new GoodsDataToShow();
                group.itemType = TYPE_GROUPVIEW;
                group.groupName = groupNames[i];
                temp.add(group);
                for(int j = 0; j < allItem.get(i).size(); j++){
                    if(allItem.get(i).get(j).itemType != TYPE_GOODSVIEW){
                        allItem.get(i).get(j).itemType = TYPE_GOODSVIEW;
                    }
                    temp.add(allItem.get(i).get(j));
                }
            }

            list_allData = temp;
            return true;
        }

        public MyAdapter() {
            list_allData = new ArrayList<>();
        }

        @Override
        public int getItemViewType(int position) {
            if(0 == position){
                return TYPE_HEADVIEW;
            }
            return list_allData.get(position - 1).itemType;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder;

            if(TYPE_HEADVIEW == viewType){
                holder = new MyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.homefrg_headview,
                        parent, false), viewType);
            }
            else if (TYPE_GROUPVIEW == viewType){
                holder = new MyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.homefrg_goods_group,
                        parent, false), viewType);
            }
            else{
                holder = new MyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.homefrg_goods_item,
                        parent, false), viewType);
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            GoodsDataToShow item;

            switch (holder.getItemViewType()){
                case TYPE_HEADVIEW:
                    holder.headItem1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(), "点击Item1", Toast.LENGTH_SHORT).show();
                        }
                    });
                    holder.vp_advertisment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(), "广告。。。", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case TYPE_GROUPVIEW:
                    item = list_allData.get(position - 1);
                    holder.tv_goodGroupName.setText(item.groupName);
                    break;
                case TYPE_GOODSVIEW:
                    item = list_allData.get(position - 1);
                    holder.tv_goodsName.setText(item.name);
                    holder.tv_shopOfGoods.setText(item.whatShop);
                    holder.iv_goodsIcon.setImageBitmap(item.icon);
                    holder.rb_goosRate.setRating(item.rate);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return list_allData.size() + 1;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            //HeadView布局
            View headItem1, headItem2, headItem3, headItem4;
            View headItem5, headItem6, headItem7, headItem8;
            ViewPager vp_advertisment;

            //首页商品展示布局
            TextView tv_goodGroupName; //分组条目的分组名
            TextView tv_goodsName; //商品条目的商品名
            TextView tv_shopOfGoods; //商品条目的店铺名
            ImageView iv_goodsIcon; //商品条目的商品图标
            RatingBar rb_goosRate; //商品条目的评分

            public MyViewHolder(View itemView, int viewType) {
                super(itemView);
                switch (viewType){
                    case TYPE_HEADVIEW:
                        headItem1 = itemView.findViewById(R.id.tv_item1);
                        vp_advertisment = (ViewPager) itemView.findViewById(R.id.vp_advertisement);
                        break;
                    case TYPE_GROUPVIEW:
                        tv_goodGroupName = (TextView) itemView.findViewById(R.id.tv_goodGroupName);
                        break;
                    case TYPE_GOODSVIEW:
                        tv_goodsName = (TextView) itemView.findViewById(R.id.tv_goodsName);
                        tv_shopOfGoods = (TextView) itemView.findViewById(R.id.tv_shopOfGoods);
                        iv_goodsIcon = (ImageView) itemView.findViewById(R.id.iv_goodsIcon);
                        rb_goosRate = (RatingBar) itemView.findViewById(R.id.rb_goodsRate);
                        break;
                }
            }
        }
    }
}
