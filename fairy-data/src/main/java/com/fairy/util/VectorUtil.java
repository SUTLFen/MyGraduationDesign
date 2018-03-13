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
import java.text.MessageFormat;
import java.util.*;

public class VectorUtil {

    protected static Logger logger = Logger.getLogger(VectorUtil.class);
    private static String indexPath = "D:\\01_Fairy_LFen\\07_Data\\weibo\\weibo_index";

    public static double[][] getVector(int row, List<String> vectorList, IDF idf) throws IOException {
        Map<String, Double> tfidf = new HashMap<String, Double>();
        // calculating the vector of samples
        double[][] samples = new double[row][vectorList.size()];
        IndexReader reader = null;
        reader = LuceneUtil.getIndexReader(indexPath);
        for (int i = 0; i < row; i++) {

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

//                        String str = MessageFormat.format(
//                                "idfn:{0}, a:{1}, b:{2}, tf:{3}, idfn*tf",
//                                idfn + "", a + "", b + "", tf + "", samples[i][j]);

                        System.out.println(samples[i][j]);
                        break;
                    }
                }
                // not contain means this vector value is 0
                if (!isContain)
                    samples[i][j] = 0.0;
                j++;
            }
        }

        for (int i = 0; i < samples.length; i++) {
            for (int j = 0; j < samples[i].length; j++) {
                System.out.print(samples[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }

        return samples;
    }

    public static void main(String[] args) {
        double[] a = new double[5];
        try{
            for (int i = 0; i < a.length; i++) {
                a[i] = i;
            }

        }catch(Exception e){
            e.printStackTrace();
        }


        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }
}
