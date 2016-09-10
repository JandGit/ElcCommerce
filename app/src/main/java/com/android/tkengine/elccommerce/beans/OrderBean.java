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
    public double money;
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
        public String id;
        public int num;
        public ProductBean product;

        public static class ProductBean {
            public String id;
            public String name;
            public String type;
            public String city;
            public int store;
            public double price;
            public String description;
            public int sales;
            public boolean ifDeleted;
            public String picture;

        }
    }
}
