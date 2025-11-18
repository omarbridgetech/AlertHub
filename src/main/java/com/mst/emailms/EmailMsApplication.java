package com.mst.emailms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class EmailMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailMsApplication.class, args);
    }

}
