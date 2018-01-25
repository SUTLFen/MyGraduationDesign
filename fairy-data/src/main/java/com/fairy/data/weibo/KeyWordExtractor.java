package com.fairy.data.weibo;

import com.alibaba.fastjson.JSON;
import com.fairy.hotword.HotWordExtractor;
import com.fairy.hotword.Result;
import com.fairy.pojo.KeyWord;
import com.fairy.pojo.WeiboFields;
import com.fairy.util.ConfigUtil;
import com.fairy.util.LuceneUtil;
import com.fairy.utils.FileUtil;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取微博关键，以每日为单位
 */

public class KeyWordExtractor {

    private String indexPath = null;
    private IndexReader indexReader = null;
    private FileUtil fileUtil = FileUtil.getInstance();

    public void initParam() throws IOException {
        indexPath = ConfigUtil.getValue("indexPath", "config.properties");
        indexReader = LuceneUtil.getIndexReader(indexPath + "\\2016-01-08");
    }

    public void extract() throws IOException {

        initParam();

        HotWordExtractor extractor = new HotWordExtractor(indexReader);
        List<Result> list = null;

        File file = new File("fairy-data/data/weibo/keywords.txt");
        BufferedWriter bw = fileUtil.getBufferedWriter(file);

        List<KeyWord> keyWordList = new ArrayList<KeyWord>();
        KeyWord keyWord = null;
        for (int docId = 0; docId < indexReader.maxDoc(); docId++) {
            list = extractor.extract(docId, 20, true);
            if (list != null) {
                for (Result s : list) {
                    keyWord = new KeyWord(s);
                    keyWordList.add(keyWord);
//                    System.out.println(s.getTerm() + " : " + s.getFrequency() + " : " + s.getScore());
//                    bw.write(s.getTerm() + " : " + s.getFrequency() + " : " + s.getScore());
//                    bw.write("\n");
//                    bw.flush();
                }
            }
        }

        String jsonStr = JSON.toJSONString(keyWordList, true);
        fileUtil.saveToFile("fairy-data/data/weibo/keywords.json", jsonStr, false);
    }

    public static void main(String[] args) throws IOException {
        new KeyWordExtractor().extract();
    }

}
