package com.android.tkengine.elccommerce.model;

import android.content.Context;

import com.android.tkengine.elccommerce.beans.Constants;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * “我的钱包”页面的Model操作
 */
public class WalletModel {

    Context mContext = null;
    RequestQueue mQueue = null;

    public interface ResponseListener{
        void onResponse(boolean result);
        void onError();
    }

    public WalletModel(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    public void chargeMoney(String userId, String password, double money, final ResponseListener callback) {
        //构建Volley请求
        if (null == mQueue) {
            mQueue = Volley.newRequestQueue(mContext);
        }
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", userId);
            params.put("user_password", password);
            params.put("charge_money", money);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_CHARGEMONEY, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            boolean result = jsonObject.getBoolean("charge_state");
                            callback.onResponse(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callback.onError();
                    }
                });
        mQueue.add(request);
    }
}
