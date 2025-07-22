package com.richal.learnonline.api;

import com.richal.learnonline.domain.Login;
import com.richal.learnonline.result.JSONResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("service-uaa")
public interface LoginFeignAPI {

    @PostMapping("/login/register")
    JSONResult register(@RequestBody Login login);
}
