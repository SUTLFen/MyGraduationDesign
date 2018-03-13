package com.fairy.data.spatial;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.Grid;
import com.fairy.pojo.GridVector;
import com.fairy.util.JSONUtil;
import com.fairy.utils.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GridData.json : 网格左上角和右下角坐标
 * GridVector.json: 每个网格的特征向量
 */

public class GridVectorGenerator {

    private String XMatrixPath = "fairy-data/data/XMatrix.json";
    private String gridsPath = "fairy-data/data/spatial/GridData.json";
    private String gridVectorPath = "fairy-data/data/spatial/GridVector.json";

    private FileUtil fileUtil = FileUtil.getInstance();

    public void generate() throws IOException {
       int[][] XMatrix = JSONUtil.toXMatrix(XMatrixPath);
       List<Grid> grids = JSONUtil.toGrid(gridsPath);

       List<GridVector> gridsVectorList = new ArrayList<GridVector>();

       GridVector gridVector = null;
       Grid gridCur = null;
       for (int i = 0; i < grids.size(); i++) {
            gridCur = grids.get(i);

            gridVector = new GridVector();

            gridVector.setLat01(gridCur.getLat01());
            gridVector.setLng01(gridCur.getLng01());
            gridVector.setLat02(gridCur.getLat02());
            gridVector.setLng02(gridCur.getLng02());
            gridVector.setVectors(XMatrix[i]);

            gridsVectorList.add(gridVector);
        }

        String resultStr = JSON.toJSONString(gridsVectorList, true);
       fileUtil.saveToFile(gridVectorPath, resultStr, false);
    }

    public static void main(String[] args) throws IOException {
        new GridVectorGenerator().generate();
    }
}
