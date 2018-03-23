package com.fairy.data.weibo;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.KeyWord;
import com.fairy.utils.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 在KeyWordExtractorTriple之后，选取前100个关键字，存于json文件中，keywords0101_selected.json
 */
public class KeyWordExtractorTriple {

    private String rawPath = "fairy-data/data/weibo/keywords0101_plus.json";
    private String outPath = "fairy-data/data/weibo/keywords0101_selected.json";

    private FileUtil fileUtil = FileUtil.getInstance();

    public void core() throws IOException {
        BufferedReader br = fileUtil.getBufferedReader(new File(rawPath));
        String line = null;
        String[] strs = null;

        List<String> resultList = new ArrayList<String>();
        int i = 0;
        while((line = br.readLine()) != null && i < 100){
            strs = line.split(":");
            resultList.add(strs[0]);
            i++;
        }

        String jsonStr = JSON.toJSONString(resultList);
        fileUtil.saveToFile(outPath, jsonStr, false);
    }

    public static void main(String[] args) throws IOException {
        new KeyWordExtractorTriple().core();
    }
}
