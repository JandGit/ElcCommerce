package com.android.tkengine.elccommerce.beans;

import java.util.HashMap;

/**
 * 首页RecyclerView的数据条目Bean封装
 */
public class RvItemBean{

    public final static int TYPE_AD = 0;
    public final static int TYPE_CATEGORY = 1;
    public final static int TYPE_GROUPTITLE = 2;
    public final static int TYPE_ITEM1 = 3;
    public final static int TYPE_ITEM2 = 4;
    public final static int TYPE_ITEM3 = 5;

    //Bean代表的首页Item类型
    public int type = -1;
    //储存Bean中包含的数据
    public HashMap<String, Object> data = null;
}
