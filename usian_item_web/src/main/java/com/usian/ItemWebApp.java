package com.usian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/5/14 17:20
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ItemWebApp {
    public static void main(String[] args) {
        SpringApplication.run(ItemWebApp.class, args);
    }
}
