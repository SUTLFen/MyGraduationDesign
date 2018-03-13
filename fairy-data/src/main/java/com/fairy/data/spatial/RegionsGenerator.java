package com.fairy.data.spatial;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.Grid;
import com.fairy.pojo.KK;
import com.fairy.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * spatial//GridData.json中存储了每个网格的左上角和右下角地理坐标
 * 由网格地理坐标，将属于此网格的所有卡口存在同一个数组中。
 *
 * kk_info.json：所有卡口
 * allKKInfo_gps.json: 去除重复卡口id后的卡口
 */
public class RegionsGenerator {

    private String gridsPath = "fairy-data/data/spatial/GridData.json";
    private String kkDataPath = "fairy-data/data/allKKInfo_gps.json";
    private String regionsPath = "fairy-data/data/spatial/regions_kk.json"; //每个区域对应的所有卡口  最后输出结果

    private FileUtil fileUtil = FileUtil.getInstance();

    public void generate() throws IOException {
        List<Grid> grids = toGrids(gridsPath);
        List<KK> kks = toKKs(kkDataPath);

        List<ArrayList<KK>> regions = new ArrayList<ArrayList<KK>>();

        KK kkCur = null;
        ArrayList<KK> regionCur = null;
        for (int i = 0; i < grids.size(); i++) {
            Grid gridCur = grids.get(i);
            regionCur = new ArrayList<KK>();
            for (int j = 0; j < kks.size(); j++) {
                kkCur = kks.get(j);
                if(doContain(kkCur, gridCur)){
                    regionCur.add(kkCur);
                }
            }
            regions.add(regionCur);
        }

        String regionsStr = JSON.toJSONString(regions, true);
        fileUtil.saveToFile(regionsPath, regionsStr, false);

    }

    private boolean doContain(KK kkCur, Grid gridCur) {
        double kkLat = Double.valueOf(kkCur.getLat());
        double kkLng = Double.valueOf(kkCur.getLng());

        boolean flag = false;
        if(kkLat >= gridCur.lat02 && kkLat <= gridCur.lat01){
            if(kkLng >= gridCur.lng01 && kkLng <= gridCur.lng02 ){
                flag = true;
            }
        }
        return flag;
    }

    private List<KK> toKKs(String kkDataPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(kkDataPath));
        List<KK> kkList =JSON.parseArray(jsonStr, KK.class);
        return kkList;
    }

    private List<Grid> toGrids(String gridsPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(gridsPath));
        List<Grid> gridList =JSON.parseArray(jsonStr, Grid.class);
        return gridList;
    }


    public static void main(String[] args) throws IOException {
        new RegionsGenerator().generate();
    }


}
