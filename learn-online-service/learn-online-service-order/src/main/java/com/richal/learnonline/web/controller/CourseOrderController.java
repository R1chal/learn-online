package com.richal.learnonline.web.controller;

import com.richal.learnonline.dto.CourseDTO;
import com.richal.learnonline.dto.KillPlaceOrderDto;
import com.richal.learnonline.dto.PlaceOrderDTO;
import com.richal.learnonline.dto.UpdateOrderStatusDTO;
import com.richal.learnonline.service.ICourseOrderService;
import com.richal.learnonline.domain.CourseOrder;
import com.richal.learnonline.query.CourseOrderQuery;
import com.richal.learnonline.result.JSONResult;
import com.richal.learnonline.result.PageList;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courseOrder")
public class CourseOrderController {

    @Autowired
    public ICourseOrderService courseOrderService;

    /**
    * 保存和修改公用的
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public JSONResult saveOrUpdate(@RequestBody CourseOrder courseOrder){
        if(courseOrder.getId()!=null){
            courseOrderService.updateById(courseOrder);
        }else{
            courseOrderService.insert(courseOrder);
        }
        return JSONResult.success();
    }

    /**
    * 删除对象
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public JSONResult delete(@PathVariable("id") Long id){
        courseOrderService.deleteById(id);
        return JSONResult.success();
    }

    /**
   * 获取对象
   */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JSONResult get(@PathVariable("id")Long id){
        return JSONResult.success(courseOrderService.selectById(id));
    }


    /**
    * 查询所有对象
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public JSONResult list(){
        return JSONResult.success(courseOrderService.selectList(null));
    }


    /**
    * 带条件分页查询数据
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public JSONResult page(@RequestBody CourseOrderQuery query){
        Page<CourseOrder> page = new Page<CourseOrder>(query.getPage(),query.getRows());
        page = courseOrderService.selectPage(page);
        return JSONResult.success(new PageList<CourseOrder>(page.getTotal(),page.getRecords()));
    }

    @PostMapping("/placeOrder")
    public JSONResult placeOrder(@RequestBody PlaceOrderDTO placeOrderDTO){
        String order = courseOrderService.placeOrder(placeOrderDTO);
        return JSONResult.success(order);
    }

    @PostMapping("/updateOrderStatus")
    public JSONResult updateOrderStatus(@RequestBody UpdateOrderStatusDTO updateOrderStatusDTO){
        courseOrderService.updateOrderStatus(updateOrderStatusDTO);
        return JSONResult.success();

    }

    @PostMapping("/killPlaceOrder")
    public JSONResult killPlaceOrder(@RequestBody KillPlaceOrderDto killPlaceOrderDto){
        String orderNo = courseOrderService.killPlaceOrder(killPlaceOrderDto);
        return JSONResult.success();
    }
}
