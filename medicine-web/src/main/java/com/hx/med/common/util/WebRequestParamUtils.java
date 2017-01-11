package com.hx.med.common.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class WebRequestParamUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebRequestParamUtils.class);
    /**
     * 构造函数.
     */
    private WebRequestParamUtils() {

    }
    /**
     * 获取application/json的数据。post请求 .
     * @param request request对象
     * @return JSONObject对象
     */
    public static JSONObject getJsonParam(final HttpServletRequest request) {
        final StringBuilder requestBody = new StringBuilder();
        try {
            final BufferedReader buffer = request.getReader();
            String tempStr = null;
            while ((tempStr = buffer.readLine()) != null) {
                requestBody.append(tempStr);
            }
        } catch (final IOException e) {
            LOGGER.error("get request json error:{}", e.getMessage());
            return JSONObject.parseObject("{}");
        }
        LOGGER.info("{},json:{}", request.getRequestURI(), requestBody.toString());
        return JSONObject.parseObject(requestBody.toString());
    }

    /**
     * 按照 ret:1，的方式返回{"ret":"1"}的map .
     * @param param String的构造参数
     * @return map
     */
    public static Map getResult(final String... param) {
        final Map result = new HashMap();
        for (final String arg : param) {
            final String[] ret = arg.split(":");
            result.put(ret[0], ret.length>1?ret[1]:"");
        }
        return result;
    }
}
