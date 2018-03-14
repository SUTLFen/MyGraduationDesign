package com.fairy.data.weibo;

import com.fairy.pojo.Weibo;
import com.fairy.pojo.WeiboFields;
import com.fairy.util.ConfigUtil;
import com.fairy.util.LuceneUtil;
import com.hankcs.hanlp.HanLP;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

import java.io.IOException;
import java.util.List;

public class KeyWordExtractorByHanLP {

    public void keyWordExtractor() throws IOException {
//        String content = "程序员(英文Programmer)是从事程序开发、维护的专业人员。" +
//                "一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，" +
//                "特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。";
//
//        List<String> keywordList = HanLP.extractKeyword(content, 5);
//        System.out.println(keywordList);


        String indexPath = ConfigUtil.getValue("indexPath", "conf.properties");
        IndexReader reader = LuceneUtil.getIndexReader(indexPath + "\\2016-01-20");
        List<String> keywordList = null;

        StringBuffer sb = new StringBuffer("");
        for (int i=0; i<reader.maxDoc(); i++) {
            Document doc = reader.document(i);
            String contentStr = doc.getField(WeiboFields.content).stringValue();
            System.out.println(contentStr);
            sb.append(contentStr);
//            keywordList = HanLP.extractKeyword(contentStr, 3);
//            System.out.println(keywordList);
        }
//        keywordList = HanLP.extractKeyword(sb.toString(), 30);
//        System.out.println(keywordList);


    }

    public static void main(String[] args) throws IOException {
        new KeyWordExtractorByHanLP().keyWordExtractor();
    }


}
