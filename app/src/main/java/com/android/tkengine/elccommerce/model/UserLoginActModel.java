package com.android.tkengine.elccommerce.model;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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
 * 用户登录页面的Model
 */
public class UserLoginActModel {
    Context mContext;
    RequestQueue mQueue;

    public interface ResponseListener{
        void onLoginResponse(boolean result);
        void onError();
    }

    public UserLoginActModel(Context mContext) {
        this.mContext = mContext;
        mQueue = Volley.newRequestQueue(mContext);
    }

    /**
     *
     * @param userName 登录的用户名
     * @param password 登录密码
     */
    public void login(String userName, final String password, final UserLoginActModel.ResponseListener callback){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_phone", userName);
            jsonObject.put("user_password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_ADDRESS_LOGIN, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String userId = jsonObject.getString("user_id");
                            if (userId != null && !userId.isEmpty()) {
                                SharedPreferences sp = mContext.getSharedPreferences(Constants.SP_LOGIN_USERINFO, Context.MODE_PRIVATE);
                                sp.edit().putString("UserId", userId).putBoolean("isLogin", true)
                                        .putString("password", password).apply();
                                updataUserInfo(userId);
                                callback.onLoginResponse(true);
                            }
                            else{
                                callback.onLoginResponse(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError();
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

    private void updataUserInfo(String userId){
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
                        editor.putString("UserPhone", info.getUser_phone())
                                .putString("UserName", info.getUser_name())
                                .putString("UserIcon", info.getUser_picture_url())
                                .putString("UserSex", info.getUser_sex())
                                .putFloat("UserMoney", (float) info.getUser_money())
                                .apply();
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
