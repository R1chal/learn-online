package com.richal.learnonline.web.controller;

import com.richal.learnonline.service.IKillCourseService;
import com.richal.learnonline.domain.KillCourse;
import com.richal.learnonline.query.KillCourseQuery;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/killCourse")
public class KillCourseController {

    @Autowired
    public IKillCourseService killCourseService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JSONResult save(@RequestBody KillCourse killCourse){
        killCourseService.save(killCourse);
        return JSONResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JSONResult delete(@PathVariable("id") Long id){
        killCourseService.deleteById(id);
        return JSONResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JSONResult get(@PathVariable("id")Long id){
        return JSONResult.success(killCourseService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JSONResult list(){
        return JSONResult.success(killCourseService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JSONResult page(@RequestBody KillCourseQuery query){
        Page<KillCourse> page = new Page<KillCourse>(query.getPage(),query.getRows());
        page = killCourseService.selectPage(page);
        return JSONResult.success(new PageList<KillCourse>(page.getTotal(),page.getRecords()));
    }

    @GetMapping("/online/all")
    public JSONResult queryOnlineALL(){
        List<KillCourse> killCourses = killCourseService.queryOnlineALL();
        return JSONResult.success(killCourses);
    }

    @GetMapping("/online/one/{activityId}/{courseId}")
    public JSONResult queryOnlineOne(@PathVariable("activityId") Long activityId,@PathVariable("courseId") Long courseId){
        KillCourse killCourse = killCourseService.queryOnlineOne(activityId, courseId);
        return JSONResult.success(killCourse);
    }
}
