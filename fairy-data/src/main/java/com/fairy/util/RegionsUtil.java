package com.fairy.util;

import com.fairy.pojo.POISimple;
import com.fairy.pojo.PrCar;
import com.fairy.pojo.Region;

public class RegionsUtil {
    public static boolean isInRegion(PrCar prCar, Region region){

        double lat = prCar.getLat();
        double lng = prCar.getLng();

        if(lat >= region.getLat02() && lat <= region.getLat01() && lng >= region.getLng01()
                && lng <= region.getLng02())
        {
            return true;
        }
        return false;
    }

    public static boolean isInRegion(Region region, POISimple poi) {
        double lat = poi.getLocationx();
        double lng = poi.getLocationy();

        if(lat >= region.getLat02() && lat <= region.getLat01() && lng >= region.getLng01()
                && lng <= region.getLng02())
        {
            return true;
        }
        return false;
    }

}
