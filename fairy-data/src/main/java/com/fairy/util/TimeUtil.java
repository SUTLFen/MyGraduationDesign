package com.fairy.util;

import com.fairy.pojo.PrCar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {


    /**
     * 判断车辆所处的时间是否在hourCur中
     * @param prCar
     * @param hourCur
     * @return
     */
      public static boolean isInTime(PrCar prCar, int hourCur){
          long stayPointDate = prCar.getStayPointDate();
          Date date = new Date(stayPointDate);
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String dateStr = sdf.format(date);
          String[] strs = dateStr.split("\\s|:|\\-");
          int hourCar = Integer.valueOf(strs[3]);
          return hourCar == hourCur;
      }

}
