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
    //public static final String SERVER_ADDRESS = "http://192.168.1.118:8080/TKBaas/";
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
     * user_id = 用户ID,String
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
    /**
     * 注册用户
     */
    public static final String SERVER_SIGNUP = SERVER_ADDRESS + "user/app/regist";
    public static final String SERVER_GET_CART = SERVER_ADDRESS + "cart/app/getUserProduct";
    /**
     * 删除购物车商品
     * 调用时向服务器Post数据:user_id = XXX
     */
    public static final String SERVER_DELETE_CARTGOODS = SERVER_ADDRESS + "cart/app/delIncart";
    /**
     * 商品加入购物车
     * 调用时向服务器Post数据:user_id = XXX
     */
    public static final String SERVER_ADD_CARTGOODS = SERVER_ADDRESS + "cart/app/addInCart";
    /**
     * 购物车页面退出时，提交商品数量更改的信息
     * 调用时向服务器Post数据:user_id = XXX
     */
    public static final String SERVER_UPDATE_CART = SERVER_ADDRESS + "cart/app/updateCart";
    /**
     * 从服务器获取用户设置的收货地址
     * 调用时向服务器Post数据:user_id = XXX
     */
    public static final String SERVER_GET_ADDRESSINFO = SERVER_ADDRESS + "address/app/getList";
    /**
     *调用第三方API，获取全国各省各市各县
     */
    public static final String HTTP_GET_POSITIONINFO = "http://www.weather.com.cn/data/list3/city";
    /**
     * 提交用户新添加的地址
     * 调用时向服务器Post数据:user_id = XXX
     */
    public static final String SERVER_POST_NEWADDRESS = SERVER_ADDRESS + "address/app/add";
    /**
     * 提交用户所删除地址的
     * 调用时向服务器Post数据:user_id = XXX
     */
    public static final String SERVER_POST_DELETEDADDRESS = SERVER_ADDRESS + "address/app/delete";
    /**
     * 提交用户所编辑地址的
     * 调用时向服务器Post数据:user_id = XXX
     */
    public static final String SERVER_POST_EDITADDRESS = SERVER_ADDRESS + "address/app/update";
    /**
     * 搜索获取商品
     * 调用时向服务器Post数据:user_id = XXX
     */
    public static final String SERVER_GET_GOODS = SERVER_ADDRESS + "product/app/getProductListByAll";
    /**
     * 购物车结算，确认订单
     * 调用时向服务器Post数据:user_id = XXX
     */
    public static final String SERVER_POST_ORDER = SERVER_ADDRESS + "order/app/submitOrder";
    /**
     * 钱包充值
     * 调用时向服务器Post数据:user_id = XXX
     */
    public static final String SERVER_POST_MONEY = SERVER_ADDRESS + "user/app/changeMoney";






}
