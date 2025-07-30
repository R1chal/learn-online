package com.richal.learnonline.mq;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.domain.CourseOrder;
import com.richal.learnonline.domain.PayOrder;
import com.richal.learnonline.service.IPayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static com.richal.learnonline.constant.BusinessConstants.ROCKET_MQ_LEAVE_TIMEOUT_TAGS;
import static com.richal.learnonline.constant.BusinessConstants.ROCKET_MQ_LEAVE_TIMEOUT_TOPIC;
import static org.apache.rocketmq.spring.annotation.MessageModel.BROADCASTING;

/**
 * 超时消费关单
 *
 * @author Richal
 * @since 2025/07/30
 */
@Slf4j
@RocketMQMessageListener(topic =ROCKET_MQ_LEAVE_TIMEOUT_TOPIC,
        selectorExpression =ROCKET_MQ_LEAVE_TIMEOUT_TAGS,
        consumerGroup = "service-order-consumer",
        messageModel = BROADCASTING
)
@Component
public class OrderDelayConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private IPayOrderService payOrderService;

    @Override
    public void onMessage(MessageExt messageExt) {
        if(messageExt != null && messageExt.getBody() != null && messageExt.getBody().length > 0) {
            String orderNo = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            Wrapper<PayOrder> ww = new EntityWrapper<>();
            ww.eq("order_no", orderNo);
            PayOrder payOrder = payOrderService.selectOne(ww);
            if(payOrder.getPayStatus() == 0){
                payOrder.setPayStatus(CourseOrder.ORDER_CANCEL);
            }
            payOrderService.updateById(payOrder);
            log.info("支付单超时：{}", payOrder);
            payOrderService.cancel(orderNo);
        }else {
            log.info("支付单已支付：{}", ROCKET_MQ_LEAVE_TIMEOUT_TAGS);
        }
    }
}
