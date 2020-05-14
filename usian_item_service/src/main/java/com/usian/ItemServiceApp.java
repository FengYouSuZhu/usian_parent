package com.usian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/14 17:15
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(value = "com.usian.mapper")
public class ItemServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApp.class, args);
    }
}
