package com.fairy.data.preprocess;

import com.fairy.pojo.WeiboFields;
import com.fairy.util.JSONUtil;
import com.fairy.util.LuceneUtil;
import com.fairy.utils.FileUtil;
import com.fairy.utils.GeoDistance;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.List;

/**
 * 输入：微博关键字
 * 输出：矩阵keywords*keywords
 * 关键词关系图
 */
public class ZMatrix {

    private String dateStr = "2016-01-01";
    private String keyWordDicPath = "fairy-data/data/weibo/keywords0101_selected.json";
    private String weiboIndexPath = "D:\\Users\\liuf\\03_Data\\weibo\\weibo_index\\" + dateStr;

    private LuceneUtil luceneUtil = LuceneUtil.getInstance();
    private FileUtil fileUtil = FileUtil.getInstance();
    private GeoDistance geoDistance = GeoDistance.getinstance();
    private IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher(weiboIndexPath);

    public ZMatrix() throws IOException {
    }


    /**
     * 构建矩阵Z
     * 若关键词所在微博之间在发布时间和发布地点上都在一定阈值范围内，则权值加1.
     * 时间权值：1h
     * 地理权值：1km
     * @throws IOException
     */
    public void generate() throws IOException, ParseException {

        List<String> keyWordList = JSONUtil.toKeyWordList(keyWordDicPath);

        Analyzer ikAnalyzer =  new IKAnalyzer(true);
        QueryParser parser = new QueryParser(WeiboFields.content, ikAnalyzer);

        String keyWord01 = null;
        String keyWord02 = null;

        Query query01 = null;
        Query query02 = null;

        ScoreDoc[] hits01 = null;
        ScoreDoc[] hits02 = null;

        int size = keyWordList.size();
        int[][] matrixZ = new int[size][size];
        for (int i = 0; i < size - 1 ; i++) {

            query01 = parser.parse(keyWordList.get(i));
            hits01 = indexSearcher.search(query01, 1000, null).scoreDocs;

            for (int j = i + 1; j < size ; j++) {
                query02 = parser.parse(keyWordList.get(j));
                hits02 = indexSearcher.search(query02, 1000, null).scoreDocs;

                matrixZ[i][j] = correlation(hits01, hits02);
            }
        }
    }

    //计算相互关系
    public int correlation(ScoreDoc[] hits01, ScoreDoc[] hits02) throws IOException {

        Document doc = null;
        for (ScoreDoc scoreDoc : hits01) {
            doc = indexSearcher.doc(scoreDoc.doc);
            String content = doc.get(WeiboFields.content);
            System.out.println(content);
        }

        Document doc01 = null;
        Document doc02 = null;

        long time01 , time02;
        double lat01,lng01, lat02, lng02;

        int count = 0;

        for (int i = 0; i < hits01.length; i++) {

            doc01 = indexSearcher.doc(hits01[i].doc);
            time01 = Long.valueOf(doc01.get(WeiboFields.time));
            lat01 = Double.valueOf(doc01.get(WeiboFields.latitude));
            lng01 = Double.valueOf(doc01.get(WeiboFields.longitude));


            for (int j = 0; j < hits02.length; j++) {
                doc02 = indexSearcher.doc(hits02[j].doc);
                time02 = Long.valueOf(doc02.get(WeiboFields.time));
                lat02 = Double.valueOf(doc02.get(WeiboFields.latitude));
                lng02 = Double.valueOf(doc02.get(WeiboFields.longitude));

                if(isIn1h(time01, time02) && isIn1Km(lat01, lng01, lat02, lng02)){
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isIn1Km(double lat01, double lng01, double lat02, double lng02) {
        double disThresh = geoDistance.GetDistance(lat01,lng01,lat02,lng02);
        if(disThresh <= 1.0f){
            return true;
        }
        return false;
    }

    //相差一个小时
    private boolean isIn1h(long time01, long time02) {
        long timeThresh = 24*3600*1000;  //一小时

        long timeDis = Math.abs(time01 - time02);
        if(timeDis <= timeThresh){
            return true;
        }
        return false;
    }


    public static void main(String[] args) throws IOException, ParseException {
         new ZMatrix().generate();
    }
}
