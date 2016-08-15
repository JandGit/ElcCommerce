package com.android.tkengine.elccommerce.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 陈嘉shuo on 2016/8/12.
 */
public class GoodsAddressBean implements Serializable{


    /**
     * id : 1
     * receiver : 地址2
     * phone : 18813290346
     * province : 天津市
     * city : 天津市
     * countyTown : 和平区
     * street : 111
     * detailsAddress : 22222222222
     * defaultAddress : false
     */

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable{
        private String id;
        private String receiver;
        private String phone;
        private String province;
        private String city;
        private String countyTown;
        private String street;
        private String detailsAddress;
        private boolean defaultAddress;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountyTown() {
            return countyTown;
        }

        public void setCountyTown(String countyTown) {
            this.countyTown = countyTown;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getDetailsAddress() {
            return detailsAddress;
        }

        public void setDetailsAddress(String detailsAddress) {
            this.detailsAddress = detailsAddress;
        }

        public boolean isDefaultAddress() {
            return defaultAddress;
        }

        public void setDefaultAddress(boolean defaultAddress) {
            this.defaultAddress = defaultAddress;
        }
    }
}
