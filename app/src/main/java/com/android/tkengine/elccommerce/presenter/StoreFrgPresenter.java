package com.android.tkengine.elccommerce.presenter;import android.content.Context;import android.content.Intent;import android.os.Handler;import android.os.Message;import android.support.v7.widget.RecyclerView;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.RatingBar;import android.widget.TextView;import com.android.tkengine.elccommerce.R;import com.android.tkengine.elccommerce.UI.StoreDetailsActivity;import com.android.tkengine.elccommerce.beans.StoreBean;import com.android.tkengine.elccommerce.model.ElcModel;import com.android.tkengine.elccommerce.utils.StoreRefresh;import com.squareup.picasso.Picasso;import java.util.List;/** * Created by FangYu on 2016/8/14. */public class StoreFrgPresenter {    protected StoreRefresh mStoreRefresh;    public List<StoreBean.ResultBean> mDatas;    public MyRecycleViewAdapter mMyRecycleViewAdapter;    public void setmStoreRefresh(StoreRefresh mStoreRefresh) {        this.mStoreRefresh = mStoreRefresh;    }    public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyViewHolder> {        private LayoutInflater mInflater;        private Context mContext;        private static final int GET_SUCCESS = 1;        private static final int GET_FAIL = 2;        private static final String PAGESIZE = "15";        private String type = "";        private class MyHandler extends Handler{            @Override            public void handleMessage(Message msg) {                switch (msg.what){                    case GET_SUCCESS:                        mStoreRefresh.showData();                        notifyDataSetChanged();                        break;                    case  GET_FAIL:                        if(mDatas != null){                            mDatas.clear();                        }                        mStoreRefresh.showViewStub();                        break;                    default:                        break;                }}}        private MyHandler handler = new MyHandler();        public MyRecycleViewAdapter(Context context,String type){            this.mContext = context;            this.type = type;            mInflater = LayoutInflater.from(context);            initData();        }        public void initData() {            new Thread(new Runnable() {                @Override                public void run() {                    try{                        mDatas = new ElcModel(mContext).getStoreList("1", PAGESIZE, type);                        Message message = new Message();                        message.what = GET_SUCCESS;                        handler.sendMessage(message);                    }catch (Exception e){                        Message message = new Message();                        message.what = GET_FAIL;                        handler.sendMessage(message);                    }                }            }).start();        }        @Override        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {            View view = mInflater.inflate(R.layout.storelist_item , parent, false);            MyViewHolder myViewHolder = new MyViewHolder(view);            return myViewHolder;        }        @Override        public void onBindViewHolder(MyViewHolder holder, final int position) {            if(mDatas != null){                Picasso.with(mContext).load(mDatas.get(position).getShopPicture()).fit().into(holder.storeIcon);                holder.storeName.setText(mDatas.get(position).getShopName());                holder.storeSales.setText("销量" + mDatas.get(position).getSales());                holder.storeGrade.setProgress(mDatas.get(position).getGrade());                holder.storeDes.setText(mDatas.get(position).getShopDescription());                holder.store.setOnClickListener(new View.OnClickListener() {                    @Override                    public void onClick(View view) {                        Intent intent = new Intent(mContext, StoreDetailsActivity.class);                        intent.putExtra("storeID", mDatas.get(position).getId());                        mContext.startActivity(intent);                    }                });            }        }        @Override        public int getItemCount() {            if(mDatas != null){                return mDatas.size();            }            return 0;        }    }    class MyViewHolder extends RecyclerView.ViewHolder {        LinearLayout store;        ImageView storeIcon ;        TextView storeName, storeSales, storeDes;        RatingBar storeGrade;        public MyViewHolder(View itemView) {            super(itemView);            store = (LinearLayout) itemView.findViewById(R.id.store_list_item);            storeGrade = (RatingBar) itemView.findViewById(R.id.store_list_grade);            storeName = (TextView) itemView.findViewById(R.id.store_list_name);            storeDes = (TextView) itemView.findViewById(R.id.store_list_more);            storeSales = (TextView) itemView.findViewById(R.id.store_list_sales);            storeIcon = (ImageView) itemView.findViewById(R.id.store_list_icon);        }    }}