package com.yuhung.gotoeat.UsedComponent;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RunThread extends Thread{

    public String apiURL= "";
    public String result = "";

    @Override
    public void run() {

        try {
            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true); // 允許輸出
            connection.setDoInput(true); // 允許讀入
            connection.setUseCaches(false); // 不使用快取
            connection.connect(); // 開始連線
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);

                StringBuffer data = new StringBuffer();
                String line = null;
                while((line = bufReader.readLine()) != null) {
                    data.append(line + "\n");
                }
                inputStream.close(); // 關閉輸入串流
                result = data.toString(); // 把存放用字串放到全域變數
                Log.i("tags", "Get From DataBase: " + result);
            }

        } catch(Exception ex) {
            Log.i("tags", "Error Message: " + ex.getMessage());
        }
    }

    public String getResult() {
        Log.i("tags","Return Message: " + result);
        return result;
    }

    public void setApiUrl(String Url){

        this.apiURL = Url;
        Log.i("tags", apiURL);
    }
}
