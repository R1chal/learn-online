package com.richal.learnonline.api;

import com.richal.learnonline.result.JSONResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-course")
public interface CourseFeignAPI {

    @GetMapping("/course/info/{courseId}")
    JSONResult info(@PathVariable("courseId") String courseId);
}
