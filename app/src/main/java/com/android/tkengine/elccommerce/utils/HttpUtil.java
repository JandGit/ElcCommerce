package com.android.tkengine.elccommerce.utils;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by FangYu on 2016/5/5.
 */
public class HttpUtil {


    public static void sentHttpRequest( final String httpUrl, final HttpCallbackListener listener) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                    BufferedReader reader = null;
                    URL url = null;
                    HttpURLConnection connection = null;
                    String result = "";
                    StringBuffer sbf = new StringBuffer();

                    try {
                        url = new URL(httpUrl);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.connect();

                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                        String strRead = null;
                        while ((strRead = reader.readLine()) != null) {
                            result += strRead;
                        }
                        reader.close();
                        if(listener != null){
                            listener.onFinish(result.toString());
                    }
                    } catch (Exception e) {
                        if (listener != null) {
                            listener.onError(e);
                        }

                    } finally{
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }

                }
        }).start();

    }

}



