package com.wx.fmode;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by 廖师兄
 * 2017-07-23 21:43
 */
@Data
public class FCategory {

    private Integer categoryId;

    /** 类目名字. */
    private String categoryName;

    /** 类目编号. */
    @NotNull(message = "类目编号不能为空")
    private Integer categoryType;
}
