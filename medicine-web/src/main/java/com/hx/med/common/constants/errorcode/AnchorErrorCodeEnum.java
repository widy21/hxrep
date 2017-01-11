package com.hx.med.common.constants.errorcode;

/**
 * Created by huaxiao on 2016/12/7.
 * 主播信息错误编码类
 * @author huaxiao
 *
 * @since 1.0
 */
public enum AnchorErrorCodeEnum implements BaseError {
    /**
     * REQUEST_ERROR.
     */
    REQUEST_ERROR("1001", "请求参数错误"),
    /**
     * ERROR_ID.
     */
    ERROR_ID("1002", "身份证号码错误"),
    /**
     * ERROR_PHONE_NUM.
     */
    ERROR_PHONE_NUM("1003", "手机号码错误"),
    /**
     * ERROR_CHECKCODE.
     */
    ERROR_CHECKCODE("1004", "验证码错误"),
    /**
     * ERROR_NICKNAME.
     */
    ERROR_NICKNAME("1005", "昵称格式错误"),
    /**
     * EXPIRED_CHECKCODE.
     */
    EXPIRED_CHECKCODE("1006", "验证码过期"),
    /**
     * SUCCESS.
     */
    SUCCESS("0000", "验证成功");

    /**
     * 枚举编码.
     */
    private String code;
    /**
     * 枚举描述.
     */
    private String desc;

    /**
     * 构造函数.
     *
     * @param code code
     * @param desc desc
     */
    private AnchorErrorCodeEnum(final String code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 枚举类型转换.
     *
     * @param code code
     * @return AnchorErrorCodeEnum
     */
    public static AnchorErrorCodeEnum parse(final String code) {
        for (final AnchorErrorCodeEnum b : AnchorErrorCodeEnum.values()) {
            if (b.getCode() == code) {
                return b;
            }
        }
        return null;
    }

    /**
     * 获取枚举编码.
     *
     * @return String
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置枚举编码.
     * @param code code
     */
    public void setCode(final String code) {
        this.code = code;
    }
    /**
     * 获取枚举描述.
     *
     * @return String
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置枚举描述.
     * @param desc desc
     */
    public void setDesc(final String desc) {
        this.desc = desc;
    }

    @Override
    public String getEnumCode() {
        return code;
    }

    @Override
    public String getEnumDesc() {
        return desc;
    }
}
