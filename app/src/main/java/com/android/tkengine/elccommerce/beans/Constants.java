package com.android.tkengine.elccommerce.beans;

/**
 * 用于储存一些常量
 */
public class Constants {
    /**
     * 当前用户登录信息的SharePreference文件名
     * 存储格式：
     * "isLogin"   -----当前用户是否登录，boolean
     "UserPhone" ------用户手机号码，即登录账户,String
     "password" -------用户登录密码, String
     "UserName" -----------用户名,String
     "UserId" ------------用户ID，String
     "UserIcon"  -------------用户头像URL
     "UserSex"   -------------用户性别,String
     "UserMoney",  -----------用户钱包，float
     */
    public static final String SP_LOGIN_USERINFO = "LoginInfo";

    /**
     * 后台服务器地址
     */
    public static final String SERVER_ADDRESS = "http://qq84429406.eicp.net/TKBaas/";
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
     * 获取商品详情
     */
    public static final String SERVER_PRODUCTDETAILS = SERVER_ADDRESS + "product/app/getProduct";

    /**
     * 添加商品到购物车
     */
    public static final String SERVER_ADDCART = SERVER_ADDRESS + "cart/app/addInCart";

    /**
     * 获取评论详情
     */
    public static final String SERVER_COMMENTS = SERVER_ADDRESS + "comment/app/getProductComment";

    /**
     * 获取商店详情
     */
    public static final String SERVER_STOREDETAILS = SERVER_ADDRESS + "product/app/getSellerProductList";

    /**
     * 获取商店列表
     */
    public static final String SERVER_STORE = SERVER_ADDRESS + "seller/app/queryList";

    /**
     * 订单接口
     * 访问参数json数据格式,{"userId":?, "currentPage":?, "pageSize":?}
     */
    public static final String SERVER_GETORDER_ALL = SERVER_ADDRESS + "order/app/userOrder";
    /**
     * 订单接口
     * 访问参数Json格式,{"userId":?, "currentPage":?, "pageSize":?, "state":"unpaid"|"unsent"|"unreceived"|"uncomment"}
     */
    public static final String SERVER_GETORDER_SPC = SERVER_ADDRESS + "order/app/searchUserOrder";
}
