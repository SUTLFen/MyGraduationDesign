package com.fairy.data.preprocess;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.Grid;
import com.fairy.pojo.PrCar;
import com.fairy.util.ConfigUtil;
import com.fairy.util.GridUtil;
import com.fairy.util.JSONUtil;
import com.fairy.util.TimeUtil;
import com.fairy.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 输入：卡口数据
 * 输出：Time*Region
 */
public class YMatrix {
    private String prCarDir = "fairy-data/data/01_PrCarNew";
    private String YMatrixPath = "fairy-data/data/YMatrix.json";
    private String gridsPath;
    private int time_v; //时间维度

    private FileUtil fileUtil = FileUtil.getInstance();

    public void initParam(){
        gridsPath = ConfigUtil.getValue("gridPath", "conf.properties");
        String time_v_str = ConfigUtil.getValue("time_v", "conf.properties");
        time_v = Integer.valueOf(time_v_str);
    }

    public void YMatrixGen() throws IOException {
        initParam();

//        String gridsJSONStr =  fileUtil.readJsonFileToStr(new File(gridsPath));
//        List<Grid> gridsList = JSON.parseArray(gridsJSONStr, Grid.class);

        List<Grid> gridsList = JSONUtil.toGrid(gridsPath);

        File prCarDir = new File(this.prCarDir);
        File[] prCarFiles = prCarDir.listFiles();

        int[][] matrixY = new int[24][gridsList.size()];
        int[][] temp = null;

        String str;
        for (int i = 0; i < 1; i++) {
            temp =  handleSingleFile(prCarFiles[i], gridsList);
            str = JSON.toJSONString(temp, true);
            fileUtil.saveToFile("fairy-data/data/01_PrCarNew/testMatrixY.josn",
                    str, false);
            matrixY = matrixPlus(matrixY, temp);
        }

//        String jsonStr = JSON.toJSONString(matrixY, true);
//        fileUtil.saveToFile(YMatrixPath, jsonStr, false);


    }

    private int[][] matrixPlus(int[][] matrixY, int[][] temp) {
        for (int i = 0; i < matrixY.length; i++) {
            for (int j = 0; j < matrixY[i].length; j++) {
                matrixY[i][j] += temp[i][j];
            }
        }
        return matrixY;
    }

    private int[][] handleSingleFile(File prCarFile, List<Grid> gridsList) throws IOException {
        List<PrCar> prCarList = toPrCarList(prCarFile);

        int[][] matrixY = new int[time_v][gridsList.size()];
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < gridsList.size(); j++) {
                for (PrCar prCarTmp : prCarList) {
                    if(TimeUtil.isInTime(prCarTmp, i) &&
                            GridUtil.isInGrid(prCarTmp, gridsList.get(i))){
                        matrixY[i][j]++;
                    }
                }
            }
        }
        return matrixY;
    }

    private List<PrCar> toPrCarList(File prCarFile) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(prCarFile);
        List<PrCar> list = JSON.parseArray(jsonStr, PrCar.class);
        return list;
    }


    public static void main(String[] args) throws IOException {
        new YMatrix().YMatrixGen();
    }
}
