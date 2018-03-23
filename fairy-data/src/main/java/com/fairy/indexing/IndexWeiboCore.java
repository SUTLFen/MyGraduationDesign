package com.fairy.indexing;

import com.fairy.pojo.Weibo;
import com.fairy.pojo.WeiboFields;
import com.fairy.util.ConfigUtil;
import com.fairy.utils.FileUtil;
import com.fairy.util.LuceneUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.elasticsearch.index.mapper.LegacyDoubleFieldMapper;
import org.elasticsearch.index.mapper.StringFieldType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;

public class IndexWeiboCore {

    private String inPath = ConfigUtil.getValue("dataPath",
            "conf.properties");
    private String outPath = ConfigUtil.getValue("indexPath",
            "conf.properties");

    private FileUtil fileUtil = FileUtil.getInstance();
    private LuceneUtil luceneUtil = LuceneUtil.getInstance();


    public void indexWeibo() throws IOException, ParseException {
        File rawDataPath = new File(inPath);
        File[] weiboFiles = rawDataPath.listFiles();

        BufferedReader br = null;
        IndexWriter indexWriter = null;
        String line;
        String[] strs = null;
        Weibo weibo = null;
        Document doc = null;
        String indexFileName = null;

        for (int i = 0; i < weiboFiles.length; i++) {
            br = fileUtil.getBufferedReader(weiboFiles[i]);
            indexFileName = extractDayStr(weiboFiles[i].getName());
            indexWriter = luceneUtil.getAnalyzerIndexWriter(outPath, indexFileName);

            String regexStr = "(http?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
            regexStr = regexStr + "|" + "\\[.*]";
            String content = null;
            while ((line = br.readLine()) != null) {
                strs = line.split("\\t");
                weibo = weibo.create(strs);
                if(weibo != null){

                    updateWeiboContent(weibo, regexStr);
                    System.out.println(weibo.getContent());

                    if(weibo.getContent().equals(""))
                    doc = createDoc(weibo);

                    indexWriter.addDocument(doc);
                }
            }
            indexWriter.close();
        }
        System.out.println("索引创建成功。。。");
    }


    private void updateWeiboContent(Weibo weibo, String regexStr) {
        String contentStr = weibo.getContent();
        contentStr = contentStr.trim();
        weibo.setContent(contentStr.replaceAll(regexStr, ""));
    }

    private String extractDayStr(String name) {
        String[] strs = name.split("\\s+|\\-|\\.");
        String yearStr = strs[1];
        String monthStr = strs[2];
        String dayStr = strs[3];

        String resultStr = MessageFormat.format("{0}-{1}-{2}", yearStr, monthStr, dayStr);
        return resultStr;
    }


    private Document createDoc(Weibo weibo) {
        Document doc = new Document();
        doc.add(new Field(WeiboFields.id, weibo.id, StringField.TYPE_STORED));
        doc.add(new DoublePoint(WeiboFields.time, weibo.time));

        FieldType type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(true);
        type.setStoreTermVectors(true);
        type.setTokenized(true);

        doc.add(new Field(WeiboFields.content, weibo.content, type));

        doc.add(new DoublePoint(WeiboFields.longitude, weibo.longitude));
        doc.add(new DoublePoint(WeiboFields.latitude, weibo.latitude));
        doc.add(new Field(WeiboFields.locationName, weibo.locationName, StringField.TYPE_STORED));
        doc.add(new Field(WeiboFields.provinceId, weibo.provinceId, StringField.TYPE_STORED));
        doc.add(new Field(WeiboFields.cityId, weibo.cityId, StringField.TYPE_STORED));
        doc.add(new Field(WeiboFields.location, weibo.location, StringField.TYPE_STORED));
        return doc;
    }


    public static void main(String[] args) throws IOException, ParseException {
        new IndexWeiboCore().indexWeibo();
    }

}
