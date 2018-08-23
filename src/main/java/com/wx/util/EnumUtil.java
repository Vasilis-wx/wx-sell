package com.wx.util;

import com.wx.enums.CodeEnum;

public class EnumUtil {

    /**
     * 根据枚举的code获取枚举
     * @param code
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        //遍历所有枚举
        for(T each : enumClass.getEnumConstants()){
            if(each.getCode().equals(code)){
                return each;
            }
        }
        return null;
    }
}