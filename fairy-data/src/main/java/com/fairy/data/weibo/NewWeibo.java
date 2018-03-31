package com.fairy.data.weibo;

import com.alibaba.fastjson.JSON;
import com.fairy.indexing.IndexWeiboCore;
import com.fairy.pojo.Weibo;
import com.fairy.pojo.WeiboFields;
import com.fairy.util.ConfigUtil;
import com.fairy.util.LuceneUtil;
import com.fairy.utils.FileUtil;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将整理后的数据存于json数据
 */
@Deprecated
public class NewWeibo {

//    private String inPath = ConfigUtil.getValue("dataPath",
//            "conf.properties");
//    private String outPath = ConfigUtil.getValue("indexPath",
//            "conf.properties");

    private String inPath = "D:\\Users\\liuf\\03_Data\\weibo\\weibo_data_v2";
    private String outPath = "D:\\Users\\liuf\\03_Data\\weibo\\weibo_data_json";

    private FileUtil fileUtil = FileUtil.getInstance();
    private LuceneUtil luceneUtil = LuceneUtil.getInstance();

    public void createNewWeibo() throws IOException, ParseException {
        BufferedReader br = null;
        IndexWriter indexWriter = null;
        String line;
        String[] strs = null;
        Weibo weibo = null;
        Document doc = null;
        String indexFileName = null;

        File rawInData = new File(inPath);
        File[] files = rawInData.listFiles();
        File weiboFileInDay = null;
        File[] weiboFiles = null;
        List<Weibo> weiboList = null;

        for (int j = 0; j < files.length; j++) {
            weiboFileInDay = files[j];    //每日文件
            weiboFiles = weiboFileInDay.listFiles(); //每日文件中的所有文件
            weiboList = new ArrayList<>();
            for (int i = 0; i < weiboFiles.length; i++) {
                br = fileUtil.getBufferedReader(weiboFiles[i]);

                String regexStr = "(http?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
                regexStr = regexStr + "|" + "\\[.*]";
                while ((line = br.readLine()) != null) {
                    strs = line.split("\\t");
                    weibo = weibo.create(strs);
                    if (weibo != null) {
                        updateWeiboContent(weibo, regexStr);

                        if (weibo.getContent().equals("")) continue;
                        weiboList.add(weibo);
                    }
                }
            }

            String jsonStr = JSON.toJSONString(weiboList, true);

            fileUtil.saveToFile(outPath + "\\" + weiboFileInDay.getName() + ".json",
                    jsonStr, false);
        }
        System.out.println("处理结果。。。");
    }


    private void updateWeiboContent(Weibo weibo, String regexStr) {
        String contentStr = weibo.getContent();
        contentStr = contentStr.trim();
        weibo.setContent(contentStr.replaceAll(regexStr, ""));
    }

    public static void main(String[] args) throws IOException, ParseException {
        new NewWeibo().createNewWeibo();
    }
}
