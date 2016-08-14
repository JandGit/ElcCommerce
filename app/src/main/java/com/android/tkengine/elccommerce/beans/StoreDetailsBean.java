package com.android.tkengine.elccommerce.beans;import java.util.List;/** * Created by FangYu on 2016/8/13. */public class StoreDetailsBean {    /**     * currentPage : 1     * pageSize : 10     * totalNum : 37     * totalPage : 4     * product_list : [{"product_id":"402881e65681c0bc015681c1e3bd0000","product_name":"刘备","product_city":"west","product_type":"苹果","product_description":"3245第三方二十投放的发生过","product_price":33,"product_sales":0,"product_store":33,"picture_url":"http://qq84429406.eicp.net/TKBaas/imgs/8d400370-617f-4c66-9e84-79704b71bd7c.jpg"},{"product_id":"402881e65681c40a015681c4765f0000","product_name":"留给","product_city":"west","product_type":"苹果","product_description":"的广泛地三个地方","product_price":0.2,"product_sales":0,"product_store":2,"picture_url":"http://qq84429406.eicp.net/TKBaas/imgs/f7871cb9-666b-4358-a7cf-1ea29dd0dc9c.jpg"},{"product_id":"402891815678675c0156786de2200000","product_name":"香蕉","product_city":"西域果情","product_type":"香蕉","product_description":"爱是刘德华","product_price":12.34,"product_sales":0,"product_store":1223,"picture_url":"http://qq84429406.eicp.net/TKBaas/imgs/1f764e1f-5fdd-407f-b3b4-62b34d22ecb2.jpg"},{"product_id":"402891815678675c0156786df3cd000b","product_name":"香蕉","product_city":"西域果情","product_type":"香蕉","product_description":"爱是刘德华","product_price":12.34,"product_sales":0,"product_store":1223,"picture_url":"http://qq84429406.eicp.net/TKBaas/imgs/60922f87-af78-4278-ae95-744e4c5377c9.jpg"},{"product_id":"402891815678675c0156786e00a30016","product_name":"香蕉","product_city":"西域果情","product_type":"香蕉","product_description":"爱是刘德华","product_price":12.34,"product_sales":0,"product_store":1223,"picture_url":"http://qq84429406.eicp.net/TKBaas/imgs/fb32a3b5-5fc4-450b-9cfe-e5fb5645ce0a.jpg"},{"product_id":"402891815678675c0156786e09950021","product_name":"香蕉","product_city":"西域果情","product_type":"香蕉","product_description":"爱是刘德华","product_price":12.34,"product_sales":0,"product_store":1223,"picture_url":"http://qq84429406.eicp.net/TKBaas/imgs/054fe7df-fb52-48f2-9fa4-1b22c2e15ff9.jpg"},{"product_id":"402891815678675c0156786e12d2002c","product_name":"香蕉","product_city":"西域果情","product_type":"香蕉","product_description":"爱是刘德华","product_price":12.34,"product_sales":0,"product_store":1223,"picture_url":"http://qq84429406.eicp.net/TKBaas/imgs/162992d3-aabd-4c4d-a792-25601ba9e5b6.jpg"},{"product_id":"402891815678675c0156786e1c980037","product_name":"香蕉，香蕉 进口","product_city":"西域果情","product_type":"香蕉","product_description":"这个是商品描述，这个是商品描述，这个是商品描述，这个是商品描述。","product_price":12.34,"product_sales":888,"product_store":1223,"picture_url":"http://qq84429406.eicp.net/TKBaas/imgs/9d77c464-61f0-4a33-9dcf-b1e7c4204030.jpg"},{"product_id":"402891815678675c0156786e24a60042","product_name":"香蕉，水果，热带 热销","product_city":"西域果情","product_type":"香蕉","product_description":"商品描述，爱是刘德华","product_price":12.34,"product_sales":8,"product_store":158,"picture_url":"http://qq84429406.eicp.net/TKBaas/imgs/403e20b9-1359-411d-b8b3-8d84098f8b29.jpg"},{"product_id":"402891815678675c0156786e2ff9004d","product_name":"香蕉","product_city":"西域果情","product_type":"香蕉","product_description":"爱是刘德华","product_price":12.34,"product_sales":0,"product_store":1223,"picture_url":"http://qq84429406.eicp.net/TKBaas/imgs/e3522d07-e250-4c97-a7e6-c897b869d317.jpg"}]     * seller_id : 1     * seller_name : 杭进     * seller_shopName : 鲜果之家     * seller_shopPicture : http://qq84429406.eicp.net/TKBaas/imgs/e0f8c89c-5484-41ef-83ea-ef509125e32b.jpg     * seller_shopDescription : 这个是商家详情：爱上的流程单纯     * seller_grade : 3     * seller_phone : 18813293140     * seller_sales : 23     */    private int currentPage;    private int pageSize;    private int totalNum;    private int totalPage;    private String seller_id;    private String seller_name;    private String seller_shopName;    private String seller_shopPicture;    private String seller_shopDescription;    private int seller_grade;    private String seller_phone;    private int seller_sales;    /**     * product_id : 402881e65681c0bc015681c1e3bd0000     * product_name : 刘备     * product_city : west     * product_type : 苹果     * product_description : 3245第三方二十投放的发生过     * product_price : 33     * product_sales : 0     * product_store : 33     * picture_url : http://qq84429406.eicp.net/TKBaas/imgs/8d400370-617f-4c66-9e84-79704b71bd7c.jpg     */    private List<ProductListBean> product_list;    public int getCurrentPage() {        return currentPage;    }    public void setCurrentPage(int currentPage) {        this.currentPage = currentPage;    }    public int getPageSize() {        return pageSize;    }    public void setPageSize(int pageSize) {        this.pageSize = pageSize;    }    public int getTotalNum() {        return totalNum;    }    public void setTotalNum(int totalNum) {        this.totalNum = totalNum;    }    public int getTotalPage() {        return totalPage;    }    public void setTotalPage(int totalPage) {        this.totalPage = totalPage;    }    public String getSeller_id() {        return seller_id;    }    public void setSeller_id(String seller_id) {        this.seller_id = seller_id;    }    public String getSeller_name() {        return seller_name;    }    public void setSeller_name(String seller_name) {        this.seller_name = seller_name;    }    public String getSeller_shopName() {        return seller_shopName;    }    public void setSeller_shopName(String seller_shopName) {        this.seller_shopName = seller_shopName;    }    public String getSeller_shopPicture() {        return seller_shopPicture;    }    public void setSeller_shopPicture(String seller_shopPicture) {        this.seller_shopPicture = seller_shopPicture;    }    public String getSeller_shopDescription() {        return seller_shopDescription;    }    public void setSeller_shopDescription(String seller_shopDescription) {        this.seller_shopDescription = seller_shopDescription;    }    public int getSeller_grade() {        return seller_grade;    }    public void setSeller_grade(int seller_grade) {        this.seller_grade = seller_grade;    }    public String getSeller_phone() {        return seller_phone;    }    public void setSeller_phone(String seller_phone) {        this.seller_phone = seller_phone;    }    public int getSeller_sales() {        return seller_sales;    }    public void setSeller_sales(int seller_sales) {        this.seller_sales = seller_sales;    }    public List<ProductListBean> getProduct_list() {        return product_list;    }    public void setProduct_list(List<ProductListBean> product_list) {        this.product_list = product_list;    }    public static class ProductListBean {        private String product_id;        private String product_name;        private String product_city;        private String product_type;        private String product_description;        private double product_price;        private int product_sales;        private int product_store;        private String picture_url;        public String getProduct_id() {            return product_id;        }        public void setProduct_id(String product_id) {            this.product_id = product_id;        }        public String getProduct_name() {            return product_name;        }        public void setProduct_name(String product_name) {            this.product_name = product_name;        }        public String getProduct_city() {            return product_city;        }        public void setProduct_city(String product_city) {            this.product_city = product_city;        }        public String getProduct_type() {            return product_type;        }        public void setProduct_type(String product_type) {            this.product_type = product_type;        }        public String getProduct_description() {            return product_description;        }        public void setProduct_description(String product_description) {            this.product_description = product_description;        }        public double getProduct_price() {            return product_price;        }        public void setProduct_price(int product_price) {            this.product_price = product_price;        }        public int getProduct_sales() {            return product_sales;        }        public void setProduct_sales(int product_sales) {            this.product_sales = product_sales;        }        public int getProduct_store() {            return product_store;        }        public void setProduct_store(int product_store) {            this.product_store = product_store;        }        public String getPicture_url() {            return picture_url;        }        public void setPicture_url(String picture_url) {            this.picture_url = picture_url;        }    }}