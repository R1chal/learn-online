package com.richal.learnonline.mq;

import com.alibaba.fastjson.JSONObject;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.domain.MessageStation;
import com.richal.learnonline.dto.MessageStationDto;
import com.richal.learnonline.service.IMessageStationService;
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
 * 站内推送消息
 *
 * @author Richal
 * @since 2025/07/27
 */
@RocketMQMessageListener(topic = BusinessConstants.ROCKETMQ_TOPIC,
        selectorExpression = BusinessConstants.ROCKETMQ_COURSE_STATION_TAGS,
        consumerGroup = "service-common-consumer")
@Component
public class MQConsumerStation implements RocketMQListener<MessageExt> {

    @Autowired
    private IMessageStationService messageStationService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String s = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        MessageStationDto messageStationDto = JSONObject.parseObject(s, MessageStationDto.class);
        List<Long> userIds = messageStationDto.getUserIds();
        List<MessageStation> messageStations = new ArrayList<>();
        userIds.forEach(userId -> {
            MessageStation messageStation = new MessageStation();
            BeanUtils.copyProperties(messageStationDto, messageStation);
            messageStation.setSendTime(new Date());
            messageStation.setIsread(MessageStation.READ_NO);
            messageStations.add(messageStation);
        });
        messageStationService.insertBatch(messageStations);
    }
}
