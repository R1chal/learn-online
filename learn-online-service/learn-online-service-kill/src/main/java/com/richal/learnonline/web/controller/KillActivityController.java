package com.richal.learnonline.web.controller;

import com.richal.learnonline.dto.KillActivityDTO;
import com.richal.learnonline.service.IKillActivityService;
import com.richal.learnonline.domain.KillActivity;
import com.richal.learnonline.query.KillActivityQuery;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/killActivity")
public class KillActivityController {

    @Autowired
    public IKillActivityService killActivityService;

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JSONResult delete(@PathVariable("id") Long id){
        killActivityService.deleteById(id);
        return JSONResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JSONResult get(@PathVariable("id")Long id){
        return JSONResult.success(killActivityService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JSONResult list(){
        return JSONResult.success(killActivityService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JSONResult page(@RequestBody KillActivityQuery query){
        Page<KillActivity> page = new Page<KillActivity>(query.getPage(),query.getRows());
        page = killActivityService.selectPage(page);
        return JSONResult.success(new PageList<KillActivity>(page.getTotal(),page.getRecords()));
    }

    @PostMapping("/save")
    public JSONResult save(@RequestBody KillActivityDTO killActivityDTO){
        killActivityService.saveActivity(killActivityDTO);
        return JSONResult.success();
    }

//    请求网址
//    http://localhost:10010/ymcc/kill/killActivity/publish/2
//    请求方法
//            POST
    @PostMapping("/publish/{id}")
    public JSONResult publish(@PathVariable("id") Long id){
        killActivityService.publish(id);
        return JSONResult.success();
    }
}
