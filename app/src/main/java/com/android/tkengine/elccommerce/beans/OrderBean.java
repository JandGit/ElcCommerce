package com.android.tkengine.elccommerce.beans;

/**
 * 储存订单信息
 */
public class OrderBean {
    public static class Product{
        String id; //商品id
        String name; //商品名称
        String type; //商品类型
        String city; //所属地区
        String store; //所属商店
        double price; //商品价格
        String description; //商品描述
        int  sales; //商品销量
        String ifDeleted;//
        String picture; //商品图片
    }
    public static class OrderItem{
        String id; //商品订单项目id
        int num; //数量
        Product product; //具体商品
    }

    String boughtDate; //订单日期
    String id; //订单ID
    double money; //订单总价
    String sellerId; //订单商家Id
    String shopName; //商家名称
    String userId; //订单用户ID
    String state; //订单状态
    OrderItem[] proItems; //订单item

}
