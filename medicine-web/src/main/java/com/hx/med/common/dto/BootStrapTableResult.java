package com.hx.med.common.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * ajax 请求的返回类型封装JSON结果.
 * 
 * 主要用于bootstrap table
 */
public class BootStrapTableResult<T> implements Serializable {


    private static final long serialVersionUID = -4185151304730685014L;


    private List<T> data;

    /**
     * web返回json构造函数.
     * @param data 列表数据构造
     */
    public BootStrapTableResult(final List<T> data) {
        super();
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }


    public void setData(final List<T> data) {
        this.data = data;
    }



}
