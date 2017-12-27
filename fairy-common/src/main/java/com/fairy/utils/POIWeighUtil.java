package com.fairy.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.MessageFormat;

/**
 * Created by Fairy_LFen on 2017/9/9.
 */
public class POIWeighUtil {

    public static int POIWeight(String keyWord){
//        String keyWord = "杭州西湖";
        String formatUrl = "http://www.baidu.com//s?wd={0}";
        String url = MessageFormat.format(formatUrl, keyWord);

        String resultStr = HttpUtil.doGet(url);
        Document doc = Jsoup.parse(resultStr);
        Elements element  = doc.getElementsByClass("nums");
        String str = element.text();

        System.out.println("------------------------");
        System.out.println(str);

        int startIndex = str.indexOf("约");
        int endIndex = str.indexOf("个");

        String countStr = str.substring(startIndex + 1, endIndex);
        return Integer.valueOf(countStr.replace(",", ""));
    }
}
