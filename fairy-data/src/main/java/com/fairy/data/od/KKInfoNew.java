package com.fairy.data.od;

/**
 * Created by Fairy_LFen on 2017/4/27.
 */

import com.fairy.pojo.KK;

/**
 * 在KKInfo的基础上，添加 double[] Vector 特征向量;
 * */
public class KKInfoNew {

    private String id;  //1. 序号
    private String kkId;  //2. 卡口id
    private String fxbh;  //3. 方向编号
    private String kkType;  //4. 卡口类型
    private String kkName;    //7. 卡口名称
    private String lng;    //8.经度
    private String lat;    //9.纬度
    private double[] vector;


    public KKInfoNew(){

    }
    public KKInfoNew(KK kk, double[] featureVector) {
        this.id = kk.getId();
        this.kkId = kk.getKkId();
        this.fxbh = kk.getFxbh();
        this.kkType = kk.getKkType();
        this.kkName = kk.getKkName();
        this.lng = kk.getLng();
        this.lat = kk.getLat();
        this.vector = featureVector;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKkId() {
        return kkId;
    }

    public void setKkId(String kkId) {
        this.kkId = kkId;
    }

    public String getFxbh() {
        return fxbh;
    }

    public void setFxbh(String fxbh) {
        this.fxbh = fxbh;
    }

    public String getKkType() {
        return kkType;
    }

    public void setKkType(String kkType) {
        this.kkType = kkType;
    }

    public String getKkName() {
        return kkName;
    }

    public void setKkName(String kkName) {
        this.kkName = kkName;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public double[] getVector() {
        return vector;
    }

    public void setVector(double[] vector) {
        this.vector = vector;
    }
}
