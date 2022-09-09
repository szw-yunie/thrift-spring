package com.willing.thrift.spring.boot.client.autoconfig;

import lombok.Data;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.async.TAsyncClient;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties("thrift.client")
public class ClientProperties {

    private String zkAddress;

    /**
     * Key 为 TServiceClient，value 为 zkNode
     */
    private Map<Class<? extends TServiceClient>, String> services;

    /**
     * Key 为 TAsyncClient，value 为 zkNode
     */
    private Map<Class<? extends TAsyncClient>, String> asyncs;
}
