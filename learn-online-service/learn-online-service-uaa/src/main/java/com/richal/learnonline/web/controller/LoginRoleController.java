package com.richal.learnonline.web.controller;

import com.richal.learnonline.service.ILoginRoleService;
import com.richal.learnonline.domain.LoginRole;
import com.richal.learnonline.query.LoginRoleQuery;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loginRole")
public class LoginRoleController {

    @Autowired
    public ILoginRoleService loginRoleService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JSONResult saveOrUpdate(@RequestBody LoginRole loginRole){
        if(loginRole.getId()!=null){
            loginRoleService.updateById(loginRole);
        }else{
            loginRoleService.insert(loginRole);
        }
        return JSONResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JSONResult delete(@PathVariable("id") Long id){
        loginRoleService.deleteById(id);
        return JSONResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JSONResult get(@PathVariable("id")Long id){
        return JSONResult.success(loginRoleService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JSONResult list(){
        return JSONResult.success(loginRoleService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JSONResult page(@RequestBody LoginRoleQuery query){
        Page<LoginRole> page = new Page<LoginRole>(query.getPage(),query.getRows());
        page = loginRoleService.selectPage(page);
        return JSONResult.success(new PageList<LoginRole>(page.getTotal(),page.getRecords()));
    }
}
