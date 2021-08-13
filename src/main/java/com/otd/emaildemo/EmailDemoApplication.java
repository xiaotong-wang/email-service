package com.otd.emaildemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xiaotong.wang
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class EmailDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailDemoApplication.class, args);
    }

}
