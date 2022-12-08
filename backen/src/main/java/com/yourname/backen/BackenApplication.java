package com.yourname.backen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.yourname.backen.mapper")
//@SpringBootApplication(scanBasePackages = "com.yourname.backen.dtoMapper")
@SpringBootApplication
public class BackenApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackenApplication.class, args);
    }
}
