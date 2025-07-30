package com.richal.learnonline.mapper;

import com.richal.learnonline.domain.PayOrder;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Richal
 * @since 2025-07-29
 */
public interface PayOrderMapper extends BaseMapper<PayOrder> {

    void updateStatusByOrderNo(@Param("outTradeNo") String outTradeNo,@Param("date") Date date,@Param("payStatus") Integer payStatus);
}
