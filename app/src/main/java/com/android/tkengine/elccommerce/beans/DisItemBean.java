package com.android.tkengine.elccommerce.beans;

import java.util.HashMap;

/**
 * 首页RecyclerView的数据条目Bean封装
 */
public class DisItemBean {

    public final static int TYPE_ITEM1 = 0;
    public final static int TYPE_ITEM2 = 1;
    public final static int TYPE_ITEM3 = 2;

    //Bean代表的首页Item类型
    public int type = -1;
    //储存Bean中包含的数据
    public HashMap<String, Object> data = null;
}
