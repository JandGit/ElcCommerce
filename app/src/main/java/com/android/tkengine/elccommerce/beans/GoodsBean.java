package com.android.tkengine.elccommerce.beans;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by 陈嘉shuo on 2016/8/9.
 */
public class GoodsBean implements Serializable {
    protected String goodsId;    //商品ID(用于后台对商品的删除、修改）
    protected String id;     //商品ID（用于后台区分不同商品）
    protected String goodsName;   //商品名
    protected double goodsPrice;  //商品价格
    protected String goodsIcon;   //商品图片
    protected int goodsNum;      //所订购商品的数量
    protected boolean goodsSelected;   //是否订购
    protected String Id;

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

    public void setId(String id) {
        Id = id;
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

    public String getId() {
        return Id;
    }
}
