package com.fairy.utils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fairy_LFen on 2017/3/6.
 */
public class DateUtil {
    private static DateUtil instance = null;

    public static DateUtil getInstance(){
        if(instance == null){
            instance = new DateUtil();
        }
        return instance;
    }
/*
* @param timeStr : 时间字符串
* @return : Unix时间戳
* */
    public long getTime(String timeStr) throws ParseException {
//        String seg_str = "2016-01-01 10:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(timeStr);
        return date.getTime();
    }


/**
 * 构建时间字符串
 * @param dateStr : 2016-01-04
 * @param hour : 小时
 * */
    public String newStartTime(String dateStr, int hour) {
        String hourStr;
        if(hour < 10){
            hourStr = "0"+hour;
        }
        else hourStr = hour + "";
        String startTime = MessageFormat.format("{0} {1}:00:00", dateStr, hourStr);
        return startTime;
    }

    public String newEndTime(String dateStr, int hour) {
        String hourStr;
        if(hour < 10){
            hourStr = "0"+hour;
        }
        else hourStr = hour + "";
        String endTime = MessageFormat.format("{0} {1}:59:59", dateStr, hourStr);
        return endTime;
    }

}
