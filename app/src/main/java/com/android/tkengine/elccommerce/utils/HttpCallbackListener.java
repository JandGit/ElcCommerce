package com.android.tkengine.elccommerce.utils;


public interface HttpCallbackListener {

        void onFinish(String result);

        void onError(Exception e);
}
