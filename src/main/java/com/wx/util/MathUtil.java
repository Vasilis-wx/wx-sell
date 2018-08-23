package com.wx.util;

/**
 * Created by wx
 * 2018/8/23
 */
public class MathUtil {

    private static Double RANGE = 0.01;
    /**
     * 判断两个金额是否相等
     * @param d1
     * @param d2
     * @return
     */
    public static boolean equals(Double d1,Double d2){

        Double result = Math.abs(d1-d2);
        if(result < RANGE){
            return true;
        }
        return false;
    }
}
