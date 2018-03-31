package com.fairy.data.spatial;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.GridVector;
import com.fairy.pojo.MaxMin;
import com.fairy.util.ConfigUtil;
import com.fairy.util.JSONUtil;
import com.fairy.utils.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 对Grid（区域）特征向量进行max-min标准化
 */
public class GridVectorNormal {


    private String XMatrixPath = "fairy-data/data/XMatrix.json";
    private String gridVectorPath = "fairy-data/data/spatial/GridVector.json";
    private String outPath = "fairy-data/data/spatial/GridVectorNormal.json";

    private final int dimension_poi = Integer.valueOf(ConfigUtil.getValue("poi_v",
            "conf.properties"));

    private FileUtil fileUtil = FileUtil.getInstance();

    public void normalize() throws IOException {
        int[][] XMatrix = JSONUtil.toXMatrix(XMatrixPath);
        List<GridVector> gridVectorList = JSONUtil.toGridVectorList(gridVectorPath);
        List<MaxMin> MaxMinArray = findMaxMin(XMatrix);

        GridVector gridVectorCur = null;
        float entropy;
        int[] vectorCur = null;

        List<float[]> resultList = new ArrayList<float[]>();
        float[] vectorNormalized = null;

        for (int i = 0; i < gridVectorList.size(); i++) {

            gridVectorCur = gridVectorList.get(i);
            vectorCur = gridVectorCur.getVectors();
            System.out.println(vectorCur);

            vectorNormalized = normalizedSingleGridVector(vectorCur, MaxMinArray);
            resultList.add(vectorNormalized);
        }

        String jsonStr = JSON.toJSONString(resultList, true);
        fileUtil.saveToFile(outPath, jsonStr, false);
    }

    private float[] normalizedSingleGridVector(int[] vectorCur, List<MaxMin> maxMinArray) {

        //特征向量标准化
        float[] nomarlizedVector = new float[vectorCur.length];
        MaxMin maxMinCur = null;
        for (int i = 0; i < vectorCur.length; i++) {
            maxMinCur = maxMinArray.get(i);
            int max = maxMinCur.getMax();
            int min = maxMinCur.getMin();
            nomarlizedVector[i] = (float) ((vectorCur[i] - min)/(max - min - 0.0));
//            System.out.print(nomarlizedVector[i]+"  ");
        }

        return nomarlizedVector;
    }

    private List<MaxMin> findMaxMin(int[][] xMatrix) {

        String valueStr = ConfigUtil.getValue("poi_v",
                "conf.properties");
        int poi_v = Integer.valueOf(valueStr);

        List<MaxMin> MaxMinList = new ArrayList<MaxMin>();

        MaxMin maxmin = null;
        if(xMatrix.length > 1){
            for (int j = 0; j < poi_v; j++) {
                maxmin = new MaxMin();
                int maxTmp = xMatrix[0][j];
                int minTmp = xMatrix[0][j];

                for (int i = 1 ; i < xMatrix.length; i++){
                    if(xMatrix[i][j] > maxTmp){
                        maxTmp = xMatrix[i][j];
                    }
                    if(xMatrix[i][j] < minTmp){
                        minTmp = xMatrix[i][j];
                    }
                }

                maxmin.max = maxTmp;
                maxmin.min = minTmp;

                MaxMinList.add(maxmin);
            }
        }
        return  MaxMinList;
    }

    public static void main(String[] args) throws IOException {
        new GridVectorNormal().normalize();
    }

}

