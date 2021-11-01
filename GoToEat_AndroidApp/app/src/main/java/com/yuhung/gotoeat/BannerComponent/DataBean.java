package com.yuhung.gotoeat.BannerComponent;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.yuhung.gotoeat.R;
import com.yuhung.gotoeat.UsedComponent.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataBean {

//    public Integer imageRes;
    public String imageUrl;
    public String title;
    public int viewType;

//    public DataBean(Integer imageRes, String title, int viewType) {
//        this.imageRes = imageRes;
//        this.title = title;
//        this.viewType = viewType;
//    }

    public DataBean(String imageUrl, String title, int viewType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.viewType = viewType;
    }

    public static List<DataBean> getADData(String[] adTitle, String[] adPhoto) {

        List<DataBean> list = new ArrayList<>();
        for(int i=0; i < adTitle.length; i++){
            list.add(new DataBean(adPhoto[i], adTitle[i], 1));
        }
        return list;
    }

    public static List<String> getColors(int size) {
        List<String> list = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            list.add(getRandColor());
        }
        return list;
    }

    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     *
     * @return String
     */
    public static String getRandColor() {
        String R, G, B;
        Random random = new Random();
        R = Integer.toHexString(random.nextInt(256)).toUpperCase();
        G = Integer.toHexString(random.nextInt(256)).toUpperCase();
        B = Integer.toHexString(random.nextInt(256)).toUpperCase();

        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        return "#" + R + G + B;
    }
}
