package com.willing.thrift.spring.core.factory;

import com.willing.thrift.spring.core.TConfigProvider;
import org.apache.thrift.async.TAsyncClient;

/**
 * @author szw
 */
public abstract class TAsyncClientFactory<T> {
    protected final Class<? extends TAsyncClient> clientType;
    protected final T clientConfig;

    public TAsyncClientFactory(TConfigProvider<?, T> configProvider, Class<? extends TAsyncClient> clientType) {
        this.clientType = clientType;
        this.clientConfig = configProvider.getAsyncClientConfig(clientType);
    }

    /**
     * 返回一个新的 {@link TAsyncClient}
     *
     * @return {@link TAsyncClient}
     */
    public abstract TAsyncClient getAsyncClient();
}
