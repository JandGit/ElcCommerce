package com.android.tkengine.elccommerce.UI;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*import com.android.tkengine.elccommerce.PayActivity;*/
import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.GoodsBean;
import com.android.tkengine.elccommerce.presenter.CartFrgPresenter;
import com.android.tkengine.elccommerce.utils.DividerItemDecoration;
import com.android.tkengine.elccommerce.utils.OnRecyclerViewItemClickListener;

import java.text.DecimalFormat;

public class CartFragment extends Fragment implements OnRecyclerViewItemClickListener{
    View cartView;
    //购物车页面下拉刷新
    SwipeRefreshLayout cartSwipeRefreshLayout;
    //购物车列表
    RecyclerView cartRecyclerView;
    //总金额
    TextView cartGoodsSum;
    //购物车全选
    CheckBox cartSelectAll;
    //商品价格精确度
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    //购物车结算总金额
    double cartSum;
    //结算
    Button cartPay;
    //结算支付商品数量
    int goodsNumber;
    //购物车状态（编辑/完成）
    TextView cartStatus;
    //不同购物车状态下页面底部对应的布局（结算/删除）
    RelativeLayout payLayout;
    RelativeLayout deleteLayout;
    //删除
    Button cartDelete;


    CartFrgPresenter cartFrgPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cartView = inflater.inflate(R.layout.fragment_cart, container, false);
        initCartView();
        return cartView;

    }

    protected void initCartView(){
        payLayout = (RelativeLayout)cartView.findViewById(R.id.layout_pay);
        deleteLayout = (RelativeLayout)cartView.findViewById(R.id.layout_delete);
        cartSelectAll = (CheckBox)cartView.findViewById(R.id.chk_cart_selectAll);
        cartGoodsSum = (TextView)cartView.findViewById(R.id.tv_cart_goodsSum);
        //初始化下拉刷新控件
        cartSwipeRefreshLayout = (SwipeRefreshLayout)cartView.findViewById(R.id.srf_cart_goodsList);
        cartRecyclerView = (RecyclerView)cartView.findViewById(R.id.rv_cart_goodsList);
        cartSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        cartSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        cartSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cartSwipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cartSwipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });

        //初始化购物列表控件
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(cartView.getContext());
        cartRecyclerView.setLayoutManager(linearLayoutManager);
        cartRecyclerView.addItemDecoration(new DividerItemDecoration(cartView.getContext(),LinearLayoutManager.VERTICAL));
        cartFrgPresenter = new CartFrgPresenter(cartView.getContext());
        cartRecyclerView.setAdapter(cartFrgPresenter);
        cartFrgPresenter.setOnItemClickListener(this);


        //购物车全选操作
        cartSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartSelectAll.isChecked()){
                    goodsNumber = 0;
                    cartSum = 0;
                    for(GoodsBean cartGoodsItem:cartFrgPresenter.cartGoodsList){
                        cartGoodsItem.setGoodsSelected(true);
                        cartSum = cartSum + cartGoodsItem.getGoodsPrice() *cartGoodsItem.getGoodsNum();
                        goodsNumber = goodsNumber + cartGoodsItem.getGoodsNum();

                    }
                    cartFrgPresenter.notifyDataSetChanged();
                    cartGoodsSum.setText(String.valueOf(decimalFormat.format(cartSum)));
                    cartPay.setText("结算（"+String.valueOf(goodsNumber) +"）");

                }else if(!cartSelectAll.isChecked()){
                    for(GoodsBean cartGoodsItem:cartFrgPresenter.cartGoodsList){
                        cartGoodsItem.setGoodsSelected(false);
                    }
                    cartSum = 0;
                    cartFrgPresenter.notifyDataSetChanged();
                    cartGoodsSum.setText(String.valueOf(decimalFormat.format(cartSum)));
                    goodsNumber = 0;
                    cartPay.setText("结算（"+String.valueOf(goodsNumber) +"）");
                }
            }
        });



        //购物车支付操作
        cartPay = (Button)cartView.findViewById(R.id.btn_cart_pay);
        cartPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(goodsNumber == 0){
                    Toast.makeText(cartView.getContext(),"你还没有选择商品哦",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(cartView.getContext(),PayActivity.class);
                    startActivity(intent);
                }
            }
        });

        //切换购物车状态
        cartStatus = (TextView)cartView.findViewById(R.id.tv_cart_status);
        cartStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = cartStatus.getText().toString();
                if(status.equals("编辑")){
                    cartStatus.setText("完成");
                    payLayout.setVisibility(View.GONE);
                    deleteLayout.setVisibility(View.VISIBLE);
                }else if(status.equals("完成")){
                    cartStatus.setText("编辑");
                    payLayout.setVisibility(View.VISIBLE);
                    deleteLayout.setVisibility(View.GONE);
                    for(GoodsBean cartGoodsItem:cartFrgPresenter.cartGoodsList){
                        cartGoodsItem.setGoodsSelected(false);
                    }
                    cartSum = 0;
                    cartFrgPresenter.notifyDataSetChanged();
                    cartGoodsSum.setText(String.valueOf(decimalFormat.format(cartSum)));
                    goodsNumber = 0;
                    cartPay.setText("结算（"+String.valueOf(goodsNumber) +"）");
                }
            }
        });

        //删除
        cartDelete = (Button)cartView.findViewById(R.id.btn_cart_delete);
        cartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               cartFrgPresenter.deleteItem(cartFrgPresenter.getSelectedItem());
            }
        });


    }


    /** 添加 */
    public void onItemAddClick(CartFrgPresenter.GoodsViewHolder holder) {
        int nums = cartFrgPresenter.cartGoodsList.get(holder.getPosition()).getGoodsNum();
        nums ++;
        holder.goodsNumber.setText(String.valueOf(nums));
        cartFrgPresenter.cartGoodsList.get(holder.getPosition()).setGoodsNum(nums);

        /** 统计购物总数和购物总价 */
        if(holder.goodsSelected.isChecked()){
            cartSum = cartSum + Double.parseDouble(holder.goodsPrice.getText().toString());
            cartGoodsSum.setText(String.valueOf(decimalFormat.format(cartSum)));
            goodsNumber = goodsNumber + 1;
            cartPay.setText("结算（"+String.valueOf(goodsNumber) +"）");
        }
    }


    /** 减少 */
    public void onItemReduceClick(CartFrgPresenter.GoodsViewHolder holder) {
        int nums = cartFrgPresenter.cartGoodsList.get(holder.getPosition()).getGoodsNum();
        if(nums != 1){   //购物车各商品数量不能小于1
            nums --;
            holder.goodsNumber.setText(String.valueOf(nums));
            cartFrgPresenter.cartGoodsList.get(holder.getPosition()).setGoodsNum(nums);

            if(holder.goodsSelected.isChecked()){
                cartSum = cartSum - Double.parseDouble(holder.goodsPrice.getText().toString());
                cartGoodsSum.setText(String.valueOf(decimalFormat.format(cartSum)));
                goodsNumber = goodsNumber - 1;
                cartPay.setText("结算（"+String.valueOf(goodsNumber) +"）");
            }
        }
    }


    //点击选择一个商品
    public void onItemCheckboxClick(CartFrgPresenter.GoodsViewHolder holder){
        if( holder.goodsSelected.isChecked()){
            cartSum = cartSum + Double.parseDouble(holder.goodsPrice.getText().toString()) * Integer.parseInt(holder.goodsNumber.getText().toString());
            cartGoodsSum.setText(String.valueOf(decimalFormat.format(cartSum)));
            cartFrgPresenter.cartGoodsList.get(holder.getPosition()).setGoodsSelected(true);
            goodsNumber = goodsNumber + Integer.parseInt(holder.goodsNumber.getText().toString());
            cartPay.setText("结算（"+String.valueOf(goodsNumber) +"）");

            //判断组内所有选项都被选中
            int startPosition;
            int position = holder.getPosition() - 1;
            while (cartFrgPresenter.cartGoodsList.get(position).getGoodsPrice() != 0){
                position -- ;
            }
            startPosition = position;
            cartFrgPresenter.cartGoodsList.get(startPosition).setGoodsSelected(true);
            position = position +1;
            while(position <= cartFrgPresenter.cartGoodsList.size() - 1 && cartFrgPresenter.cartGoodsList.get(position).getGoodsPrice() != 0 ){
                if(!cartFrgPresenter.cartGoodsList.get(position).getGoodsSelected()){
                    cartFrgPresenter.cartGoodsList.get(startPosition).setGoodsSelected(false);
                    break;
                }
                cartFrgPresenter.notifyDataSetChanged();
                position ++;
            }

            //判断购物车所有选项都被选中
           if(cartFrgPresenter.allGoodsSelected())
                cartSelectAll.setChecked(true);


        }else {
            cartSum =cartSum - Double.parseDouble(holder.goodsPrice.getText().toString()) * Integer.parseInt(holder.goodsNumber.getText().toString());
            cartGoodsSum.setText(String.valueOf(decimalFormat.format(cartSum)));
            cartFrgPresenter.cartGoodsList.get(holder.getPosition()).setGoodsSelected(false);
            goodsNumber = goodsNumber - Integer.parseInt(holder.goodsNumber.getText().toString());
            cartPay.setText("结算（"+String.valueOf(goodsNumber) +"）");

            int position = holder.getPosition() - 1;
            while (cartFrgPresenter.cartGoodsList.get(position).getGoodsPrice() != 0){
                position -- ;
            }
            if(cartFrgPresenter.cartGoodsList.get(position).getGoodsSelected()){
                cartFrgPresenter.cartGoodsList.get(position).setGoodsSelected(false);
                cartFrgPresenter.notifyDataSetChanged();
            }

            //购物车没有全选
            if(cartSelectAll.isChecked()){
                cartSelectAll.setChecked(false);
            }

        }
    }


    //点击各商品进入商品详情
    public void onItemViewClick(){
        Intent intent = new Intent(cartView.getContext(),DisplayActivity.class);
        startActivity(intent);
    }

    //点击选择一组商品
    public void onGroupCheckboxClick(CartFrgPresenter.StoreViewHolder holder){
        if(holder.storeSelected.isChecked()){
            cartFrgPresenter.cartGoodsList.get(holder.getPosition()).setGoodsSelected(true);
            int start = holder.getPosition() + 1 ;
            while(start <= cartFrgPresenter.cartGoodsList.size() - 1 && cartFrgPresenter.cartGoodsList.get(start).getGoodsPrice() != 0 ){
                Log.d("position",String.valueOf(start));
                GoodsBean cartGoodsItem = cartFrgPresenter.cartGoodsList.get(start);
                if( !cartGoodsItem.getGoodsSelected()){  //设组内没被选中的商品为被选中状态
                    cartGoodsItem.setGoodsSelected(true);
                    cartSum = cartSum + cartGoodsItem.getGoodsPrice() * cartGoodsItem.getGoodsNum();
                    goodsNumber = goodsNumber + cartGoodsItem.getGoodsNum();
                }
                start ++;
            }
            cartFrgPresenter.notifyDataSetChanged();
            cartGoodsSum.setText(String.valueOf(decimalFormat.format(cartSum)));
            cartPay.setText("结算（"+String.valueOf(goodsNumber) +"）");

            //判断购物车所有选项都被选中
            if(cartFrgPresenter.allGoodsSelected())
                cartSelectAll.setChecked(true);


        }else{
            cartFrgPresenter.cartGoodsList.get(holder.getPosition()).setGoodsSelected(false);
            int start = holder.getPosition() + 1 ;
            while(start <= cartFrgPresenter.cartGoodsList.size() - 1 && cartFrgPresenter.cartGoodsList.get(start).getGoodsPrice() != 0 ){
                Log.d("position",String.valueOf(start));
                GoodsBean cartGoodsItem = cartFrgPresenter.cartGoodsList.get(start);
                goodsNumber = goodsNumber - cartGoodsItem.getGoodsNum();
                cartGoodsItem.setGoodsSelected(false);
                cartSum = cartSum - cartGoodsItem.getGoodsPrice() * cartGoodsItem.getGoodsNum();
                start ++;
            }
            cartFrgPresenter.notifyDataSetChanged();
            cartGoodsSum.setText(String.valueOf(decimalFormat.format(cartSum)));
            cartPay.setText("结算（"+String.valueOf(goodsNumber) +"）");

            //购物车没有全选
            if(cartSelectAll.isChecked()){
                cartSelectAll.setChecked(false);
            }

        }
    }







}