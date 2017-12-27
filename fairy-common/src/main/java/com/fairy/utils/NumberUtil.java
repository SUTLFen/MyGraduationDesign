package com.fairy.utils;

import java.text.DecimalFormat;

/**
 * Created by Fairy_LFen on 2017/3/12.
 */
public class NumberUtil {
    private static NumberUtil instance = null;
    private NumberUtil(){}
    public static NumberUtil getInstance(){
        if(instance == null){
            instance = new NumberUtil();
        }
        return instance;
    }

    public static double save6Decimal(double value_d){
        DecimalFormat decimalFormat = new DecimalFormat("#.#######");
        String result_str = decimalFormat.format(value_d);
        double result_d = Double.valueOf(result_str);
        double[] object = new double[20];

        return result_d;
    }
}
