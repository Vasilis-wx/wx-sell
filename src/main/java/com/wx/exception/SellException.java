package com.wx.exception;

import com.wx.enums.ResultEnum;
import lombok.Getter;

/**
 * Created by wx
 * 2018/8/18
 */
@Getter
public class SellException extends RuntimeException{

    private Integer code;

    public SellException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();

    }

    public SellException(ResultEnum resultEnum, String message) {
        super(message);
        this.code = resultEnum.getCode();
    }
}
