package com.android.tkengine.elccommerce.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.HomePageItemBean;
import com.android.tkengine.elccommerce.utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 首页Model
 */
public class HomeFrgModel {
    Context mContext;

    /**
     * 加载首页标题，广告等数据
     * 在非UI线程调用
     * @return
     */
    public List<HomePageItemBean> getHomePageData() throws Exception {
        ArrayList<HomePageItemBean> allData = new ArrayList<>();

        HomePageItemBean headitem = new HomePageItemBean();
        headitem.type = HomePageItemBean.TYPE_HEAD;
        headitem.data = new HashMap<>(1);
        Bitmap[] imgs = new Bitmap[3];
        imgs[0] = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.advertise1);
        imgs[1] = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.advertise2);
        imgs[2] = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.advertise3);
        headitem.data.put("AD", imgs);
        allData.add(headitem);

        return allData;
    }

    /**
     * 获取首页商品列表
     * 网络错误时抛出异常
     * @param type 商品类型，0为热销，1为推荐，2为北果，3为南果，4为西果
     * @return
     */
    public List<HomePageItemBean> getGoods(int type) throws Exception {
        ArrayList<HomePageItemBean> allData = new ArrayList<>();
        HomePageItemBean item = null;

        String params = null;
        HomePageItemBean group = new HomePageItemBean();
        group.type = HomePageItemBean.TYPE_GROUP;
        group.data = new HashMap<>(1);
        switch (type) {
            case 0:
                params = "{\"type\":\"sales\"}";
                group.data.put("groupName", "热销商品");
                break;
            case 1:
                params = "{\"type\":\"recommend\"}";
                group.data.put("groupName", "推荐商品");
                break;
            case 2:
                params = "{\"type\":\"north\"}";
                group.data.put("groupName", "北果风光");
                break;
            case 3:
                params = "{\"type\":\"south\"}";
                group.data.put("groupName", "南果缤纷");
                break;
            case 4:
                params = "{\"type\":\"west\"}";
                group.data.put("groupName", "西域果情");
                break;
            default:
                throw new Exception("类型type错误");
        }
        allData.add(group);

        // Log.i("EclModel:", "发送服务器请求,获取首页\n" + params);
        String response = HttpUtil.sentHttpPost(Constants.SERVER_HOMEPAGE, params);
        // Log.i("EclModel:", "服务器返回：" + response);
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jArray = jsonObject.getJSONArray("product_list");
        // Log.i("EclModel:", "开始解析商品列表, 数目:" + jArray.length());
        for(int i = 0; i < jArray.length(); i++){
            jsonObject = jArray.getJSONObject(i);
            if (0 == i % 2) {
                item = new HomePageItemBean();
                item.data = new HashMap<>(18);
                item.type = HomePageItemBean.TYPE_GOODS;
                item.data.put("id1", jsonObject.get("product_id"));
                item.data.put("name1", jsonObject.get("product_name"));
                item.data.put("type1", jsonObject.get("product_type"));
                item.data.put("city1", jsonObject.get("product_city"));
                item.data.put("store1", jsonObject.getInt("product_store"));
                item.data.put("sales1", jsonObject.getInt("product_sales"));
                item.data.put("price1", jsonObject.getDouble("product_price"));
                item.data.put("description1", jsonObject.get("product_description"));
                item.data.put("icon1", jsonObject.get("picture_url"));

                if(jArray.length() - 1 == i){
                    allData.add(item);
                }
                // Log.i("EclModel:", "解析第" + i + "个商品：，名称:" + item.data.get("name1") +
                //"\nid:" + item.data.get("id1") + "\n图片：" + item.data.get("icon1"));
            }
            else {
                assert item != null;
                item.data.put("id2", jsonObject.get("product_id"));
                item.data.put("name2", jsonObject.get("product_name"));
                item.data.put("type2", jsonObject.get("product_type"));
                item.data.put("city2", jsonObject.get("product_city"));
                item.data.put("store2", jsonObject.getInt("product_store"));
                item.data.put("sales2", jsonObject.getInt("product_sales"));
                item.data.put("price2", jsonObject.getDouble("product_price"));
                item.data.put("description2", jsonObject.get("product_description"));
                item.data.put("icon2", jsonObject.get("picture_url"));

                allData.add(item);
                //Log.i("EclModel:", "解析第" + i + "个商品：，名称:" + item.data.get("name2") +
                //"\nid:" + item.data.get("id2") + "\n图片：" + item.data.get("icon2"));
            }
        }

        // Log.i("EclModel:", "解析完毕，返回数据,数据量" + allData.size());
        if (allData.size() > 1) {
            return allData;
        }
        return null;
    }
}
