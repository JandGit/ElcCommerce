package com.android.tkengine.elccommerce.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.GoodsBean;
import com.android.tkengine.elccommerce.model.ElcModel;
import com.android.tkengine.elccommerce.utils.HttpCallbackListener;
import com.android.tkengine.elccommerce.utils.HttpUtil;
import com.android.tkengine.elccommerce.utils.OnRecyclerViewItemClickListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈嘉shuo on 2016/8/9.
 */
public class CartFrgPresenter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public List<GoodsBean> cartGoodsList = new ArrayList<GoodsBean>();
    private Context context;
    private static final int TYPE_STORE = 1;
    private static final int TYPE_GOODS = 2;
    private static final int GET_SUCCESS = 3;
    private static final int POST_SUCCESS = 4;
    private static final int POST_FAIL = 5;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_SUCCESS:
                    notifyDataSetChanged();
                    break;
                case POST_SUCCESS:
                    Log.d("ok","ok");
                default:
                    break;
            }
        }
    };

    public CartFrgPresenter(Context context) {
        this.context = context;
        initCartGoodsList();
    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_GOODS) {
            View view = LayoutInflater.from(context).inflate(R.layout.cartfrg_goods_item, parent, false);
            return new GoodsViewHolder(view);
        } else if (viewType == TYPE_STORE) {
            View view = LayoutInflater.from(context).inflate(R.layout.cartfrg_store, parent, false);
            return new StoreViewHolder(view);
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsViewHolder) {
            GoodsBean cartGoodsItem = cartGoodsList.get(position);
            ((GoodsViewHolder) holder).goodsName.setText(cartGoodsItem.getGoodsName());
            Picasso.with(context).load(cartGoodsItem.getGoodsIcon()).fit().into(((GoodsViewHolder) holder).goodsIcon);
           /* ((GoodsViewHolder)holder).goodsIcon.setImageBitmap(cartGoodsItem.getGoodsIcon());*/
           /* ((GoodsViewHolder) holder).goodsIcon.setImageResource(R.mipmap.ic_launcher);*/
            ((GoodsViewHolder) holder).goodsPrice.setText(String.valueOf(cartGoodsItem.getGoodsPrice()));
            ((GoodsViewHolder) holder).goodsNumber.setText(String.valueOf(cartGoodsItem.getGoodsNum()));
            ((GoodsViewHolder) holder).goodsSelected.setChecked(cartGoodsItem.getGoodsSelected());
            setOnListtener((GoodsViewHolder) holder);
            holder.itemView.setTag(position);
        } else if (holder instanceof StoreViewHolder) {
            GoodsBean cartGoodsItem = cartGoodsList.get(position);
            ((StoreViewHolder) holder).storeName.setText(cartGoodsItem.getGoodsName());
            ((StoreViewHolder) holder).storeSelected.setChecked(cartGoodsItem.getGoodsSelected());
            setOnGroupListner((StoreViewHolder) holder);
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (cartGoodsList.get(position).getGoodsPrice() == 0) {
            return TYPE_STORE;
        } else {
            return TYPE_GOODS;
        }
    }

    @Override
    public int getItemCount() {
        return cartGoodsList.size();
    }


    //初始化购物车数据
    private void initCartGoodsList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    cartGoodsList = new ElcModel(context).getCartGoodsList();
                    Message message = new Message();
                    message.what = GET_SUCCESS;
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener){
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    //事件监听
    protected void setOnListtener(final GoodsViewHolder holder){
            holder.goodsNumAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewItemClickListener.onItemAddClick(holder);
                }
            });
            holder.goodsNumReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewItemClickListener.onItemReduceClick(holder);
                }
            });

            holder.goodsSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecyclerViewItemClickListener.onItemCheckboxClick(holder);
                }
            });
            holder.goodsContext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecyclerViewItemClickListener.onItemViewClick();
                }
            });

    }

    protected void setOnGroupListner(final StoreViewHolder holder){
        holder.storeSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecyclerViewItemClickListener.onGroupCheckboxClick(holder);
            }
        });
    }

    public void addItem(final List<GoodsBean> newGoodsList){
        //先遍历原数据中是否有相同的店铺名
        new Thread(new Runnable() {
            @Override
            public void run() {
                int index = 0;
                GoodsBean newStore = newGoodsList.get(0);  //List中第一个是店铺名信息
                String storeName = newStore.getGoodsName();
                while(index <= cartGoodsList.size() - 1 && !cartGoodsList.get(index).getGoodsName().equals(storeName)){
                    index ++;
                }
                if(index == cartGoodsList.size()){   //没有相同店铺名，插入到原数据第一个位置
                    cartGoodsList.addAll(0,newGoodsList);
                }else{          //相同店铺名，合并在一起
                    newGoodsList.remove(0);
                    cartGoodsList.addAll(index,newGoodsList);
                }
            }
        }).start();
        notifyDataSetChanged();
    }

    public void deleteItem( List<GoodsBean> selectedGoodsList){
      /* new Thread(new Runnable() {
            @Override
            public void run() {
                if(selectedGoodsList != null){
                    for(GoodsBean selectedGoodsItem:selectedGoodsList){
                        cartGoodsList.remove(selectedGoodsItem);
                    }
                }
                notifyDataSetChanged();
            }
        }).start();*/
        postCartGoodsList(selectedGoodsList,"http://192.168.1.105:8080/TKBaas/cart/app/delIncart");
        if(selectedGoodsList != null){
            for(GoodsBean selectedGoodsItem:selectedGoodsList){
                cartGoodsList.remove(selectedGoodsItem);
            }
        }
        notifyDataSetChanged();

    }

    public List<GoodsBean> getSelectedItem(){
        final List<GoodsBean> goodsSelectedList = new ArrayList<GoodsBean>();
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                for(GoodsBean cartGoodsItem:cartGoodsList){
                    if(cartGoodsItem.getGoodsSelected()){
                        goodsSelectedList.add(cartGoodsItem);
                    }
                }
            }
        }).start();*/
        for(GoodsBean cartGoodsItem:cartGoodsList){
            if(cartGoodsItem.getGoodsSelected()){
                goodsSelectedList.add(cartGoodsItem);
            }
        }
        return goodsSelectedList;
    }

    //判断是否全选
    public boolean allGoodsSelected(){
        int index = 0;
        for(GoodsBean cartGoodsItem:cartGoodsList){
            if( !cartGoodsItem.getGoodsSelected()){
                break;
            }
            index ++;
        }
        if(index == cartGoodsList.size()){
            return true;
        }else{
            return false;
        }
    }


    //提交购物车信息到服务器
    public  void postCartGoodsList(final List<GoodsBean>goodsList, final String postUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    boolean result = new ElcModel(context).postCartInfo(goodsList,postUrl);
                    if(result){
                        Message message = new Message();
                        message.what = POST_SUCCESS;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }




    public static class GoodsViewHolder extends RecyclerView.ViewHolder {
        public View goodsContext;
        public TextView goodsName;
        public   TextView goodsPrice;
        public ImageView goodsIcon;
        public EditText goodsNumber;
        public TextView goodsNumAdd;
        public   TextView goodsNumReduce;
        public CheckBox goodsSelected;


        public GoodsViewHolder(View view){
            super(view);
            goodsContext = view;
            goodsName = (TextView)view.findViewById(R.id.tv_cart_goodsName);
            goodsPrice = (TextView)view.findViewById(R.id.tv_cart_goodsPrice);
            goodsIcon = (ImageView)view.findViewById(R.id.iv_cart_goodsIcon);
            goodsNumber = (EditText) view.findViewById(R.id.et_cart_goodsNum);
            goodsNumAdd = (TextView)view.findViewById(R.id.tv_cart_goodsAdd);
            goodsNumReduce = (TextView)view.findViewById(R.id.tv_cart_goodsReduce);
            goodsSelected = (CheckBox)view.findViewById(R.id.chk_cart_select);
        }

    }

    public static class StoreViewHolder extends  RecyclerView.ViewHolder{
        public CheckBox storeSelected;
        protected TextView storeName;
        public StoreViewHolder(View view) {
              super(view);
             storeName=(TextView)view.findViewById(R.id.tv_cart_storeName);
            storeSelected = (CheckBox)view.findViewById(R.id.chk_cart_selectPart);
        }
    }




}
