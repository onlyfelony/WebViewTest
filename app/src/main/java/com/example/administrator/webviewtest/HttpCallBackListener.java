package com.example.administrator.webviewtest;

public interface HttpCallBackListener {
    void onFinish(String response);//当服务器成功响应的时候调用

    void onError(Exception e);//当网络操作出现错误的时候调用


}
