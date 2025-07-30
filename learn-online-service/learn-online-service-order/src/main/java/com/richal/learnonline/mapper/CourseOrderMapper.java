package com.richal.learnonline.mapper;

import com.richal.learnonline.domain.CourseOrder;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.richal.learnonline.dto.UpdateOrderStatusDTO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Richal
 * @since 2025-07-29
 */
public interface CourseOrderMapper extends BaseMapper<CourseOrder> {

    void updateOrderStatus(UpdateOrderStatusDTO updateOrderStatusDTO);
}
