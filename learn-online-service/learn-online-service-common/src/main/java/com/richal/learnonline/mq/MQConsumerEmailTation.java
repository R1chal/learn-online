package com.richal.learnonline.mq;

import com.alibaba.fastjson.JSONObject;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.domain.MessageEmail;
import com.richal.learnonline.dto.EmailDto;
import com.richal.learnonline.dto.UserEmailDto;
import com.richal.learnonline.service.IMessageEmailService;
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
 * 邮箱推送消息
 *
 * @author Richal
 * @since 2025/07/27
 */
@Slf4j
@RocketMQMessageListener(topic = BusinessConstants.ROCKETMQ_TOPIC,
        selectorExpression = BusinessConstants.ROCKETMQ_COURSE_EMAIL_TAGS,
        consumerGroup = "service-common-consumer")
@Component
public class MQConsumerEmailTation implements RocketMQListener<MessageExt> {

    @Autowired
    private IMessageEmailService messageEmailService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String s = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        EmailDto emailDto = JSONObject.parseObject(s, EmailDto.class);
        List<UserEmailDto> userEmailDtos = emailDto.getUserEmailDto();
        List<MessageEmail> messageEmails = new ArrayList<>();
        userEmailDtos.forEach(userEmailDto -> {
            MessageEmail messageEmail = new MessageEmail();
            BeanUtils.copyProperties(userEmailDto, messageEmail);
            messageEmail.setSendTime(new Date());
            messageEmail.setUserId(userEmailDto.getUserId());
            messageEmail.setEmail(userEmailDto.getEmail());
            messageEmails.add(messageEmail);
        });
        messageEmailService.insertBatch(messageEmails);
        log.info("消费成功：{}", messageExt);
    }
}
