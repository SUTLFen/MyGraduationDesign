package com.fairy.data.preprocess;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.PrCarOld;
import com.fairy.pojo.Region;
import com.fairy.util.ConfigUtil;
import com.fairy.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 输入：卡口数据
 * 输出：Time*Region
 */
public class YMatrix {
    private String prCarDir = "fairy-data/data/01_PrCar";
    private String YMatrixPath = "fairy-data/data/YMatrix.json";
    private String regionsPath;

    private FileUtil fileUtil = FileUtil.getInstance();

    public void initParam(){
        regionsPath = ConfigUtil.getValue("regionsPath", "conf.properties");
    }

    public void YMatrixGen() throws IOException {
        initParam();

        String regionsJSONStr =  fileUtil.readJsonFileToStr(new File(regionsPath));
        List<Region> regionsList = JSON.parseArray(regionsJSONStr, Region.class);

        List<PrCarOld> prCarOldList = toPrCarList(prCarDir);

        int[][] matrixY = new int[24][regionsList.size()];

        for (int i = 0; i < 24; i++) {

            for (int j = 0; j < regionsList.size(); j++) {
                for (PrCarOld prCarOld : prCarOldList) {

                }
            }
        }
    }

    private List<PrCarOld> toPrCarList(String prCarDir) throws IOException {
        File prCarParentPath = new File(prCarDir);
        File[] prCarFiles = prCarParentPath.listFiles();

        String jsonStr;
        List<PrCarOld> list;
        List<PrCarOld> resultList = new ArrayList<>();
        for (int i = 0; i < prCarFiles.length; i++) {
            jsonStr = fileUtil.readJsonFileToStr(prCarFiles[i]);
            list = JSON.parseArray(jsonStr, PrCarOld.class);
            resultList.addAll(list);
        }

        return resultList;
    }

    public static void main(String[] args) throws IOException {
//        new YMatrix().YMatrixGen();


    }
}
