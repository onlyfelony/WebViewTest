package com.example.administrator.webviewtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {


    public static void sendHttpRequest(final String address, final HttpCallBackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    InputStream in = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);

                    }
                    if (listener != null) {
                        //回调onFinish()
                        listener.onFinish(response.toString());
                    }

                } catch (Exception e) {
                    if (listener != null) {
                        //回调onError()
                        listener.onError(e);
                    }


                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }


                    if (connection != null) {
                        connection.disconnect();
                    }


                }


            }
        }).start();


    }

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(address)
                .build();

        client.newCall(request).enqueue(callback);

    }


}
