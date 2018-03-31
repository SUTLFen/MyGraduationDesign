package com.fairy.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fairy.pojo.*;
import com.fairy.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONUtil {

    private static FileUtil fileUtil = FileUtil.getInstance();


    //regions
    public static ArrayList<ArrayList<KK>> toRegions(String regionsPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(regionsPath));
        ArrayList<ArrayList<KK>> regions = JSON.parseObject(jsonStr,
                new TypeReference<ArrayList<ArrayList<KK>>>() {
                });
        return regions;
    }

    //poi
    public static List<POI> toPOIList(String POIPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(POIPath));
        List<POI> POIList = JSON.parseArray(jsonStr, POI.class);
        return POIList;
    }

    //Grid
    public static List<Grid> toGrid(String gridPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(gridPath));
        List<Grid> gridList = JSON.parseArray(jsonStr, Grid.class);
        return gridList;
    }

    public static int[][] toXMatrix(String xMatrixPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(xMatrixPath));
        int[][] XMatrix = JSON.parseObject(jsonStr, int[][].class);

        return XMatrix;
    }

    //GridVector
    public static List<GridVector> toGridVectorList(String gridVectorPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(gridVectorPath));
        List<GridVector> gridVectorList = JSON.parseArray(jsonStr, GridVector.class);
        return gridVectorList;
    }

    //关键字json
    public static List<String> toKeyWordList(String keyWordPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(keyWordPath));
        List<String> keyWordList = JSON.parseArray(jsonStr, String.class);
        return keyWordList;
    }

    //weibo
    public static List<Weibo> toWeiboList(String weiboPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(weiboPath));
        List<Weibo> weiboList = JSON.parseArray(jsonStr, Weibo.class);
        return weiboList;
    }

    //ATensor
    public static List<List<Integer>> toATensor(String ATensorPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(ATensorPath));
        List<List<Integer>> ATensor = JSON.parseObject(jsonStr,
                new TypeReference<List<List<Integer>>>(){});
        return ATensor;
    }

}
