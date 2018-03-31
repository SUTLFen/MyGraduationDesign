package com.fairy.pojo;

public class Grid {
    public double lat01;  // lat01 lng01 : 左上角   (lng01 < lng02), (lat02 < lat01)
    public double lng01;
    public double lat02;    // lat02 lng02 : 右下角
    public double lng02;

    public Grid() {
    }

    public double getLat01() {
        return lat01;
    }

    public void setLat01(double lat01) {
        this.lat01 = lat01;
    }

    public double getLng01() {
        return lng01;
    }

    public void setLng01(double lng01) {
        this.lng01 = lng01;
    }

    public double getLat02() {
        return lat02;
    }

    public void setLat02(double lat02) {
        this.lat02 = lat02;
    }

    public double getLng02() {
        return lng02;
    }

    public void setLng02(double lng02) {
        this.lng02 = lng02;
    }
}
