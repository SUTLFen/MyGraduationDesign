package com.fairy.data.preprocess;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.PrCar;
import com.fairy.pojo.Region;
import com.fairy.util.ConfigUtil;
import com.fairy.util.RegionsUtil;
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
    private String regionsPath;
    private int time_v; //时间维度

    private FileUtil fileUtil = FileUtil.getInstance();

    public void initParam(){
        regionsPath = ConfigUtil.getValue("regionsPath", "conf.properties");
        String time_v_str = ConfigUtil.getValue("time_v", "conf.properties");
        time_v = Integer.valueOf(time_v_str);
    }

    public void YMatrixGen() throws IOException {
        initParam();

        String regionsJSONStr =  fileUtil.readJsonFileToStr(new File(regionsPath));
        List<Region> regionsList = JSON.parseArray(regionsJSONStr, Region.class);

        File prCarDir = new File(this.prCarDir);
        File[] prCarFiles = prCarDir.listFiles();

        int[][] matrixY = new int[24][regionsList.size()];
        int[][] temp = null;

        for (File prCarFile : prCarFiles) {
            temp =  handleSingleFile(prCarFile, regionsList);
            matrixY = matrixPlus(matrixY, temp);
        }

        String jsonStr = JSON.toJSONString(matrixY, true);
        fileUtil.saveToFile(YMatrixPath, jsonStr, false);


    }

    private int[][] matrixPlus(int[][] matrixY, int[][] temp) {
        for (int i = 0; i < matrixY.length; i++) {
            for (int j = 0; j < matrixY[i].length; j++) {
                matrixY[i][j] += temp[i][j];
            }
        }
        return matrixY;
    }

    private int[][] handleSingleFile(File prCarFile, List<Region> regionsList) throws IOException {
        List<PrCar> prCarList = toPrCarList(prCarFile);

        int[][] matrixY = new int[time_v][regionsList.size()];
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < regionsList.size(); j++) {
                for (PrCar prCarTmp : prCarList) {
                    if(TimeUtil.isInTime(prCarTmp, i) &&
                            RegionsUtil.isInRegion(prCarTmp, regionsList.get(i))){
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

//    private List<PrCarOld> toPrCarList(String prCarDir) throws IOException {
//        File prCarParentPath = new File(prCarDir);
//        File[] prCarFiles = prCarParentPath.listFiles();
//
//        String jsonStr;
//        List<PrCarOld> list;
//        List<PrCarOld> resultList = new ArrayList<>();
//        for (int i = 0; i < prCarFiles.length; i++) {
//            jsonStr = fileUtil.readJsonFileToStr(prCarFiles[i]);
//            list = JSON.parseArray(jsonStr, PrCarOld.class);
//            resultList.addAll(list);
//        }
//
//        return resultList;
//    }

    public static void main(String[] args) throws IOException {
//        new YMatrix().YMatrixGen();


    }
}
