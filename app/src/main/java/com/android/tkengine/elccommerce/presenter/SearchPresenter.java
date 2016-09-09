package com.android.tkengine.elccommerce.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.UI.DisplayActivity;
import com.android.tkengine.elccommerce.beans.SearchGoodsBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈嘉shuo on 2016/8/14.
 */
public class SearchPresenter {

    private static final int SUCCESS = 1;
    private callBackListener callBackActivity;
    private Context context;
    public GoodsRecycleViewAdapter goodsRecycleViewAdapter = new GoodsRecycleViewAdapter();
    public  List<SearchGoodsBean.ProductListBean> searchGoodsList = new ArrayList<SearchGoodsBean.ProductListBean>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    goodsRecycleViewAdapter.notifyDataSetChanged();
            }
        }
    };


    public SearchPresenter(callBackListener callBackActivity,Context context) {
        this.callBackActivity = callBackActivity;
        this.context = context;
    }

    public void getGoodsList(final String key, final String sort, final String left, final String right, final int currentPage, final int pageSize){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<SearchGoodsBean.ProductListBean> newSearchGoodsList = new ElcModel(context).getSearchResult(key,sort,left,right,currentPage,pageSize);
                        searchGoodsList.addAll(newSearchGoodsList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = SUCCESS;
                    handler.sendMessage(message);
                }
            }).start();
    }

    public class GoodsRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.search_goods_item, parent, false);
            return new viewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final SearchGoodsBean.ProductListBean searchGoodsItem = searchGoodsList.get(position);
            Picasso.with(context).load(searchGoodsItem.getPicture_url()).fit().into(((viewHolder) holder).searchGoodsIcon);
            ((viewHolder) holder).searchGoodsName.setText(searchGoodsItem.getProduct_description());
            ((viewHolder) holder).searchGoodsPrice.setText(String.valueOf(searchGoodsItem.getProduct_price()));
            ((viewHolder) holder).searchGoodsPlace.setText(searchGoodsItem.getProduct_city());
            ((viewHolder) holder).searchGoodsSale.setText(String.valueOf(searchGoodsItem.getProduct_sales()));
            ((viewHolder) holder).searchGoodsStore.setText(String.valueOf(searchGoodsItem.getProduct_store()));
             ((viewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBackActivity.startDisplayActivity(searchGoodsItem.getProduct_id());
                }
            });
        }

        @Override
        public int getItemCount() {
            return searchGoodsList.size();
        }
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        public View view;
        public TextView searchGoodsName;
        public TextView searchGoodsPrice;
        public TextView searchGoodsPlace;
        public TextView searchGoodsSale;
        public TextView searchGoodsStore;
        public ImageView searchGoodsIcon;

        public viewHolder(View view) {
            super(view);
            this.view = view;
            searchGoodsName = (TextView)view.findViewById(R.id.tv_search_goodsName);
            searchGoodsPrice = (TextView)view.findViewById(R.id.tv_search_goodsPrice);
            searchGoodsPlace = (TextView)view.findViewById(R.id.tv_search_goodsPlace);
            searchGoodsSale = (TextView)view.findViewById(R.id.tv_search_goodsSale);
            searchGoodsStore = (TextView)view.findViewById(R.id.tv_search_goodsStore);
            searchGoodsIcon = (ImageView)view.findViewById(R.id.iv_search_goodsIcon);
        }

    }

    public interface callBackListener{
        void startDisplayActivity(String productId);
    }


}
