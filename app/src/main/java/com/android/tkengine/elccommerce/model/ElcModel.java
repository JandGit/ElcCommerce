package com.android.tkengine.elccommerce.model;


import android.content.Context;
import android.util.Log;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.GoodsBean;
import com.android.tkengine.elccommerce.beans.HomePageItemBean;
import com.android.tkengine.elccommerce.beans.UserInfoBean;
import com.android.tkengine.elccommerce.utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ElcModel {

    Context mContext;

    public ElcModel(Context mContext) {
        this.mContext = mContext;
    }



    public List<HomePageItemBean> getHomePageData(int from, int to) {
        HomePageItemBean itemData;
        ArrayList<HomePageItemBean> allData = new ArrayList<>(to - from + 1);

        return allData;
    }

    /**
     *
     * @param userName 登录的用户名
     * @param password 登录密码
     * @return 用户信息，如果登录用户名或密码错误返回null
     * @throws Exception 网络连接错误
     */
    public UserInfoBean login(String userName, String password) throws Exception {

        JSONObject jsonObject = new JSONObject();
        String params;
        String userId = null;
        jsonObject.put("user_phone", userName);
        jsonObject.put("user_password", password);
        params = jsonObject.toString();
        String result;
        Log.i("mModel:", "发送请求：" + params);
        result = HttpUtil.sentHttpPost(Constants.SERVER_ADDRESS_LOGIN, params);
        Log.i("mModel:", "服务器返回：" + result);
        jsonObject = new JSONObject(result);
        userId = jsonObject.getString("user_id");

        if (null == userId || userId.isEmpty()) {
            return null;
        }
        UserInfoBean info = new UserInfoBean();
        info.setUserId(userId);
        jsonObject = new JSONObject();
        jsonObject.put("user_id", userId);
        params = jsonObject.toString();
        Log.i("mModel:", "发送请求：" + params);
        result = HttpUtil.sentHttpPost(Constants.SERVER_GETUSERINFO, params);
        Log.i("mModel:", "服务器返回：" + result);
        jsonObject = new JSONObject(result);
        if (jsonObject.has("user_name")) {
            info.setUser_name(jsonObject.getString("user_name"));
        }
        if (jsonObject.has("user_phone")) {
            info.setUser_phone(jsonObject.getString("user_phone"));
        }
        if (jsonObject.has("user_sex")) {
            info.setUser_sex(jsonObject.getString("user_sex"));
        }
        if (jsonObject.has("user_picture_url")) {
            info.setUser_picture_url(jsonObject.getString("user_picture_url"));
        }
        if (jsonObject.has("user_money")) {
            info.setUser_money(jsonObject.getDouble("user_money"));
        }

        return info;
    }

    /**
     * 得到购物车列表，注意在非UI线程调用此接口
     * 发生网络错误时抛出异常
     */
    public List<GoodsBean> getCartGoodsList() throws Exception {
        final List<GoodsBean> cartShopList = new ArrayList<>();

        String userId = null;
        JSONObject user = new JSONObject();
        user.put("userId", "2");
        userId = user.toString();

        String result = HttpUtil.sentHttpPost("http://192.168.1.105:8080/TKBaas/cart/app/getUserProduct"
        , userId);
        Log.d("cartGet",result);
        JSONObject cartJson = new JSONObject(result);
        JSONArray storeArray = (JSONArray) cartJson.get("sellerItem");
        for (int i = 0; i < storeArray.length(); i++) {
            JSONObject storeItem = (JSONObject) storeArray.get(i);
            GoodsBean shopItem = new GoodsBean();
            shopItem.setGoodsId(storeItem.getString("id"));
            shopItem.setGoodsName(storeItem.getString("shopName"));
            shopItem.setGoodsPrice(0);
            cartShopList.add(shopItem);
            JSONArray goodsArray = (JSONArray) storeItem.get("proItem");
            for (int j = 0; j < goodsArray.length(); j++) {
                JSONObject goods = (JSONObject) goodsArray.get(j);
                GoodsBean goodsItem = new GoodsBean();
                JSONObject product = (JSONObject) goods.get("product");
                goodsItem.setGoodsId(product.getString("id"));
                goodsItem.setGoodsName(product.getString("name"));
                goodsItem.setGoodsPrice(product.getDouble("price"));
                goodsItem.setGoodsIcon(product.getString("picture"));
                goodsItem.setGoodsNum(Integer.parseInt(goods.getString("num")));
                cartShopList.add(goodsItem);
            }

        }

        return cartShopList;
    }

    /**
     * 提交购物车信息（用户id,商品id以及对应的数量），在非UI线程调用此接口
     * 发生网络错误时抛出异常
     */
    public boolean postCartInfo(List<GoodsBean>cartGoodsList,String postUrl) throws Exception {
        JSONObject cartInfo = new JSONObject();
        //用户ID
        cartInfo.put("userId","2");
        //各商品ID
        JSONArray productId = new JSONArray();
        for(GoodsBean cartGoodsItem:cartGoodsList){
            if(cartGoodsItem.getGoodsPrice() != 0){
                productId.put(cartGoodsItem.getGoodsId());
                Log.d("productID",cartGoodsItem.getGoodsId());
            }
        }
        cartInfo.put("productId", productId);
        //各商品对应的数量
        JSONArray num = new JSONArray();
        for(GoodsBean cartGoodsItem:cartGoodsList){
            if(cartGoodsItem.getGoodsPrice() != 0){
                num.put(cartGoodsItem.getGoodsNum());
                Log.d(" num",String.valueOf(cartGoodsItem.getGoodsNum()));
            }
        }
        cartInfo.put("num", num);
        //发送到服务器
        String result = HttpUtil.sentHttpPost(postUrl, cartInfo.toString());
        JSONObject cartJson = new JSONObject(result);
        boolean postResult = cartJson.getBoolean("result");
        return postResult;

    }




}
