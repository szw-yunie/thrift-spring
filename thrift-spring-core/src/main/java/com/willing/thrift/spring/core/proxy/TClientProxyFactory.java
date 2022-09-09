package com.willing.thrift.spring.core.proxy;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.async.TAsyncClient;

/**
 * @author szw
 */
public interface TClientProxyFactory {

    Object getTServiceClientProxy(Class<? extends TServiceClient> serviceClientClass);

    Object getTAsyncClientProxy(Class<? extends TAsyncClient> asyncClientClass);
}
