package com.usian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 枫柚素主
 * @version 1.0
 * @date 2020/6/8 13:08
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class DetailWebApp {
    public static void main(String[] args) {
        SpringApplication.run(DetailWebApp.class, args);
    }
}
