package com.fairy.data.preprocess;


import com.alibaba.fastjson.JSON;
import com.fairy.pojo.Grid;
import com.fairy.pojo.Weibo;
import com.fairy.pojo.WeiboFields;
import com.fairy.util.*;
import com.fairy.utils.DateUtil;
import com.fairy.utils.FileUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.search.*;
import org.elasticsearch.common.lucene.Lucene;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ATensor {
    private String dateStr = "2016-01-01";

    private String keyWordsPath = "fairy-data/data/weibo/keywords0101_selected.json";
    private String gridDataPath = "fairy-data/data/spatial/GridData.json"; //区域
    private String weiboPath = "D:\\Users\\liuf\\03_Data\\weibo\\weibo_data_json\\"
            + dateStr + ".json";

    private String outPath = "fairy-data/data/ATensor0101.json";

    private FileUtil fileUtil = FileUtil.getInstance();
    private DateUtil dateUtil = DateUtil.getInstance();


    public void generate() throws IOException, ParseException {
        List<String> keyWordsList = JSONUtil.toKeyWordList(keyWordsPath);
        List<Grid> gridsList = JSONUtil.toGrid(gridDataPath);
        List<Weibo> weiboList = JSONUtil.toWeiboList(weiboPath);

        List<Integer> keyWordsInGrid = null;
        List<List<Integer>> result = new ArrayList<>();
        Grid gridCur = null;

        for (int i = 0; i < gridsList.size(); i++) {
            gridCur = gridsList.get(i);
            keyWordsInGrid = calKeyWordsWeightOfSomeGrid(gridCur, keyWordsList, weiboList);
            System.out.println(keyWordsInGrid);
            result.add(keyWordsInGrid);
        }

        String jsonStr = JSON.toJSONString(result, true);
        fileUtil.saveToFile(outPath, jsonStr, false);
    }

    /**
     * 指定区域gridCur的。。。各关键词的权重
     * @param gridCur  指定区域
     * @param keyWordsList  dateStr的所有关键词
     * @param weiboList   dateStr的所有微博
     * @return
     * @throws ParseException
     */
    private List<Integer> calKeyWordsWeightOfSomeGrid(Grid gridCur, List<String> keyWordsList,
                                                      List<Weibo> weiboList) throws ParseException {
        String keyWordCur = null;
        List<Integer> resultList = new ArrayList<>();

        // i : hour
        for (int i = 0; i < 24; i++) {

            List<Weibo> weibosInEntry = findWeibosInGridAndTime(weiboList, gridCur, i);
            System.out.println("size : " + weibosInEntry.size());
            for (int j = 0; j < keyWordsList.size(); j++) {
                keyWordCur = keyWordsList.get(i);
                int count = weiboCount(keyWordCur, weibosInEntry);
                resultList.add(count);
            }
        }
        return resultList;
    }

    /**
     * 包含关键词keyWordCur的微博数量
     * @param keyWordCur
     * @param weibosInEntry
     * @return
     */
    private int weiboCount(String keyWordCur, List<Weibo> weibosInEntry) {

        String weiboContent = null;
        Weibo weiboCur = null;

        int count = 0;

        for (int i = 0; i < weibosInEntry.size(); i++) {
            weiboCur = weibosInEntry.get(i);
            weiboContent = weiboCur.getContent();
            if(weiboContent.contains(keyWordCur)){
                count++;
            }
        }
        return count;
    }

    /**
     * 整理出属于指定区域和指定时间段内所有微博
     * @param weiboList  dateStr中所有的微博
     * @param gridCur   指定区域
     * @param hour  指定时间 如i=0 , 则时间为dateStr + "00:00:00"
     * @return  指定时间和指定区域内所有的微博
     */
    private List<Weibo> findWeibosInGridAndTime(List<Weibo> weiboList, Grid gridCur, int hour) throws ParseException {
//        String startTimeStr = dateUtil.newStartTime(dateStr, hour);
//        String endTimeStr = dateUtil.newEndTime(dateStr, hour);
//
//        long startTime = dateUtil.getTime(startTimeStr);
//        long endTime = dateUtil.getTime(endTimeStr);

        List<Weibo> weiboResultList = new ArrayList<Weibo>();

        Weibo weiboCur = null;
        for (int i = 0; i < weiboList.size(); i++) {
            weiboCur = weiboList.get(i);

            if(GridUtil.isInGrid(gridCur, weiboCur)){
//                System.out.println("inGrid");
                if(TimeUtil.isInTime(weiboCur, hour)){

//                    System.out.println("inTime");
                    weiboResultList.add(weiboCur);
                }
            }
        }
        return weiboResultList;
    }

    public static void main(String[] args) throws IOException, ParseException {
        new ATensor().generate();
    }
}
