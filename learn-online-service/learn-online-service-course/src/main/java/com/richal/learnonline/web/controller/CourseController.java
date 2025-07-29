package com.richal.learnonline.web.controller;

import com.richal.learnonline.dto.CourseSaveDto;
import com.richal.learnonline.service.ICourseService;
import com.richal.learnonline.domain.Course;
import com.richal.learnonline.query.CourseQuery;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    public ICourseService courseService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value = "/onLineCourse",method= RequestMethod.POST)
    public JSONResult onlineCourse(@RequestBody List<Long> courseIds) {
        courseService.onlineCourse(courseIds);
        return JSONResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JSONResult delete(@PathVariable("id") Long id){
        courseService.deleteById(id);
        return JSONResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JSONResult get(@PathVariable("id")Long id){
        return JSONResult.success(courseService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JSONResult list(){
        return JSONResult.success(courseService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JSONResult page(@RequestBody CourseQuery query){
        Page<Course> page = new Page<Course>(query.getPage(),query.getRows());
        page = courseService.selectPage(page);
        return JSONResult.success(new PageList<Course>(page.getTotal(),page.getRecords()));
    }


    @GetMapping("/detail/data/{courseId}")
    public  JSONResult detail(@PathVariable("courseId") Long courseId){
        return JSONResult.success(courseService.detail(courseId));
    }
}
