package com.example.administrator.webviewtest;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //private WebView webView;
    private Button sendRequest;
    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendRequest = findViewById(R.id.get_data);
        responseText = findViewById(R.id.scorll);

        sendRequest.setOnClickListener(this);
       /* webView = findViewById(R.id.webview);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//支持Java脚本

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.baidu.com");*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_data:
               // sendHttpRequest();
                String address = "http://www.baidu.com";
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //对异常进行处理

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String responseData = response.body().string();//得到服务器返回的内容
                        updateUI(responseData);
                    }
                });

               /* HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {

                        updateUI(response);//更新UI
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });*/

                break;

            default:
                break;

        }
    }

    private void sendHttpRequest() {
        //开启线程发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Log.d("cdsfsy", "123");
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("http://127.0.0.1/get_data.xml")
                            .build();
                    Log.d("cdsfsy", "456");
                    Response response = client.newCall(request).execute();
                    Log.d("cdsfsy", "dfs");
                    String responseData = response.body().string();

                  //  updateUI(responseData);
                    parseXMLWithPull(responseData);
                } catch (IOException e) {
                    Log.d("cdsfsy", "78");
                    e.printStackTrace();
                }


            }
        }).start();


    }

/*    private void sendHttpRequest() {
        //开启线程发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL("https://www.baidu.com");

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");//从服务器得到数据
                    connection.setConnectTimeout(8000);//设置连接超时，读取超时为8000毫秒
                    connection.setReadTimeout(8000);

                    InputStream inputStream = connection.getInputStream();

                    //对获取到的数据进行处理
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null){
                        response.append(line);
                    }

                    updateUI(response.toString());//把得到的结果在TextView显示
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {

                    if(reader!=null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (connection!=null){
                        connection.disconnect();//关闭连接
                    }

                }


            }
        }).start();


    }*/

    private void updateUI(final String response) {
        //将线程切换到主线程更新UI

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                responseText.setText(response);
            }
        });

    }


    private void parseXMLWithPull(String xmlData) {
        try {
            Log.d("cdsfsy", "trst");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    // 开始解析某个结点
                    case XmlPullParser.START_TAG: {
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        } else if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG: {
                        if ("app".equals(nodeName)) {
                            Log.d("cdsfsy", "id is " + id);
                            Log.d("cdsfsy", "name is " + name);
                            Log.d("cdsfsy", "version is " + version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
