package com.richal.learnonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CourseAPP {
    public static void main(String[] args) {
        SpringApplication.run(CourseAPP.class, args);
    }
}