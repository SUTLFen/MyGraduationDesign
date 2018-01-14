package com.fairy.clustering;

import com.fairy.IDF;
import com.fairy.hotword.LuceneMemoryIDF;
import com.fairy.index.WordBag;
import com.fairy.util.ConfigUtil;
import com.fairy.util.DataReader;
import com.fairy.util.StringUtil;
import com.fairy.util.VectorUtil;
import smile.clustering.KMeans;

import java.awt.image.Kernel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KMeansCluster {

    private static String indexPath = null;

    private IDF idf = new LuceneMemoryIDF();

    public static int K = 5;

    private static int ITERATE = 10000;


    public void learn(String indexPath) throws IOException {
        indexPath = ConfigUtil.getValue("indexPath", "conf.properties");
        List<String> textList = WordBag.getContentList(indexPath);   //读取微博每条信息
        List<String> vectorList = WordBag.generate(indexPath);   //微博关键字

        double[][] datas = VectorUtil.getVector(textList.size(), vectorList, idf);   //转为特征向量

        System.out.println(datas.length);
//        KMeans kmeans = new KMeans(datas, K, ITERATE);
//
//        int[] labels = kmeans.getClusterLabel();
//        for (int j = 0; j < KMeansCluster.K; j++) {
//            System.out.println("class " + j + " : ");
//
//            for (int i = 0; i < labels.length; i++)
//                if (labels[i] == j)
//                    System.out.println(textList.get(i));
//        }


    }



    public static void main(String[] args) throws IOException {
        new KMeansCluster().learn(indexPath);
    }
}
