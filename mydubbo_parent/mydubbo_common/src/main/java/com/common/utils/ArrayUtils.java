package com.common.utils;

/**
 * Created by Administrator on 2017/11/25.
 */
public class ArrayUtils {

    /**
     * 数组转字符串
     * */
    public static String arrayToString(Object[] obs){
        String resultStr = "";
        for (int i = 0; i < obs.length; i++) {
            if(i == obs.length-1){
                resultStr += obs[i];
            }else{
                resultStr += obs[i] + ",";
            }
        }
        
        return resultStr;
    }

    /**
     * 字符串转数组
     * */
    public static Object[] stringToArray(String param){
        return param.split(",");
    }

}
