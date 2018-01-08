package com.fairy.pojo;

import com.fairy.utils.DateUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Fairy_LFEn on 2016/3/15/0015.
 */
public class Weibo {
    public String id ;   //0
    public long time ;  //1   文本中形式：  Fri Jan 01 00:58:20 +0800 2016
    public String content ;  //2
    public double longitude ; //3
    public double latitude;  //4

    public String locationName;//6  如：大桥镇

    public String provinceId;    //13
    public String cityId;     //14
    public String location;      //15   详细地址

    private static DateUtil dateUtil = DateUtil.getInstance();

    public Weibo() {
    }

    public Weibo(String[] strs) {
        System.out.println(ArrayUtils.toString(strs));
        try{
            this.id = strs[0];
            this.time = dateUtil.getTime(strs[1], true);
            this.content = strs[2];
            this.locationName = strs[6];
            this.provinceId = strs[13];
            this.cityId = strs[14];
            this.location = strs[15];

        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            this.longitude = Double.valueOf(strs[3]);
            this.latitude = Double.valueOf(strs[4]);
        }catch(Exception e){
            this.longitude = Double.MAX_VALUE;
            this.latitude = Double.MAX_VALUE;
        }
    }

    public static Weibo create(String[] strs) {
        System.out.println(ArrayUtils.toString(strs));

        if(StringUtils.isBlank(strs[0])) return null ;

        Weibo weibo = new Weibo();
        weibo.setId(strs[0]);

        try {
            weibo.setTime(dateUtil.getTime(strs[1], true));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        weibo.setContent(strs[2]);
        weibo.setLocationName(strs[6]);
        weibo.setProvinceId(strs[13]);
        weibo.setCityId(strs[14]);
        weibo.setLocation(strs[15]);

        try{
            weibo.setLongitude(Double.valueOf(strs[3]));
            weibo.setLatitude(Double.valueOf(strs[4]));
        }catch(Exception e){
            return null;
        }

        return weibo;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public DateUtil getDateUtil() {
        return dateUtil;
    }

    public void setDateUtil(DateUtil dateUtil) {
        this.dateUtil = dateUtil;
    }
}