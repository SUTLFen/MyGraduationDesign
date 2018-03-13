package com.fairy.data.spatial;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.Grid;
import com.fairy.utils.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 地理网格数据处理
 */
public class GridDataHandler {

    private String rawPath = "fairy-data/data/spatial/RawGridData.txt";
    private String outPath = "fairy-data/data/spatial/GridData.json";

    private FileUtil fileUtil = FileUtil.getInstance();

    public  void convert() throws IOException {
        BufferedReader br = fileUtil.getBufferedReader(new File(rawPath));
        List<Grid> gridList = new ArrayList<>();

        String line = null;
        String[] strs = null;
        Grid grid = null;

        while((line = br.readLine()) != null){
            strs = line.split(",");
            if(strs.length >= 4){
                grid = new Grid();
                grid.lat01 = Double.valueOf(strs[0]);
                grid.lng01 = Double.valueOf(strs[1]);
                grid.lat02 = Double.valueOf(strs[2]);
                grid.lng02 = Double.valueOf(strs[3]);

                gridList.add(grid);
            }
        }

        String str = JSON.toJSONString(gridList, true);
        fileUtil.saveToFile(outPath, str, false);

    }

    public static void main(String[] args) throws IOException {
        new GridDataHandler().convert();
    }

}
