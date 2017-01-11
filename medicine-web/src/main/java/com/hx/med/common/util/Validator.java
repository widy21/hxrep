package com.hx.med.common.util;

import java.util.regex.Pattern;

/**
 * Created by huaxiao on 2016/12/7.
 * 正则校验类
 * @author huaxiao
 *
 * @since 1.0
 */


public final class Validator {
    /**
     * 正则表达式：验证用户名.
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,15}$";

    /**
     * 正则表达式：验证密码.
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

    /**
     * 正则表达式：验证手机号.
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    /**
     * 正则表达式：验证邮箱.
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字.
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证.
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 正则表达式：验证URL.
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址.
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    /**
     * 构造函数.
     */
    private Validator() {
    }

    /**
     * 校验用户名.
     *
     * @param username username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(final String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码.
     *
     * @param password password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(final String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号.
     *
     * @param mobile mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(final String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验邮箱.
     *
     * @param email email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(final String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字.
     *
     * @param chinese chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(final String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证.
     *
     * @param idCard idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(final String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校验URL.
     *
     * @param url url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(final String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址.
     *
     * @param ipAddr ipAddr
     * @return boolean
     */
    public static boolean isIPAddr(final String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }
}
