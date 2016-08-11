package com.android.tkengine.elccommerce.beans;

/**
 * 用于储存一些常量
 */
public class Constants {
    /**
     * 后台服务器地址
     */
    public static final String SERVER_ADDRESS = "http://192.168.1.108:8080/TKBaas/";
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
}
