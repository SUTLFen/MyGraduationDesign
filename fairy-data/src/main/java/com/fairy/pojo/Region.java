package com.fairy.pojo;

/**
 * 由四叉树分割后的地理区域
 */
public class Region {
    public String lat01;
    public String lng01;
    public String lat02;
    public String lng02;

    public String getLat01() {
        return lat01;
    }

    public void setLat01(String lat01) {
        this.lat01 = lat01;
    }

    public String getLng01() {
        return lng01;
    }

    public void setLng01(String lng01) {
        this.lng01 = lng01;
    }

    public String getLat02() {
        return lat02;
    }

    public void setLat02(String lat02) {
        this.lat02 = lat02;
    }

    public String getLng02() {
        return lng02;
    }

    public void setLng02(String lng02) {
        this.lng02 = lng02;
    }
}
