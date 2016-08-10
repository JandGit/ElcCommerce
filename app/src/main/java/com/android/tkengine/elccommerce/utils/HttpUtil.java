package com.android.tkengine.elccommerce.utils;


import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpUtil {


    public static void sentHttpGet(final String httpUrl, final HttpCallbackListener listener) {
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
                    if (listener != null) {
                        listener.onFinish(result.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }
        }).start();

    }

    public static void sentHttpPost(final String httpUrl, final String params, final HttpCallbackListener listener) {
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
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.connect();

                    OutputStream os = connection.getOutputStream();
                    os.write(params.getBytes("utf-8"));
                    os.flush();
                    os.close();

                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    String strRead = null;
                    while ((strRead = reader.readLine()) != null) {
                        result += strRead;
                    }
                    reader.close();
                    if (listener != null) {
                        listener.onFinish(result.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }

    public static String sentHttpPost(final String httpUrl, final String params) throws Exception {
        BufferedReader reader = null;
        URL url = null;
        HttpURLConnection connection = null;
        String result = "";
        StringBuffer sbf = new StringBuffer();

        url = new URL(httpUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.connect();

        OutputStream os = connection.getOutputStream();
        os.write(params.getBytes("utf-8"));
        os.flush();
        os.close();

        reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String strRead = null;
        while ((strRead = reader.readLine()) != null) {
            result += strRead;
        }
        reader.close();
        connection.disconnect();

        return result;
    }
}



