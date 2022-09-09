package com.willing.thrift.spring.boot.client.autoconfig;

import com.willing.nodemgr.TConnectionPool;
import com.willing.thrift.spring.core.TConfigProvider;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.async.TAsyncClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
@ConditionalOnClass({TServiceClient.class, TAsyncClient.class, TConnectionPool.class})
@EnableConfigurationProperties(ClientProperties.class)
public class ThriftClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(TConfigProvider.class)
    TConfigProvider<?, ?> getClientConfigProvider(ClientProperties properties) {
        Assert.notNull(properties, "Can not auto generate bean with type " +
                "com.willing.thrift.spring.client.ConfigProvider," +
                "please manually provide a bean with this type.");
        return TConfigProvider.of(properties.getServices(), properties.getAsyncs());
    }

    @Bean
    @ConditionalOnBean(TConfigProvider.class)
    @ConditionalOnMissingBean(TClientInvocationHandlerHandler.class)
    TClientInvocationHandlerHandler getClientProxyHandler(
            TConfigProvider<String, String> configProvider,
            ClientProperties properties) {
        return new TClientInvocationHandlerHandler(configProvider, properties.getZkAddress());
    }
}
