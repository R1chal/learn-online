package com.richal.learnonline.constant;

/**
 *错误码枚举类
 *
 * @author Richal
 * @since 2025/07/21
 */
public enum ErrorCode {

    NETWORK_ERROR("500", "网络异常");

    private String code;

    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
