package com.richal.learnonline.exception;

/**
 * 业务异常
 *
 * @author Richal
 * @since 2025/07/20
 */
public class GlobleBusinessException extends RuntimeException {

    public GlobleBusinessException(String message) {
        super(message);
    }
}
