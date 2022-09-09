package com.willing.thrift.spring.boot.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;


@Slf4j
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        RedisAutoConfiguration.class,
        MongoAutoConfiguration.class
})
public class TestApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    /*@Autowired
    ReportApiService.Iface iface;*/

    @Override
    public void run(String... args) throws Exception {
        log.info("======================= begin =======================");

//        System.out.println(iface.queryMaterialReport(new CommonReportRequest()));

        log.info("======================= end =======================");

    }
}
