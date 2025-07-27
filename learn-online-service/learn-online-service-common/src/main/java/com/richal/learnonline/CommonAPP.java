package com.richal.learnonline;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import com.richal.learnonline.service.impl.OssServiceImpl;
import com.richal.learnonline.web.controller.OssController;

@SpringBootApplication
@MapperScan("com.richal.learnonline.mapper")
@ComponentScan(excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
                         classes = {OssServiceImpl.class, OssController.class})
})
public class CommonAPP {
    public static void main(String[] args) {
        SpringApplication.run(CommonAPP.class, args);
    }
}
