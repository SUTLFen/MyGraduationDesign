package com.fairy.util;

import com.fairy.IDF;
import com.fairy.pojo.WeiboFields;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.*;

public class VectorUtil {

    protected static Logger logger = Logger.getLogger(VectorUtil.class);
    private static final String indexPath = "index4vector";

    public static double[][] getVector(int row, List<String> vectorList, IDF idf) {
        Map<String, Double> tfidf = new HashMap<String, Double>();
        // calculating the vector of samples
        double[][] samples = new double[row][vectorList.size()];
        IndexReader reader = null;
        try {
            reader = LuceneUtil.getIndexReader("E:\\01_IdeaProjectData\\weibo\\weibo_index");
            for (int i = 0; i < row; i++) {
                try {
                    int j = 0;
                    for (int m = 0; m < vectorList.size(); m++) {
                        String vTerm = vectorList.get(m);

                        Terms terms = reader.getTermVector(i, WeiboFields.content); //获取索引中词term

                        TermsEnum termsEnum = terms.iterator();
                        BytesRef thisTerm = null;
                        boolean isContain = false;
                        while ((thisTerm = termsEnum.next()) != null) {
                            String term = thisTerm.utf8ToString();
                            if (term.equals(vTerm)) {

                                float idfn = idf.getIDF(term);
                                int a = (int) termsEnum.totalTermFreq();
                                long b = terms.size();
                                float tf = (float) a / b;

                                samples[i][j] = idfn * tf;
                                tfidf.put(term, samples[i][j]);
                                isContain = true;
                                break;
                            }
                        }
                        // not contain means this vector value is 0
                        if (!isContain) samples[i][j] = 0.0;
                        j++;
                    }
                } catch (IOException e) {
                    logger.error("IOException happens ", e);
                }
            }
        } catch (IOException e) {
            logger.error("IOException happens ", e);
        }
        return samples;
    }

}
