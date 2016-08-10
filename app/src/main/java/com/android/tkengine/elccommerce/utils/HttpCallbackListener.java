package com.android.tkengine.elccommerce.utils;

/**
 * Created by FangYu on 2016/5/5.
 */
public interface HttpCallbackListener {

        void onFinish(String result);

        void onError(Exception e);
}
