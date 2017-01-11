package com.hx.med.common.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/**
 * 时间类
 */
public final class JodaTimeUtils {
    /** timeformat .**/
    private static final String NORMAL_TIME_FORMAT = "yyyy-MM-dd";
    /** datetime timeformat .**/
    private static final DateTimeFormatter TIMEFORMATTER = DateTimeFormat.forPattern(NORMAL_TIME_FORMAT);
    /**
     * 构造函数 .
     */
    private JodaTimeUtils() {

    }

    /**
     * 获取当天时间字符串 .
     * @return java.lang.String
     */
    public static String getNowDateStr() {
        return DateTime.now().toString(TIMEFORMATTER);
    }


}

