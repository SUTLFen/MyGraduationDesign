package com.fairy.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fairy.pojo.Grid;
import com.fairy.pojo.GridVector;
import com.fairy.pojo.KK;
import com.fairy.pojo.POI;
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

    public static List<GridVector> toGridVectorList(String gridVectorPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(gridVectorPath));
        List<GridVector> gridVectorList = JSON.parseArray(jsonStr, GridVector.class);
        return gridVectorList;
    }
}
