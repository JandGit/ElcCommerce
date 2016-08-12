package com.android.tkengine.elccommerce.beans;

import java.util.List;

/**
 * 储存订单信息
 */
public class OrderBean {
    /**
     * 购买时间
     */
    public BoughtDateBean boughtDate;
    /**
     * 订单标识ID
     */
    public String id;
    /**
     * 订单金额
     */
    public int money;
    /**
     * 商家标识ID
     */
    public String sellerId;
    /**
     * 商家名称
     */
    public String shopName;
    /**
     * 用户标识ID
     */
    public String userId;
    /**
     * 订单状态
     */
    public String state;
    /**
     * 订单商品List
     */
    public List<ProItemsBean> proItems;

    public static class BoughtDateBean {
        public int date;
        public int day;
        public int hours;
        public int minutes;
        public int month;
        private int nanos;
        public int seconds;
        private long time;
        private int timezoneOffset;
        public int year;
    }

    public static class ProItemsBean {
        private String id;
        private int num;
        private ProductBean product;

        public static class ProductBean {
            private String id;
            private String name;
            private String type;
            private String city;
            private int store;
            private int price;
            private String description;
            private int sales;
            private boolean ifDeleted;
            private String picture;

        }
    }
}
