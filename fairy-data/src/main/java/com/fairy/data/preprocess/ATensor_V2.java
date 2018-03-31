package com.fairy.data.preprocess;

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

@Deprecated
public class ATensor_V2 {
    private String dateStr = "2016-01-01";
    private String indexPath = ConfigUtil.getValue("indexPath",
            "conf.properties");

    private String keyWordsPath = "fairy-data/data/weibo/keywords0101_selected.json";
    private String gridDataPath = "fairy-data/data/spatial/GridData.json"; //区域
    private FileUtil fileUtil = FileUtil.getInstance();
    private DateUtil dateUtil = DateUtil.getInstance();

    private IndexSearcher indexSearcher = null;


    public void generate() throws IOException, ParseException {
        List<String> keyWordsList = JSONUtil.toKeyWordList(keyWordsPath);
        List<Grid> gridsList = JSONUtil.toGrid(gridDataPath);

        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher(indexPath + "\\" + dateStr);

        Grid gridCur = null;
        for (int i = 0; i < gridsList.size(); i++) {
            gridCur = gridsList.get(i);
            List<Integer> keyWordsInGrid = calKeyWordsInSingleGrid(gridCur, keyWordsList, indexSearcher);
        }
    }

    //每个区域---
    private List<Integer> calKeyWordsInSingleGrid(Grid gridCur, List<String> keyWordsList,
                                                  IndexSearcher indexSearcher) throws ParseException, IOException {
        Query timeQuery , latQuery, lngQuery;

        String keyWordCur = null;
        String startTimeStr, endTimeStr;
        long startTime, endTime;
        ScoreDoc[] scoreDocs = null;
        int count;
        List<Integer> countsInRegion = new ArrayList<>();  //每个Region的关键词的weight
        BooleanQuery booleanQuery;

        for (int i = 0; i < keyWordsList.size(); i++) {
            keyWordCur = keyWordsList.get(i);

            for (int j = 0; j < 24; j++) {
                startTimeStr = dateUtil.newStartTime(dateStr, j);
                endTimeStr = dateUtil.newEndTime(dateStr, j);
                startTime = dateUtil.getTime(startTimeStr);
                endTime = dateUtil.getTime(endTimeStr);

                timeQuery = DoublePoint.newRangeQuery(WeiboFields.time, startTime, endTime);
                latQuery = DoublePoint.newRangeQuery("Longitude", gridCur.getLat02(), gridCur.getLat01());
                lngQuery = DoublePoint.newRangeQuery("Latitude", gridCur.getLng01(), gridCur.getLng02());
                booleanQuery =  new BooleanQuery.Builder()
                        .add(timeQuery, BooleanClause.Occur.MUST)
                        .add(latQuery, BooleanClause.Occur.MUST)
                        .add(lngQuery, BooleanClause.Occur.MUST)
                        .build();

                scoreDocs = indexSearcher.search(latQuery, 100000).scoreDocs;

                System.out.println(scoreDocs.length);

                //包含关键字的微博数
//                System.out.print(keyWordCur + " : ");
                count = countOfWeiboContainKeyWord(keyWordCur, scoreDocs, indexSearcher);
//                System.out.println(count);

                countsInRegion.add(count);
            }
        }

        return null;
    }

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
        new ATensor_V2().generate();
    }
}
