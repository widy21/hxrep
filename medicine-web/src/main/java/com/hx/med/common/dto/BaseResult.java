package com.hx.med.common.dto;

import com.hx.med.common.constants.errorcode.BaseError;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.io.Serializable;
/**
 * @author yingjun
 *         <p>
 *         ajax 请求的返回类型封装JSON结果
 * @since 1.7
 */

public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = 5361709599458513802L;

    /**
     * 错误信息.
     */
    private String errmsg = "__test";

    /**
     * 返回数据.
     */
    private T data;

    /**
     * 错误码.
     */
    private String errcode = "-1";

    /**
     * 默认空构造函数.
     */
    public BaseResult() {

    }

    /**
     * 构造函数，生成BaseResult.
     *
     * @param errcode 错误码
     * @param errmsg  错误描述
     */
    public BaseResult(final String errcode, final String errmsg) {
        this.errmsg = errmsg;
        this.errcode = errcode;
    }

    /**
     * 构造函数.
     *
     * @param errcode 错误码
     * @param errmsg  错误信息
     * @param data    返回数据
     */
    public BaseResult(final String errcode, final String errmsg, final T data) {
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.data = data;
    }

    /**
     * 构造方法，支持用错误码初始化.
     *
     * @param errorCodeEnum ErrorCodeEnum对象.
     * @param data          返回数据
     */
    public BaseResult(final BaseError errorCodeEnum, final T data) {
        Assert.notNull(errorCodeEnum);

        this.errcode = errorCodeEnum.getEnumCode();
        this.errmsg = errorCodeEnum.getEnumDesc();
        this.data = data;
    }

    /**
     * 构造方法，支持用错误码初始化.
     *
     * @param errorCodeEnum ErrorCodeEnum对象.
     */
    public BaseResult(final BaseError errorCodeEnum) {
        Assert.notNull(errorCodeEnum);

        this.errcode = errorCodeEnum.getEnumCode();
        this.errmsg = errorCodeEnum.getEnumCode();
    }

    /**
     * get errmsg.
     *
     * @return java.lang.String
     **/
    public String getErrmsg() {
        return errmsg;
    }

    /**
     * set errmsg.
     *
     * @param errmsg 错误消息
     */
    public void setErrmsg(final String errmsg) {
        this.errmsg = errmsg;
    }

    /**
     * get data.
     *
     * @return T
     **/
    public T getData() {
        return data;
    }

    /**
     * set data.
     *
     * @param data 返回数据
     */
    public void setData(final T data) {
        this.data = data;
    }

    /**
     * get errcode.
     *
     * @return java.lang.String
     **/
    public String getErrcode() {
        return errcode;
    }

    /**
     * set errcode.
     *
     * @param errcode 错误码
     */
    public void setErrcode(final String errcode) {
        this.errcode = errcode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("errmsg", errmsg)
                .append("data", data)
                .append("errcode", errcode)
                .toString();
    }
}
