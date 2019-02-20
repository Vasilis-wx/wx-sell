package com.wx.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * http请求返回的最外层对象
 * @author: wx
 * @date: 2018/8/17
 */
@Data
public class ResultVO<T> implements Serializable{

    private static final long serialVersionUID = 5420749653462447138L;

    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;
}
