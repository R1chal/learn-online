package com.richal.learnonline.mq;

import com.alibaba.fastjson.JSONObject;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.domain.MessageSms;
import com.richal.learnonline.dto.UserPhoneDto;
import com.richal.learnonline.dto.SMSDto;
import com.richal.learnonline.service.IMessageSmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 短信推送消息
 *
 * @author Richal
 * @since 2025/07/27
 */
@RocketMQMessageListener(topic = BusinessConstants.ROCKETMQ_TOPIC,
        selectorExpression = BusinessConstants.ROCKETMQ_COURSE_STATION_TAGS,
        consumerGroup = "service-common-consumer")
@Component
@Slf4j
public class MQConsumerSMStation implements RocketMQListener<MessageExt> {

    @Autowired
    private IMessageSmsService messageSmsService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String s = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        SMSDto smsDto = JSONObject.parseObject(s, SMSDto.class);
        List<UserPhoneDto> userPhoneDtos = smsDto.getUserPhoneDtos();
        List<MessageSms> messageSmsList = new ArrayList<>();
        userPhoneDtos.forEach(userPhoneDto -> {
            MessageSms messageSms = new MessageSms();
            BeanUtils.copyProperties(userPhoneDto, messageSms);
            messageSms.setSendTime(new Date());
            messageSms.setUserId(userPhoneDto.getUseId());
            messageSms.setIp(userPhoneDto.getIp());
            messageSms.setSendTime(new Date());
            messageSmsList.add(messageSms);
        });
        messageSmsService.insertBatch(messageSmsList);
        log.info("消费成功");
    }
}
