package com.fairy.data.kakou;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fairy.utils.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 将CarFlowHourly.json转为CarFlowHourly.csv
 * .csv文件中的字段 hour, count
 */
public class TimeViewDataConvert_02 {

    private String rawDataPath = "fairy-data/data/kakou/CarFlowHourly.json";
    private String outPath = "fairy-data/data/kakou/CarFlowHourly.csv";
    private FileUtil fileUtil = FileUtil.getInstance();

    public void convert() throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(rawDataPath));

        List<HourlyCount> list = JSON.parseArray(jsonStr, HourlyCount.class);

        File outFile = new File(outPath);
        if(!outFile.exists()) outFile.createNewFile();
        BufferedWriter bw = fileUtil.getBufferedWriter(outFile);

        bw.append("hour,count");
        bw.append("\n");
        bw.flush();

        String hour;
        int count;
        for (HourlyCount item: list) {
            hour = item.getHourStr();
            count = item.getCount();
            bw.append(hour + "," + count);
            bw.append("\n");
            bw.flush();
        }

    }

    public static void main(String[] args) throws IOException {
        new TimeViewDataConvert_02().convert();
    }


}
