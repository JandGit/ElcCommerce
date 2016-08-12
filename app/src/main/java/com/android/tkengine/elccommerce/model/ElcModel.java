package com.android.tkengine.elccommerce.model;


import android.content.Context;
import android.util.Log;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.GoodsBean;
import com.android.tkengine.elccommerce.beans.GoodsDetailsBean;
import com.android.tkengine.elccommerce.beans.HomePageItemBean;
import com.android.tkengine.elccommerce.beans.UserInfoBean;
import com.android.tkengine.elccommerce.utils.HttpCallbackListener;
import com.android.tkengine.elccommerce.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ElcModel {

    Context mContext;

    public ElcModel(Context mContext) {
        this.mContext = mContext;
    }






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
        allData.add(headitem);
        allData.addAll(getGoods(0));

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

        Log.i("EclModel:", "发送服务器请求,获取首页\n" + params);
        String response = HttpUtil.sentHttpPost(Constants.SERVER_HOMEPAGE, params);
        Log.i("EclModel:", "服务器返回：" + response);
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jArray = jsonObject.getJSONArray("product_list");
        Log.i("EclModel:", "开始解析商品列表, 数目:" + jArray.length());
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
                Log.i("EclModel:", "解析第" + i + "个商品：，名称:" + item.data.get("name1") +
                        "\nid:" + item.data.get("id1") + "\n图片：" + item.data.get("icon1"));
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
                Log.i("EclModel:", "解析第" + i + "个商品：，名称:" + item.data.get("name2") +
                        "\nid:" + item.data.get("id2") + "\n图片：" + item.data.get("icon2"));
            }
        }

        Log.i("EclModel:", "解析完毕，返回数据,数据量" + allData.size());
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
     * 得到商品详情，注意在非UI线程调用此接口
     * 发生网络错误时抛出异常
     */

    public GoodsDetailsBean getGoodsDetails( String goodsId) throws Exception {
        GoodsDetailsBean goodsDetailsList = new GoodsDetailsBean();
        String productId = null;
        JSONObject product = new JSONObject();
        product.put("product_id", goodsId);
        productId = product.toString();
        String result = HttpUtil.sentHttpPost("http://192.168.1.101:8080/TKBaas/product/app/getProduct", productId);
        Gson gson = new Gson();
        //反射获取含NewsAllBean信息的类型信息
        java.lang.reflect.Type type = new TypeToken<GoodsDetailsBean>() {}.getType();
        goodsDetailsList = gson.fromJson(result, type);
        Log.d("getGoodsDetails: ", goodsDetailsList.getProduct_name());
        return goodsDetailsList;
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
                goodsItem.setId(goods.getString("id"));
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

    /**
     * 提交订单信息（用户id,收货人地址，订单总额，商品goodsId以及商品id），在非UI线程调用此接口
     * 发生网络错误时抛出异常
     */
    public boolean postOrderInfo(List<GoodsBean> receiverGoodsList,String address,String moneyAmount,String postUrl) throws Exception {
        JSONObject goodsInfo = new JSONObject();
        //用户ID
        goodsInfo.put("userId","2");
        //收货人地址
        goodsInfo.put("addressId",address);
        //订单总额
        goodsInfo.put("totalMoney",moneyAmount);
        //各商品goodsId
        JSONArray goodsIds = new JSONArray();
        for(GoodsBean cartGoodsItem:receiverGoodsList){
            JSONObject goodsId = new JSONObject();
            goodsId.put("proId",cartGoodsItem.getGoodsId());
            goodsIds.put(goodsId);
        }
        goodsInfo.put("proIds", goodsIds);
        //各商品id
        JSONArray ids = new JSONArray();
        for(GoodsBean cartGoodsItem:receiverGoodsList){
            JSONObject id = new JSONObject();
            id.put("proItemId",cartGoodsItem.getId());
            ids.put(id);
        }
        goodsInfo.put("proItemIds", ids);
        //发送到服务器
        String result = HttpUtil.sentHttpPost(postUrl, goodsInfo.toString());
        Log.d("json",result);
        JSONObject cartJson = new JSONObject(result);
        boolean postResult = cartJson.getBoolean("result");
        return postResult;
    }




}
