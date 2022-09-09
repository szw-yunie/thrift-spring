package com.willing.thrift.spring.cloud.client;

import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ThriftSpringCloudStaterApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ThriftSpringCloudStaterApplication.class, args);
    }


    @SneakyThrows
    @Override
    public void run(String... args) throws Exception {
    }
}
