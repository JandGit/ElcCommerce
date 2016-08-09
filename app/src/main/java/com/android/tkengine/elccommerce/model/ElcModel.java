package com.android.tkengine.elccommerce.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.RvItemBean;
import com.android.tkengine.elccommerce.presenter.HomeFrgPresenter;
import com.android.tkengine.elccommerce.utils.ImageTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ElcModel implements HomeFrgPresenter.CallbackOfModel{

    Context mContext;

    public ElcModel(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<RvItemBean> getHomePageData(int from, int to) {
        RvItemBean itemData;
        ArrayList<RvItemBean> allData = new ArrayList<>(to - from + 1);
        for(int i = from; i <= to; i++){
            itemData = new RvItemBean();
            if(0 == i){
                itemData.data = new HashMap<>(1);
                itemData.type = RvItemBean.TYPE_AD;
                int[] imgsId = new int[4];
                imgsId[0] =  R.mipmap.advertise1;
                imgsId[1] = R.mipmap.advertise2;
                imgsId[2] = R.mipmap.advertise3;
                imgsId[3] = R.mipmap.advertise4;
                itemData.data.put("advertisement", imgsId);
            }
            else if(1 == i){
                itemData.type = RvItemBean.TYPE_CATEGORY;
            }
            else if(2 == i){
                itemData.data = new HashMap<>(1);
                itemData.type = RvItemBean.TYPE_GROUPTITLE;
                itemData.data.put("groupName", "小图标商品列表");
            }
            else if(20 == i){
                itemData.data = new HashMap<>(1);
                itemData.type = RvItemBean.TYPE_GROUPTITLE;
                itemData.data.put("groupName", "大图标商品列表");
            }
            else if(i > 20 && i <= 300){
                itemData.data = new HashMap<>(8);
                itemData.type = RvItemBean.TYPE_ITEM2;
                itemData.data.put("goodsIconId1", R.mipmap.background);
                itemData.data.put("goodsIconId2", R.mipmap.apple);
                itemData.data.put("goodsName1", "水果1");
                itemData.data.put("goodsName2", "水果2");
                itemData.data.put("rating1", 4.0f);
                itemData.data.put("rating2", 4.8f);
                itemData.data.put("sale1", "2000");
                itemData.data.put("sale2", "1000");
            }
            else {
                itemData.data = new HashMap<>(4);
                itemData.type = RvItemBean.TYPE_ITEM1;
                itemData.data.put("goodsIconId", R.mipmap.apple);
                itemData.data.put("goodsName", "水果" + i);
                itemData.data.put("shopName", "水果商店");
                itemData.data.put("rating", 4.5f);
            }
            allData.add(itemData);
        }

        return allData;
    }
}
