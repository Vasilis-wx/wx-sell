package com.wx.constant;

/**
 * redis常量
 * @author: wx
 * @date: 2018/8/24
 */
public interface RedisConstant {

    String TOKEN_PREFIX = "token_%s";

    /**
     * 过期时间
     */
    Integer EXPIRE = 7200; //2小时
}
