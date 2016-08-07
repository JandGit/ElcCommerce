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
                Bitmap[] img = new Bitmap[4];
                Bitmap temp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.apple);
                for(int j = 0; j < 4; j++){
                    img[j] = temp;
                }
                itemData.data.put("advertisement", img);
            }
            else if(1 == i){
                itemData.type = RvItemBean.TYPE_CATEGORY;
            }
            else if(from + 2 == i){
                itemData.data = new HashMap<>(1);
                itemData.type = RvItemBean.TYPE_GROUPTITLE;
                itemData.data.put("groupName", "分组名字");
            }
            else {
                itemData.data = new HashMap<>(4);
                itemData.type = RvItemBean.TYPE_ITEM1;
                itemData.data.put("goodsIcon", ImageTools.zoomBitmap(BitmapFactory.decodeResource(
                        mContext.getResources(), R.mipmap.apple), 100, 100));
                itemData.data.put("goodsName", "水果" + i);
                itemData.data.put("shopName", "水果商店");
                itemData.data.put("rating", 4.5f);
            }
            allData.add(itemData);
        }

        return allData;
    }
}
