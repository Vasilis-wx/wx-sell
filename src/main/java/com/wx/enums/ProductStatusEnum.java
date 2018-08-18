package com.wx.enums;

import lombok.Getter;

/**
 * 商品状态
 * Created by wx
 * 2018/8/17
 */
@Getter
public enum  ProductStatusEnum {
    UP(0,"在架"),
    down(1,"下架")
    ;

    private Integer code;
    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
