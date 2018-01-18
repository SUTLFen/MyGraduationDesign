package com.fairy.pojo;

import org.apache.lucene.document.Document;

/**
 * Created by Fairy_LFen on 2017/2/17.
 */
public class POI {
    private String name ;
    private String type ;
    private String tel;
    private double locationx;  //locationx - 高德
    private double locationy;  //locationy - 高德
    private String addr;
    private String province;
    private String citycode;
    private String district;
    private String street;
    private String business;
    private String adcode;
    private String typecode;
    private String address;
    private String cityname;
    private String number;
    private double gpsx;
    private double gpsy;
    private double bdx;
    private double bdy;

    public POI(){}
    public POI(String[] str_arry) {
            this.name = str_arry[0];
            this.type = str_arry[1];
            this.tel = str_arry[2];
        try{
            this.locationx = Double.valueOf(str_arry[3]);
            this.locationy = Double.valueOf(str_arry[4]);

        }catch(Exception e){
            this.locationx = -9999;
            this.locationy = -9999;
        }
            this.addr = str_arry[5];
            this.province = str_arry[6];
            this. citycode= str_arry[7];
            this.district = str_arry[8];
            this.street = str_arry[9];
            this.business = str_arry[10];
            this.adcode = str_arry[11];
            this.typecode = str_arry[12];
            this.address = str_arry[13];
            this.cityname = str_arry[14];
            this.number = str_arry[15];
        try{
            this.gpsx = Double.valueOf(str_arry[16]);
            this.gpsy = Double.valueOf(str_arry[17]);
            this.bdx = Double.valueOf(str_arry[18]);
            this.bdy = Double.valueOf(str_arry[19]);
        }catch(Exception e){
            this.gpsx = -9999;
            this.gpsy = -9999;
            this.bdx = -9999;
            this.bdy = -9999;
        }

    }

    public POI(Document doc) {
        this.name = doc.getField("name").stringValue();
        this.type = doc.getField("type").stringValue();
        this.tel = doc.getField("tel").stringValue();
        try{
            this.locationx = (double)doc.getField("locationx").numericValue();
            this.locationy = (double)doc.getField("locationy").numericValue();

        }catch(Exception e){
            this.locationx = -9999;
            this.locationy = -9999;
        }
        this.addr = doc.getField("addr").stringValue();
        this.province = doc.getField("province").stringValue();
        this. citycode= doc.getField("citycode").stringValue();
        this.district = doc.getField("district").stringValue();
        this.street = doc.getField("street").stringValue();
        this.business = doc.getField("business").stringValue();
        this.adcode = doc.getField("adcode").stringValue();
        this.typecode = doc.getField("typecode").stringValue();
        this.address = doc.getField("address").stringValue();
        this.cityname = doc.getField("cityname").stringValue();
        this.number = doc.getField("number").stringValue();
    try{
        this.gpsx = (double)doc.getField("gpsx").numericValue();
        this.gpsy = (double)doc.getField("gpsy").numericValue();
        this.bdx = (double)doc.getField("bdx").numericValue();
        this.bdy = (double)doc.getField("bdy").numericValue();
    }catch(Exception e){
        this.gpsx = -9999;
        this.gpsy = -9999;
        this.bdx = -9999;
        this.bdy = -9999;
    }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getGpsx() {
        return gpsx;
    }

    public void setGpsx(double gpsx) {
        this.gpsx = gpsx;
    }

    public double getGpsy() {
        return gpsy;
    }

    public void setGpsy(double gpsy) {
        this.gpsy = gpsy;
    }

    public double getBdx() {
        return bdx;
    }

    public void setBdx(double bdx) {
        this.bdx = bdx;
    }

    public double getBdy() {
        return bdy;
    }

    public void setBdy(double bdy) {
        this.bdy = bdy;
    }
}
