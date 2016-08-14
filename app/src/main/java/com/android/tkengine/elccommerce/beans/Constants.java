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
    /**
     * 用户充值钱包接口
     * Json格式:
     * user_idd = 用户ID,String
     * user_password = 密码,String
     * charge_money = 重置金额, double
     * 返回数据：
     * charge_state = boolean, 成功为true
     */
    public static final String SERVER_CHARGEMONEY = SERVER_ADDRESS + "user/app/changeMoney";
    /**
     * 用户信息修改
     * Json格式:
     * user_id = 用户ID,String
     * picture_name = 头像文件名
     * picture_str = 头像byte数据字符串
     * user_sex = 用户性别,String
     * user_name = 用户名,String
     *
     * 返回格式：
     * result = boolean
     */
    public static final String SERVER_CHANGE_USERINFO = SERVER_ADDRESS + "user/app/changeInfo";
    /**
     * 修改用户密码
     * Json格式:
     * user_id = 用户Id， String
     * old_password = 用户旧密码, String
     * new_password = 用户新密码,String
     *
     * 返回格式：
     * result = boolean
     */
    public static final String SERVER_CHANGE_PASSWORD = SERVER_ADDRESS + "user/app/changePassword";
}
