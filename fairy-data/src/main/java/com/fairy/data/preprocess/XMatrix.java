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
    private String regionsPath = ConfigUtil.getValue("regionsPath", "conf.properties");
    private FileUtil fileUtil = FileUtil.getInstance();

    public void XMatrixGen() throws IOException {
        String regionStr = fileUtil.readJsonFileToStr(new File(regionsPath));
        List<Region> regionList = JSON.parseArray(regionStr, Region.class);

        String poiStr = fileUtil.readJsonFileToStr(new File(poiPath));
        List<POI> poiList = JSON.parseArray(poiStr, POI.class);

    }

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
//        new XMatrix().XMatrixGen();
        new XMatrix().simplePOIData();
    }
}
