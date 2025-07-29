package com.richal.learnonline.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.richal.learnonline.api.CourseFeignAPI;
import com.richal.learnonline.constant.BusinessConstants;
import com.richal.learnonline.domain.CourseOrder;
import com.richal.learnonline.domain.CourseOrderItem;
import com.richal.learnonline.dto.CourseDTO;
import com.richal.learnonline.dto.CourseInfoDTO;
import com.richal.learnonline.dto.PlaceOrderDTO;
import com.richal.learnonline.exception.GlobleBusinessException;
import com.richal.learnonline.mapper.CourseOrderMapper;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.service.ICourseOrderItemService;
import com.richal.learnonline.service.ICourseOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.richal.learnonline.util.CodeGenerateUtils;
import com.richal.learnonline.util.StrUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.Glob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
public class CourseOrderServiceImpl extends ServiceImpl<CourseOrderMapper, CourseOrder> implements ICourseOrderService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private CourseFeignAPI courseFeignAPI;

    @Autowired
    private ICourseOrderItemService courseOrderItemService;

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

        insert(courseOrder);

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
        courseOrderItemService.insertBatch(courseOrderItems);
        redisTemplate.delete(key);
        return orderNO;
    }
}
