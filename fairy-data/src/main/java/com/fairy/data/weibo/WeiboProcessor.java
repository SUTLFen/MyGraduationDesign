package com.fairy.data.weibo;

import com.alibaba.fastjson.JSON;
import com.fairy.pojo.WeiboFields;
import com.fairy.util.ConfigUtil;
import com.fairy.util.LuceneUtil;
import com.fairy.util.StringUtil;
import com.fairy.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeiboProcessor {

    private String  indexPath;
    private FileUtil fileUtil = FileUtil.getInstance();

    public void initParam(){
        indexPath = ConfigUtil.getValue("indexPath", "conf.properties");
    }


    public void process() throws IOException {
        initParam();
        IndexReader reader = LuceneUtil.getIndexReader(indexPath);

//        Set<String> vector = new HashSet<String>();

        List<String> termList = null;
        List<String> resultList = null;
        for (int docId=0; docId<reader.maxDoc(); docId++) {

            Terms terms = reader.getTermVector(docId, WeiboFields.content);
            TermsEnum termsEnum = terms.iterator();
            BytesRef thisTerm = null;

            termList = new ArrayList<>();

            while ((thisTerm = termsEnum.next()) != null) {
                String term = thisTerm.utf8ToString();
                if ((term.length() > 1) && (!StringUtil.isNumericAndLetter(term))
                        && (!StringUtil.isMobile(term)) && (!StringUtil.isPhone(term))
                        && (!StringUtil.isContainNumber(term)) && (!StringUtil.isDate(term)))
                    termList.add(term);
            }
            resultList.add(StringUtils.join(termList, " "));
        }

        String jsonStr = JSON.toJSONString(resultList, false);
        fileUtil.saveToFile("fairy-data/data/weibo/weiboTest.json", jsonStr, false);
//        List<String> list = new ArrayList<>(vector);

    }





    public static void main(String[] args) throws IOException {
        new WeiboProcessor().process();
    }
}
