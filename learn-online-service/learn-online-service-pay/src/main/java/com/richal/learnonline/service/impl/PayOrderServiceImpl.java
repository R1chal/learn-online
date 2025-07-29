package com.richal.learnonline.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.domain.AlipayInfo;
import com.richal.learnonline.domain.PayOrder;
import com.richal.learnonline.dto.ApplyDto;
import com.richal.learnonline.exception.GlobleBusinessException;
import com.richal.learnonline.mapper.PayOrderMapper;
import com.richal.learnonline.service.IAlipayInfoService;
import com.richal.learnonline.service.IPayOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-29
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

    @Autowired
    private IAlipayInfoService alipayInfoService;

    @Override
    public PayOrder checkPayOrder(String orderNo) {
        if(StringUtils.isEmpty(orderNo)){
            throw new GlobleBusinessException("订单为空");
        }
        Wrapper<PayOrder> ww = new EntityWrapper<PayOrder>();
        ww.eq("order_no", orderNo);
        return selectOne(ww);
    }

    @Override
    public String apply(ApplyDto applyDto) {

        String callUrl = applyDto.getCallUrl();
        String orderNo = applyDto.getOrderNo();
        Integer payType = applyDto.getPayType();

        List<AlipayInfo> alipayInfos = alipayInfoService.selectList(null);
        AlipayInfo alipayInfo = alipayInfos.get(0);

        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions(alipayInfo));

        Wrapper<PayOrder> ww =  new EntityWrapper<>();
        ww.eq("order_no", orderNo);
        PayOrder payOrder = selectOne(ww);

        try {
            // 2. 发起API调用（以创建当面付收款二维码为例）
            AlipayTradePagePayResponse response = Factory.Payment.Page().pay(
                    payOrder.getSubject(), payOrder.getOrderNo(), payOrder.getAmount().toString(),callUrl
            );
            // 3. 处理响应或异常
            if (ResponseChecker.success(response)) {
                return response.getBody();
            } else {
                System.err.println("调用失败");
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

    public static Config getOptions(AlipayInfo alipayInfo) {


        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = alipayInfo.getGatewayHost();
        config.signType = alipayInfo.getSignType();
        config.appId = alipayInfo.getAppId();
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = alipayInfo.getMerchantPrivateKey();
        config.alipayPublicKey = alipayInfo.getAlipayPublicKey();
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = alipayInfo.getNotifyUrl();
        return config;
    }
}
