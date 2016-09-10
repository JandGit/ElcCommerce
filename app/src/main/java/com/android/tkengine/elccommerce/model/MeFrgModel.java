package com.android.tkengine.elccommerce.model;

import android.content.Context;
import android.content.SharedPreferences;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.UserInfoBean;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的页面Model
 */
public class MeFrgModel {

    Context mContext;
    RequestQueue mQueue;

    public interface ResponseListener{
        void onResponse();
    }

    public MeFrgModel(Context mContext) {
        this.mContext = mContext;
        mQueue = Volley.newRequestQueue(mContext);
    }

    /**
     * 加载已登录的用户信息，并将信息写入SharePreferrence
     */
    public void loadUserInfo(String userId, final ResponseListener callback) {
        if (null == userId || userId.isEmpty()) {
            return;
        }
        JSONObject jsonObject;
        jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_GETUSERINFO, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Gson gson = new Gson();
                        UserInfoBean info = gson.fromJson(jsonObject.toString(), UserInfoBean.class);
                        SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_LOGIN_USERINFO, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("UserName", info.getUser_name())
                                .putString("UserIcon", info.getUser_picture_url())
                                .putString("UserSex", info.getUser_sex())
                                .putFloat("UserMoney", (float) info.getUser_money())
                                .apply();
                        callback.onResponse();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });
        mQueue.add(request);
    }
}
