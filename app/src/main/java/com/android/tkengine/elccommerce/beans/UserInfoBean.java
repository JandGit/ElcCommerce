package com.android.tkengine.elccommerce.beans;

import java.io.Serializable;

/**
 * 储存用户个人信息的bean
 */

public class UserInfoBean implements Serializable{

    String userId;
    String user_name;
    String user_phone;
    String user_sex;
    String user_picture_url;
    double user_money;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_picture_url() {
        return user_picture_url;
    }

    public void setUser_picture_url(String user_picture_url) {
        this.user_picture_url = user_picture_url;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public double getUser_money() {
        return user_money;
    }

    public void setUser_money(double user_money) {
        this.user_money = user_money;
    }

    public String toString(){
        return "用户名：" + user_name + "\n用户ID：" + userId + "\n头像：" + user_picture_url;
    }
}
