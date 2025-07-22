package com.richal.learnonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 认证管理
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
public class UaaAPP {
    public static void main(String[] args) {
        SpringApplication.run(UaaAPP.class, args);
    }
}