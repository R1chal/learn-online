package com.richal.learnonline.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.richal.learnonline.api.CourseFeignAPI;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.domain.CourseOrder;
import com.richal.learnonline.domain.CourseOrderItem;
import com.richal.learnonline.domain.PayOrder;
import com.richal.learnonline.dto.CourseDTO;
import com.richal.learnonline.dto.CourseInfoDTO;
import com.richal.learnonline.dto.PlaceOrderDTO;
import com.richal.learnonline.dto.UpdateOrderStatusDTO;
import com.richal.learnonline.exception.GlobleBusinessException;
import com.richal.learnonline.mapper.CourseOrderMapper;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.service.ICourseOrderItemService;
import com.richal.learnonline.service.ICourseOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.richal.learnonline.util.CodeGenerateUtils;
import com.richal.learnonline.util.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.elasticsearch.common.Glob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Richal
 * @since 2025-07-29
 */
@Service
@Slf4j
public class CourseOrderServiceImpl extends ServiceImpl<CourseOrderMapper, CourseOrder> implements ICourseOrderService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private CourseFeignAPI courseFeignAPI;

    @Autowired
    private ICourseOrderItemService courseOrderItemService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private CourseOrderMapper courseOrderMapper;

    @Override
    public String placeOrder(PlaceOrderDTO placeOrderDTO) {

        List<Long> courseIds = placeOrderDTO.getCourseIds();
        Integer payType = placeOrderDTO.getPayType();
        String token = placeOrderDTO.getToken();
        //校验 token
        Long loginId = 3L;
        String key = BusinessConstants.REDIS_PREVENT_REPEAT_SUBMIT_ORDER + ":" +
                loginId + ":" + courseIds.get(0);
        Object redisToken = redisTemplate.opsForValue().get(key);

        if (redisToken == null || !redisToken.toString().equals(token)) {
            throw new GlobleBusinessException("请勿重复提交");
        }

        String join = StringUtils.join(courseIds, ",");

        JSONResult jsonResult = courseFeignAPI.info(join);
        Object data = jsonResult.getData();
        String jsonString = JSONObject.toJSONString(data);
        CourseInfoDTO courseInfoDTO = JSONObject.parseObject(jsonString, CourseInfoDTO.class);
        List<CourseDTO> coursesDTOs = courseInfoDTO.getCoursesDTOs();
        BigDecimal totalAmount = courseInfoDTO.getTotalAmount();

        CourseOrder courseOrder = new CourseOrder();
        Date date = new Date();
        courseOrder.setCreateTime(date);
        courseOrder.setUpdateTime(date);

        String orderNO = CodeGenerateUtils.generateOrderSn(loginId);
        courseOrder.setOrderNo(orderNO);
        courseOrder.setPayType(payType);
        courseOrder.setTotalAmount(totalAmount);
        courseOrder.setTotalCount(courseIds.size());
        courseOrder.setStatusOrder(CourseOrder.NO_PAY);
        courseOrder.setUserId(loginId);
        courseOrder.setTitle(coursesDTOs.get(0).getCourse().getName());

        //insert(courseOrder);

        List<CourseOrderItem> courseOrderItems = new ArrayList<>();
        coursesDTOs.forEach(course -> {
            CourseOrderItem courseOrderItem = new CourseOrderItem();
            courseOrderItem.setCreateTime(date);
            courseOrderItem.setUpdateTime(date);
            courseOrderItem.setAmount(course.getCourseMarket().getPrice());
            courseOrderItem.setCourseId(course.getCourse().getId());
            courseOrderItem.setCourseName(course.getCourse().getName());
            courseOrderItem.setCoursePic(course.getCourse().getPic());
            courseOrderItem.setOrderNo(orderNO);
            courseOrderItem.setCount(courseIds.size());
            courseOrderItem.setOrderId(courseOrder.getId());
            courseOrderItems.add(courseOrderItem);
        });

        courseOrder.setCourseOrderItems(courseOrderItems);
       // courseOrderItemService.insertBatch(courseOrderItems);

        PayOrder payOrder = new PayOrder();
        payOrder.setUpdateTime(date);
        payOrder.setCreateTime(date);
        payOrder.setAmount(totalAmount);
        payOrder.setPayType(payType);
        payOrder.setOrderNo(orderNO);
        payOrder.setUserId(loginId);
        payOrder.setSubject(coursesDTOs.get(0).getCourse().getName());
        payOrder.setPayStatus(PayOrder.NO_PAY);

        //消息内容到底应该长成什么样子？
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(
                //主题：标签
                BusinessConstants.MQ_TOPIC_ORDER + ":" + BusinessConstants.MQ_TAGS_COURSEORDER_PAYORDER,
                //消息：用作保存支付单
                MessageBuilder.withPayload(JSONObject.toJSONString(payOrder)).build(),
                //参数：用作保存课程订单和明细
                courseOrder);
        log.info("事务消息发送结果：{}", transactionSendResult.getSendStatus());

        SendStatus sendStatus = transactionSendResult.getSendStatus();
        if (sendStatus != SendStatus.SEND_OK){
            throw new GlobleBusinessException("分布式事务失败");
        }

        //处理超时订单
        SendResult result = rocketMQTemplate.syncSend(BusinessConstants.ROCKET_MQ_LEAVE_TIMEOUT_TOPIC +
                        ":" + BusinessConstants.ROCKET_MQ_LEAVE_TIMEOUT_TAGS,
                MessageBuilder.withPayload(orderNO).build(),
                3000,
                4);

        log.info("发送结果：{}",result);
        if (!result.getSendStatus().equals(SendStatus.SEND_OK)){
            log.error("发送延时消息失败:{}",result.getSendStatus());
        }

        redisTemplate.delete(key);
        return orderNO;
    }

    @Override
    public void saveOrderAndItem(CourseOrder courseOrder) {
        Wrapper<CourseOrder> ww = new EntityWrapper<CourseOrder>();
        ww.eq("order_no", courseOrder.getOrderNo());
        CourseOrder courseOrderDB = selectOne(ww);
        if (courseOrderDB != null) {
            throw new GlobleBusinessException("请勿重复提交");
        }
        insert(courseOrder);
        List<CourseOrderItem> courseOrderItems = courseOrder.getCourseOrderItems();
        for (CourseOrderItem item : courseOrderItems) {
            item.setOrderId(courseOrder.getId());
        }
        courseOrderItemService.insertBatch(courseOrderItems);
    }

    @Override
    public void updateOrderStatus(UpdateOrderStatusDTO updateOrderStatusDTO) {
        courseOrderMapper.updateOrderStatus(updateOrderStatusDTO);
    }


}
