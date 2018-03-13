package com.fairy.data.kakou;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fairy.utils.FileUtil;
import org.apache.lucene.analysis.hi.HindiAnalyzer;
import org.joda.time.DateMidnight;

import javax.sound.midi.MidiChannel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TimeViewDataConvert_03 {

    private String rawDataPath = "fairy-data/data/kakou/hourly_flow_raw.json";
    private String outPath = "fairy-data/data/kakou/CarFlowHourly.csv";
    private FileUtil fileUtil = FileUtil.getInstance();

    public void convert() throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(rawDataPath));

        Map<String, HourlyCount> rawDataMap = JSON.parseObject(jsonStr,
                new TypeReference<Map<String, HourlyCount>>(){});


        File outFile = new File(outPath);
        if(!outFile.exists()) outFile.createNewFile();
        BufferedWriter bw = fileUtil.getBufferedWriter(outFile);

        bw.append("hour,count");
        bw.append("\n");
        bw.flush();

        HourlyCount hourlyCount = null;

        String dateStr, monthStr, hourStr;
        for (int MIndex = 1; MIndex <= 27 ; MIndex ++) {
            for (int HIndex = 0; HIndex <= 23; HIndex++) {
                if(MIndex < 10){
                    monthStr = "0" + MIndex;
                }else monthStr = MIndex + "";

                if(HIndex < 10){
                    hourStr = "0" + HIndex;
                }else hourStr = HIndex + "";

                if(monthStr.equals("01") && hourStr.equals("00")){
                    continue;
                }
                if(monthStr.equals("27") && hourStr.equals("18")){
                    break;
                }

                // “2016-01-01 00:00:00”
                dateStr = "2016-01" + "-" + monthStr + " " + hourStr + ":00:00";

                System.out.println(dateStr);
                hourlyCount = rawDataMap.get(dateStr);

                System.out.println(hourlyCount.toString());

                bw.append(hourlyCount.getHourStr() + "," + hourlyCount.getCount());
                bw.append("\n");
                bw.flush();

            }
        }


    }

    public static void main(String[] args) throws IOException {
        new TimeViewDataConvert_03().convert();
    }
}
