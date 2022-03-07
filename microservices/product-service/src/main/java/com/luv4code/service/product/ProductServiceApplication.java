package com.luv4code.service.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.luv4code")
@Slf4j
public class ProductServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ProductServiceApplication.class, args);

        String mongodbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
        String mongodbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
        log.info("Connected to Mongodb: " + mongodbHost + ":" + mongodbPort);

    }

}
