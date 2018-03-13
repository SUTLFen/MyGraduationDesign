package com.fairy.data.kakou;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fairy.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class TimeViewDataConvert {

    private String rawDataPath = "fairy-data/data/kakou/hourly_flow_raw.json";
    private String outPath = "fairy-data/data/kakou/CarFlowHourly.json";
    private FileUtil fileUtil = FileUtil.getInstance();

    public void convert() throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(rawDataPath));

        Map<String, HourlyCount> rawDataMap = JSON.parseObject(jsonStr,
                new TypeReference<Map<String, HourlyCount>>(){});

        List<HourlyCount> resultList = new ArrayList<HourlyCount>();

        Set<String> keySet = rawDataMap.keySet();
        HourlyCount hourlyCount = null;
        for (String keyStr : keySet) {
            hourlyCount = rawDataMap.get(keyStr);
            resultList.add(hourlyCount);
        }

        String resultStr = JSON.toJSONString(resultList, true);
        fileUtil.saveToFile(outPath, resultStr, false);
    }

    public static void main(String[] args) throws IOException {
        new TimeViewDataConvert().convert();
    }
}
