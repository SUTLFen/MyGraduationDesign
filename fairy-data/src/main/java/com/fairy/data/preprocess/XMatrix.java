package com.fairy.data.preprocess;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.Grid;
import com.fairy.pojo.POI;
import com.fairy.pojo.POISimple;
import com.fairy.util.ConfigUtil;
import com.fairy.util.GridUtil;
import com.fairy.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 输入：POI数据
 * 输出：矩阵Region*Features
 *      Grid特征向量
 */
public class XMatrix {

    private String poiPath = ConfigUtil.getValue("poiPath", "conf.properties");
    private String poiSimplePath = ConfigUtil.getValue("poiSimplePath", "conf.properties");
    private String gridPath = ConfigUtil.getValue("gridPath", "conf.properties");
    private String outPath = "fairy-data/data/preprocess/0101/XMatrix.json";

    private FileUtil fileUtil = FileUtil.getInstance();
    private int poi_v;

    public void initParam(){
        String poiVStr = ConfigUtil.getValue("poi_v", "conf.properties");
        poi_v = Integer.valueOf(poiVStr);
    }


    public void XMatrixGen() throws IOException {
        initParam();

        String gridStr = fileUtil.readJsonFileToStr(new File(gridPath));
        List<Grid> gridList = JSON.parseArray(gridStr, Grid.class);

        String poiSimpleStr = fileUtil.readJsonFileToStr(new File(poiSimplePath));
        List<POISimple> poiSimpleList = JSON.parseArray(poiSimpleStr, POISimple.class);


        int[][] matrixX = new int[gridList.size()][poi_v];
        Grid grid = null;
        for (int i = 0; i < gridList.size(); i++) {
            grid = gridList.get(i);
            for (POISimple poi : poiSimpleList) {
                if(GridUtil.isInGrid(grid, poi)){
                    int poiIndex = toPOIIndex(poi);
                    if(poiIndex > 0){
                        matrixX[i][poiIndex - 5]++;
                    }
                }
            }
        }

        String jsonStr = JSON.toJSONString(matrixX);
        fileUtil.saveToFile(outPath, jsonStr, false);
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
    }

    private void collectPOIType() throws IOException {

        String[] result = new String[100];

        String poiSimpleStr = fileUtil.readJsonFileToStr(new File(poiSimplePath));
        List<POISimple> poiSimpleList = JSON.parseArray(poiSimpleStr, POISimple.class);

        POISimple poiSimple = null;
        for (int i = 0; i < poiSimpleList.size(); i++) {
            poiSimple = poiSimpleList.get(i);
            int poiIndex = toPOIIndex(poiSimple);
            System.out.println(poiIndex);
            result[poiIndex] = poiSimple.getType();
        }

        String poiTypeStr = JSON.toJSONString(result);
        fileUtil.saveToFile("fairy-data/data/poiType.json", poiTypeStr, false);
    }
}
