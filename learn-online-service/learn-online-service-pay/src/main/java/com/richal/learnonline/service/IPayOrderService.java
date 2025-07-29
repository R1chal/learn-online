package com.richal.learnonline.service;

import com.richal.learnonline.domain.PayOrder;
import com.baomidou.mybatisplus.service.IService;
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

    PayOrder checkPayOrder(String orderNo);

    String apply(ApplyDto applyDto);
}
