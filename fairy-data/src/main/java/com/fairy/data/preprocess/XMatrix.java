package com.fairy.data.preprocess;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.POI;
import com.fairy.pojo.POISimple;
import com.fairy.pojo.Region;
import com.fairy.util.ConfigUtil;
import com.fairy.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 输入：POI数据
 * 输出：矩阵Region*Features
 */
public class XMatrix {

    private String poiPath = ConfigUtil.getValue("poiPath", "conf.properties");
    private String poiSimplePath = ConfigUtil.getValue("poiSimplePath", "conf.properties");
    private String regionsPath = ConfigUtil.getValue("regionsPath", "conf.properties");
    private FileUtil fileUtil = FileUtil.getInstance();
    private int poi_v;

    public void initParam(){
        String poiVStr = ConfigUtil.getValue("poi_v", "conf.properties");
        poi_v = Integer.valueOf(poiVStr);
    }


    public void XMatrixGen() throws IOException {
        initParam();

        String regionStr = fileUtil.readJsonFileToStr(new File(regionsPath));
        List<Region> regionList = JSON.parseArray(regionStr, Region.class);

        String poiSimpleStr = fileUtil.readJsonFileToStr(new File(poiSimplePath));
        List<POISimple> poiSimpleList = JSON.parseArray(poiSimpleStr, POISimple.class);


        int[][] matrixX = new int[regionList.size()][poi_v];
        Region region = null;
        for (int i = 0; i < regionList.size(); i++) {
            region = regionList.get(i);
            for (POISimple poi : poiSimpleList) {
                if(isContained(region, poi)){
                    int poiIndex = toPOIIndex(poi);
                    if(poiIndex > 0){
                        matrixX[i][poiIndex - 5]++;
                    }

                }
            }
        }

        String jsonStr = JSON.toJSONString(matrixX);
        fileUtil.saveToFile("fairy-data/data/XMatrix.json", jsonStr, false);
    }

    /**
     * 获取poi类型，转为数组下标
     * @param poi
     * @return
     */
    private int toPOIIndex(POISimple poi) {
        try {
            String typeCode = poi.getTypecode();
            String subCode = typeCode.substring(0, 2);
            int index = Integer.valueOf(subCode);
            if (index <= 20 && index >= 1) {
                return index;
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    private boolean isContained(Region region, POISimple poi) {
        double lat = poi.getLocationx();
        double lng = poi.getLocationy();

        if(lat >= region.getLat02() && lat <= region.getLat01() && lng >= region.getLng01()
                && lng <= region.getLng02())
        {
            return true;
        }
        return false;
    }

    //将POI转为POISimple
    public void simplePOIData() throws IOException {
        String poiStr = fileUtil.readJsonFileToStr(new File(poiPath));
        List<POI> poiList = JSON.parseArray(poiStr, POI.class);

        POISimple poiSimple ;
        List<POISimple> list = new ArrayList<>();
        for (int i = 0; i < poiList.size(); i++) {
            poiSimple = new POISimple(poiList.get(i));
            list.add(poiSimple);
        }
        String jsonStr = JSON.toJSONString(list);
        fileUtil.saveToFile("fairy-data/data/poiSimple_hangzhou.json", jsonStr, false);
    }

    public static void main(String[] args) throws IOException {
        new XMatrix().XMatrixGen();
//        new XMatrix().simplePOIData();
    }
}
