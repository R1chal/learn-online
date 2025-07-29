package com.richal.learnonline.controller;

import com.richal.learnonline.doc.CourseDoc;
import com.richal.learnonline.dto.CourseSearchDTO;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.service.ESCourseSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/es")
//@CrossOrigin
public class ESCourseSaveController {

    @Autowired
    private ESCourseSaveService esCourseSaveService;

    @PostMapping("/es/save")
    public JSONResult save(@RequestBody List<CourseDoc> courseDocs) {
        System.out.println("接收到保存课程文档请求，文档数量: " + (courseDocs != null ? courseDocs.size() : 0));
        if (courseDocs != null && !courseDocs.isEmpty()) {
            System.out.println("第一个文档: " + courseDocs.get(0));
        }
        
        try {
            esCourseSaveService.save(courseDocs);
            System.out.println("保存课程文档成功");
            return JSONResult.success();
        } catch (Exception e) {
            System.err.println("保存课程文档失败: " + e.getMessage());
            e.printStackTrace();
            return JSONResult.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/course/search")
    public JSONResult search(@RequestBody CourseSearchDTO courseSearchDTO) {
        return JSONResult.success(esCourseSaveService.search(courseSearchDTO));
    }
}
