package com.richal.learnonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class PayAPP {
    public static void main(String[] args) {
        SpringApplication.run(PayAPP.class, args);
    }
}