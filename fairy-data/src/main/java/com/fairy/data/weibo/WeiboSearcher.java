package com.fairy.data.weibo;

import com.fairy.pojo.WeiboFields;
import com.fairy.util.ConfigUtil;
import com.fairy.util.LuceneUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;

/**
 * 搜索微博，查找关键字相关的微博，并存储
 */
public class WeiboSearcher {


    public void searchWithKeyWord() throws IOException, ParseException {
        String indexPath = ConfigUtil.getValue("indexPath", "conf.properties");
        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher(indexPath + "\\2016-01-08");
        Analyzer ikAnalyzer =  new IKAnalyzer(true);
        QueryParser parser = new QueryParser(WeiboFields.content, ikAnalyzer);
        Query query = parser.parse("堵");
        ScoreDoc[] hits = indexSearcher.search(query, 1000, null).scoreDocs;

        Document doc = null;
        for (ScoreDoc scoreDoc : hits) {
            doc = indexSearcher.doc(scoreDoc.doc);
            String content = doc.get(WeiboFields.content);
            System.out.println(content);
        }


    }



    public static void main(String[] args) throws IOException, ParseException {
         new WeiboSearcher().searchWithKeyWord();
    }


}
