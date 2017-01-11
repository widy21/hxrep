package com.hx.med.common.util;

import com.hx.med.common.constants.errorcode.AnchorErrorCodeEnum;
import com.hx.med.common.constants.errorcode.BaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huaxiao on 2016/12/7.
 * @author huaxiao
 * @since 1.0
 */
public final class ErrorMessageUtil {

    /**
     * 构造函数.
     */
    private ErrorMessageUtil() {
    }

    /**
     * 把枚举值转换为map返回.
     * @param param param
     * @return Map
     */
    public static Map getErrorMessageByEnum(final BaseError param) {
        final Map result = new HashMap();
        result.put("error_code", param.getEnumCode());
        result.put("error_message", param.getEnumDesc());
        return result;
    }

    /**
     * 把错误信息转换为map返回.
     * @param code code.
     * @param desc desc.
     * @return Map
     */
    public static Map getErrorMessage(final int code, final String desc) {
        final Map result = new HashMap();
        result.put("error_code", code);
        result.put("error_message", desc);
        return result;
    }
    /**
     * 返回成功信息.
     * @return Map
     */
    public static Map getSuccessMessage() {
        final Map result = new HashMap();
        result.putAll(getErrorMessageByEnum(AnchorErrorCodeEnum.SUCCESS));
        return result;
    }

}
