package com.richal.learnonline.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.domain.MediaFile;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.service.IMediaFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 *
 */
@Slf4j
@RocketMQMessageListener(consumerGroup = BusinessConstants.ROCKETMQ_CONSUMERS_GROUP, topic = BusinessConstants.ROCKETMQ_TOPIC, selectorExpression =  BusinessConstants.ROCKETMQ_TAGS)
@Component
public class ConsumerMediaServiceImpl implements RocketMQListener<MessageExt> {

    @Autowired
    private IMediaFileService mediaFileService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String string = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        MediaFile mediaFile = JSONObject.parseObject(string.getBytes(), MediaFile.class);
        JSONResult jsonResult = mediaFileService.handleFile2m3u8(mediaFile);
        log.info("消费推流结果:{}", jsonResult);
    }
}
