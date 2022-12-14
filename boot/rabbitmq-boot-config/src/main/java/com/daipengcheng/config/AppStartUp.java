package com.daipengcheng.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class AppStartUp {
    public static void main(String[] args) {
        SpringApplication.run(AppStartUp.class,args);
        log.info("rabbit config start success..");
    }
}
