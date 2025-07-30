package com.richal.learnonline.api;

import com.richal.learnonline.dto.UpdateOrderStatusDTO;
import com.richal.learnonline.result.JSONResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "service-order")
public interface OrderFeignApi {

    @PostMapping("/courseOrder/updateOrderStatus")
    public JSONResult updateOrderStatus(@RequestBody UpdateOrderStatusDTO updateOrderStatusDTO);
}
