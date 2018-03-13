package com.fairy.pojo;

import java.text.MessageFormat;

/**
 * 卡口
 */
public class KK {
    private String fxbh;     //方向编号
    private String id;       //序号
    private String kkId;     //卡口id
    private String kkName;   //卡口名字
    private String kkType;   //卡口类型
    private String lat;      //经纬坐标-纬度
    private String lng;      //经纬坐标-经度

    public KK() {
    }

    public String getFxbh() {
        return fxbh;
    }

    public void setFxbh(String fxdh) {
        this.fxbh = fxdh;
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

    public String getKkName() {
        return kkName;
    }

    public void setKkName(String kkName) {
        this.kkName = kkName;
    }

    public String getKkType() {
        return kkType;
    }

    public void setKkType(String kkType) {
        this.kkType = kkType;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        String str = MessageFormat.format("{0}:[{1}, {2}]",
                this.kkId, this.lat, this.lng);
        return str;
    }
}
