package com.richal.learnonline;

import com.richal.learnonline.doc.CourseDoc;
import com.richal.learnonline.result.JSONResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "service-search")
public interface SearchFeignAPI {

    @PostMapping("/es/save")
    JSONResult save(@RequestBody List<CourseDoc> courseDocs);
}
