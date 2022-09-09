package com.willing.thrift.spring.core.chain;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.async.TAsyncClient;

public interface TClientExecutorFactory {

    TExecutor getTServiceClientExecutor(Class<? extends TServiceClient> serviceClientClass);

    TExecutor getTAsyncClientExecutor(Class<? extends TAsyncClient> asyncClientClass);
}
