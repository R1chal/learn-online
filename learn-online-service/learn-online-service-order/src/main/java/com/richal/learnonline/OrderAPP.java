package com.richal.learnonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrderAPP {
    public static void main(String[] args) {
        SpringApplication.run(OrderAPP.class, args);
    }
}