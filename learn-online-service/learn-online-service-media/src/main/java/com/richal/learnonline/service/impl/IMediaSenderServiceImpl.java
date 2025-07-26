package com.richal.learnonline.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.domain.MediaFile;
import com.richal.learnonline.service.IMediaSenderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
@Service
public class IMediaSenderServiceImpl implements IMediaSenderService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public Boolean send(MediaFile mediaFile) {

        if(mediaFile==null){
            throw new RuntimeException("消息为空");
        }
        SendResult result = rocketMQTemplate.syncSend(
                BusinessConstants.ROCKETMQ_TOPIC
                        + ":" + BusinessConstants.ROCKETMQ_TAGS,
                MessageBuilder.withPayload(JSONObject.toJSONString(mediaFile)).build());
        log.info("消息发送结果:{}", result);
        return result.getSendStatus() == SendStatus.SEND_OK;
    }
}
