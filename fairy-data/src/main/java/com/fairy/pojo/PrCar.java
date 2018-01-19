package com.fairy.pojo;

import com.fairy.utils.DateUtil;

import java.text.ParseException;

public class PrCar {
    private String direction;   //"04"
    private String plateNumber;   //"浙AN7N53"
    private long stayPointDate;   // 字符串：2016-01-01 00:00:22    存储 unix 时间戳
    private double lng;
    private double lat;

    private DateUtil dateUtil = DateUtil.getInstance();

    public PrCar() {
    }

    public PrCar(PrCarOld prCarOldTmp, String lat, String lng) throws ParseException {
        this.direction = prCarOldTmp.getDirection();
        this.plateNumber = prCarOldTmp.getPlateNumber();
        this.stayPointDate = dateUtil.getTime(prCarOldTmp.getStayPointDate());
        this.lng = Double.valueOf(lng);
        this.lat = Double.valueOf(lat);
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public long getStayPointDate() {
        return stayPointDate;
    }

    public void setStayPointDate(long stayPointDate) {
        this.stayPointDate = stayPointDate;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
