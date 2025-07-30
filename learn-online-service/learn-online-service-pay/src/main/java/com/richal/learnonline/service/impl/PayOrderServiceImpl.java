package com.richal.learnonline.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.api.OrderFeignApi;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.domain.AlipayInfo;
import com.richal.learnonline.domain.PayFlow;
import com.richal.learnonline.domain.PayOrder;
import com.richal.learnonline.dto.AlipayNotifyDto;
import com.richal.learnonline.dto.ApplyDto;
import com.richal.learnonline.dto.UpdateOrderStatusDTO;
import com.richal.learnonline.exception.GlobleBusinessException;
import com.richal.learnonline.mapper.PayOrderMapper;
import com.richal.learnonline.service.IAlipayInfoService;
import com.richal.learnonline.service.IPayOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.richal.learnonline.service.IPayFlowService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-29
 */
@Slf4j
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

    @Autowired
    private IAlipayInfoService alipayInfoService;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private IPayFlowService payFlowService;

    @Autowired
    private OrderFeignApi orderFeignApi;

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

    @Override
    public String asyncNotify(AlipayNotifyDto notifyDto) {

        try {
            Boolean signSuccess = Factory.Payment.Common()
                    .verifyNotify(JSON.parseObject(JSON.toJSONString(notifyDto),Map.class));
            if(!signSuccess){
                return "fail";
            }
            Date date = new Date();
            if (notifyDto.getTrade_status().equals(AlipayNotifyDto.TRADE_CLOSED)){
                payOrderMapper.updateStatusByOrderNo(notifyDto.getOut_trade_no(), date, BusinessConstants.PAY_CLOSED);
            }else if(notifyDto.getTrade_status().equals(AlipayNotifyDto.TRADE_SUCCESS)
            || notifyDto.getTrade_status().equals(AlipayNotifyDto.TRADE_FINISHED)){
                payOrderMapper.updateStatusByOrderNo(notifyDto.getOut_trade_no(), date, BusinessConstants.PAY_OK);
            }
            payOrderMapper.updateStatusByOrderNo(notifyDto.getOut_trade_no(), date, BusinessConstants.PAY_DEFAULT);

            // 增加流水记录
            PayFlow payFlow = new PayFlow();
            payFlow.setNotifyTime(new Date());
            payFlow.setSubject(notifyDto.getSubject());
            payFlow.setOutTradeNo(notifyDto.getOut_trade_no());
            payFlow.setTradeStatus(notifyDto.getTrade_status());
            payFlow.setCode(notifyDto.getCode());
            payFlow.setMsg(notifyDto.getMsg());
            payFlow.setPaySuccess(notifyDto.getTrade_status().equals(AlipayNotifyDto.TRADE_SUCCESS) ||
                                 notifyDto.getTrade_status().equals(AlipayNotifyDto.TRADE_FINISHED));
            payFlow.setTotalAmount(new BigDecimal(notifyDto.getTotal_amount()));
            payFlow.setResultDesc("支付状态为: " +
                                ((notifyDto.getTrade_status().equals(AlipayNotifyDto.TRADE_SUCCESS) ||
                                  notifyDto.getTrade_status().equals(AlipayNotifyDto.TRADE_FINISHED)) ? "成功" : "失败"));

            // 保存支付流水记录
            payFlowService.insert(payFlow);

            UpdateOrderStatusDTO updateOrderStatusDTO = new UpdateOrderStatusDTO();
            updateOrderStatusDTO.setOrderNo(notifyDto.getOut_trade_no());
            updateOrderStatusDTO.setUpdateTime(date);
            orderFeignApi.updateOrderStatus(updateOrderStatusDTO);

            return "success";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @Override
    public void cancel(String orderNo) {
        try {
            // 1. 先查询订单是否存在
            Wrapper<PayOrder> wrapper = new EntityWrapper<>();
            wrapper.eq("order_no", orderNo);
            PayOrder payOrder = selectOne(wrapper);
            
            if (payOrder == null) {
                log.error("订单不存在，无法取消，订单号:{}", orderNo);
                return;
            }
            
            // 2. 获取支付宝配置并初始化
            List<AlipayInfo> alipayInfos = alipayInfoService.selectList(null);
            if (alipayInfos == null || alipayInfos.isEmpty()) {
                log.error("支付宝配置信息不存在");
                return;
            }
            AlipayInfo alipayInfo = alipayInfos.get(0);
            
            // 3. 设置参数（全局只需设置一次）
            Factory.setOptions(getOptions(alipayInfo));
            
            // 4. 调用支付宝取消接口
            Factory.Payment.Common().cancel(orderNo);
            log.info("关单成功:{}", orderNo);
            
            // 5. 更新订单状态
            payOrderMapper.updateStatusByOrderNo(orderNo, new Date(), BusinessConstants.PAY_CLOSED);
        } catch (Exception e) {
            log.error("关单失败，订单号:{}，错误信息:{}", orderNo, e.getMessage(), e);
            e.printStackTrace();
        }
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
