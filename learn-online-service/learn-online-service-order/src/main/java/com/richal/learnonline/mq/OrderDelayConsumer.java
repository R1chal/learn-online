package com.richal.learnonline.mq;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.domain.CourseOrder;
import com.richal.learnonline.service.ICourseOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 超时消费关单
 *
 * @author Richal
 * @since 2025/07/30
 */
@Slf4j
@RocketMQMessageListener(topic = BusinessConstants.ROCKET_MQ_LEAVE_TIMEOUT_TOPIC,
        selectorExpression = BusinessConstants.ROCKET_MQ_LEAVE_TIMEOUT_TAGS,
        consumerGroup = "service-order-consumer",
        messageModel = MessageModel.BROADCASTING
)
@Component
public class OrderDelayConsumer implements RocketMQListener<MessageExt> {

    @Autowired
    private ICourseOrderService courseOrderService;

    @Override
    public void onMessage(MessageExt messageExt) {
        if(messageExt != null && messageExt.getBody() != null && messageExt.getBody().length > 0) {
            String orderNo = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            Wrapper<CourseOrder> ww = new EntityWrapper<>();
            ww.eq("order_no", orderNo);
            CourseOrder courseOrder = courseOrderService.selectOne(ww);
            if(courseOrder.getStatusOrder() == 0){
                courseOrder.setStatusOrder(CourseOrder.ORDER_CANCEL);
                courseOrderService.updateById(courseOrder);
                log.info("订单超时：{}", orderNo);
            }else {
                log.info("订单已消费:{}",orderNo);
            }
        }
    }
}
