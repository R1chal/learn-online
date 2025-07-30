package com.richal.learnonline.service;

import com.richal.learnonline.domain.PayOrder;
import com.baomidou.mybatisplus.service.IService;
import com.richal.learnonline.dto.AlipayNotifyDto;
import com.richal.learnonline.dto.ApplyDto;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Richal
 * @since 2025-07-29
 */
public interface IPayOrderService extends IService<PayOrder> {

    /**
     * 检查支付订单
     * @param orderNo 订单号
     * @return 支付订单
     */
    PayOrder checkPayOrder(String orderNo);

    /**
     * 支付申请
     * @param applyDto 申请参数
     * @return 支付表单
     */
    String apply(ApplyDto applyDto);
    
    /**
     * 处理支付宝异步通知
     * @param alipayNotifyDto 支付宝通知参数
     * @return 处理结果
     */
    String asyncNotify(AlipayNotifyDto alipayNotifyDto);

    void cancel(String payOrder);

    String queryOrderStatus(String orderNo);
}
