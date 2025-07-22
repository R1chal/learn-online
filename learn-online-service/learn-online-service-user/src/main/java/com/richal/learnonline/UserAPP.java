package com.richal.learnonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用户管理
 */
@EnableFeignClients
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
public class UserAPP {
    public static void main(String[] args) {
        SpringApplication.run(UserAPP.class, args);
    }
}