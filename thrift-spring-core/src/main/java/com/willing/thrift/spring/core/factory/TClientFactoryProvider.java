package com.willing.thrift.spring.core.factory;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.async.TAsyncClient;

/**
 * @author szw
 */
public interface TClientFactoryProvider{

    TServiceClientFactory<?> getServiceClientFactory(Class<? extends TServiceClient> serviceClientClass);

    TAsyncClientFactory<?> getAsyncClientFactory(Class<? extends TAsyncClient> asyncClientClass);
}
