package com.fairy.data.weibo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fairy.pojo.KeyWord;
import com.fairy.utils.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * KeyWordExtractor 获取关键字 结果：keywords****.json
 * KeyWordExtractorPlus 对关键字进行排序  结果：keywords****_plus.json
 */
public class KeyWordExtractorPlus {

    private String keyWordPath = "fairy-data/data/weibo/keywords0101.json";
    private String keyWordPath_02 = "fairy-data/data/weibo/keywords0101_plus.json";
    private FileUtil fileUtil = FileUtil.getInstance();


    public void extract() throws IOException {
       List<List<KeyWord>> list = toKeyWordList(keyWordPath);

       List<KeyWord> resultList = new ArrayList<KeyWord>();
        for (List<KeyWord> listItem : list) {
            for (KeyWord keyWordItem : listItem) {
                addTo(keyWordItem, resultList);
            }
        }


        Collections.sort(resultList);
        StringBuffer sb = new StringBuffer();
        BufferedWriter bw = fileUtil.getBufferedWriter(new File(keyWordPath_02));
        for (KeyWord keyword : resultList) {
            System.out.println(keyword.toString());
            sb.append(keyword.toString());
            sb.append("\n");
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    /**
     * 将keyword加入集合满足的条件：
     * 1. 集合list中不存在keyword
     * 2. 若存在则使用给分的keyword替代低分的keyword
     * @param keyWord01
     * @param result02
     */
    private void addTo(KeyWord keyWord01, List<KeyWord> result02) {

        String word01 = keyWord01.getWord();
        double score01 = keyWord01.getScore();

        boolean flag = false;
        KeyWord keyWord02 = null;
        for (int i = 0; i < result02.size(); i++) {
            keyWord02 = result02.get(i);
            String word02 = keyWord02.getWord();
            double score02 = keyWord02.getScore();

            //存在
            if(word01.contains(word02) || word02.contains(word01)){
                flag = true;
                if (score01 >= score02) {

                    keyWord02.setScore((float) score01);
//                    result02.remove(keyWord02);
//                    result02.add(keyWord01);
                    break;
                }
            }
        }
        if(!flag) result02.add(keyWord01);
    }

    private boolean doContain(KeyWord keyWord, List<KeyWord> result02) {

        String word01 = keyWord.getWord();
        String word02 = null;
        for (KeyWord keyWordItem: result02) {
            word02 = keyWordItem.getWord();
            if(word01.contains(word02)&&word02.contains(word01)){
                return true;
            }
        }
        return false;
    }

    private List<List<KeyWord>> toKeyWordList(String keyWordPath) throws IOException {
        String jsonStr = fileUtil.readJsonFileToStr(new File(keyWordPath));
        List<List<KeyWord>> list = JSON.parseObject(jsonStr,
                new TypeReference<List<List<KeyWord>>>(){});
        return list;
    }

    public static void main(String[] args) throws IOException {
        new KeyWordExtractorPlus().extract();

//        List<String> list = new ArrayList<String>();
//        list.add("hello");
//        list.add("world");
//        list.remove("hello");
//        System.out.println(list);

    }
}
