package com.richal.learnonline.dto;

import lombok.Data;

/**
 * 支付宝异步通知DTO
 * 
 * @author Richal
 * @since 2025-07-30
 */
@Data
public class AlipayNotifyDto {
    
    // 商户订单号
    private String out_trade_no;
    
    // 支付宝交易号
    private String trade_no;
    
    // 交易状态
    private String trade_status;
    
    // 订单金额
    private String total_amount;
    
    // 商品名称
    private String subject;
    
    // 商户业务号
    private String out_biz_no;
    
    // 买家支付宝用户号
    private String buyer_id;
    
    // 卖家支付宝用户号
    private String seller_id;
    
    // 交易创建时间
    private String gmt_create;
    
    // 交易付款时间
    private String gmt_payment;
    
    // 交易结束时间
    private String gmt_close;
    
    // 通知时间
    private String notify_time;
    
    // 通知类型
    private String notify_type;
    
    // 通知校验ID
    private String notify_id;
    
    // 签名类型
    private String sign_type;
    
    // 签名
    private String sign;
    
    // 返回码
    private String code;
    
    // 返回信息
    private String msg;
    
    // 交易成功常量
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    
    // 交易完成常量
    public static final String TRADE_FINISHED = "TRADE_FINISHED";
    
    // 交易关闭常量
    public static final String TRADE_CLOSED = "TRADE_CLOSED";
    
    /**
     * 获取交易状态
     */
    public String getTrade_status() {
        return trade_status;
    }
    
    /**
     * 获取商户订单号
     */
    public String getOut_trade_no() {
        return out_trade_no;
    }
    
    /**
     * 获取订单金额
     */
    public String getTotal_amount() {
        return total_amount;
    }
    
    /**
     * 获取商品名称
     */
    public String getSubject() {
        return subject;
    }
    
    /**
     * 获取返回码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 获取返回信息
     */
    public String getMsg() {
        return msg;
    }
} 