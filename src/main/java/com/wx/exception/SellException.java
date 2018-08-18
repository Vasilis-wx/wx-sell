package com.wx.exception;

import com.wx.enums.ResultEnum;

/**
 * Created by wx
 * 2018/8/18
 */
public class SellException extends RuntimeException{

    private Integer code;

    public SellException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();

    }
}
