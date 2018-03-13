package com.fairy.data.od;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fairy.pojo.KK;
import com.fairy.pojo.ODPair;
import com.fairy.pojo.ODPairNew;
import com.fairy.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fairy_LFen on 2017/6/28.
 * 区域OD流量
 */
public class ODInRegionsGenerator {

    private String dataStr = "data_0104", segStr = "seg_04";

    private String rawPath_ODPair = "fairy-data\\data\\od\\"+dataStr+"\\"+ segStr;
    private String outPath_ODInRegions = "fairy-data\\data\\od\\"+dataStr+"\\result\\ODPairIn0101_"+segStr+".json";

    private String regionsPath = "fairy-data/data/spatial/regions.json";

    private FileUtil fileUtil = FileUtil.getInstance();

    public void generateODInRegions() throws IOException {

        ArrayList<ArrayList<KK>> regions = toRegions(regionsPath);
        ArrayList<ArrayList<String>> regionsNew = toRegionsNew(regions);


        int size = regions.size();
        ODPairNew[][] ODWeight_Arry = initODPairNewArry(size);

        File odPairParentFile = new File(rawPath_ODPair);
        File[] odPairFiles = odPairParentFile.listFiles();

        for (File odPairFile : odPairFiles) {
            calODFlowWeightInRegions(odPairFile, regionsNew, ODWeight_Arry);
        }

        printResultArry(ODWeight_Arry);

        String jsonStr = JSON.toJSONString(ODWeight_Arry, true);
        fileUtil.saveToFile(outPath_ODInRegions, jsonStr, false);
    }

    private ArrayList<ArrayList<String>> toRegionsNew(ArrayList<ArrayList<KK>> regions) {
        ArrayList<KK> regions_old = null;
        ArrayList<ArrayList<String>> regions_New = new ArrayList<ArrayList<String>>();
        ArrayList<String> region_new = null;

        KK kkCur = null;

        for (int i = 0; i < regions.size(); i++) {
            regions_old = regions.get(i);
            region_new = new ArrayList<String>();
            for (int j = 0; j < regions_old.size(); j++) {
                kkCur = regions_old.get(j);
                region_new.add(kkCur.getKkId());
            }
            regions_New.add(region_new);
        }
        return regions_New;
    }

    private ArrayList<ArrayList<KK>> toRegions(String regionsPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(regionsPath));
        ArrayList<ArrayList<KK>> regions = JSON.parseObject(jsonStr,
                new TypeReference<ArrayList<ArrayList<KK>>>() {
                });
        return regions;
    }

    /**
     * 1. 将ODPair中起始点，找到对应的Region；
     * 2. 根据Region，对应的ODWeight_arry加一。
     */
    private void calODFlowWeightInRegions(File odPairFile, ArrayList<ArrayList<String>> regionsNew,
                                          ODPairNew[][] odWeight_arry) throws IOException {
        List<ODPair> odPairList = toODPairList(odPairFile);
        int[] indexArry;
        for (ODPair odPair : odPairList) {
            indexArry = findReginIndexByODPair(regionsNew, odPair);
            if (indexArry != null) {
                int rowIndex = indexArry[0];
                int clumIndex = indexArry[1];
                 ODPairNew odPairNewTmp = odWeight_arry[rowIndex][clumIndex];
                 int weight = odPairNewTmp.getWeight();
                 odPairNewTmp.setWeight(++weight);
            }
        }
    }

    private int[] findReginIndexByODPair(ArrayList<ArrayList<String>> kkClusters_new, ODPair odPair) {
        int[] indexArry = new int[2];
        String o_kkid = odPair.getO_kkid();
        String d_kkid = odPair.getD_kkid();

        ArrayList<String> cluster = null;
        for (int i = 0; i < kkClusters_new.size(); i++) {
            cluster = kkClusters_new.get(i);
            if (cluster.contains(o_kkid)) {
                indexArry[0] = i;
            }
            if (cluster.contains(d_kkid)) {
                indexArry[1] = i;
            }
        }

        if (indexArry[0] >= 0 && indexArry[1] >= 0) {
            return indexArry;
        } else {
            return null;
        }
    }

    private List<ODPair> toODPairList(File odPairFile) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(odPairFile);
        List<ODPair> list = JSON.parseArray(jsonStr, ODPair.class);
        return list;
    }

    private void printResultArry(ODPairNew[][] resultArry) {
        System.out.println("----------------------------result------------------------------");
        for (int i = 0; i < resultArry.length; i++) {
            for (int j = 0; j < resultArry[i].length; j++) {
                System.out.print(resultArry[i][j].getWeight() + "  ");
            }
            System.out.println();
        }
    }

    private ODPairNew[][] initODPairNewArry(int size) {
        ODPairNew[][] odPairNewArry = new ODPairNew[size][size];
        for (int i = 0; i < odPairNewArry.length; i++) {
            for (int j = 0; j < odPairNewArry.length; j++) {
                odPairNewArry[i][j] = new ODPairNew();
                odPairNewArry[i][j].setO_regin_index(i);
                odPairNewArry[i][j].setD_region_index(j);
            }
        }
        return odPairNewArry;
    }

    public static void main(String[] args) throws IOException {
        new ODInRegionsGenerator().generateODInRegions();
    }
}
