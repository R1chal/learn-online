package com.richal.learnonline.query;

import lombok.Data;

/**
 * 基础查询对象
 *
 * @author Richal
 * @since 2025/07/21
 */
@Data
public class BaseQuery {

    //关键字
    private String keyword;

    //有公共属性-分页
    private Integer page = 1;

    //每页显示多少条
    private Integer rows = 10;

    //排序的字段
    private String sortField;

    //排序的方式：desc ; asc
    private String sortType;

}
