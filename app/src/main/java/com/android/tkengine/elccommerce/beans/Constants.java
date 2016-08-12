package com.android.tkengine.elccommerce.beans;

/**
 * 用于储存一些常量
 */
public class Constants {
    /**
     * 后台服务器地址
     */
    public static final String SERVER_ADDRESS = "http://192.168.1.112:8080/TKBaas/";
    /**
     * 后台用户登录接口
     */
    public static final String SERVER_ADDRESS_LOGIN = SERVER_ADDRESS + "user/app/login";
    /**
     * 获取用户信息的接口
     */
    public static final String SERVER_GETUSERINFO = SERVER_ADDRESS + "user/app/getInfo";
    /**
     * 获取用户购物车
     * 调用时向服务器Post数据:user_id = XXX
     */
    public static final String SERVER_GETCART = SERVER_ADDRESS + "cart/app/getUserProduct";

    /**
     * 获取首页商品列表
     */
    public static final String SERVER_HOMEPAGE = SERVER_ADDRESS + "product/app/home";

    /**
     * 订单接口
     * 访问参数json数据格式,{"userId":?, "currentPage":?, "pageSize":?}
     */
    public static final String SERVER_GETORDER = SERVER_ADDRESS + "/order/app/userOrder";
}
