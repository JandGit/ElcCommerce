package com.android.tkengine.elccommerce.model;

import android.content.Context;
import android.util.Log;
import com.android.tkengine.elccommerce.beans.Constants;
import com.android.tkengine.elccommerce.beans.OrderBean;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;

/**
 * 订单页面的Model
 */
public class OrderActModel {

    Context mContext;
    RequestQueue mQueue;

    public OrderActModel(Context mContext) {
        this.mContext = mContext;
        mQueue = Volley.newRequestQueue(mContext);
    }

    public interface ResponseListener{
        //正常状态
        void onResponse(OrderBean[] orders);
        //网络连接错误或服务器解析异常时
        void onError();
    }

    /**
     * 获取订单
     * @param userId 用户Id
     * @param page 0为所有订单，1为待付款，2为待发货，3为待收货，4为待评价
     * @param from 用于订单分块显示，指定加载订单的起始位置,页码,默认为1
     */
    public void getOrder(String userId, int page, int from, final ResponseListener callback){
        String url = Constants.SERVER_GETORDER_SPC;
        String params;
        switch (page){
            case 0:
                url = Constants.SERVER_GETORDER_ALL;
                params = "{\"userId\":\"" + userId + "\", \"currentPage\":" + from + ",\"pageSize\":30}";
                break;
            case 1:
                params = "{\"userId\":\"" + userId + "\", \"currentPage\":" + from + ",\"pageSize\":30, \"state\":\"unpaid\"}";
                break;
            case 2:
                params = "{\"userId\":\"" + userId + "\", \"currentPage\":" + from + ",\"pageSize\":30, \"state\":\"unsent\"}";
                break;
            case 3:
                params = "{\"userId\":\"" + userId + "\", \"currentPage\":" + from + ",\"pageSize\":30, \"state\":\"unreceived\"}";
                break;
            case 4:
                params = "{\"userId\":\"" + userId + "\", \"currentPage\":" + from + ",\"pageSize\":30, \"state\":\"uncomment\"}";
                break;
            default:
                Log.e("OrderActModel:", "错误的Page参数，停止网络访问");
                return;
        }
        JSONObject json;
        try {
            json = new JSONObject(params);
        } catch (JSONException e) {
            Log.e("OrderActModel:", "params参数错误，停止网络访问");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        String result;
                        try {
                            result = jsonObject.getJSONArray("result").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("OrderActModel:", "服务器返回格式错误");
                            callback.onError();
                            return;
                        }
                        Gson gson = new Gson();
                        Type type = new TypeToken<OrderBean[]>(){}.getType();
                        OrderBean[] orders = gson.fromJson(result, type);
                        callback.onResponse(orders);
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

    /**
     * 更新订单状态
     * @param id 订单Id
     * @param state 更新状态
     * @param callback 返回Response参数为null
     */
    public void updateOrder(String id, String state, final ResponseListener callback){
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("state", state);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_ADDRESS_UPDATEORDER, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        boolean result;
                        try {
                            result = jsonObject.getBoolean("result");
                            if(result){
                                callback.onResponse(null);
                            }
                            else{
                                callback.onError();
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
}
