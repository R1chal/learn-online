package com.richal.learnonline.api;

import com.richal.learnonline.result.JSONResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-media")
public interface MediaFeignAPI {

    @GetMapping("/mediaFile/selectMediaFileByCourseId/{courseId}")
    public JSONResult selectMediaFileByCourseId(@PathVariable("courseId") Long courseId);
}
