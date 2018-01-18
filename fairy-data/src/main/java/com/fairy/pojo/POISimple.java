package com.fairy.pojo;

/**
 * Created by Fairy_LFen on 2017/4/7.
 */
public class POISimple {
    private String name ;
    private double locationx;  //locationx - 高德
    private double locationy;  //locationy - 高德
    private String address;
    private String province;
    private String cityname;
    private String type ;
    private String typecode;

    public POISimple() {
    }

    public POISimple(POI poi) {
        this.name = poi.getName();
        this.locationx = poi.getLocationx();
        this.locationy = poi.getLocationy();
        this.address = poi.getAddress();
        this.province = poi.getProvince();
        this.cityname = poi.getCityname();
        this.type = poi.getType();
        this.typecode = poi.getTypecode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLocationx() {
        return locationx;
    }

    public void setLocationx(double locationx) {
        this.locationx = locationx;
    }

    public double getLocationy() {
        return locationy;
    }

    public void setLocationy(double locationy) {
        this.locationy = locationy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }
}
