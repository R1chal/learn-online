package com.richal.learnonline.web.controller;

import com.richal.learnonline.domain.PayOrder;
import com.richal.learnonline.dto.ApplyDto;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.service.IPayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 支付控制器
 * 处理支付相关的请求
 * @author Richal
 * @since 2025-07-30
 */
@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private IPayOrderService payOrderService;

    /**
     * 检查支付状态
     * @param orderNo 订单号
     * @return 支付状态
     */
    @GetMapping("/checkPayOrder/{orderNo}")
    public JSONResult checkPayOrder(@PathVariable("orderNo") String orderNo) {
        PayOrder payOrder = payOrderService.checkPayOrder(orderNo);
        if(payOrder == null) {
            return JSONResult.error();
        }
        return JSONResult.success();
    }

    @PostMapping("/apply")
    public JSONResult apply(@RequestBody ApplyDto applyDto) {
        String form = payOrderService.apply(applyDto);
        return JSONResult.success(form);
    }

    /**
     * 异步回调函数
     */
    @PostMapping("/notifyPay")
    public String asyncNotify(){
        log.info("异步通知已接收到=============================================");
        return "success";
    }

} 