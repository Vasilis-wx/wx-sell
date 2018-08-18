package com.wx.util;

import java.util.Random;

/**
 * Created by wx
 * 2018/8/18
 */
public class KeyUtil {

    /**
     * 生成唯一主键
     * 格式： 时间+随机数（6位）
     * @return
     */
    public static synchronized String getUniqueKey(){

        Random random = new Random();

        //生成6位随机数
        Integer value = random.nextInt(900000) + 100000;

        return System.currentTimeMillis()+String.valueOf(value);
    }
}
