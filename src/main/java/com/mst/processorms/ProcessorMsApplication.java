package com.mst.processorms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.mst.processorms.client")
public class ProcessorMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessorMsApplication.class, args);
    }

}
