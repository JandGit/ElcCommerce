package com.android.tkengine.elccommerce.beans;

import android.graphics.Bitmap;

/**
 * Created by 陈嘉shuo on 2016/8/9.
 */
public class GoodsBean {
    protected String goodsId;    //商品ID
    protected String goodsName;   //商品名
    protected double goodsPrice;  //商品价格
    protected String goodsIcon;   //商品图片
    protected int goodsNum;      //所订购商品的数量
    protected boolean goodsSelected;   //是否订购

    public void setGoodsId(String goodsId){
        this.goodsId = goodsId;
    }

    public void setGoodsName(String goodsName){
        this.goodsName = goodsName;
    }

    public void setGoodsPrice(double goodsPrice){
        this.goodsPrice = goodsPrice;
    }

    public void setGoodsNum(int goodsNum){
        this.goodsNum = goodsNum;
    }

    public void setGoodsIcon(String goodsIcon){
        this.goodsIcon = goodsIcon;
    }

    public void setGoodsSelected(boolean goodsSelected){
        this.goodsSelected = goodsSelected;
    }

    public String getGoodsId(){
        return goodsId;
    }

    public String getGoodsName(){
        return goodsName;
    }

    public double getGoodsPrice(){
        return goodsPrice;
    }

    public int getGoodsNum(){
        return goodsNum;
    }

    public String getGoodsIcon(){
        return goodsIcon;
    }

    public boolean getGoodsSelected(){
        return goodsSelected;
    }
}
