package com.fairy.index;

import com.fairy.pojo.WeiboFields;
import com.fairy.util.ConfigUtil;
import com.fairy.util.LuceneUtil;
import com.fairy.util.StringUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordBag {

    private String indexPath = ConfigUtil.getValue("indexPath",
            "conf.properties");


    public static List<String> generate(String indexPath) throws IOException {
        IndexReader reader = LuceneUtil.getIndexReader(indexPath);

        Set<String> vector = new HashSet<String>();
        for (int docId=0; docId<reader.maxDoc(); docId++) {

            Terms terms = reader.getTermVector(docId, WeiboFields.content);
            TermsEnum termsEnum = terms.iterator();
            BytesRef thisTerm = null;
            while ((thisTerm = termsEnum.next()) != null) {
                String term = thisTerm.utf8ToString();
                if ((term.length() > 1) && (!StringUtil.isNumericAndLetter(term))
                        && (!StringUtil.isMobile(term)) && (!StringUtil.isPhone(term))
                        && (!StringUtil.isContainNumber(term)) && (!StringUtil.isDate(term)))
                    vector.add(term);
            }
        }

        List<String> list = new ArrayList<>(vector);
//        System.out.println(list.size());
        return list;
    }


    public static List<String> getContentList(String indexPath) throws IOException {

        List<String> contentList = new ArrayList<String>();
        IndexReader reader = LuceneUtil.getIndexReader(indexPath);

        Document doc = null;
        for (int docId=0; docId<reader.maxDoc(); docId++) {
            doc = reader.document(docId);
            Field contentField = (Field) doc.getField(WeiboFields.content);
            contentList.add(contentField.stringValue());
        }
        return contentList;
    }

    public static void main(String[] args) throws IOException {
        String indexPath = "E:\\01_IdeaProjectData\\weibo\\weibo_index";
        List<String> list = new WordBag().getContentList(indexPath);

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

    }
}
