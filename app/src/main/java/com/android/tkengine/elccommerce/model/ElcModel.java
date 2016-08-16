package com.android.tkengine.elccommerce.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.tkengine.elccommerce.R;
import com.android.tkengine.elccommerce.beans.commentsBean;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.GoodsAddressBean;
import com.android.tkengine.elccommerce.beans.GoodsBean;
import com.android.tkengine.elccommerce.beans.GoodsDetailsBean;
import com.android.tkengine.elccommerce.beans.HomePageItemBean;
import com.android.tkengine.elccommerce.beans.OrderBean;
import com.android.tkengine.elccommerce.beans.StoreBean;
import com.android.tkengine.elccommerce.beans.StoreDetailsBean;
import com.android.tkengine.elccommerce.beans.SearchGoodsBean;
import com.android.tkengine.elccommerce.beans.UserInfoBean;
import com.android.tkengine.elccommerce.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.codehaus.plexus.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class ElcModel {

    Context mContext;

    public ElcModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 注册新用户
     * @param userPhone 用户名
     * @param password 密码
     * @return
     */
    public boolean signUp(String userPhone, String password) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone_number", userPhone);
        jsonObject.put("user_password", password);
        String params = jsonObject.toString();
        Log.i("Model:signUp", "向服务器发送数据：" + params);
        String result = HttpUtil.sentHttpPost(Constants.SERVER_SIGNUP, params);
        Log.i("Model:signUp", "服务器返回：" + result);
        jsonObject = new JSONObject(result);

        return jsonObject.getBoolean("result");
    }

    /**
     * 获取订单
     * 网络连接错误时抛出异常
     * @param userId 用户Id
     * @param page 0为所有订单，1为待付款，2为待发货，3为待收货，4为待评价
     * @param from 用于订单分块显示，指定加载订单的起始位置
     * @return 若无信息返回null,其他情况为数组
     */
    public OrderBean[] getOrder(String userId, int page, int from) throws Exception{
        userId = "1";
        String url = Constants.SERVER_GETORDER_SPC;
        String params;
        switch (page){
            case 0:
                url = Constants.SERVER_GETORDER_ALL;
                params = "{\"userId\":\"" + userId + "\", \"currentPage\":" + from + ",\"pageSize\":30}";
                break;
            case 1:
                params = "{\"userId\":\"" + userId + "\", \"currentPage\":" + from + ",\"pageSize\":30, \"state\":\"unpaid\"}";
                break;
            case 2:
                params = "{\"userId\":\"" + userId + "\", \"currentPage\":" + from + ",\"pageSize\":30, \"state\":\"unsent\"}";
                break;
            case 3:
                params = "{\"userId\":\"" + userId + "\", \"currentPage\":" + from + ",\"pageSize\":30, \"state\":\"unreceived\"}";
                break;
            case 4:
                params = "{\"userId\":\"" + userId + "\", \"currentPage\":" + from + ",\"pageSize\":30, \"state\":\"uncomment\"}";
                break;
            default:
                throw new Exception("错误的page参数");
        }
        Log.i("ElcModel:", "向服务器:" + url + "发送POST请求，\n参数：" + params);
        String result = HttpUtil.sentHttpPost(url, params);
        Log.i("ElcModel:", "服务器返回数据：" + result);
        if (result != null && !result.isEmpty()) {
            JSONObject jsonObject = new JSONObject(result);
            result = jsonObject.getJSONArray("result").toString();
            Log.i("ElcModel:", "开始解析JSON：" + result);
            if (result != null && !result.isEmpty()) {
                Gson gson = new Gson();
                Type type = new TypeToken<OrderBean[]>(){}.getType();

                return gson.fromJson(result, type);
            }
        }
        return null;
    }

    public boolean setPassword(String userId, String oldpassword, String newpassword) throws JSONException, IOException {
        if(null == userId || userId.isEmpty()){
            return false;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", userId);
        jsonObject.put("old_password", oldpassword);
        jsonObject.put("new_password", newpassword);
        String params = jsonObject.toString();
        Log.i("Model:setPassword", "向服务器发送数据：" + params);
        String result = HttpUtil.sentHttpPost(Constants.SERVER_CHANGE_PASSWORD, params);
        Log.i("Model:setPassword", "服务器返回：" + result);
        jsonObject = new JSONObject(result);

        return jsonObject.getBoolean("result");
    }

    /**
     * 修改个人信息
     * @param userId 用户ID
     * @param icon 修改的头像
     * @param sex 修改的性别
     * @param userName  修改的用户名
     * @return 成功返回true,参数错误返回false
     * @throws JSONException 请求参数错误
     * @throws IOException  网络错误
     */
    public boolean setUserInfo(String userId, File icon, String sex, String userName) throws JSONException, IOException {
        if(null == userId || userId.isEmpty()){
            return false;
        }
        String iconJson;

        if (icon != null) {
            FileInputStream  input = new FileInputStream(icon);
            ByteArrayOutputStream baas = new ByteArrayOutputStream((int)icon.length());
            int len;
            byte[] b = new byte[1024];
            while((len = input.read(b)) != -1){
                baas.write(b, 0, len);
            }
            byte[] data = baas.toByteArray();
            ByteArrayOutputStream byteZip = new ByteArrayOutputStream();
            GZIPOutputStream zip = new GZIPOutputStream(byteZip);
            zip.write(data);
            zip.finish();
            zip.flush();
            //压缩后的二进制流
            byte[] zipData = byteZip.toByteArray();
            iconJson = new String(Base64.encodeBase64(zipData));
        }
        else {
            iconJson = "0";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", userId);
        jsonObject.put("picture_name", userId + ".jpg");
        jsonObject.put("picture_str", iconJson);
        jsonObject.put("user_sex", sex);
        jsonObject.put("user_name", userName);
        String params = jsonObject.toString();
        Log.i("Model:setUserInfo", "向服务器发送数据：" + params);
        String result = HttpUtil.sentHttpPost(Constants.SERVER_CHANGE_USERINFO, params);
        Log.i("Model:setUserInfo", "服务器返回：" + result);
        jsonObject = new JSONObject(result);

        return jsonObject.getBoolean("result");
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
        Bitmap[] imgs = new Bitmap[3];
        imgs[0] = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.advertise1);
        imgs[1] = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.advertise2);
        imgs[2] = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.advertise3);
        headitem.data.put("AD", imgs);
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
        result = HttpUtil.sentHttpPost(Constants.SERVER_ADDRESS_LOGIN, params);
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
    public UserInfoBean getUserInfo(String userId) throws JSONException, IOException {
        if (null == userId || userId.isEmpty()) {
            return null;
        }
        UserInfoBean info = new UserInfoBean();
        info.setUserId(userId);
        String params, result;
        JSONObject jsonObject;
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
        String result = HttpUtil.sentHttpPost(Constants.SERVER_PRODUCTDETAILS, productId);
        Gson gson = new Gson();
        //反射获取含NewsAllBean信息的类型信息
        java.lang.reflect.Type type = new TypeToken<GoodsDetailsBean>() {}.getType();
        goodsDetailsList = gson.fromJson(result, type);
        return goodsDetailsList;
    }

    /**
     * 得到评论详情，注意在非UI线程调用此接口
     * 发生网络错误时抛出异常
     */

    public List<commentsBean.ResultBean> getCommentsDetails(String productID) throws Exception {
        List<commentsBean.ResultBean> resultBeen = new ArrayList<commentsBean.ResultBean>();
        String productId = null;
        JSONObject product = new JSONObject();
        product.put("productId", productID);
        product.put("pageSize", "15");
        product.put("currentPage", "1");
        productId = product.toString();
        String result = HttpUtil.sentHttpPost(Constants.SERVER_COMMENTS, productId);
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<commentsBean>() {}.getType();
        commentsBean commentsBean = gson.fromJson(result, type);
        resultBeen = commentsBean.getResult();
        return resultBeen;
    }

    /**
     * 添加到购物车，注意在非UI线程调用此接口
     * 发生网络错误时抛出异常
     */

    public boolean addInCart(String productID, String num) throws Exception {
        String productId = null;
        JSONObject product = new JSONObject();
        product.put("productId", productID);
        product.put("userId", "402891815678675c015678717688014a");
        product.put("num", num);
        productId = product.toString();
        String result = HttpUtil.sentHttpPost(Constants.SERVER_ADDCART, productId);
        JSONObject addCartResult = new JSONObject(result);
        boolean callback= addCartResult.getBoolean("result");
        return callback;
    }

    /**
     * 得到商铺信息，注意在非UI线程调用此接口
     * 发生网络错误时抛出异常
     */

    public StoreDetailsBean getStore(String storeId, String currentPage, String pageSize) throws Exception {
        StoreDetailsBean storeDetailsBean = new StoreDetailsBean();
        String productId = null;
        JSONObject product = new JSONObject();
        product.put("seller_id", storeId);
        product.put("currentPage", currentPage);
        product.put("pageSize", pageSize);
        productId = product.toString();
        String result = HttpUtil.sentHttpPost(Constants.SERVER_STOREDETAILS, productId);
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<StoreDetailsBean>() {}.getType();
        storeDetailsBean = gson.fromJson(result, type);
        return storeDetailsBean;
    }

    /**
     * 得到商铺列表，注意在非UI线程调用此接口
     * 发生网络错误时抛出异常
     */

    public  List <StoreBean.ResultBean>  getStoreList (String currentPage, String pageSize, String sort) throws Exception {
        List <StoreBean.ResultBean> resultBean = new ArrayList<StoreBean.ResultBean>();
        String productId = null;
        JSONObject product = new JSONObject();
        JSONObject page = new JSONObject();
        page.put("currentPage",currentPage);
        page.put("pageSize",pageSize);
        product.put("key", "");
        product.put("sort", sort);
        product.put("page",page);
        productId = product.toString();
        String result = HttpUtil.sentHttpPost(Constants.SERVER_STORE, productId);
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<StoreBean>() {}.getType();
        StoreBean storeBean = gson.fromJson(result, type);
        resultBean = storeBean.getResult();
        return resultBean;
    }

    /**
     * 得到购物车列表，注意在非UI线程调用此接口
     * 发生网络错误时抛出异常
     */
    public List<GoodsBean> getCartGoodsList() throws Exception {
        final List<GoodsBean> cartShopList = new ArrayList<>();

        String userId = null;
        JSONObject user = new JSONObject();
        user.put("userId", "402891815678675c015678717688014a");
        userId = user.toString();

        String result = HttpUtil.sentHttpPost(Constants.SERVER_GET_CART, userId);
        Log.d("cartGet",result);
        JSONObject cartJson = new JSONObject(result);
        JSONArray storeArray = (JSONArray) cartJson.get("sellerItem");
        for (int i = 0; i < storeArray.length(); i++) {
            JSONObject storeItem = (JSONObject) storeArray.get(i);
            GoodsBean shopItem = new GoodsBean();
            shopItem.setId(storeItem.getString("sellerId"));
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
        cartInfo.put("userId","402891815678675c015678717688014a");
        //各商品ID
        JSONArray productId = new JSONArray();
        for(GoodsBean cartGoodsItem:cartGoodsList){
            if(cartGoodsItem.getGoodsPrice() != 0){
                productId.put(cartGoodsItem.getGoodsId());
                Log.d("shop",cartGoodsItem.getGoodsId());
            }
        }
        cartInfo.put("productId", productId);
        //各商品对应的数量
        JSONArray num = new JSONArray();
        for(GoodsBean cartGoodsItem:cartGoodsList){
            if(cartGoodsItem.getGoodsPrice() != 0){
                num.put(cartGoodsItem.getGoodsNum());
                Log.d("shop",String.valueOf(cartGoodsItem.getGoodsNum()));
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
     * 提交订单信息（用户id,收货人地址id，订单总额，商品goodsId以及商品id），在非UI线程调用此接口
     * 发生网络错误时抛出异常
     */
    public boolean postOrderInfo(List<GoodsBean> receiverGoodsList,String addressId,String moneyAmount) throws Exception {
        JSONObject goodsInfo = new JSONObject();
        //用户ID
        goodsInfo.put("userId","2");
        //收货人地址
        goodsInfo.put("addressId",addressId);
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
        String result = HttpUtil.sentHttpPost(Constants.SERVER_POST_ORDER, goodsInfo.toString());
        Log.d("json",result);
        JSONObject cartJson = new JSONObject(result);
        boolean postResult = cartJson.getBoolean("result");
        return postResult;
    }

    /**
     * 获取收货地址
     * 发生网络错误时抛出异常
     */
    public List<GoodsAddressBean.ResultBean> getGoodsAddressList()throws Exception{
        JSONObject json = new JSONObject();
        json.put("userId", "2");
        json.put("key","");
        json.put("currentPage","1");
        json.put("pageSize","30");

        String result = HttpUtil.sentHttpPost(Constants.SERVER_GET_ADDRESSINFO,json.toString());
        Log.d("address",result);
        Gson gson = new Gson();
        GoodsAddressBean goodsAddressList = gson.fromJson(result,new TypeToken<GoodsAddressBean>(){}.getType());
        List<GoodsAddressBean.ResultBean> resultList = goodsAddressList.getResult();
        return resultList;
    }

    /**
     * 新添加收货地址提交给服务器(用户id,GoodsAddressBean.ResultBean中各成员）
     * 发生网络错误时抛出异常
     */
    public boolean postAddressInfo(String userId,GoodsAddressBean.ResultBean addressBean) throws Exception{
        JSONObject info = new JSONObject();
        info.put("userId",userId);
        JSONObject address = new JSONObject();
        address.put("receiver",addressBean.getReceiver());
        address.put("phone",addressBean.getPhone());
        address.put("province",addressBean.getProvince());
        address.put("city",addressBean.getCity());
        address.put("countyTown",addressBean.getCountyTown());
        address.put("street",addressBean.getStreet());
        address.put("detailsAddress",addressBean.getDetailsAddress());
        address.put("defaultAddress",addressBean.isDefaultAddress());
        info.put("address",address);
        String result = HttpUtil.sentHttpPost(Constants.SERVER_POST_NEWADDRESS,info.toString());
        JSONObject resultJson = new JSONObject(result);
        boolean postResult = resultJson.getBoolean("result");
        Log.d("newaddress",String.valueOf(postResult));
        return postResult;

    }

    /**
     * 用户所删除的收货地址提交给服务器(用户id,地址id）
     * 发生网络错误时抛出异常
     */
    public boolean postDeletedAddressInfo(String userId,String addressId) throws Exception{
        JSONObject info = new JSONObject();
        info.put("userId",userId);
        info.put("addressId",addressId);
        String result = HttpUtil.sentHttpPost(Constants.SERVER_POST_DELETEDADDRESS,info.toString());
        JSONObject resultJson = new JSONObject(result);
        boolean postResult = resultJson.getBoolean("result");
        return postResult;

    }


    /**
     * 编辑的收货地址提交给服务器(用户id,GoodsAddressBean.ResultBean中各成员）
     * 发生网络错误时抛出异常
     */
    public boolean postEditAddressInfo(String userId,GoodsAddressBean.ResultBean addressBean) throws Exception{
        JSONObject info = new JSONObject();
        info.put("userId",userId);
        JSONObject address = new JSONObject();
        address.put("id",addressBean.getId());
        address.put("receiver",addressBean.getReceiver());
        address.put("phone",addressBean.getPhone());
        address.put("province",addressBean.getProvince());
        address.put("city",addressBean.getCity());
        info.put("address",addressBean.getCity());
        address.put("countyTown",addressBean.getCountyTown());
        address.put("street",addressBean.getStreet());
        address.put("detailsAddress",addressBean.getDetailsAddress());
        address.put("defaultAddress",addressBean.isDefaultAddress());
        Log.d("default",String.valueOf(addressBean.isDefaultAddress()));
        info.put("address",address);
        String result = HttpUtil.sentHttpPost(Constants.SERVER_POST_EDITADDRESS,info.toString());
        JSONObject resultJson = new JSONObject(result);
        boolean postResult = resultJson.getBoolean("result");
        Log.d("newaddress",String.valueOf(postResult));
        return postResult;

    }

    /**
     * 搜索要求（搜索关键字key，排序方式sort，价格区间范围（left，right），当前页currentPage，每页的商品数pageSize）提交给服务器
     * 发生网络错误时抛出异常
     */
    public List<SearchGoodsBean.ProductListBean> getSearchResult(String key,String sort,String left,String right,int currentPage,int pageSize)throws Exception{
        JSONObject searchInfo = new JSONObject();
        searchInfo.put("key",key);
        searchInfo.put("sort",sort);
        Log.d("sort",sort);
        searchInfo.put("left",left);
        Log.d("left",left);
        searchInfo.put("right",right);
        Log.d("right",right);
        searchInfo.put("currentPage",currentPage);
        searchInfo.put("pageSize",pageSize);
        Log.d("pageSize",String.valueOf(pageSize));
        String result = HttpUtil.sentHttpPost(Constants.SERVER_GET_GOODS,searchInfo.toString());
        Log.d("search",result);
        Gson gson = new Gson();
        SearchGoodsBean searchGoodsList = gson.fromJson(result,new TypeToken<SearchGoodsBean>(){}.getType());
        List<SearchGoodsBean.ProductListBean> resultList = searchGoodsList.getProduct_list();
        return resultList;

    }



}
