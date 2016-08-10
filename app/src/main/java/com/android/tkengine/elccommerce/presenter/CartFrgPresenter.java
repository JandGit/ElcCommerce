package com.android.tkengine.elccommerce.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.GoodsBean;
import com.android.tkengine.elccommerce.utils.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈嘉shuo on 2016/8/9.
 */
public class CartFrgPresenter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    protected static OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public List<GoodsBean> cartGoodsList= new ArrayList<GoodsBean>();
    private Context context;
    private static final int TYPE_STORE = 1;
    private static final int TYPE_GOODS = 2;

    public CartFrgPresenter(Context context){
        this.context = context;
        cartGoodsList = initCartGoodsList();
    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_GOODS){
            View view= LayoutInflater.from(context).inflate(R.layout.cartfrg_goods_item,parent,false);
            return new GoodsViewHolder(view);
        }else if(viewType == TYPE_STORE){
            View view = LayoutInflater.from(context).inflate(R.layout.cartfrg_store,parent,false);
            return new StoreViewHolder(view);
        }
    return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof GoodsViewHolder) {
            GoodsBean cartGoodsItem = cartGoodsList.get(position);
            ((GoodsViewHolder)holder).goodsName.setText(cartGoodsItem.getGoodsName());
           /* ((GoodsViewHolder)holder).goodsIcon.setImageBitmap(cartGoodsItem.getGoodsIcon());*/
            ((GoodsViewHolder)holder).goodsIcon.setImageResource(R.mipmap.ic_launcher);
            ((GoodsViewHolder)holder).goodsPrice.setText(String.valueOf(cartGoodsItem.getGoodsPrice()));
            ((GoodsViewHolder)holder).goodsNumber.setText(String.valueOf(cartGoodsItem.getGoodsNum()));
            ((GoodsViewHolder)holder).goodsSelected.setChecked(cartGoodsItem.getGoodsSelected());
            setOnListtener((GoodsViewHolder)holder);
            holder.itemView.setTag(position);
        }else if(holder instanceof StoreViewHolder){
            GoodsBean cartGoodsItem = cartGoodsList.get(position);
            ((StoreViewHolder)holder).storeName.setText(cartGoodsItem.getGoodsName());
            ((StoreViewHolder)holder).storeSelected.setChecked(cartGoodsItem.getGoodsSelected());
            setOnGroupListner((StoreViewHolder)holder);
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (cartGoodsList.get(position).getGoodsPrice() == 0){
            return TYPE_STORE;
        }else{
            return TYPE_GOODS;
        }
    }

    @Override
     public int getItemCount() {
        return cartGoodsList.size();
    }


    //初始化购物车数据
    private List<GoodsBean> initCartGoodsList(){
        try{
            List<GoodsBean> cartShopList = new ArrayList<GoodsBean>();
            GoodsBean cartGoodsItem1 = new GoodsBean();
            cartGoodsItem1.setGoodsName("商店");
            cartGoodsItem1.setGoodsPrice(0);
            cartGoodsItem1.setGoodsNum(0);
            cartShopList.add(cartGoodsItem1);
            for(int i =0 ;i <4 ;i ++) {
                GoodsBean cartGoodsItem = new GoodsBean();
                cartGoodsItem.setGoodsName("水果wk9fiewpokfeofkw0 oeifwfeeewc oiwjoidwjoijw");
                cartGoodsItem.setGoodsPrice(10.02);
                cartGoodsItem.setGoodsNum(1);
                cartShopList.add(cartGoodsItem);
            }
            GoodsBean cartGoodsItem2 = new GoodsBean();
            cartGoodsItem2.setGoodsName("商店");
            cartGoodsItem2.setGoodsPrice(0);
            cartGoodsItem2.setGoodsNum(0);
            cartShopList.add(cartGoodsItem2);

            for(int j =0 ;j <4 ;j ++) {
                GoodsBean cartGoodsItem = new GoodsBean();
                cartGoodsItem.setGoodsName("水果");
                cartGoodsItem.setGoodsPrice(10.02);
                cartGoodsItem.setGoodsNum(1);
                cartShopList.add(cartGoodsItem);
            }
            return cartShopList;
        }catch (Exception e){
            return null;
        }
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
