package com.fairy.data.spatial;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.Grid;
import com.fairy.pojo.GridVector;
import com.fairy.pojo.MaxMin;
import com.fairy.util.ConfigUtil;
import com.fairy.util.JSONUtil;
import com.fairy.utils.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridEntropyCal {


    private String XMatrixPath = "fairy-data/data/XMatrix.json";
    private String gridVectorPath = "fairy-data/data/spatial/GridVector.json";

    private FileUtil fileUtil = FileUtil.getInstance();

    public void cal() throws IOException {
        int[][] XMatrix = JSONUtil.toXMatrix(XMatrixPath);
        List<GridVector> gridVectorList = JSONUtil.toGridVectorList(gridVectorPath);

        List<MaxMin> MaxMinArray = findMaxMin(XMatrix);

        GridVector gridVectorCur = null;
        float entropy;
        int[] vectorCur = null;
        for (int i = 0; i < gridVectorList.size(); i++) {
            gridVectorCur = gridVectorList.get(i);
            vectorCur = gridVectorCur.getVectors();
            entropy = calEntropy(vectorCur, MaxMinArray);

        }

    }

    private float calEntropy(int[] vectorCur, List<MaxMin> maxMinArray) {

        //特征向量标准化
        float[] nomarlizedVector = new float[vectorCur.length];
        MaxMin maxMinCur = null;
        for (int i = 0; i < vectorCur.length; i++) {
            maxMinCur = maxMinArray.get(i);
            int max = maxMinCur.getMax();
            int min = maxMinCur.getMin();
            nomarlizedVector[i] = (float) ((vectorCur[i] - min)/(max - min - 0.0));
        }

        //继续。。。和为1，
        float[] nomarlizedVector02 = new float[nomarlizedVector.length];
        float sum = 0.0f;
        for (int j = 0; j < nomarlizedVector.length; j++) {
            sum += nomarlizedVector[j];
        }

        for (int k = 0; k < nomarlizedVector.length; k++) {
            nomarlizedVector02[k] = nomarlizedVector[k] / sum;
        }

//       计算熵值
        float entropyValue = 0.0f;
        for (int i = 0; i < nomarlizedVector02.length; i++) {
            entropyValue += nomarlizedVector02[i] * Math.log(nomarlizedVector02[i]);
        }

        return entropyValue;
    }

    private List<MaxMin> findMaxMin(int[][] xMatrix) {
        int poi_v = Integer.valueOf(ConfigUtil.getValue("poi_v",
                "conf.properties"));

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
        new GridEntropyCal().cal();
    }
}
