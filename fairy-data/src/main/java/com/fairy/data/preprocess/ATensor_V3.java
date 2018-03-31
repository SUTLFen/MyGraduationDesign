package com.fairy.data.preprocess;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.Grid;
import com.fairy.pojo.WeiboFields;
import com.fairy.util.ConfigUtil;
import com.fairy.util.JSONUtil;
import com.fairy.util.LuceneUtil;
import com.fairy.utils.DateUtil;
import com.fairy.utils.FileUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ATensor_V3 {
    private String dateStr = "2016-01-01";
    private String indexPath = ConfigUtil.getValue("indexPath",
            "conf.properties");

    private String keyWordsPath = "fairy-data/data/weibo/keywords0101_selected.json";
    private String gridDataPath = "fairy-data/data/spatial/GridData.json"; //区域
    private String outPath = "fairy-data/data/preprocess/ATensor0101.json";

    private FileUtil fileUtil = FileUtil.getInstance();
    private DateUtil dateUtil = DateUtil.getInstance();

    private IndexSearcher indexSearcher = null;


    public void generate() throws IOException, ParseException {
        List<String> keyWordsList = JSONUtil.toKeyWordList(keyWordsPath);
        List<Grid> gridsList = JSONUtil.toGrid(gridDataPath);

        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher(indexPath + "\\" + dateStr);
        Grid gridCur = null;

        //ATensor 结果
        List<List<Integer>> resultList = new ArrayList<List<Integer>>();

        for (int i = 0; i < gridsList.size(); i++) {
            gridCur = gridsList.get(i);
            List<Integer> keyWordsInGrid = calKeyWordsInSingleGrid(gridCur, keyWordsList, indexSearcher);

            System.out.println(keyWordsInGrid);
            resultList.add(keyWordsInGrid);
        }
        String jsonStr = JSON.toJSONString(resultList, true);
        fileUtil.saveToFile(outPath, jsonStr, false);

    }

    /**
     * 指定区域gridCur，每个关键词在24小时的权重（出现此关键词的微博数量）
     * @param gridCur  指定区域
     * @param keyWordsList   提取当天的关键词
     * @param indexSearcher
     * @return
     * @throws ParseException
     * @throws IOException
     */
    private List<Integer> calKeyWordsInSingleGrid(Grid gridCur, List<String> keyWordsList,
                                                  IndexSearcher indexSearcher) throws ParseException, IOException {
        Query timeQuery;

        String keyWordCur = null;
        String startTimeStr, endTimeStr;
        long startTime, endTime;
        ScoreDoc[] scoreDocs = null;
        int count;
        List<Integer> countsInRegion = new ArrayList<>();  //每个Region的关键词的weight
        BooleanQuery booleanQuery;

        Query lngQuery = DoublePoint.newRangeQuery("Longitude", gridCur.getLat02(), gridCur.getLat01());
        Query latQuery = DoublePoint.newRangeQuery("Latitude", gridCur.getLng01(), gridCur.getLng02());

        for (int i = 0; i < 24; i++) {

            startTimeStr = dateUtil.newStartTime(dateStr, i);
            endTimeStr = dateUtil.newEndTime(dateStr, i);
            startTime = dateUtil.getTime(startTimeStr);
            endTime = dateUtil.getTime(endTimeStr);

            timeQuery = DoublePoint.newRangeQuery(WeiboFields.time, startTime, endTime);
            booleanQuery =  new BooleanQuery.Builder()
                    .add(timeQuery, BooleanClause.Occur.MUST)
                    .add(latQuery, BooleanClause.Occur.MUST)
                    .add(lngQuery, BooleanClause.Occur.MUST)
                    .build();

            scoreDocs = indexSearcher.search(booleanQuery, 100000).scoreDocs;
//            System.out.println(scoreDocs.length);

            for (int j = 0; j < keyWordsList.size(); j++) {
                keyWordCur = keyWordsList.get(j);

                //包含关键字的微博数
                count = countOfWeiboContainKeyWord(keyWordCur, scoreDocs, indexSearcher);
                countsInRegion.add(count);
            }
        }

        return countsInRegion;
    }

    /**
     * 在查询结果scoreDocs中，包含关键词的所有微博数
     * @param keyWordCur  关键词
     * @param scoreDocs   查询结果（指定区域指定时间内的所有微博）
     * @param indexSearcher
     * @return
     * @throws IOException
     */
    private int countOfWeiboContainKeyWord(String keyWordCur, ScoreDoc[] scoreDocs, IndexSearcher indexSearcher) throws IOException {
        Document doc = null;
        String contentStr = null;
        int count = 0;
        for (int i = 0; i < scoreDocs.length; i++) {
            doc = indexSearcher.doc(scoreDocs[i].doc);
            contentStr = doc.getField("Content").stringValue();
            if(contentStr.contains(keyWordCur)){
                count++;
            }
        }
        return count;
    }


    public static void main(String[] args) throws IOException, ParseException {
        new ATensor_V3().generate();
    }
}
