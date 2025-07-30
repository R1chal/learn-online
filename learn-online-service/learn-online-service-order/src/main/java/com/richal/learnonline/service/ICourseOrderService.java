package com.richal.learnonline.service;

import com.richal.learnonline.domain.CourseOrder;
import com.baomidou.mybatisplus.service.IService;
import com.richal.learnonline.dto.PlaceOrderDTO;
import com.richal.learnonline.dto.UpdateOrderStatusDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Richal
 * @since 2025-07-29
 */
public interface ICourseOrderService extends IService<CourseOrder> {

    String placeOrder(PlaceOrderDTO placeOrderDTO);

    void saveOrderAndItem(CourseOrder courseOrder);

    void updateOrderStatus(UpdateOrderStatusDTO updateOrderStatusDTO);

}
