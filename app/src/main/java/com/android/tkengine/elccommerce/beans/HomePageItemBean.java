package com.android.tkengine.elccommerce.beans;

import java.util.HashMap;

/**
 * 首页RecyclerView的数据条目Bean封装
 */
public class HomePageItemBean {

    //标题
    public static final int TYPE_HEAD = 0;
    //分组
    public static final int TYPE_GROUP = 1;
    //商品
    public static final int TYPE_GOODS = 2;
    //广告
    public static final int TYPE_AD = 3;
    //提示
    public static final int TYPE_TIPS = 4;

    //Bean代表的首页Item类型
    public int type = -1;
    //储存Bean中包含的数据
    public HashMap<String, Object> data = null;
}
