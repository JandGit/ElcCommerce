package com.android.tkengine.elccommerce.beans;

import java.util.List;

/**
 * Created by 陈嘉shuo on 2016/8/14.
 */
public class SearchGoodsBean {

    /**
     * product_id : 402891815678675c0156786de2200000
     * product_name : 香蕉
     * product_city : 西域果情
     * product_type : 香蕉
     * product_description : 爱是刘德华
     * product_price : 12.34
     * product_sales : 0
     * product_store : 1223
     * picture_url : http://192.168.1.102:9999/TKBaas/imgs/b24cc81d-b8eb-4b9e-b2cd-59f54093043b.jpg
     */

    private List<ProductListBean> product_list;

    public List<ProductListBean> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<ProductListBean> product_list) {
        this.product_list = product_list;
    }

    public static class ProductListBean {
        private String product_id;
        private String product_name;
        private String product_city;
        private String product_type;
        private String product_description;
        private double product_price;
        private int product_sales;
        private int product_store;
        private String picture_url;

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_city() {
            return product_city;
        }

        public void setProduct_city(String product_city) {
            this.product_city = product_city;
        }

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        public String getProduct_description() {
            return product_description;
        }

        public void setProduct_description(String product_description) {
            this.product_description = product_description;
        }

        public double getProduct_price() {
            return product_price;
        }

        public void setProduct_price(double product_price) {
            this.product_price = product_price;
        }

        public int getProduct_sales() {
            return product_sales;
        }

        public void setProduct_sales(int product_sales) {
            this.product_sales = product_sales;
        }

        public int getProduct_store() {
            return product_store;
        }

        public void setProduct_store(int product_store) {
            this.product_store = product_store;
        }

        public String getPicture_url() {
            return picture_url;
        }

        public void setPicture_url(String picture_url) {
            this.picture_url = picture_url;
        }
    }
}
