package com.hx.med.common.util;

import java.security.MessageDigest;
import java.util.Objects;

/**
 * @author huaxiao
 * @description
 * @since 1.0.0
 */
public final class WebExt {

    /**
     * 构造器.
     */
    private WebExt() {
    }

    /**
     * 判断对象是否为空.
     *
     * @param o o
     * @return boolean
     */
    public static boolean isBlank(final Object o) {
        return o == null || Objects.equals("", o);
    }

    /**
     * MD5 加密 (小写32位).
     * @param str str
     * @return String
     */
    public static String getMD5Str(final String str) {
        MessageDigest messageDigest = null;
        byte[] byteArray = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byteArray = messageDigest.digest();
        } catch (final Exception e) {

        }
        final StringBuilder md5StrBuff = new StringBuilder();
        if (byteArray == null) {
            return "";
        }
        for (int i = 0; i < byteArray.length; i++) {
            final int md5Key = 0xFF;
            if (Integer.toHexString(md5Key & byteArray[i]).length() == 1) {
                md5StrBuff.append('0').append(Integer.toHexString(md5Key & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(md5Key &  byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }
}
