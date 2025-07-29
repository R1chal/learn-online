package com.richal.learnonline.mq;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.domain.PayOrder;
import com.richal.learnonline.service.IPayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 站内推送消息
 *
 * @author Richal
 * @since 2025/07/27
 */
@Slf4j
@RocketMQMessageListener(topic = BusinessConstants.MQ_TOPIC_ORDER,
        selectorExpression = BusinessConstants.MQ_TAGS_COURSEORDER_PAYORDER,
        consumerGroup = "service-pay-consumer")
@Component
public class MQTXConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private IPayOrderService payOrderService;

    @Override
    public void onMessage(MessageExt messageExt) {
        if(messageExt != null && messageExt.getBody() != null && messageExt.getBody().length > 0) {
            String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            PayOrder payOrder = JSONObject.parseObject(body, PayOrder.class);
            Wrapper<PayOrder> ww = new EntityWrapper<PayOrder>();
            ww.eq("order_no", payOrder.getOrderNo());
            PayOrder payOrderDB = payOrderService.selectOne(ww);
            if(payOrderDB == null){
                payOrderService.insert(payOrder);
            }
            log.info("成功消费消息：{}", JSONObject.toJSONString(payOrder));
        }
    }
}
