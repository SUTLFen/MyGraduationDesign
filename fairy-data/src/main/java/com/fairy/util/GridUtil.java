package com.fairy.util;

import com.fairy.pojo.Grid;
import com.fairy.pojo.POISimple;
import com.fairy.pojo.PrCar;
import com.fairy.pojo.Weibo;

public class GridUtil {

    public static boolean isInGrid(PrCar prCar, Grid grid){

        double lat = prCar.getLat();
        double lng = prCar.getLng();

        if(lat >= grid.getLat02() && lat <= grid.getLat01() && lng >= grid.getLng01()
                && lng <= grid.getLng02())
        {
            return true;
        }
        return false;
    }

    public static boolean isInGrid(Grid grid, POISimple poi) {
        double lat = poi.getLocationx();
        double lng = poi.getLocationy();

        if(lat >= grid.getLat02() && lat <= grid.getLat01() && lng >= grid.getLng01()
                && lng <= grid.getLng02())
        {
            return true;
        }
        return false;
    }


    /**
     * 判断该weibo是否在该Grid中
     * @param grid 区域
     * @param weibo  微博
     * @return
     */
    public static boolean isInGrid(Grid grid, Weibo weibo){
        double lng = weibo.getLatitude();
        double lat = weibo.getLongitude();

        if(lat >= grid.getLat02() && lat <= grid.getLat01() && lng >= grid.getLng01()
                && lng <= grid.getLng02())
        {
            return true;
        }
        return false;
    }
}
